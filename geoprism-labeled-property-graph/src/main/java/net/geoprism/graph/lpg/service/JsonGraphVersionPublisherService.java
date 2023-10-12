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
package net.geoprism.graph.lpg.service;

import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.GeoObjectJsonAdapters;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.system.metadata.MdEdge;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.graph.GeoObjectTypeSnapshot;
import net.geoprism.graph.HierarchyTypeSnapshot;
import net.geoprism.graph.LabeledPropertyGraphSynchronization;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.graph.lpg.business.GeoObjectTypeSnapshotBusinessServiceIF;
import net.geoprism.graph.lpg.business.HierarchyTypeSnapshotBusinessServiceIF;

@Service
public class JsonGraphVersionPublisherService extends AbstractGraphVersionPublisherService
{
  private static class TypeSnapshotCacheObject
  {
    private GeoObjectTypeSnapshot snapshot;

    private GeoObjectType         type;

    private MdVertex              mdVertex;

    public TypeSnapshotCacheObject(GeoObjectTypeSnapshot snapshot)
    {
      this.snapshot = snapshot;
      this.type = snapshot.toGeoObjectType();
      this.mdVertex = snapshot.getGraphMdVertex();
    }

  }

  private static class HierarchySnapshotCacheObject
  {
    private HierarchyTypeSnapshot snapshot;

    private MdEdge                mdEdge;

    public HierarchySnapshotCacheObject(HierarchyTypeSnapshot snapshot)
    {
      this.snapshot = snapshot;
      this.mdEdge = snapshot.getGraphMdEdge();
    }
  }

  @Autowired
  private HierarchyTypeSnapshotBusinessServiceIF hierarchyService;

  @Autowired
  private GeoObjectTypeSnapshotBusinessServiceIF objectService;

  public void publish(LabeledPropertyGraphSynchronization synchronization, LabeledPropertyGraphTypeVersion version, JsonObject graph)
  {
    State state = new State(synchronization, version);

    this.publishGeoObjects(state, graph.get("geoObjects").getAsJsonArray());
    this.publishEdges(state, graph.get("edges").getAsJsonArray());
  }

  public void publishEdges(State state, JsonArray edges)
  {
    for (int i = 0; i < edges.size(); i++)
    {
      JsonObject object = edges.get(i).getAsJsonObject();
      String parentUid = object.get("startNode").getAsString();
      String parentType = object.get("startType").getAsString();
      String childUid = object.get("endNode").getAsString();
      String childType = object.get("endType").getAsString();
      String typeCode = object.get("type").getAsString();

      String key = "hierarchy-" + typeCode;

      if (!state.cache.containsKey(key))
      {
        HierarchyTypeSnapshot hierarchy = this.hierarchyService.get(state.version, typeCode);

        state.cache.put(key, new HierarchySnapshotCacheObject(hierarchy));
      }

      HierarchySnapshotCacheObject cachedObject = (HierarchySnapshotCacheObject) state.cache.get(key);

      VertexObject parent = this.service.getVertex(state.version, parentUid, parentType);
      VertexObject child = this.service.getVertex(state.version, childUid, childType);

      parent.addChild(child, cachedObject.mdEdge.definesType()).apply();
    }
  }

  public void publishGeoObjects(State state, JsonArray array)
  {
    for (int i = 0; i < array.size(); i++)
    {
      JsonObject object = array.get(i).getAsJsonObject();

      String typeCode = GeoObjectJsonAdapters.GeoObjectDeserializer.getTypeCode(object);

      String key = "snapshot-" + typeCode;

      if (!state.cache.containsKey(key))
      {
        GeoObjectTypeSnapshot snapshot = this.objectService.get(state.version, typeCode);

        state.cache.put(key, new TypeSnapshotCacheObject(snapshot));
      }

      TypeSnapshotCacheObject cachedObject = (TypeSnapshotCacheObject) state.cache.get(key);
      GeoObjectTypeSnapshot snapshot = cachedObject.snapshot;

      if (!snapshot.getIsAbstract())
      {
        GeoObject geoObject = GeoObject.fromJSON(cachedObject.type, object.toString());

        this.publish(state, cachedObject.mdVertex, geoObject);
      }
    }
  }

}
