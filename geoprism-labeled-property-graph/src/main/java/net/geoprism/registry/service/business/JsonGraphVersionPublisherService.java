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

import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.GeoObjectJsonAdapters;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.system.metadata.MdEdge;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.graph.BusinessEdgeTypeSnapshot;
import net.geoprism.graph.BusinessTypeSnapshot;
import net.geoprism.graph.GeoObjectTypeSnapshot;
import net.geoprism.graph.HierarchyTypeSnapshot;
import net.geoprism.graph.LabeledPropertyGraphSynchronization;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;

@Service
public class JsonGraphVersionPublisherService extends AbstractGraphVersionPublisherService implements JsonGraphVersionPublisherServiceIF
{
  private static class BusinessTypeSnapshotCacheObject
  {
    private BusinessTypeSnapshot snapshot;

    private MdVertexDAOIF        mdVertex;

    public BusinessTypeSnapshotCacheObject(BusinessTypeSnapshot snapshot)
    {
      this.snapshot = snapshot;
      this.mdVertex = MdVertexDAO.get(snapshot.getGraphMdVertexOid());
    }

  }

  private static class BusinessEdgeTypeSnapshotCacheObject
  {
    private BusinessEdgeTypeSnapshot snapshot;

    private MdEdgeDAOIF              mdEdge;

    public BusinessEdgeTypeSnapshotCacheObject(BusinessEdgeTypeSnapshot snapshot)
    {
      this.snapshot = snapshot;
      this.mdEdge = MdEdgeDAO.get(snapshot.getGraphMdEdgeOid());
    }

  }

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
  private HierarchyTypeSnapshotBusinessServiceIF    hierarchyService;

  @Autowired
  private GeoObjectTypeSnapshotBusinessServiceIF    objectService;

  @Autowired
  private BusinessTypeSnapshotBusinessServiceIF     businessService;

  @Autowired
  private BusinessEdgeTypeSnapshotBusinessServiceIF bEdgeService;

  public void publish(LabeledPropertyGraphSynchronization synchronization, LabeledPropertyGraphTypeVersion version, JsonObject graph)
  {
    State state = new State(synchronization, version);

    this.publishGeoObjects(state, graph.get("geoObjects").getAsJsonArray());
    this.publishBusinessObjects(state, graph.get("businessObjects").getAsJsonArray());
    this.publishEdges(state, graph.get("edges").getAsJsonArray());
    this.publishBusinessEdges(state, graph.get("businessEdges").getAsJsonArray());
  }

  public void publish(State state, VertexObject parent, VertexObject child, MdEdge mdEdge)
  {
    parent.addChild(child, mdEdge.definesType()).apply();
  }

  public void publish(State state, VertexObject parent, VertexObject child, MdEdgeDAOIF mdEdge)
  {
    parent.addChild(child, mdEdge.definesType()).apply();
  }
  
  @Override
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

      publish(state, parent, child, cachedObject.mdEdge);
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

  public void publishBusinessObjects(State state, JsonArray array)
  {
    for (int i = 0; i < array.size(); i++)
    {
      JsonObject object = array.get(i).getAsJsonObject();

      String typeCode = object.get("code").getAsString();

      String key = "b-snapshot-" + typeCode;

      if (!state.cache.containsKey(key))
      {
        BusinessTypeSnapshot snapshot = this.businessService.get(state.version, typeCode);

        state.cache.put(key, new BusinessTypeSnapshotCacheObject(snapshot));
      }

      BusinessTypeSnapshotCacheObject cachedObject = (BusinessTypeSnapshotCacheObject) state.cache.get(key);

      this.publishBusiness(state, cachedObject.mdVertex, object);
    }
  }

  public void publishBusinessEdges(State state,  JsonArray edges)
  {
    for (int i = 0; i < edges.size(); i++)
    {
      JsonObject object = edges.get(i).getAsJsonObject();
      
      String parentUid = object.get("startNode").getAsString();
      String parentType = object.get("startType").getAsString();

      String childUid = object.get("endNode").getAsString();
      String childType = object.get("endType").getAsString();
      
      String typeCode = object.get("type").getAsString();

      String key = "be-" + typeCode;

      if (!state.cache.containsKey(key))
      {
        BusinessEdgeTypeSnapshot hierarchy = this.bEdgeService.get(state.version, typeCode);

        state.cache.put(key, new BusinessEdgeTypeSnapshotCacheObject(hierarchy));
      }

      BusinessEdgeTypeSnapshotCacheObject cachedObject = (BusinessEdgeTypeSnapshotCacheObject) state.cache.get(key);
      BusinessEdgeTypeSnapshot snapshot = cachedObject.snapshot;

      VertexObject parent = snapshot.getIsParentGeoObject() ? this.service.getVertex(state.version, parentUid, parentType) : this.service.getBusinessVertex(state.version, parentUid, parentType);
      VertexObject child = snapshot.getIsChildGeoObject() ? this.service.getVertex(state.version, childUid, childType) : this.service.getBusinessVertex(state.version, childUid, childType);

      publish(state, parent, child, cachedObject.mdEdge);
    }
  }


}
