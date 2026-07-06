package net.geoprism.graph;

import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.view.ConceptClassDTO;

public class ConceptClassSnapshot extends ConceptClassSnapshotBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -130238130;

  public ConceptClassSnapshot()
  {
    super();
  }

  @Override
  public String getKey()
  {
    return super.getCode();
  }

  @Override
  public String toString()
  {
    return this.getCode();
  }

  public ConceptClassDTO toDTO()
  {
    ConceptClassDTO typeObject = new ConceptClassDTO();
    typeObject.setCode(this.getCode());
    typeObject.setOrganization(this.getOrgCode());
    typeObject.setOrigin(this.getOrigin());
    typeObject.setSequence(this.getSequence());
    typeObject.setDisplayLabel(LocalizedValueConverter.convertNoAutoCoalesce(this.getDisplayLabel()));
    typeObject.setAttributes(this.getAttributeTypes());

    return typeObject;
  }

}
