/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism Registry(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.etl.export;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.GeoObjectJsonAdapters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;

import net.geoprism.registry.graph.GeoVertex;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.model.graph.VertexServerGeoObject;
import net.geoprism.registry.service.ServiceFactory;

public class GeoObjectJsonExporter
{
  final private Logger              logger     = LoggerFactory.getLogger(GeoObjectJsonExporter.class);

  final private ServerGeoObjectType got;

  final private ServerHierarchyType hierarchyType;

  private Integer                   pageSize   = null;

  private Integer                   pageNumber = null;

  private Long                      total      = null;

  private Date                      since;

  private Boolean                   includeLevel;

  private GeoObjectExportFormat     format;

  private Map<Type, Object> typeAdapters = null;

  // private DHIS2Facade dhis2;
  //
  // private SyncLevel syncLevel;

  public GeoObjectJsonExporter(String gotCode, String hierarchyCode, Date since, Boolean includeLevel, GeoObjectExportFormat format, Map<Type, Object> typeAdapters, Integer pageSize, Integer pageNumber)
  {
    this.got = ServerGeoObjectType.get(gotCode);
    this.hierarchyType = ServerHierarchyType.get(hierarchyCode);
    this.since = since;
    this.includeLevel = includeLevel == null ? Boolean.FALSE : includeLevel;
    this.format = format == null ? GeoObjectExportFormat.JSON_CGR : format;
    this.pageSize = pageSize;
    this.pageNumber = pageNumber;
    this.typeAdapters = typeAdapters;
    
    init();
  }

  public GeoObjectJsonExporter(ServerGeoObjectType got, ServerHierarchyType hierarchyType, Date since, Boolean includeLevel, GeoObjectExportFormat format, Map<Type, Object> typeAdapters, Integer pageSize, Integer pageNumber)
  {
    this.got = got;
    this.hierarchyType = hierarchyType;
    this.typeAdapters = typeAdapters;
    this.since = since;
    this.includeLevel = includeLevel == null ? Boolean.FALSE : includeLevel;
    this.format = format == null ? GeoObjectExportFormat.JSON_CGR : format;
    this.pageSize = pageSize;
    this.pageNumber = pageNumber;

    init();
  }

  public void init()
  {
    if (this.pageSize == null || this.pageSize == 0 || this.pageSize > 1000)
    {
      this.pageSize = 1000;
    }
    if (this.pageNumber == null || this.pageNumber == 0)
    {
      this.pageNumber = 1;
    }

    ServiceFactory.getGeoObjectPermissionService().enforceCanRead(this.got.getOrganization().getCode(), this.got);
  }

  public List<VertexServerGeoObject> query()
  {
    MdVertexDAOIF mdVertex = got.getMdVertex();
    MdAttributeDAOIF mdAttribute = MdAttributeDAO.getByKey(GeoVertex.CLASS + "." + GeoVertex.LASTUPDATEDATE);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());

    if (this.since != null)
    {

      statement.append(" WHERE " + mdAttribute.getColumnName() + " >= :lastUpdateDate");
    }

    statement.append(" ORDER BY " + mdAttribute.getColumnName() + ", oid ASC");

    if (this.pageSize != null && this.pageNumber != null && this.pageSize != -1 && this.pageNumber != -1)
    {
      statement.append(" SKIP " + ( ( pageNumber - 1 ) * pageSize ) + " LIMIT " + this.pageSize);
    }

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());

    if (this.since != null)
    {
      query.setParameter("lastUpdateDate", this.since);
    }

    List<VertexObject> vObjects = query.getResults();

    List<VertexServerGeoObject> response = new LinkedList<VertexServerGeoObject>();

    for (VertexObject vObject : vObjects)
    {
      VertexServerGeoObject vSGO = new VertexServerGeoObject(got, vObject);
      vSGO.setDate(ValueOverTime.INFINITY_END_DATE);

      response.add(vSGO);
    }

    return response;
  }

  public Long count()
  {
    MdVertexDAOIF mdVertex = got.getMdVertex();
    MdAttributeDAOIF mdAttribute = MdAttributeDAO.getByKey(GeoVertex.CLASS + "." + GeoVertex.LASTUPDATEDATE);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT COUNT(*) FROM " + mdVertex.getDBClassName());

    if (this.since != null)
    {
      statement.append(" WHERE " + mdAttribute.getColumnName() + " >= :lastUpdateDate");
    }

    statement.append(" ORDER BY " + mdAttribute.getColumnName() + ", oid ASC");

    GraphQuery<Long> query = new GraphQuery<Long>(statement.toString());

    if (this.since != null)
    {
      query.setParameter("lastUpdateDate", this.since);
    }

    return query.getSingleResult();
  }

  protected JsonElement exportObject(VertexServerGeoObject go) throws IOException
  {
    GsonBuilder builder = new GsonBuilder();

    if (typeAdapters == null)
    {
      typeAdapters = new HashMap<Type, Object>();
      typeAdapters.put(VertexServerGeoObject.class, new SeverGeoObjectJsonAdapters.ServerGeoObjectSerializer(this.since));
      typeAdapters.put(GeoObject.class, new GeoObjectJsonAdapters.GeoObjectSerializer());
    }
    
    for (Entry<Type,Object> e : typeAdapters.entrySet())
    {
      builder.registerTypeAdapter(e.getKey(), e.getValue());
    }
    
    // builder.registerTypeAdapter(GeoObject.class, new
    // DHIS2GeoObjectJsonAdapters.DHIS2Serializer(this.dhis2, this.syncLevel,
    // this.got, this.hierarchyType, this.externalSystem));

    return builder.create().toJsonTree(go, go.getClass());
  }

  public JsonObject export() throws IOException
  {
    JsonObject jo = new JsonObject();

    JsonArray results = new JsonArray();

    this.total = this.count();

    List<VertexServerGeoObject> objects = this.query();

    for (VertexServerGeoObject object : objects)
    {
      results.add(this.exportObject(object));
    }

    jo.add("results", results);

    JsonObject page = new JsonObject();
    page.addProperty("pageSize", this.pageSize);
    page.addProperty("pageNumber", this.pageNumber);
    page.addProperty("total", this.count());
    jo.add("page", page);

    return jo;
  }
}
