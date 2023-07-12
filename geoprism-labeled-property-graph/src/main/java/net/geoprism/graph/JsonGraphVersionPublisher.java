package net.geoprism.graph;

import java.util.Map;

import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.GeoObjectJsonAdapters;
import org.commongeoregistry.adapter.metadata.GeoObjectType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.system.metadata.MdEdge;
import com.runwaysdk.system.metadata.MdVertex;

public class JsonGraphVersionPublisher extends AbstractGraphVersionPublisher
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

  private LabeledPropertyGraphSynchronization synchronization;

  private LabeledPropertyGraphTypeVersion     version;

  public JsonGraphVersionPublisher(LabeledPropertyGraphSynchronization synchronization, LabeledPropertyGraphTypeVersion version)
  {
    this.synchronization = synchronization;
    this.version = version;
  }

  public void publish(JsonObject graph)
  {
    Map<String, Object> cache = this.getCache();

    JsonArray array = graph.get("geoObjects").getAsJsonArray();

    for (int i = 0; i < array.size(); i++)
    {
      JsonObject object = array.get(i).getAsJsonObject();

      String typeCode = GeoObjectJsonAdapters.GeoObjectDeserializer.getTypeCode(object);

      String key = "snapshot-" + typeCode;

      if (!cache.containsKey(key))
      {
        GeoObjectTypeSnapshot snapshot = GeoObjectTypeSnapshot.get(version, typeCode);

        cache.put(key, new TypeSnapshotCacheObject(snapshot));
      }

      TypeSnapshotCacheObject cachedObject = (TypeSnapshotCacheObject) cache.get(key);
      GeoObjectTypeSnapshot snapshot = cachedObject.snapshot;

      if (!snapshot.getIsAbstract())
      {
        GeoObject geoObject = GeoObject.fromJSON(cachedObject.type, object.toString());

        this.publish(this.synchronization, cachedObject.mdVertex, geoObject);
      }
    }

    JsonArray edges = graph.get("edges").getAsJsonArray();

    for (int i = 0; i < edges.size(); i++)
    {
      JsonObject object = edges.get(i).getAsJsonObject();
      String parentUid = object.get("startNode").getAsString();
      String parentType = object.get("startType").getAsString();
      String childUid = object.get("endNode").getAsString();
      String childType = object.get("endType").getAsString();
      String typeCode = object.get("type").getAsString();

      String key = "hierarchy-" + typeCode;

      if (!cache.containsKey(key))
      {
        HierarchyTypeSnapshot hierarchy = HierarchyTypeSnapshot.get(this.version, typeCode);

        cache.put(key, new HierarchySnapshotCacheObject(hierarchy));
      }

      HierarchySnapshotCacheObject cachedObject = (HierarchySnapshotCacheObject) cache.get(key);

      VertexObject parent = version.getVertex(parentUid, parentType);
      VertexObject child = version.getVertex(childUid, childType);

      parent.addChild(child, cachedObject.mdEdge.definesType()).apply();
    }
  }

}
