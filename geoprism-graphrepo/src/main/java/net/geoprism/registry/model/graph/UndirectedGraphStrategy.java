/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.model.graph;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import org.commongeoregistry.adapter.constants.DefaultAttribute;

import com.runwaysdk.business.graph.EdgeObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;

import net.geoprism.registry.DateFormatter;
import net.geoprism.registry.graph.DataSource;
import net.geoprism.registry.graph.UndirectedGraphType;
import net.geoprism.registry.model.EdgeType;
import net.geoprism.registry.model.ServerChildGraphNode;
import net.geoprism.registry.model.ServerGraphNode;
import net.geoprism.registry.model.ServerParentGraphNode;
import net.geoprism.registry.query.graph.VertexAndEdgeQuery;
import net.geoprism.registry.query.graph.VertexAndEdgeQuery.EdgeQueryObject;

public class UndirectedGraphStrategy extends AbstractGraphStrategy implements GraphStrategy
{
  protected static class EdgeComparator implements Comparator<EdgeObject>
  {
    @Override
    public int compare(EdgeObject o1, EdgeObject o2)
    {
      Date d1 = o1.getObjectValue(EdgeType.START_DATE);
      Date d2 = o2.getObjectValue(EdgeType.START_DATE);

      return d1.compareTo(d2);
    }
  }

  private UndirectedGraphType type;

  public UndirectedGraphStrategy(UndirectedGraphType type)
  {
    super(type);
    this.type = type;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends ServerGraphNode> T getChildren(VertexServerGeoObject source, Boolean recursive, Date date, String boundsWKT, Long skip, Long limit)
  {
    return (T) getChildren(source, recursive, date, new TreeSet<String>(), boundsWKT, skip, limit);
  }

  private ServerChildGraphNode getChildren(VertexServerGeoObject source, Boolean recursive, Date date, TreeSet<String> visited, String boundsWKT, Long skip, Long limit)
  {
    return this.getEdgeChildren(source, recursive, date, boundsWKT, skip, limit);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends ServerGraphNode> T getParents(VertexServerGeoObject child, Boolean recursive, Date date, String boundsWKT, Long skip, Long limit)
  {
    return (T) getParents(child, recursive, date, new TreeSet<String>(), boundsWKT, skip, limit);
  }

  private ServerParentGraphNode getParents(VertexServerGeoObject source, Boolean recursive, Date date, TreeSet<String> visited, String boundsWKT, Long skip, Long limit)
  {
    return this.getEdgeParents(source, recursive, date, boundsWKT, skip, limit);
  }

  @Override
  public ServerChildGraphNode getEdgeChildren(VertexServerGeoObject source, Boolean recursive, Date date, String boundsWKT, Long skip, Long limit)
  {
    List<EdgeQueryObject> results = new VertexAndEdgeQuery(source.getVertex(), this.type.getMdEdgeDAO().getDBClassName(), VertexAndEdgeQuery.Direction.CHILDREN, VertexServerGeoObject::processTraverseResults).setDate(date).setBoundsWKT(boundsWKT).setSkip(skip).setLimit(limit).getResults();

    return this.buildChildGraphNode(source, this.type, date, results);
  }

  @Override
  public ServerParentGraphNode getEdgeParents(VertexServerGeoObject source, Boolean recursive, Date date, String boundsWKT, Long skip, Long limit)
  {
    List<EdgeQueryObject> results = new VertexAndEdgeQuery(source.getVertex(), this.type.getMdEdgeDAO().getDBClassName(), VertexAndEdgeQuery.Direction.PARENTS, VertexServerGeoObject::processTraverseResults).setDate(date).setBoundsWKT(boundsWKT).setSkip(skip).setLimit(limit).getResults();

    return this.buildParentGraphNode(source, this.type, date, results);
  }

  @Override
  public void addChild(VertexServerGeoObject geoObject, VertexServerGeoObject child, Date startDate, Date endDate, String uid, DataSource source, boolean validate)
  {
    this.addParent(child, geoObject, startDate, endDate, uid, source, validate);
  }

  @Override
  public void addParent(VertexServerGeoObject geoObject, VertexServerGeoObject parent, Date startDate, Date endDate, String uid, DataSource source, boolean validate)
  {
    if (validate)
    {
      if (this.isCycle(geoObject, parent, startDate, endDate))
      {
        throw new UnsupportedOperationException("Cyclic graph is not supported");
      }

      if (this.getEdges(geoObject, parent, startDate, endDate).size() > 0)
      {
        throw new UnsupportedOperationException("Duplicate edge between child [" + geoObject.getCode() + "] and parent [" + parent.getCode() + "] with relationship type [" + UndirectedGraphType.CLASS + "].");
      }
    }

    HashMap<String, Object> values = new HashMap<String, Object>();
    values.put(EdgeType.START_DATE, startDate);
    values.put(EdgeType.END_DATE, endDate);
    values.put(DefaultAttribute.UID.getName(), uid);
    values.put("inRid", geoObject.getRID());
    values.put("inOid", geoObject.getOid());
    values.put("outRid", parent.getRID());
    values.put("outOid", parent.getOid());
    values.put(DefaultAttribute.DATA_SOURCE.getName(), source != null ? source.getRID() : null);

    EdgeEntry value = new EdgeEntry(values);

    SortedSet<EdgeEntry> existingEdges = this.getParentEdges(geoObject);

    // Determine if there are overlaps between the object and the existing data

    for (EdgeEntry edge : existingEdges)
    {
      if (this.contains(edge, value))
      {
        // The edge is contained by the value delete the edge
        this.delete(edge);
      }
      else if (this.contains(value, edge))
      {
        // The existing edge contains the entire range of a new edge

        // TODO: Determine the appropriate solution, for now delete the existing
        // edge
        this.delete(edge);
      }
      else if (this.overlapsStart(value, edge))
      {
        // Update the start date of the existing edge to after the end date of
        // the incoming edge
        Calendar calendar = Calendar.getInstance(DateFormatter.SYSTEM_TIMEZONE);
        calendar.clear();
        calendar.setTime(value.getEndDate());
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        edge.put(EdgeType.START_DATE, calendar.getTime());

        this.update(edge);
      }
      else if (this.overlapsEnd(value, edge))
      {
        // Update the end date of the existing edge to before the start date of
        // the incoming edge
        Calendar calendar = Calendar.getInstance(DateFormatter.SYSTEM_TIMEZONE);
        calendar.clear();
        calendar.setTime(value.getStartDate());
        calendar.add(Calendar.DAY_OF_YEAR, -11);

        edge.put(EdgeType.END_DATE, calendar.getTime());

        this.update(edge);
      }
    }

    // Create or update the edge
    Optional<EdgeEntry> entry = existingEdges.stream().filter(e -> e.getUid().equals(value.getUid())).findFirst();

    if (entry.isPresent())
    {
      // Update the existing edge
      value.put("rid", entry.get().getRid());

      this.update(value);
    }
    else
    {
      this.create(value);
    }
  }

  @Override
  public void removeParent(VertexServerGeoObject geoObject, VertexServerGeoObject parent, Date startDate, Date endDate)
  {
    this.getEdges(geoObject, parent, startDate, endDate).forEach(edge -> {
      edge.delete();
    });
  }

  private SortedSet<EdgeObject> getEdges(VertexServerGeoObject geoObject, VertexServerGeoObject parent, Date startDate, Date endDate)
  {
    TreeSet<EdgeObject> set = new TreeSet<EdgeObject>(new EdgeComparator());

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM (");
    statement.append("  SELECT expand(bothE('" + this.type.getMdEdgeDAO().getDBClassName() + "'))");
    statement.append("  FROM :child");
    statement.append(")");
    statement.append(" WHERE (out = :parent OR in = :parent)");
    statement.append(" AND startDate = :startDate");
    statement.append(" AND endDate = :endDate");

    GraphQuery<EdgeObject> query = new GraphQuery<EdgeObject>(statement.toString());
    query.setParameter("child", geoObject.getVertex().getRID());
    query.setParameter("parent", parent.getVertex().getRID());
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);

    set.addAll(query.getResults());

    return set;
  }

  public boolean isCycle(VertexServerGeoObject geoObject, VertexServerGeoObject parent, Date startDate, Date endDate)
  {
    VertexObject vertex = geoObject.getVertex();

    StringBuffer statement = new StringBuffer();
    statement.append("SELECT count(*) FROM (");
    statement.append("MATCH {class: " + geoObject.getType().getMdVertexDAO().getDBClassName() + ", where: (@rid = :rid)}.(outE('" + this.type.getMdEdgeDAO().getDBClassName() + "')");
    statement.append(" {where: (:startDate BETWEEN startDate AND endDate OR :endDate BETWEEN startDate AND endDate)}.outV())");
    statement.append(" {as: friend, while: ($depth < 10)} RETURN friend.code AS code");
    statement.append(")");
    statement.append(" WHERE code = :code");

    GraphQuery<Long> query = new GraphQuery<Long>(statement.toString());
    query.setParameter("rid", vertex.getRID());
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);
    query.setParameter("code", parent.getCode());

    Long count = query.getSingleResult();

    return ( count != null && count > 0 );
  }

}
