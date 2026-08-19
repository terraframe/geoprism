package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import net.geoprism.registry.conversion.LocalizedValueConverter;

public abstract class ConceptSet extends ConceptSetBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 863915630;

  public ConceptSet()
  {
    super();
  }

  public LocalizedValue getLabel()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(DISPLAYLABEL));
  }

  public LocalizedValue getDescriptionLV()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(DESCRIPTION));
  }
  
}
