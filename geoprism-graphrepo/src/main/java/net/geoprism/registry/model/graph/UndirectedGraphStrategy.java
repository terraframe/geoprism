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

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.commons.collections4.map.HashedMap;

import com.runwaysdk.business.graph.EdgeObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;

import net.geoprism.registry.UndirectedGraphType;
import net.geoprism.registry.graph.GeoVertex;
import net.geoprism.registry.model.EdgeConstant;
import net.geoprism.registry.model.ServerChildGraphNode;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerGraphNode;
import net.geoprism.registry.model.ServerParentGraphNode;

public class UndirectedGraphStrategy extends AbstractGraphStrategy implements GraphStrategy
{
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
    ServerChildGraphNode tnRoot = new ServerChildGraphNode(source, this.type, date, null, null);

    if (limit != null && limit <= 0)
    {
      return tnRoot;
    }

    if (skip != null && recursive)
    {
      throw new UnsupportedOperationException();
    }

    List<VertexServerGeoObject> nodes = this.getObjects(source, date, boundsWKT, "in", skip, limit);

    long resultsCount = nodes.size();

    for (VertexServerGeoObject target : nodes)
    {

      if (!source.getUid().equals(target.getUid()))
      {
        ServerChildGraphNode tnParent;

        if (recursive && !visited.contains(target.getUid()) && ( limit == null || limit - resultsCount > 0 ))
        {
          visited.add(target.getUid());

          tnParent = this.getChildren(target, recursive, date, visited, boundsWKT, null, ( limit == null ? null : limit - resultsCount ));
          tnParent.setOid(UUID.randomUUID().toString());

          resultsCount += tnParent.getChildren().size();
        }
        else
        {
          tnParent = new ServerChildGraphNode(target, this.type, date, null, UUID.randomUUID().toString());
        }

        tnRoot.addChild(tnParent);
      }
    }

    return tnRoot;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends ServerGraphNode> T getParents(VertexServerGeoObject child, Boolean recursive, Date date, String boundsWKT, Long skip, Long limit)
  {
    return (T) getParents(child, recursive, date, new TreeSet<String>(), boundsWKT, skip, limit);
  }

  private ServerParentGraphNode getParents(VertexServerGeoObject source, Boolean recursive, Date date, TreeSet<String> visited, String boundsWKT, Long skip, Long limit)
  {
    ServerParentGraphNode tnRoot = new ServerParentGraphNode(source, this.type, date, null, null);

    if (limit != null && limit <= 0)
    {
      return tnRoot;
    }

    if (skip != null && recursive)
    {
      throw new UnsupportedOperationException();
    }

    List<VertexServerGeoObject> nodes = this.getObjects(source, date, boundsWKT, "in", skip, limit);

    long resultsCount = nodes.size();

    for (VertexServerGeoObject target : nodes)
    {
      if (!target.getUid().equals(source.getUid()))
      {
        ServerParentGraphNode tnParent;

        if (recursive & !visited.contains(target.getUid()) && ( limit == null || limit - resultsCount > 0 ))
        {
          visited.add(target.getUid());

          tnParent = this.getParents(target, recursive, date, visited, boundsWKT, null, ( limit == null ? null : limit - resultsCount ));
          tnParent.setOid(UUID.randomUUID().toString());

          resultsCount += tnParent.getParents().size();
        }
        else
        {
          tnParent = new ServerParentGraphNode(target, this.type, date, null, UUID.randomUUID().toString());
        }

        tnRoot.addParent(tnParent);
      }
    }

    return tnRoot;
  }

  @Override
  public <T extends ServerGraphNode> T addChild(VertexServerGeoObject geoObject, VertexServerGeoObject child, Date startDate, Date endDate, boolean validate)
  {
    return this.addParent(child, geoObject, startDate, endDate, validate);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends ServerGraphNode> T addParent(VertexServerGeoObject geoObject, VertexServerGeoObject parent, Date startDate, Date endDate, boolean validate)
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

    Set<ValueOverTime> votc = this.getParentCollection(geoObject);
    votc.add(new ValueOverTime(startDate, endDate, parent));

    SortedSet<EdgeObject> newEdges = this.setParentCollection(geoObject, votc);

    ServerParentGraphNode node = new ServerParentGraphNode(geoObject, this.type, startDate, endDate, null);
    node.addParent(new ServerParentGraphNode(parent, this.type, startDate, endDate, newEdges.first().getOid()));

    return (T) node;
  }

  @Override
  public void removeParent(VertexServerGeoObject geoObject, VertexServerGeoObject parent, Date startDate, Date endDate)
  {
    this.getEdges(geoObject, parent, startDate, endDate).forEach(edge -> {
      edge.delete();
    });
  }

  private Set<ValueOverTime> getParentCollection(VertexServerGeoObject geoObject)
  {
    Set<ValueOverTime> set = new TreeSet<ValueOverTime>(new Comparator<ValueOverTime>()
    {
      @Override
      public int compare(ValueOverTime o1, ValueOverTime o2)
      {
        return o1.getOid().compareTo(o2.getOid());
      }
    });

    SortedSet<EdgeObject> edges = this.getParentEdges(geoObject);

    for (EdgeObject edge : edges)
    {
      final Date startDate = edge.getObjectValue(GeoVertex.START_DATE);
      final Date endDate = edge.getObjectValue(GeoVertex.END_DATE);

      VertexObject parentVertex = edge.getParent();
      MdVertexDAOIF mdVertex = (MdVertexDAOIF) parentVertex.getMdClass();
      ServerGeoObjectType parentType = ServerGeoObjectType.get(mdVertex);
      VertexServerGeoObject parent = new VertexServerGeoObject(parentType, parentVertex, new TreeMap<>(), startDate);

      set.add(new ValueOverTime(edge.getOid(), startDate, endDate, parent));
    }

    return set;
  }

  private SortedSet<EdgeObject> setParentCollection(VertexServerGeoObject geoObject, Set<ValueOverTime> votc)
  {
    SortedSet<EdgeObject> resultEdges = new TreeSet<EdgeObject>(new EdgeComparator());
    SortedSet<EdgeObject> existingEdges = this.getParentEdges(geoObject);

    for (EdgeObject edge : existingEdges)
    {
      final Date startDate = edge.getObjectValue(GeoVertex.START_DATE);
      final Date endDate = edge.getObjectValue(GeoVertex.END_DATE);

      VertexObject parentVertex = edge.getParent();
      MdVertexDAOIF mdVertex = (MdVertexDAOIF) parentVertex.getMdClass();
      ServerGeoObjectType parentType = ServerGeoObjectType.get(mdVertex);
      final VertexServerGeoObject edgeGo = new VertexServerGeoObject(parentType, parentVertex, new TreeMap<>(), startDate);

      ValueOverTime inVot = null;

      for (ValueOverTime vot : votc)
      {
        if (vot.getOid().equals(edge.getOid()))
        {
          inVot = vot;
          break;
        }
      }

      if (inVot == null)
      {
        edge.delete();
      }
      else
      {
        VertexServerGeoObject inGo = (VertexServerGeoObject) inVot.getValue();

        boolean hasValueChange = false;

        if ( ( inGo == null && edgeGo != null ) || ( inGo != null && edgeGo == null ))
        {
          hasValueChange = true;
        }
        else if ( ( inGo != null && edgeGo != null ) && !inGo.equals(edgeGo))
        {
          hasValueChange = true;
        }

        if (hasValueChange)
        {
          edge.delete();

          EdgeObject newEdge = geoObject.getVertex().addParent(inGo.getVertex(), this.type.getMdEdgeDAO());
          newEdge.setValue(GeoVertex.START_DATE, startDate);
          newEdge.setValue(GeoVertex.END_DATE, endDate);
          newEdge.apply();

          resultEdges.add(newEdge);
        }
        else
        {
          boolean hasChanges = false;
          Date votStartDate = inVot.getStartDate();
          Date votEndDate = inVot.getEndDate();

          if (!startDate.equals(votStartDate))
          {
            hasChanges = true;
            edge.setValue(GeoVertex.START_DATE, votStartDate);
          }

          if (endDate != votEndDate)
          {
            hasChanges = true;
            edge.setValue(GeoVertex.END_DATE, endDate);
          }

          if (hasChanges)
          {
            edge.apply();
          }
        }
      }
    }

    for (ValueOverTime vot : votc)
    {
      boolean isNew = true;

      for (EdgeObject edge : existingEdges)
      {
        if (vot.getOid().equals(edge.getOid()))
        {
          isNew = false;

          resultEdges.add(edge);
        }
      }

      if (isNew)
      {
        EdgeObject newEdge = geoObject.getVertex().addParent( ( (VertexServerGeoObject) vot.getValue() ).getVertex(), this.type.getMdEdgeDAO());
        newEdge.setValue(GeoVertex.START_DATE, vot.getStartDate());
        newEdge.setValue(GeoVertex.END_DATE, vot.getEndDate());
        newEdge.apply();

        resultEdges.add(newEdge);
      }
    }

    return resultEdges;
  }

  private List<VertexServerGeoObject> getObjects(VertexServerGeoObject geoObject, Date date, String boundsWKT, String inOrOut, Long skip, Long limit)
  {
    Map<String, Object> parameters = new HashedMap<String, Object>();
    parameters.put("rid", geoObject.getVertex().getRID());

    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
    statement.append(" SELECT FROM (");

    statement.append(" SELECT EXPAND(bothE(");
    statement.append("'" + this.type.getMdEdgeDAO().getDBClassName() + "'");
    statement.append(")");

    if (date != null)
    {
      statement.append("[:date BETWEEN startDate AND endDate]");
      parameters.put("date", date);
    }

    statement.append(".bothV()) FROM :rid");
    statement.append(") WHERE @rid != :rid");

    if (boundsWKT != null)
    {
      if (date != null)
      {
        statement.append(" AND out('has_geometry')[:date BETWEEN startDate AND endDate AND ST_INTERSECTS(value, :bounds) = true].size() > 0");
      }
      else
      {
        statement.append(" AND out('has_geometry')[ST_INTERSECTS(value, :bounds) = true].size() > 0");
      }

      parameters.put("bounds", boundsWKT);
    }

    if (skip != null)
    {
      statement.append(" SKIP " + skip);
    }

    if (limit != null)
    {
      statement.append(" LIMIT " + limit);
    }
    statement.append(" )");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString(), parameters);

    return VertexServerGeoObject.processTraverseResults(query.getResults(), date);
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
    statement.append("MATCH {class: " + geoObject.getType().getMdVertex().getDBClassName() + ", where: (@rid = :rid)}.(outE('" + this.type.getMdEdgeDAO().getDBClassName() + "')");
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
