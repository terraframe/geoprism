package net.geoprism.graph;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.query.OIterator;

import net.geoprism.registry.conversion.AttributeTypeConverter;

public class DirectedAcyclicGraphTypeSnapshot extends DirectedAcyclicGraphTypeSnapshotBase implements GraphTypeSnapshot
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1867928283;
  
  public DirectedAcyclicGraphTypeSnapshot()
  {
    super();
  }

  @Override
  public String getTypeCode()
  {
    return GraphTypeSnapshot.getTypeCode(this);
  }

  @Override
  public JsonObject toJSON(GeoObjectTypeSnapshot root)
  {
    JsonArray nodes = new JsonArray();

    try (OIterator<? extends GeoObjectTypeSnapshot> it = root.getAllChildSnapshot())
    {
      it.forEach(snapshot -> {
        nodes.add(this.toNode(snapshot));
      });
    }

    JsonObject hierarchyObject = new JsonObject();
    hierarchyObject.addProperty(HierarchyTypeSnapshotBase.CODE, this.getCode());
    hierarchyObject.addProperty(GraphTypeSnapshot.TYPE_CODE, GraphTypeSnapshot.DIRECTED_ACYCLIC_GRAPH_TYPE);
    hierarchyObject.add(HierarchyTypeSnapshotBase.DISPLAYLABEL, AttributeTypeConverter.convertNoAutoCoalesce(this.getDisplayLabel()).toJSON());
    hierarchyObject.add(HierarchyTypeSnapshotBase.DESCRIPTION, AttributeTypeConverter.convertNoAutoCoalesce(this.getDescription()).toJSON());
    hierarchyObject.add("nodes", nodes);

    return hierarchyObject;
  }
  
  private JsonObject toNode(GeoObjectTypeSnapshot snapshot)
  {
    JsonArray children = new JsonArray();

    try (OIterator<? extends GeoObjectTypeSnapshot> it = snapshot.getAllChildSnapshot())
    {
      it.forEach(child -> {
        children.add(this.toNode(child));
      });
    }

    JsonObject node = new JsonObject();
    node.addProperty(GeoObjectTypeSnapshot.CODE, snapshot.getCode());
    node.add("nodes", children);

    return node;
  }
}
