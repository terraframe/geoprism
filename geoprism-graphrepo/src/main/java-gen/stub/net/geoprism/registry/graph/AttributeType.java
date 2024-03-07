package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.ValueStrategy;

public abstract class AttributeType extends AttributeTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -1935054848;

  public AttributeType()
  {
    super();
  }

  public abstract org.commongeoregistry.adapter.metadata.AttributeType toDTO();

  public abstract ValueStrategy getStrategy();

  @Override
  public GeoObjectType getGeoObjectType()
  {
    String oid = this.getObjectValue(GEOOBJECTTYPE);

    return GeoObjectType.get(oid);
  }

  public LocalizedValue getLocalizedLabel()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(LABEL));
  }

  public LocalizedValue getLocalizedDescription()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(DESCRIPTION));
  }
  
  protected void populate(org.commongeoregistry.adapter.metadata.AttributeType dto)
  {
    dto.setIsChangeOverTime(this.getIsChangeOverTime());
  }

  protected void populate(MdAttributeConcreteDAO mdAttribute)
  {
    GeoObjectType type = this.getGeoObjectType();
    MdVertex mdVertex = type.getMdVertex();

    mdAttribute.setValue(MdAttributeConcreteInfo.NAME, this.getCode());
    LocalizedValueConverter.populate(mdAttribute, MdAttributeBooleanInfo.DISPLAY_LABEL, this.getLocalizedLabel());
    LocalizedValueConverter.populate(mdAttribute, MdAttributeBooleanInfo.DESCRIPTION, this.getLocalizedDescription());
    mdAttribute.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdVertex.getOid());
    mdAttribute.setValue(MdAttributeConcreteInfo.REQUIRED, this.getRequired());

    if (this.getUnique())
    {
      mdAttribute.addItem(MdAttributeConcreteInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
    }
  }

  public void fromDTO(org.commongeoregistry.adapter.metadata.AttributeType dto)
  {
    this.setCode(dto.getName());
    this.setRequired(dto.isRequired());
    this.setUnique(dto.isUnique());
    this.setIsChangeOverTime(dto.isChangeOverTime());

    LocalizedValueConverter.populate(this, AttributeType.LABEL, dto.getLabel());
    LocalizedValueConverter.populate(this, AttributeType.DESCRIPTION, dto.getDescription());

    // TODO: HEADS UP
    // if (dto instanceof AttributeClassificationType)
    // {
    // // Refresh the terms
    // MdAttributeClassification mdAttributeTerm = (MdAttributeClassification)
    // mdAttribute;
    //
    // AttributeClassificationType attributeClassificationType =
    // (AttributeClassificationType) dto;
    // String classificationTypeCode =
    // attributeClassificationType.getClassificationType();
    //
    // ClassificationType classificationType =
    // this.cTypeService.getByCode(classificationTypeCode);
    //
    // Term root = attributeClassificationType.getRootTerm();
    //
    // if (root != null)
    // {
    // Classification classification = this.cService.get(classificationType,
    // root.getCode());
    //
    // mdAttributeTerm.setValue(MdAttributeClassification.ROOT,
    // classification.getOid());
    // }
    // }
    //
    // if (dto instanceof AttributeTermType)
    // {
    // // Refresh the terms
    // AttributeTermType attributeTermType = (AttributeTermType) dto;
    //
    // org.commongeoregistry.adapter.Term getRootTerm =
    // attributeTermType.getRootTerm();
    // String classifierKey =
    // TermConverter.buildClassifierKeyFromTermCode(getRootTerm.getCode());
    //
    // TermConverter termBuilder = new TermConverter(classifierKey);
    // attributeTermType.setRootTerm(termBuilder.build());
    // }
  }

}
