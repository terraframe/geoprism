package net.geoprism.registry.graph;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.registry.RegistryConstants;

public class GeoObjectType extends GeoObjectTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -942330515;

  public GeoObjectType()
  {
    super();
  }

  @Override
  public MdVertex getMdVertex()
  {
    return MdVertex.get(this.getObjectValue(GeoObjectType.MDVERTEX));
  }

  public Map<String, AttributeType> getAttributes()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Adds default attributes to the given {@link MdBusinessDAO} according to the
   * Common Geo-Registry specification for {@link GeoObject}.
   * 
   * @param mdBusinessDAO
   *          {@link MdBusinessDAO} that will define the default attributes.
   */
  @Transaction
  public void createDefaultAttributes()
  {
    if (StringUtils.isBlank(this.getObjectValue(GeoObjectType.SUPERTYPE)))
    {
      AttributeUUIDType uuidAttr = new AttributeUUIDType();
      uuidAttr.setCode(RegistryConstants.UUID);
      uuidAttr.setEmbeddedValue(AttributeUUIDType.LABEL, LocalizedValue.DEFAULT_LOCALE, RegistryConstants.UUID_LABEL);
      uuidAttr.setEmbeddedValue(AttributeUUIDType.DESCRIPTION, LocalizedValue.DEFAULT_LOCALE, RegistryConstants.UUID_LABEL);
      uuidAttr.setValue(AttributeBooleanType.GEOOBJECTTYPE, this.getOid());
      uuidAttr.setRequired(true);
      uuidAttr.setUnique(true);
      uuidAttr.setIsChangeOverTime(false);
      uuidAttr.setIsDefault(true);
      uuidAttr.apply();

      AttributeBooleanType exists = new AttributeBooleanType();
      exists.setCode(DefaultAttribute.EXISTS.getName());
      exists.setEmbeddedValue(AttributeBooleanType.LABEL, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.EXISTS.getDefaultLocalizedName());
      exists.setEmbeddedValue(AttributeBooleanType.DESCRIPTION, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.EXISTS.getDefaultDescription());
      exists.setValue(AttributeBooleanType.GEOOBJECTTYPE, this.getOid());
      exists.setRequired(false);
      exists.setUnique(false);
      exists.setIsChangeOverTime(true);
      exists.setIsDefault(true);
      exists.apply();

      AttributeBooleanType invald = new AttributeBooleanType();
      invald.setCode(DefaultAttribute.INVALID.getName());
      invald.setEmbeddedValue(AttributeBooleanType.LABEL, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.INVALID.getDefaultLocalizedName());
      invald.setEmbeddedValue(AttributeBooleanType.DESCRIPTION, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.INVALID.getDefaultDescription());
      invald.setValue(AttributeBooleanType.GEOOBJECTTYPE, this.getOid());
      invald.setRequired(true);
      invald.setUnique(false);
      invald.setIsChangeOverTime(false);
      invald.setIsDefault(true);
      invald.apply();

      AttributeLocalType labelAttr = new AttributeLocalType();
      labelAttr.setCode(DefaultAttribute.DISPLAY_LABEL.getName());
      labelAttr.setEmbeddedValue(AttributeUUIDType.LABEL, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.DISPLAY_LABEL.getDefaultLocalizedName());
      labelAttr.setEmbeddedValue(AttributeUUIDType.DESCRIPTION, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.DISPLAY_LABEL.getDefaultDescription());
      labelAttr.setValue(AttributeBooleanType.GEOOBJECTTYPE, this.getOid());
      labelAttr.setRequired(false);
      labelAttr.setUnique(false);
      labelAttr.setIsChangeOverTime(false);
      labelAttr.setIsDefault(true);
      labelAttr.apply();
    }

    if (!this.getIsAbstract())
    {
      AttributeCharacterType code = new AttributeCharacterType();
      code.setCode(DefaultAttribute.CODE.getName());
      code.setEmbeddedValue(AttributeUUIDType.LABEL, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.CODE.getDefaultLocalizedName());
      code.setEmbeddedValue(AttributeUUIDType.DESCRIPTION, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.CODE.getDefaultDescription());
      code.setValue(AttributeBooleanType.GEOOBJECTTYPE, this.getOid());
      code.setRequired(true);
      code.setUnique(true);
      code.setIsChangeOverTime(false);
      code.setIsDefault(true);
      code.apply();
    }
  }
}
