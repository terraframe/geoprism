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
package net.geoprism.registry.service.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.system.metadata.MdEdge;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.graph.BusinessEdgeTypeSnapshot;
import net.geoprism.graph.BusinessTypeSnapshot;
import net.geoprism.graph.GeoObjectTypeSnapshot;
import net.geoprism.graph.GraphTypeSnapshot;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;

@Service
public class LabeledPropertyGraphJsonExporterService
{
  public static interface CacheObject
  {
    public TypeSnapshotCacheObject toGeoObject();

    public BusinessTypeSnapshotCacheObject toBusiness();
  }

  private static class TypeSnapshotCacheObject implements CacheObject
  {
    private GeoObjectType type;

    public TypeSnapshotCacheObject(GeoObjectTypeSnapshot snapshot)
    {
      this.type = snapshot.toGeoObjectType();
    }

    @Override
    public TypeSnapshotCacheObject toGeoObject()
    {
      return this;
    }

    @Override
    public BusinessTypeSnapshotCacheObject toBusiness()
    {
      throw new UnsupportedOperationException();
    }

  }

  private static class BusinessTypeSnapshotCacheObject implements CacheObject
  {
    private JsonObject type;

    public BusinessTypeSnapshotCacheObject(BusinessTypeSnapshot snapshot)
    {
      this.type = snapshot.toJSON();
    }

    @Override
    public TypeSnapshotCacheObject toGeoObject()
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public BusinessTypeSnapshotCacheObject toBusiness()
    {
      return this;
    }

  }

  @Autowired
  private LabeledPropertyGraphTypeVersionBusinessServiceIF versionService;

  @Autowired
  private GeoObjectTypeSnapshotBusinessServiceIF           typeService;

  @Autowired
  private BusinessTypeSnapshotBusinessServiceIF            bTypeService;

  public JsonArray getGeoObjects(LabeledPropertyGraphTypeVersion version, Long skip, Integer blockSize)
  {
    return this.getGeoObjects(new HashMap<>(), version, skip, blockSize);
  }

  private JsonArray getGeoObjects(Map<String, CacheObject> cache, LabeledPropertyGraphTypeVersion version, Long skip, Integer blockSize)
  {
    JsonArray geoObjects = new JsonArray();

    GeoObjectTypeSnapshot rootType = this.versionService.getRootType(version);
    MdVertex mdVertex = rootType.getGraphMdVertex();

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDbClassName());
    statement.append(" ORDER BY oid");
    statement.append(" SKIP " + skip);
    statement.append(" LIMIT " + blockSize);

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    List<VertexObject> objects = query.getResults();

    for (VertexObject object : objects)
    {
      // Get the type of the geo object
      GeoObjectType type = getType(cache, version, object.getMdClass());
      GeoObject geoObject = this.typeService.toGeoObject(object, type);

      geoObjects.add(geoObject.toJSON());

    }

    return geoObjects;
  }

  public JsonArray getBusinessObjects(LabeledPropertyGraphTypeVersion version, BusinessTypeSnapshot type, Long skip, Integer blockSize)
  {
    return this.getBusinessObjects(new HashMap<>(), version, type, skip, blockSize);
  }

  private JsonArray getBusinessObjects(Map<String, CacheObject> cache, LabeledPropertyGraphTypeVersion version, BusinessTypeSnapshot type, Long skip, Integer blockSize)
  {

    JsonArray businessObjects = new JsonArray();

    MdVertex mdVertex = type.getGraphMdVertex();

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDbClassName());
    statement.append(" ORDER BY oid");
    statement.append(" SKIP " + skip);
    statement.append(" LIMIT " + blockSize);

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    List<VertexObject> objects = query.getResults();

    for (VertexObject object : objects)
    {
      // Get the type of the geo object
      JsonObject dto = this.bTypeService.toDTO(type, object);

      businessObjects.add(dto);
    }

    return businessObjects;
  }

  private GeoObjectType getType(Map<String, CacheObject> cache, LabeledPropertyGraphTypeVersion version, MdClassDAOIF mdClass)
  {

    if (!cache.containsKey(mdClass.getOid()))
    {
      GeoObjectTypeSnapshot snapshot = this.typeService.get(version, (MdVertexDAOIF) mdClass);

      cache.put(mdClass.getOid(), new TypeSnapshotCacheObject(snapshot));
    }

    return cache.get(mdClass.getOid()).toGeoObject().type;
  }

  private JsonObject getBusinessType(Map<String, CacheObject> cache, LabeledPropertyGraphTypeVersion version, MdClassDAOIF mdClass)
  {

    if (!cache.containsKey(mdClass.getOid()))
    {
      BusinessTypeSnapshot snapshot = this.bTypeService.get(version, (MdVertexDAOIF) mdClass);

      cache.put(mdClass.getOid(), new BusinessTypeSnapshotCacheObject(snapshot));
    }

    return cache.get(mdClass.getOid()).toBusiness().type;
  }

  public JsonArray getEdges(LabeledPropertyGraphTypeVersion version, Long skip, Integer blockSize)
  {
    return this.getEdges(new HashMap<>(), version, skip, blockSize);
  }

  private JsonArray getEdges(Map<String, CacheObject> cache, LabeledPropertyGraphTypeVersion version, Long skip, Integer blockSize)
  {
    JsonArray edges = new JsonArray();

    GraphTypeSnapshot snapshot = this.versionService.getGraphSnapshots(version).get(0);
    MdEdge mdEdge = snapshot.getGraphMdEdge();

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT out.uid AS parentUid, out.@class AS parentClass, in.uid AS childUid, in.@class AS childClass FROM " + mdEdge.getDbClassName());
    statement.append(" ORDER BY oid");
    statement.append(" SKIP " + skip);
    statement.append(" LIMIT " + blockSize);

    GraphQuery<Map<String, Object>> query = new GraphQuery<Map<String, Object>>(statement.toString());
    List<Map<String, Object>> objects = query.getResults();

    for (Map<String, Object> object : objects)
    {
      String parentUid = (String) object.get("parentUid");
      String parentClass = (String) object.get("parentClass");
      String childUid = (String) object.get("childUid");
      String childClass = (String) object.get("childClass");

      GeoObjectType parentType = this.getType(cache, version, MdVertexDAO.getMdGraphClassByTableName(parentClass));
      GeoObjectType childType = this.getType(cache, version, MdVertexDAO.getMdGraphClassByTableName(childClass));

      JsonObject jsonEdge = new JsonObject();
      jsonEdge.addProperty("startNode", parentUid);
      jsonEdge.addProperty("startType", parentType.getCode());
      jsonEdge.addProperty("endNode", childUid);
      jsonEdge.addProperty("endType", childType.getCode());
      jsonEdge.addProperty("type", snapshot.getCode());

      edges.add(jsonEdge);

    }

    return edges;
  }

  public JsonArray getBusinessEdges(LabeledPropertyGraphTypeVersion version, BusinessEdgeTypeSnapshot snapshot, Long skip, Integer blockSize)
  {
    return this.getBusinessEdges(new HashMap<>(), version, snapshot, skip, blockSize);
  }

  private JsonArray getBusinessEdges(Map<String, CacheObject> cache, LabeledPropertyGraphTypeVersion version, BusinessEdgeTypeSnapshot snapshot, Long skip, Integer blockSize)
  {
    JsonArray edges = new JsonArray();

    MdEdge mdEdge = snapshot.getGraphMdEdge();

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT");

    if (snapshot.getIsParentGeoObject())
    {
      statement.append(" out.uid AS parentUid, out.@class AS parentClass" + mdEdge.getDbClassName());
    }
    else
    {
      statement.append(" out.code AS parentUid, out.@class AS parentClass" + mdEdge.getDbClassName());
    }

    if (snapshot.getIsChildGeoObject())
    {
      statement.append(", in.uid AS childUid, in.@class AS childClass" + mdEdge.getDbClassName());
    }
    else
    {
      statement.append(", in.code AS parentUid, in.@class AS childClass" + mdEdge.getDbClassName());
    }

    statement.append(" ORDER BY oid");
    statement.append(" SKIP " + skip);
    statement.append(" LIMIT " + blockSize);

    GraphQuery<Map<String, Object>> query = new GraphQuery<Map<String, Object>>(statement.toString());
    List<Map<String, Object>> objects = query.getResults();

    for (Map<String, Object> object : objects)
    {
      String parentUid = (String) object.get("parentUid");
      String parentClass = (String) object.get("parentClass");
      String childUid = (String) object.get("childUid");
      String childClass = (String) object.get("childClass");

      MdGraphClassDAOIF parentVertex = MdVertexDAO.getMdGraphClassByTableName(parentClass);

      String parentCode = snapshot.getIsParentGeoObject() ? this.getType(cache, version, parentVertex).getCode() : this.getBusinessType(cache, version, parentVertex).get(BusinessTypeSnapshot.CODE).getAsString();

      MdGraphClassDAOIF childVertex = MdVertexDAO.getMdGraphClassByTableName(childClass);

      String childCode = snapshot.getIsChildGeoObject() ? this.getType(cache, version, childVertex).getCode() : this.getBusinessType(cache, version, childVertex).get(BusinessTypeSnapshot.CODE).getAsString();

      JsonObject jsonEdge = new JsonObject();
      jsonEdge.addProperty("startNode", parentUid);
      jsonEdge.addProperty("startType", parentCode);
      jsonEdge.addProperty("endNode", childUid);
      jsonEdge.addProperty("endType", childCode);
      jsonEdge.addProperty("type", snapshot.getCode());

      edges.add(jsonEdge);

    }

    return edges;
  }

  public JsonObject export(LabeledPropertyGraphTypeVersion version)
  {
    Map<String, CacheObject> cache = new HashMap<>();

    JsonArray geoObjects = new JsonArray();
    JsonArray businessObjects = new JsonArray();
    JsonArray edges = new JsonArray();
    JsonArray businessEdges = new JsonArray();

    JsonArray results = new JsonArray();
    final int BLOCK_SIZE = 2000;
    long skip = 0;

    // Get all of the geoObjects
    do
    {
      results = this.getGeoObjects(cache, version, skip, BLOCK_SIZE);

      geoObjects.addAll(results);

      skip += BLOCK_SIZE;
    } while (results.size() > 0);

    // Export all of the business objects
    List<BusinessTypeSnapshot> types = this.versionService.getBusinessTypes(version);

    for (BusinessTypeSnapshot type : types)
    {

      skip = 0;

      // Get all of the business objects
      do
      {
        results = this.getBusinessObjects(cache, version, type, skip, BLOCK_SIZE);

        businessObjects.addAll(results);

        skip += BLOCK_SIZE;
      } while (results.size() > 0);
    }

    // Get all of the edges
    skip = 0;
    do
    {
      results = this.getEdges(cache, version, skip, BLOCK_SIZE);

      edges.addAll(results);

      skip += BLOCK_SIZE;
    } while (results.size() > 0);

    // Get all of the business edges
    List<BusinessEdgeTypeSnapshot> edgeTypes = this.versionService.getBusinessEdgeTypes(version);

    for (BusinessEdgeTypeSnapshot edgeType : edgeTypes)
    {

      skip = 0;
      do
      {

        results = this.getBusinessEdges(cache, version, edgeType, skip, BLOCK_SIZE);

        businessEdges.addAll(results);

        skip += BLOCK_SIZE;
      } while (results.size() > 0);
    }

    JsonObject graph = new JsonObject();
    graph.add("geoObjects", geoObjects);
    graph.add("businessObjects", businessObjects);
    graph.add("edges", edges);
    graph.add("businessEdges", businessEdges);

    return graph;
  }

}
