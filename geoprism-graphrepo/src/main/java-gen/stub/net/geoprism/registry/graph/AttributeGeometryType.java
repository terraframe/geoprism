package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.metadata.AttributeType;

public class AttributeGeometryType extends AttributeGeometryTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1252972775;

  public AttributeGeometryType()
  {
    super();
  }

  @Override
  public AttributeType toDTO()
  {
    return new org.commongeoregistry.adapter.metadata.AttributeGeometryType(this.getCode(), getLocalizedLabel(), getLocalizedDescription(), isAppliedToDb(), isNew(), isAppliedToDb());
  }

}
