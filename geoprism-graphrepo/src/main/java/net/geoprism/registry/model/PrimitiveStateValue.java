package net.geoprism.registry.model;

import com.runwaysdk.business.graph.VertexObject;

public class PrimitiveStateValue extends StateValue
{
  private String nodeAttribute;

  public PrimitiveStateValue(VertexObject node, String nodeAttribute)
  {
    super(node);
    
    this.nodeAttribute = nodeAttribute;
  }

  public <T> T getValue()
  {
    return this.getValue(this.nodeAttribute);
  }

  public void setValue(Object value)
  {
    this.setValue(nodeAttribute, value);
  }

}
