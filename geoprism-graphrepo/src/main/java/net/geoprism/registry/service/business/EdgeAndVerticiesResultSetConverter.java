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
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.graph.ResultSetConverterIF;

import net.geoprism.registry.graph.AttributeValue;
import net.geoprism.registry.graph.GeoVertex;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.graph.VertexServerGeoObject;

public class EdgeAndVerticiesResultSetConverter extends PrefixedResultSetConverter implements ResultSetConverterIF
{
  public static final String VERTEX_IN_PREFIX  = "in";

  public static final String VERTEX_OUT_PREFIX = "out";

  public static final String IN_ATTR_PREFIX    = "inAttr";

  public static final String OUT_ATTR_PREFIX   = "outAttr";

  @Override
  public EdgeAndVerticies convert(GraphRequest request, Object result)
  {
    final OResult oresult = (OResult) result;

    final String inOid = oresult.getProperty(VERTEX_IN_PREFIX + ".oid");
    final String outOid = oresult.getProperty(VERTEX_OUT_PREFIX + ".oid");
    final String edgeClass = oresult.getProperty("edgeClass");
    final String edgeOid = oresult.getProperty("edgeOid");

    if (edgeClass.equals("search_link_default") || ( inOid == null && outOid == null ))
      return null;

    VertexObject goInVertex = null;
    VertexObject inAttrVertex = null;
    if (inOid != null)
    {
      goInVertex = (VertexObject) this.build(VERTEX_IN_PREFIX, oresult);

      inAttrVertex = (VertexObject) this.build(IN_ATTR_PREFIX, oresult);
    }

    VertexObject goOutVertex = null;
    VertexObject outAttrVertex = null;
    if (outOid != null)
    {
      goOutVertex = (VertexObject) this.build(VERTEX_OUT_PREFIX, oresult);

      outAttrVertex = (VertexObject) this.build(OUT_ATTR_PREFIX, oresult);
    }

    return new EdgeAndVerticies(goInVertex, inAttrVertex, goOutVertex, outAttrVertex, edgeClass, edgeOid);
  }

  public static class EdgeAndVerticies
  {
    public String       edgeOid;

    public String       edgeClass;

    public VertexObject inVertex;

    public VertexObject inAttrVertex;

    public VertexObject outVertex;

    public VertexObject outAttrVertex;

    public EdgeAndVerticies(VertexObject inVertex, VertexObject inAttrVertex, VertexObject outVertex, VertexObject outAttrVertex, String edgeClass, String edgeOid)
    {
      super();
      this.edgeOid = edgeOid;
      this.edgeClass = edgeClass;
      this.inVertex = inVertex;
      this.inAttrVertex = inAttrVertex;
      this.outVertex = outVertex;
      this.outAttrVertex = outAttrVertex;
    }
  }

  public static class EdgeAndInOut
  {
    public String            edgeOid;

    public String            edgeClass;

    public ServerGeoObjectIF in;

    public ServerGeoObjectIF out;

    public EdgeAndInOut(ServerGeoObjectIF in, ServerGeoObjectIF out, String edgeClass, String edgeOid)
    {
      super();
      this.edgeOid = edgeOid;
      this.edgeClass = edgeClass;
      this.in = in;
      this.out = out;
    }
  }

  public static List<EdgeAndInOut> convertResults(List<EdgeAndVerticies> results, Date date)
  {
    EdgeAndVerticies previous = null;
    List<VertexObject> currentInAttributes = new LinkedList<>();
    List<VertexObject> currentOutAttributes = new LinkedList<>();
    VertexObject currentInVertex = null;
    VertexObject currentOutVertex = null;
    List<EdgeAndInOut> list = new LinkedList<EdgeAndInOut>();

    for (EdgeAndVerticies result : results)
    {
      if (previous != null && ! ( previous.edgeOid.equals(result.edgeOid) ))
      {
        MdVertexDAOIF mdVertex = (MdVertexDAOIF) currentOutVertex.getMdClass();
        ServerGeoObjectType type = ServerGeoObjectType.get(mdVertex);
        Map<String, List<VertexObject>> nodeMap = currentOutAttributes.stream().collect(Collectors.groupingBy(v -> {
          return (String) v.getObjectValue(AttributeValue.ATTRIBUTENAME);
        }));
        VertexServerGeoObject out = new VertexServerGeoObject(type, currentOutVertex, nodeMap, date);

        MdVertexDAOIF mdVertex2 = (MdVertexDAOIF) currentInVertex.getMdClass();
        ServerGeoObjectType type2 = ServerGeoObjectType.get(mdVertex2);
        Map<String, List<VertexObject>> nodeMap2 = currentInAttributes.stream().collect(Collectors.groupingBy(v -> {
          return (String) v.getObjectValue(AttributeValue.ATTRIBUTENAME);
        }));
        VertexServerGeoObject in = new VertexServerGeoObject(type2, currentInVertex, nodeMap2, date);

        list.add(new EdgeAndInOut(in, out, previous.edgeClass, previous.edgeOid));

        currentInAttributes = new LinkedList<>();
        currentOutAttributes = new LinkedList<>();
        currentInVertex = null;
        currentOutVertex = null;
      }
      else if (result.inAttrVertex != null)
      {
        currentInAttributes.add(result.inAttrVertex);
        currentInVertex = result.inVertex;
      }
      else if (result.outAttrVertex != null)
      {
        currentOutAttributes.add(result.outAttrVertex);
        currentOutVertex = result.outVertex;
      }

      previous = result;
    }

    if (previous != null)
    {
      MdVertexDAOIF mdVertex = (MdVertexDAOIF) currentOutVertex.getMdClass();
      ServerGeoObjectType type = ServerGeoObjectType.get(mdVertex);
      Map<String, List<VertexObject>> nodeMap = currentOutAttributes.stream().collect(Collectors.groupingBy(v -> {
        return (String) v.getObjectValue(AttributeValue.ATTRIBUTENAME);
      }));
      VertexServerGeoObject out = new VertexServerGeoObject(type, currentOutVertex, nodeMap, date);

      MdVertexDAOIF mdVertex2 = (MdVertexDAOIF) currentInVertex.getMdClass();
      ServerGeoObjectType type2 = ServerGeoObjectType.get(mdVertex2);
      Map<String, List<VertexObject>> nodeMap2 = currentInAttributes.stream().collect(Collectors.groupingBy(v -> {
        return (String) v.getObjectValue(AttributeValue.ATTRIBUTENAME);
      }));
      VertexServerGeoObject in = new VertexServerGeoObject(type2, currentInVertex, nodeMap2, date);

      list.add(new EdgeAndInOut(in, out, previous.edgeClass, previous.edgeOid));
    }

    return list;
  }

  public static String vertexColumns(String prefix)
  {
    Set<String> columns = new HashSet<String>();
    columns.add(prefix + ".@class");
    columns.add(prefix + ".@rid");
    columns.add(prefix + ".code");
    columns.add(prefix + ".uid");

    // GeoVertex
    for (String column : new String[] { GeoVertex.SEQ, GeoVertex.CREATEDATE, GeoVertex.LASTUPDATEDATE, GeoVertex.OID })
    {
      columns.add(prefix + "." + column);
    }

    for (ServerGeoObjectType type : ServerGeoObjectType.getAll())
    {
      List<? extends MdAttributeConcreteDAOIF> mdAttrs = type.getMdVertex().getMdClassDAO().definesAttributes();
      for (MdAttributeConcreteDAOIF mdAttr : mdAttrs)
      {
        columns.add(prefix + "." + mdAttr.getColumnName());
      }
    }

    return StringUtils.join(columns, ", ");
  }

  public static Set<String> allAttributeColumns()
  {
    Set<String> columns = new HashSet<String>();
    columns.add("@class");
    columns.add("@rid");

    // AttributeValue
    for (String column : new String[] { AttributeValue.SEQ, AttributeValue.OID, AttributeValue.ATTRIBUTENAME, AttributeValue.STARTDATE, AttributeValue.ENDDATE })
    {
      columns.add(column);
    }

    for (ServerGeoObjectType type : ServerGeoObjectType.getAll())
    {
      for (net.geoprism.registry.graph.AttributeType at : type.definesAttributes())
      {
        for (MdAttributeDAOIF mdAttr : at.getStrategy().getValueAttributes())
        {
          columns.add(mdAttr.getColumnName());
        }
      }
    }

    return columns;
  }
}
