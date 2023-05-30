package net.geoprism.graph;

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
  private LabeledPropertyGraphTypeVersion version;

  public JsonGraphVersionPublisher(LabeledPropertyGraphTypeVersion version)
  {
    this.version = version;
  }

  public void publish(JsonObject graph)
  {
    JsonArray array = graph.get("geoObjects").getAsJsonArray();

    for (int i = 0; i < array.size(); i++)
    {
      JsonObject object = array.get(i).getAsJsonObject();

      String typeCode = GeoObjectJsonAdapters.GeoObjectDeserializer.getTypeCode(object);

      GeoObjectTypeSnapshot snapshot = GeoObjectTypeSnapshot.get(version, typeCode);

      if (!snapshot.getIsAbstract())
      {
        GeoObjectType type = snapshot.toGeoObjectType();

        GeoObject geoObject = GeoObject.fromJSON(type, object.toString());

        MdVertex mdVertex = snapshot.getGraphMdVertex();

        this.publish(mdVertex, geoObject);
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

      HierarchyTypeSnapshot hierarchy = HierarchyTypeSnapshot.get(this.version, typeCode);

      MdEdge mdEdge = hierarchy.getGraphMdEdge();

      VertexObject parent = version.getVertex(parentUid, parentType);
      VertexObject child = version.getVertex(childUid, childType);

      parent.addChild(child, mdEdge.definesType()).apply();
    }
  }

}
