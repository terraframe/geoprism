package net.geoprism.registry.model;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import com.runwaysdk.business.graph.VertexObject;

import net.geoprism.registry.graph.AttributeGeometryType;
import net.geoprism.registry.graph.AttributeType;

public class AttributeState
{
  private AttributeType      attributeType;

  private List<VertexObject> values;

  private List<VertexObject> objectsToDelete;

  private boolean            isModified;

  public AttributeState(AttributeType attributeType, List<VertexObject> values)
  {
    this.attributeType = attributeType;
    this.values = values;
    this.objectsToDelete = new LinkedList<>();
    this.isModified = false;
  }

  public AttributeType getAttributeType()
  {
    return attributeType;
  }

  public Stream<VertexObject> stream()
  {
    return this.values.stream();
  }

  public boolean isModified()
  {
    return isModified;
  }

  public void add(VertexObject node)
  {
    this.isModified = true;

    this.values.add(node);
  }

  public void delete(VertexObject node)
  {
    this.isModified = true;

    this.objectsToDelete.add(node);

    this.values.remove(node);
  }

  public void persit(VertexObject vertex)
  {
    this.objectsToDelete.forEach(node -> node.delete());
    this.values.forEach(node -> {
      node.apply();

      if (attributeType instanceof AttributeGeometryType)
      {
        vertex.addChild(node, "net.geoprism.registry.graph.HasGeometry").apply();
      }
      else
      {
        vertex.addChild(node, "net.geoprism.registry.graph.HasValue").apply();
      }
    });
  }

  public void delete()
  {
    this.objectsToDelete.forEach(node -> node.delete());
    this.values.forEach(node -> node.delete());
  }

  public void clear()
  {
    this.isModified = false;
    this.objectsToDelete.clear();
  }
}
