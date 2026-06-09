/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service.business;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.orientechnologies.orient.core.sql.executor.OResult;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.graph.ResultSetConverterIF;

import net.geoprism.registry.graph.AttributeValue;
import net.geoprism.registry.graph.GeoVertex;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.graph.VertexServerGeoObject;

public class VertexAndEdgeResultSetConverter extends PrefixedResultSetConverter implements ResultSetConverterIF
{
  public static final String VERTEX_PREFIX = "v";

  public static final String ATTR_PREFIX   = "attr";

  @Override
  public VertexAndEdge convert(GraphRequest request, Object result)
  {
    final OResult oresult = (OResult) result;

    final String geoObjectOid = oresult.getProperty(VERTEX_PREFIX + ".oid");
    final String attrOid = oresult.getProperty(ATTR_PREFIX + ".oid");
    final String edgeClass = oresult.getProperty("edgeClass");
    final String edgeOid = oresult.getProperty("edgeOid");
    final String edgeUid = oresult.getProperty("edgeUid");
    final String edgeSource = oresult.getProperty("edgeSource");

    if (edgeClass.equals("search_link_default") || ( geoObjectOid == null && attrOid == null ))
      return null;

    final VertexObject goVertex = (VertexObject) this.build(VERTEX_PREFIX, oresult);
    final VertexObject attrVertex = (VertexObject) this.build(ATTR_PREFIX, oresult);

    return new VertexAndEdge(goVertex, attrVertex, geoObjectOid, edgeClass, edgeOid, edgeUid, edgeSource);
  }

  public static class VertexAndEdge
  {
    public String       edgeOid;

    public String       edgeUid;

    public String       edgeClass;

    public String       edgeSource;

    public VertexObject goVertex;

    public VertexObject attrVertex;

    public VertexAndEdge(VertexObject goVertex, VertexObject attrVertex, String geoObjectOid, String edgeClass, String edgeOid, String edgeUid, String edgeSource)
    {
      super();
      this.goVertex = goVertex;
      this.attrVertex = attrVertex;
      this.edgeOid = edgeOid;
      this.edgeClass = edgeClass;
      this.edgeUid = edgeUid;
      this.edgeSource = edgeSource;
    }
  }

  public static class GeoObjectAndEdge
  {
    public String            edgeOid;

    public String            edgeUid;

    public String            edgeClass;

    public String            edgeSource;

    public ServerGeoObjectIF geoObject;

    public GeoObjectAndEdge(ServerGeoObjectIF geoObject, String edgeClass, String edgeOid, String edgeUid, String edgeSource)
    {
      super();
      this.edgeOid = edgeOid;
      this.edgeUid = edgeUid;
      this.edgeClass = edgeClass;
      this.edgeSource = edgeSource;
      this.geoObject = geoObject;
    }
  }

  public static List<GeoObjectAndEdge> convertResults(List<VertexAndEdge> results, Date date)
  {
    VertexAndEdge previous = null;
    List<VertexObject> currentAttributes = new LinkedList<>();
    List<GeoObjectAndEdge> list = new LinkedList<GeoObjectAndEdge>();

    for (VertexAndEdge result : results)
    {
      if (previous != null && ! ( previous.goVertex.getOid().equals(result.goVertex.getOid()) && previous.edgeOid.equals(result.edgeOid) ))
      {
        MdVertexDAOIF mdVertex = (MdVertexDAOIF) previous.goVertex.getMdClass();
        ServerGeoObjectType type = ServerGeoObjectType.get(mdVertex);

        Map<String, List<VertexObject>> nodeMap = currentAttributes.stream().collect(Collectors.groupingBy(v -> {
          return (String) v.getObjectValue(AttributeValue.ATTRIBUTENAME);
        }));

        VertexServerGeoObject vsgo = new VertexServerGeoObject(type, previous.goVertex, nodeMap, date);
        list.add(new GeoObjectAndEdge(vsgo, previous.edgeClass, previous.edgeOid, previous.edgeUid, previous.edgeSource));
        currentAttributes = new LinkedList<>();
      }

      currentAttributes.add(result.attrVertex);

      previous = result;
    }

    if (previous != null)
    {
      MdVertexDAOIF mdVertex = (MdVertexDAOIF) previous.goVertex.getMdClass();
      ServerGeoObjectType type = ServerGeoObjectType.get(mdVertex);

      Map<String, List<VertexObject>> nodeMap = currentAttributes.stream().collect(Collectors.groupingBy(v -> {
        return (String) v.getObjectValue(AttributeValue.ATTRIBUTENAME);
      }));

      VertexServerGeoObject vsgo = new VertexServerGeoObject(type, previous.goVertex, nodeMap, date);
      list.add(new GeoObjectAndEdge(vsgo, previous.edgeClass, previous.edgeOid, previous.edgeUid, previous.edgeSource));
    }

    return list;
  }

  public static String geoVertexColumns(MdGraphClassDAOIF mdClass)
  {
    Set<String> columns = new HashSet<String>();
    columns.add(VERTEX_PREFIX + ".@class");
    columns.add(VERTEX_PREFIX + ".@rid");

    // GeoVertex
    for (String column : new String[] { GeoVertex.SEQ, GeoVertex.CREATEDATE, GeoVertex.LASTUPDATEDATE, GeoVertex.OID })
    {
      columns.add(VERTEX_PREFIX + "." + column);
    }

    List<? extends MdAttributeConcreteDAOIF> mdAttrs = mdClass.definesAttributes();
    for (MdAttributeConcreteDAOIF mdAttr : mdAttrs)
    {
      columns.add(VERTEX_PREFIX + "." + mdAttr.getColumnName());
    }

    return StringUtils.join(columns, ", ");
  }

  public static String geoVertexAttributeColumns(List<net.geoprism.registry.graph.AttributeType> types)
  {
    Set<String> columns = new HashSet<String>();
    columns.add(ATTR_PREFIX + ".@class");
    columns.add(ATTR_PREFIX + ".@rid");

    // AttributeValue
    for (String column : new String[] { AttributeValue.SEQ, AttributeValue.OID, AttributeValue.ATTRIBUTENAME, AttributeValue.STARTDATE, AttributeValue.ENDDATE })
    {
      columns.add(ATTR_PREFIX + "." + column);
    }

    for (net.geoprism.registry.graph.AttributeType at : types)
    {
      for (MdAttributeDAOIF mdAttr : at.getStrategy().getValueAttributes())
      {
        columns.add(ATTR_PREFIX + "." + mdAttr.getColumnName());
      }
    }

    return StringUtils.join(columns, ", ");
  }
}
