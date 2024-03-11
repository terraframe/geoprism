package net.geoprism.registry.model;

import java.util.Date;
import java.util.Map;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTimeCollection;

public interface ValueStrategy
{

  public void setValue(VertexObject vertex, Object value);

  public void setValue(VertexObject vertex, Map<String, AttributeState> valueNodeMap, Object value, Date startDate, Date endDate);

  public void setValue(VertexObject vertex, Map<String, AttributeState> valueNodeMap, Object value, Date startDate, Date endDate, boolean validate);

  public boolean isModified(VertexObject vertex, Map<String, AttributeState> valueNodeMap);

  public <T> T getValue(VertexObject vertex, Map<String, AttributeState> valueNodeMap, Date date);

  public ValueOverTimeCollection getValueOverTimeCollection(VertexObject vertex, Map<String, AttributeState> valueNodeMap);

  public void setValuesOverTime(VertexObject vertex, Map<String, AttributeState> valueNodeMap, ValueOverTimeCollection collection);
}
