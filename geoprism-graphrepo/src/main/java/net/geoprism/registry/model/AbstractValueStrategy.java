package net.geoprism.registry.model;

import java.util.Date;
import java.util.Map;

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
  
  @Override
  public void setValue(VertexObject vertex, Map<String, AttributeState> valueNodeMap, Object value, Date startDate, Date endDate)
  {
    this.setValue(vertex, valueNodeMap, value, startDate, endDate, true);
  }

  public void setValue(VertexObject vertex, Object value)
  {
    this.setValue(vertex, null, value, null, null, true);
  }
}
