package net.geoprism.registry.model;

import com.runwaysdk.business.graph.VertexObject;

import net.geoprism.registry.graph.AttributeType;

public abstract class AbstractValueStrategy implements ValueStrategy
{
  private AttributeType type;

  public AbstractValueStrategy(AttributeType type)
  {
    this.type = type;
  }

  public AttributeType getType()
  {
    return type;
  }

  public void setValue(VertexObject vertex, Object value)
  {
    this.setValue(vertex, null, value, null, null);
  }
}
