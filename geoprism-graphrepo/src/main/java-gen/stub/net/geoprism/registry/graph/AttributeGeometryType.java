package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.metadata.AttributeType;

import net.geoprism.registry.model.ValueStrategy;

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
  
  @Override
  public ValueStrategy getStrategy()
  {
    throw new UnsupportedOperationException();
    // TODO: HEADS UP 
//    if (!this.getIsChangeOverTime())
//    {
//      return new VertexValueStrategy(this);
//    }
//    else
//    {
//      return new ValueNodeStrategy(this, MdVertexDAO.getMdVertexDAO(AttributeCharacterValue.CLASS), AttributeCharacterValue.VALUE);
//    }
  }


}
