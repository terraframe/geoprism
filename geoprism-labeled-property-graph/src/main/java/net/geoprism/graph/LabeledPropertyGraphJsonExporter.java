/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism Registry(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.system.metadata.MdEdge;
import com.runwaysdk.system.metadata.MdVertex;

public class LabeledPropertyGraphJsonExporter
{
  private static class TypeSnapshotCacheObject
  {
    private GeoObjectType         type;

    public TypeSnapshotCacheObject(GeoObjectTypeSnapshot snapshot)
    {
      this.type = snapshot.toGeoObjectType();
    }

  }

  final private Logger                          logger = LoggerFactory.getLogger(LabeledPropertyGraphJsonExporter.class);

  final private LabeledPropertyGraphTypeVersion version;

  private Map<String, TypeSnapshotCacheObject>  cache;

  public LabeledPropertyGraphJsonExporter(LabeledPropertyGraphTypeVersion version)
  {
    this.version = version;
    this.cache = new HashMap<String, TypeSnapshotCacheObject>();
  }

  public JsonArray getGeoObjects(Long skip, Integer blockSize)
  {
    JsonArray geoObjects = new JsonArray();

    MdVertex mdVertex = this.version.getRootType().getGraphMdVertex();

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
      GeoObjectType type = getType(object.getMdClass());
      GeoObject geoObject = GeoObjectTypeSnapshot.toGeoObject(object, type);

      geoObjects.add(geoObject.toJSON());

    }

    return geoObjects;
  }

  private GeoObjectType getType(MdClassDAOIF mdClass)
  {
    if (!cache.containsKey(mdClass.getOid()))
    {
      GeoObjectTypeSnapshot snapshot = GeoObjectTypeSnapshot.get(version, (MdVertexDAOIF) mdClass);

      cache.put(mdClass.getOid(), new TypeSnapshotCacheObject(snapshot));
    }

    GeoObjectType type = cache.get(mdClass.getOid()).type;
    return type;
  }

  public JsonArray getEdges(Long skip, Integer blockSize)
  {
    JsonArray edges = new JsonArray();

    HierarchyTypeSnapshot hierarchy = this.version.getHierarchies().get(0);
    MdEdge mdEdge = hierarchy.getGraphMdEdge();

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT out.uuid AS parentUid, out.@class AS parentClass, in.uuid AS childUid, in.@class AS childClass FROM " + mdEdge.getDbClassName());
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

      GeoObjectType parentType = this.getType(MdVertexDAO.getMdGraphClassByTableName(parentClass));
      GeoObjectType childType = this.getType(MdVertexDAO.getMdGraphClassByTableName(childClass));

      JsonObject jsonEdge = new JsonObject();
      jsonEdge.addProperty("startNode", parentUid);
      jsonEdge.addProperty("startType", parentType.getCode());
      jsonEdge.addProperty("endNode", childUid);
      jsonEdge.addProperty("endType", childType.getCode());
      jsonEdge.addProperty("type", hierarchy.getCode());

      edges.add(jsonEdge);

    }

    return edges;
  }

  public JsonObject export()
  {
    JsonArray geoObjects = new JsonArray();
    JsonArray edges = new JsonArray();

    JsonArray results = new JsonArray();
    final int BLOCK_SIZE = 2000;
    long skip = 0;

    // Get all of the geoObjects
    do
    {
      results = this.getGeoObjects(skip, BLOCK_SIZE);

      geoObjects.addAll(results);

      skip += BLOCK_SIZE;
    } while (results.size() > 0);

    // Get all of the edges
    skip = 0;
    do
    {
      results = this.getEdges(skip, BLOCK_SIZE);

      edges.addAll(results);

      skip += BLOCK_SIZE;
    } while (results.size() > 0);

    JsonObject graph = new JsonObject();
    graph.add("geoObjects", geoObjects);
    graph.add("edges", edges);

    return graph;
  }

}
