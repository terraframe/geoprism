package net.geoprism.registry.graph;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.gis.geo.GeometryType;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.conversion.GeometryTypeFactory;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.service.request.ServiceFactory;

public class GeoObjectType extends GeoObjectTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -942330515;

  public GeoObjectType()
  {
    super();
  }

  @Override
  @Transaction
  public void delete()
  {
    // Delete all attributes types
    this.getAttributes().stream().forEach(attributeType -> attributeType.delete());

    MdVertex mdVertex = this.getMdVertex();

    super.delete();

    if (mdVertex != null)
    {
      mdVertex.delete();
    }
  }

  @Override
  public MdVertex getMdVertex()
  {
    return MdVertex.get(this.getObjectValue(GeoObjectType.MDVERTEX));
  }

  @Override
  public GraphOrganization getOrganization()
  {
    return GraphOrganization.get((String) this.getObjectValue(GeoObjectType.MDVERTEX));
  }

  @Override
  public GeoObjectType getSuperType()
  {
    String oid = (String) this.getObjectValue(GeoObjectType.SUPERTYPE);

    if (!StringUtils.isBlank(oid))
    {
      return GeoObjectType.get(oid);
    }

    return null;
  }

  public Map<String, AttributeType> getAttributeMap()
  {
    return this.getAttributes().stream().collect(Collectors.toMap(t -> t.getCode(), t -> t));
  }

  public List<AttributeType> getAttributes()
  {
    MdVertexDAOIF mdVertexDAO = MdVertexDAO.getMdVertexDAO(AttributeType.CLASS);
    MdAttributeDAOIF mdAttribute = mdVertexDAO.definesAttribute(AttributeType.GEOOBJECTTYPE);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertexDAO.getDBClassName());
    statement.append(" WHERE " + mdAttribute.getColumnName() + ".oid = :geoObjectType");

    GraphQuery<AttributeType> query = new GraphQuery<AttributeType>(statement.toString());
    query.setParameter("geoObjectType", this.getOid());

    return query.getResults();
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
      labelAttr.setIsChangeOverTime(true);
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

  public List<String> getSubTypeCodes()
  {
    MdVertexDAOIF metadata = MdVertexDAO.getMdVertexDAO(GeoObjectType.CLASS);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT " + metadata.definesAttribute(GeoObjectType.CODE).getColumnName() + " FROM " + metadata.getDBClassName());
    statement.append(" WHERE " + metadata.definesAttribute(GeoObjectType.SUPERTYPE).getColumnName() + " = :rid");

    GraphQuery<String> query = new GraphQuery<String>(statement.toString());
    query.setParameter("rid", this.getRID());

    return query.getResults();
  }

  public void fromDTO(org.commongeoregistry.adapter.metadata.GeoObjectType dto)
  {
    GeometryType geometryType = GeometryTypeFactory.get(dto.getGeometryType());

    this.setCode(dto.getCode());
    this.setIsGeometryEditable(dto.isGeometryEditable());
    this.setIsPrivate(dto.getIsPrivate());
    this.setIsAbstract(dto.getIsAbstract());
    this.setGeometryType(geometryType.getEnumName());

    RegistryLocalizedValueConverter.populate(this, GeoObjectType.LABEL, dto.getLabel());
    RegistryLocalizedValueConverter.populate(this, GeoObjectType.DESCRIPTION, dto.getDescription());
  }

  /**
   * The GeoObjectType is a DTO type, which means it contains data which has
   * been localized to a particular user's session. We need to rebuild this
   * object such that it includes relevant request information (like the correct
   * locale).
   */
  public org.commongeoregistry.adapter.metadata.GeoObjectType toDTO()
  {
    com.runwaysdk.system.gis.geo.GeometryType geoPrismgeometryType = com.runwaysdk.system.gis.geo.GeometryType.valueOf(this.getGeometryType());

    org.commongeoregistry.adapter.constants.GeometryType cgrGeometryType = GeometryTypeFactory.get(geoPrismgeometryType);

    LocalizedValue label = RegistryLocalizedValueConverter.convert(this.getEmbeddedComponent(GeoObjectType.LABEL));
    LocalizedValue description = RegistryLocalizedValueConverter.convert(this.getEmbeddedComponent(GeoObjectType.DESCRIPTION));

    GraphOrganization organization = this.getOrganization();

    String orgCode = organization != null ? organization.getCode() : null;

    GeoObjectType superType = this.getSuperType();

    org.commongeoregistry.adapter.metadata.GeoObjectType dto = new org.commongeoregistry.adapter.metadata.GeoObjectType(this.getCode(), cgrGeometryType, label, description, this.getIsGeometryEditable(), orgCode, ServiceFactory.getAdapter());
    dto.setIsAbstract(this.getIsAbstract());

    this.getAttributeMap().forEach((attributeName, attributeType) -> {
      dto.addAttribute(attributeType.toDTO());
    });

    if (superType != null)
    {
      dto.setSuperTypeCode(superType.getCode());
    }

    return dto;
  }

  public static List<GeoObjectType> getAll()
  {
    MdVertexDAOIF metadata = MdVertexDAO.getMdVertexDAO(GeoObjectType.CLASS);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + metadata.getDBClassName());

    GraphQuery<GeoObjectType> query = new GraphQuery<GeoObjectType>(statement.toString());

    return query.getResults();
  }

  public static GeoObjectType getByCode(String code)
  {
    MdVertexDAOIF metadata = MdVertexDAO.getMdVertexDAO(GeoObjectType.CLASS);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + metadata.getDBClassName());
    statement.append(" WHERE " + metadata.definesAttribute(GeoObjectType.CODE).getColumnName() + " = :code");

    GraphQuery<GeoObjectType> query = new GraphQuery<GeoObjectType>(statement.toString());
    query.setParameter("code", code);

    return query.getSingleResult();
  }

}
