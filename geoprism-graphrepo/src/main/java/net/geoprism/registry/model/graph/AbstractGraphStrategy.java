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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.constants.DefaultAttribute;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphRequest;

import net.geoprism.registry.model.EdgeType;
import net.geoprism.registry.model.GraphType;
import net.geoprism.registry.model.ServerChildGraphNode;
import net.geoprism.registry.model.ServerParentGraphNode;
import net.geoprism.registry.query.graph.VertexAndEdgeQuery.EdgeQueryObject;
import net.geoprism.registry.service.business.DataSourceBusinessServiceIF;
import net.geoprism.registry.service.business.ServiceFactory;

public class AbstractGraphStrategy
{
  protected static class EdgeEntry implements Comparable<EdgeEntry>
  {
    private Map<String, Object> values;

    public EdgeEntry(Map<String, Object> values)
    {
      this.values = values;

      if (!this.values.containsKey("oid"))
      {
        this.values.put("oid", UUID.randomUUID().toString());
      }
    }

    public void put(String key, Object value)
    {
      this.values.put(key, value);
    }

    public Date getStartDate()
    {
      return (Date) this.values.get(EdgeType.START_DATE);
    }

    public Date getEndDate()
    {
      return (Date) this.values.get(EdgeType.END_DATE);
    }

    public Object getDataSource()
    {
      return this.values.get(DefaultAttribute.DATA_SOURCE.getName());
    }

    public String getUid()
    {
      return (String) this.values.get(DefaultAttribute.UID.getName());
    }

    public String getOid()
    {
      return (String) this.values.get("oid");
    }

    public Object getInRid()
    {
      return this.values.get("inRid");
    }

    public String getInOid()
    {
      return (String) this.values.get("inOid");
    }

    public Object getOutRid()
    {
      return this.values.get("outRid");
    }

    public String getOutOid()
    {
      return (String) this.values.get("outOid");
    }

    public Object getRid()
    {
      return this.values.get("rid");
    }

    @Override
    public int compareTo(EdgeEntry entry)
    {
      return this.getStartDate().compareTo(entry.getStartDate());
    }
  }

  protected GraphType type;

  public AbstractGraphStrategy(GraphType type)
  {
    this.type = type;
  }

  protected ServerParentGraphNode buildParentGraphNode(VertexServerGeoObject root, GraphType graphType, Date date, List<EdgeQueryObject> results)
  {
    ServerParentGraphNode rootNode = new ServerParentGraphNode(root, graphType, date, null, null, null, null);

    for (EdgeQueryObject result : results)
    {
      rootNode.addParent(this.buildParentGraphNode(result, graphType));
    }

    return rootNode;
  }

  private ServerParentGraphNode buildParentGraphNode(EdgeQueryObject result, GraphType graphType)
  {
    VertexServerGeoObject object = (VertexServerGeoObject) result.getObject();

    ServerParentGraphNode node = new ServerParentGraphNode(object, graphType, result.getStartDate(), result.getEndDate(), result.getOid(), result.getUid(), null);

    ServiceFactory.getBean(DataSourceBusinessServiceIF.class).getByCode(result.getSource()).ifPresent(s -> node.setSource(s));

    for (EdgeQueryObject parent : result.getRelated())
    {
      node.addParent(this.buildParentGraphNode(parent, graphType));
    }

    return node;
  }

  protected ServerChildGraphNode buildChildGraphNode(VertexServerGeoObject root, GraphType graphType, Date date, List<EdgeQueryObject> results)
  {
    ServerChildGraphNode rootNode = new ServerChildGraphNode(root, graphType, date, null, null, null, null);

    for (EdgeQueryObject result : results)
    {
      rootNode.addChild(this.buildChildGraphNode(result, graphType));
    }

    return rootNode;
  }

  private ServerChildGraphNode buildChildGraphNode(EdgeQueryObject result, GraphType graphType)
  {
    VertexServerGeoObject object = (VertexServerGeoObject) result.getObject();

    ServerChildGraphNode node = new ServerChildGraphNode(object, graphType, result.getStartDate(), result.getEndDate(), result.getOid(), result.getUid(), null);
    ServiceFactory.getBean(DataSourceBusinessServiceIF.class).getByCode(result.getSource()).ifPresent(s -> node.setSource(s));

    for (EdgeQueryObject child : result.getRelated())
    {
      node.addChild(this.buildChildGraphNode(child, graphType));
    }

    return node;
  }

  protected SortedSet<EdgeEntry> getParentEdges(VertexServerGeoObject geoObject)
  {
    TreeSet<EdgeEntry> set = new TreeSet<EdgeEntry>();

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT @rid AS rid, in AS inRid, out as outRid, in.oid AS inOid, out.oid AS outOid, startDate, endDate, dataSource, uid, oid");
    statement.append(" FROM (");
    statement.append("   SELECT expand(inE('" + this.type.getMdEdgeDAO().getDBClassName() + "'))");
    statement.append("   FROM :child");
    statement.append(")");

    GraphQuery<Map<String, Object>> query = new GraphQuery<Map<String, Object>>(statement.toString());
    query.setParameter("child", geoObject.getVertex().getRID());

    set.addAll(query.getResults().stream().map(m -> new EdgeEntry(m)).toList());

    return set;
  }

  protected String wrapQueryWithBounds(String innerQuery, String inOrOut, Date date, String boundsWKT, Map<String, Object> parameters)
  {
    StringBuilder statement = new StringBuilder();

    statement.append("SELECT FROM (");

    statement.append(innerQuery);

    statement.append(") WHERE ");

    statement.append("(");

    String dateRestriction = "";
    if (date != null)
    {
      dateRestriction = "(:date BETWEEN startDate AND endDate) AND";
      parameters.put("date", date);
    }

    final String[] geometryTypes = new String[] { "shape_cot", "geoPoint_cot", "geoLine_cot", "geoMultiLine_cot", "geoMultiPoint_cot", "geoMultiPolygon_cot", "shape_cot", "geoPolygon_cot" };

    List<String> geometryRestrictions = new ArrayList<String>();

    for (String geometryType : geometryTypes)
    {
      geometryRestrictions.add(inOrOut + "." + geometryType + " CONTAINS ( " + dateRestriction + " ST_INTERSECTS(value, :bounds) = true )");
    }

    statement.append(StringUtils.join(geometryRestrictions, " OR "));

    statement.append(")");

    parameters.put("bounds", boundsWKT);

    return statement.toString();
  }

  protected boolean contains(EdgeEntry incoming, EdgeEntry existing)
  {
    if (incoming.getUid().equals(existing.getUid()))
    {
      return false;
    }

    if (!incoming.getOutRid().equals(existing.getOutRid()) || !incoming.getInRid().equals(existing.getInRid()))
    {
      return false;
    }

    return ( incoming.getStartDate().before(existing.getStartDate()) || incoming.getStartDate().equals(existing.getStartDate()) ) && //
        ( incoming.getEndDate().after(existing.getEndDate()) || incoming.getEndDate().equals(existing.getEndDate()) );
  }

  protected boolean overlapsStart(EdgeEntry incoming, EdgeEntry existing)
  {
    if (incoming.getUid().equals(existing.getUid()))
    {
      return false;
    }

    if (!incoming.getOutRid().equals(existing.getOutRid()) || !incoming.getInRid().equals(existing.getInRid()))
    {
      return false;
    }

    return ( incoming.getStartDate().before(existing.getStartDate()) || incoming.getStartDate().equals(existing.getStartDate()) ) && //
        ( incoming.getEndDate().after(existing.getStartDate()) || incoming.getEndDate().equals(existing.getStartDate()) );
  }

  protected boolean overlapsEnd(EdgeEntry incoming, EdgeEntry existing)
  {
    if (incoming.getUid().equals(existing.getUid()))
    {
      return false;
    }

    if (!incoming.getOutRid().equals(existing.getOutRid()) || !incoming.getInRid().equals(existing.getInRid()))
    {
      return false;
    }

    return ( incoming.getStartDate().before(existing.getEndDate()) || incoming.getStartDate().equals(existing.getEndDate()) ) && //
        ( incoming.getEndDate().after(existing.getEndDate()) || incoming.getEndDate().equals(existing.getEndDate()) );
  }

  protected void delete(EdgeEntry edge)
  {
    String clazz = type.getMdEdgeDAO().getDBClassName();

    StringBuilder statement = new StringBuilder();
    statement.append("DELETE EDGE " + clazz);
    statement.append(" WHERE uid = :uid");

    GraphDBService service = GraphDBService.getInstance();
    GraphRequest request = service.getGraphDBRequest();

    service.command(request, statement.toString(), Map.of("uid", edge.getUid()));
  }

  protected void update(EdgeEntry edge)
  {
    StringBuilder statement = new StringBuilder();
    statement.append("UPDATE :rid SET ");
    statement.append(" startDate = :startDate,");
    statement.append(" endDate = :endDate,");
    statement.append(" dataSource = :dataSource");

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("rid", edge.getRid());
    parameters.put("startDate", edge.getStartDate());
    parameters.put("endDate", edge.getEndDate());
    parameters.put("dataSource", edge.getDataSource());

    GraphDBService service = GraphDBService.getInstance();
    GraphRequest request = service.getGraphDBRequest();

    service.command(request, statement.toString(), parameters);
  }

  protected void create(EdgeEntry edge)
  {
    String clazz = type.getMdEdgeDAO().getDBClassName();

    StringBuilder statement = new StringBuilder();
    statement.append("CREATE EDGE " + clazz + " FROM :out TO :in SET");
    statement.append(" startDate = :startDate,");
    statement.append(" endDate = :endDate,");
    statement.append(" dataSource = :dataSource,");
    statement.append(" uid = :uid,");
    statement.append(" oid = :oid");

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("oid", edge.getOid());
    parameters.put("uid", edge.getUid());
    parameters.put("out", edge.getOutRid());
    parameters.put("in", edge.getInRid());
    parameters.put("startDate", edge.getStartDate());
    parameters.put("endDate", edge.getEndDate());
    parameters.put("dataSource", edge.getDataSource());

    GraphDBService service = GraphDBService.getInstance();
    GraphRequest request = service.getGraphDBRequest();

    service.command(request, statement.toString(), parameters);
  }

}
