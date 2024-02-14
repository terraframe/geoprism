package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.metadata.AttributeFloatType;
import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;

public class AttributeDoubleType extends AttributeDoubleTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -352072420;

  public AttributeDoubleType()
  {
    super();
  }

  public Integer getPrecision()
  {
    return (Integer) this.getObjectValue(AttributeDoubleType.PRECISION);
  }

  public Integer getScale()
  {
    return (Integer) this.getObjectValue(AttributeDoubleType.SCALE);
  }

  @Override
  protected void populate(MdAttributeConcreteDAO mdAttribute)
  {
    super.populate(mdAttribute);

    mdAttribute.setValue(MdAttributeDoubleInfo.LENGTH, getPrecision());
    mdAttribute.setValue(MdAttributeDoubleInfo.DECIMAL, getScale());
  }

  @Override
  public void fromDTO(AttributeType dto)
  {
    super.fromDTO(dto);

    AttributeFloatType attributeFloatType = (AttributeFloatType) dto;

    this.setValue(AttributeDoubleType.PRECISION, Integer.valueOf(attributeFloatType.getPrecision()));
    this.setValue(AttributeDoubleType.SCALE, Integer.valueOf(attributeFloatType.getScale()));
  }

  @Override
  public AttributeType toDTO()
  {
    AttributeFloatType dto = new AttributeFloatType(this.getCode(), getLocalizedLabel(), getLocalizedDescription(), isAppliedToDb(), isNew(), isAppliedToDb());
    dto.setPrecision(this.getPrecision());
    dto.setScale(this.getScale());

    return dto;
  }

}
