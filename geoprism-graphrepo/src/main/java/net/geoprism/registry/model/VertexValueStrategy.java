package net.geoprism.registry.model;

import java.util.Date;
import java.util.Map;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTimeCollection;

import net.geoprism.registry.graph.AttributeType;

/**
 * Strategy used for setting a value directly on the vertex object as opposed to
 * an object value node
 * 
 * @author jsmethie
 */
public class VertexValueStrategy extends AbstractValueStrategy implements ValueStrategy
{

  public VertexValueStrategy(AttributeType type)
  {
    super(type);
  }

  @Override
  public void setValue(VertexObject vertex, Map<String, AttributeState> valueNodeMap, Object value, Date startDate, Date endDate, boolean validate)
  {
    vertex.setValue(this.getType().getCode(), value);
  }

  @Override
  public <T> T getValue(VertexObject vertex, Map<String, AttributeState> valueNodeMap, Date date)
  {
    return vertex.getObjectValue(this.getType().getCode());
  }

  @Override
  public boolean isModified(VertexObject vertex, Map<String, AttributeState> valueNodeMap)
  {
    return vertex.isModified(this.getType().getCode());
  }

  @Override
  public ValueOverTimeCollection getValueOverTimeCollection(VertexObject vertex, Map<String, AttributeState> valueNodeMap)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setValuesOverTime(VertexObject vertex, Map<String, AttributeState> valueNodeMap, ValueOverTimeCollection collection)
  {
    throw new UnsupportedOperationException();
  }
}
