package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.metadata.AttributeFloatType;
import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.constants.MdAttributeDoubleInfo;

public class AttributeDoubleType extends AttributeDoubleTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -352072420;
  
  public AttributeDoubleType()
  {
    super();
  }
  
  
  @Override
  public void fromDTO(AttributeType dto)
  {
    AttributeFloatType attributeFloatType = (AttributeFloatType) dto;

    mdAttribute.setValue(MdAttributeDoubleInfo.LENGTH, Integer.toString(attributeFloatType.getPrecision()));
    mdAttribute.setValue(MdAttributeDoubleInfo.DECIMAL, Integer.toString(attributeFloatType.getScale()));
  }
  
}
