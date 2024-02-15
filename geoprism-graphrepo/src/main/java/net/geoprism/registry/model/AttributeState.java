package net.geoprism.registry.model;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import com.runwaysdk.business.graph.VertexObject;

public class AttributeState
{
  private String             attributeName;

  private List<VertexObject> values;

  private List<VertexObject> objectsToDelete;

  private boolean            isModified;

  public AttributeState(String attributeName, List<VertexObject> values)
  {
    this.attributeName = attributeName;
    this.values = values;
    this.objectsToDelete = new LinkedList<>();
    this.isModified = false;
  }

  public String getAttributeName()
  {
    return attributeName;
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

  public void persit()
  {
    this.objectsToDelete.forEach(node -> node.delete());
    this.values.forEach(node -> node.apply());
  }

  public void clear()
  {
    this.isModified = false;
    this.objectsToDelete.clear();
  }
}
