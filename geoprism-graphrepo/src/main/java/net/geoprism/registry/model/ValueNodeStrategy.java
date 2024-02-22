package net.geoprism.registry.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTimeCollection;

import net.geoprism.registry.graph.AttributeType;
import net.geoprism.registry.graph.AttributeValue;

/**
 * Strategy used for setting a value on an object value node
 * 
 * @author jsmethie
 */
public class ValueNodeStrategy extends AbstractValueStrategy implements ValueStrategy
{
  private MdVertexDAOIF nodeVertex;

  private String        nodeAttribute;

  public ValueNodeStrategy(AttributeType type, MdVertexDAOIF nodeVertex, String nodeAttribute)
  {
    super(type);

    this.nodeVertex = nodeVertex;
    this.nodeAttribute = nodeAttribute;
  }

  private Date getEndDate(VertexObject node)
  {
    return node.getObjectValue(AttributeValue.ENDDATE);
  }

  private Date getStartDate(VertexObject node)
  {
    return node.getObjectValue(AttributeValue.STARTDATE);
  }

  private AttributeState getState(Map<String, AttributeState> valueNodeMap)
  {
    String key = this.getType().getCode();

    valueNodeMap.putIfAbsent(key, new AttributeState(this.getType(), new LinkedList<>()));

    return valueNodeMap.get(key);
  }

  private ValueOverTime toValueOverTime(VertexObject node)
  {
    Date startDate = this.getStartDate(node);
    Date endDate = this.getEndDate(node);
    Object value = this.getNodeValue(node);

    return new ValueOverTime(node.getOid(), startDate, endDate, value);
  }

  @Override
  public void setValue(VertexObject vertex, Map<String, AttributeState> valueNodeMap, Object value, Date startDate, Date endDate)
  {
    AttributeState state = getState(valueNodeMap);

    state.stream().filter(node -> {
      return getStartDate(node).equals(startDate) && getEndDate(node).equals(endDate);
    }).findFirst().ifPresentOrElse(node -> {
      // Update the existing node
      setNodeValue(node, value);
    }, () -> {
      // If not create a new node
      VertexObject node = new VertexObject(nodeVertex.definesType());
      node.setValue(AttributeValue.ATTRIBUTENAME, this.getType().getCode());
      node.setValue(AttributeValue.STARTDATE, startDate);
      node.setValue(AttributeValue.ENDDATE, endDate);

      setNodeValue(node, value);

      state.add(node);
    });
  }

  protected void setNodeValue(VertexObject node, Object value)
  {
    node.setValue(nodeAttribute, value);
  }

  @Override
  public boolean isModified(VertexObject vertex, Map<String, AttributeState> valueNodeMap)
  {
    return getState(valueNodeMap).isModified();
  }

  @Override
  public <T> T getValue(VertexObject vertex, Map<String, AttributeState> valueNodeMap, Date date)
  {
    AttributeState state = getState(valueNodeMap);

    Optional<VertexObject> optional = state.stream().filter(node -> {
      return ( getStartDate(node).before(date) || getStartDate(node).equals(date) ) && ( getEndDate(node).after(date) || getEndDate(node).equals(date) );
    }).findFirst();

    if (optional.isPresent())
    {
      return getNodeValue(optional.get());
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  protected <T> T getNodeValue(VertexObject node)
  {
    return (T) node.getObjectValue(nodeAttribute);
  }

  @Override
  public ValueOverTimeCollection getValueOverTimeCollection(VertexObject vertex, Map<String, AttributeState> valueNodeMap)
  {
    ValueOverTimeCollection collection = new ValueOverTimeCollection();

    getState(valueNodeMap).stream().map(node -> toValueOverTime(node)).forEach(vot -> collection.add(vot, false));

    return collection;
  }

  @Override
  public void setValuesOverTime(VertexObject vertex, Map<String, AttributeState> valueNodeMap, ValueOverTimeCollection collection)
  {
    // First delete all state which do not exist in the collection
    AttributeState state = getState(valueNodeMap);

    state.stream().filter(node -> {
      final Date startDate = getStartDate(node);
      final Date endDate = getEndDate(node);

      return !collection.stream().anyMatch(vot -> {
        return startDate.equals(vot.getStartDate()) && endDate.equals(vot.getEndDate());
      });
    }).forEach(node -> state.delete(node));

    collection.forEach(vot -> this.setValue(vertex, valueNodeMap, vot.getValue(), vot.getStartDate(), vot.getEndDate()));
  }

}
