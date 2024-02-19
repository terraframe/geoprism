package net.geoprism.registry.model;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdVertexDAOIF;

import net.geoprism.registry.graph.AttributeType;

public class LocalValueNodeStrategy extends ValueNodeStrategy implements ValueStrategy
{

  public LocalValueNodeStrategy(AttributeType type, MdVertexDAOIF nodeVertex, String nodeAttribute)
  {
    super(type, nodeVertex, nodeAttribute);
  }

  @Override
  protected void setNodeValue(VertexObject node, Object value)
  {
    if (value instanceof LocalizedValue)
    {
      LocalizedValue lValue = (LocalizedValue) value;

      // TODO: HEADS UP - Handle all locales
      node.setValue(LocalizedValue.DEFAULT_LOCALE, lValue.getValue(LocalizedValue.DEFAULT_LOCALE));
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  protected <T> T getNodeValue(VertexObject node)
  {
    // TODO: HEADS UP: populate other locales

    return (T) new LocalizedValue(node.getObjectValue(LocalizedValue.DEFAULT_LOCALE));
  }

}
