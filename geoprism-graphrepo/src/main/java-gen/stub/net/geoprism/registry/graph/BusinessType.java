package net.geoprism.registry.graph;

import java.util.Map;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.conversion.RegistryAttributeTypeConverter;
import net.geoprism.registry.model.ServerElement;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.model.graph.EdgeVertexType;

public class BusinessType extends BusinessTypeBase implements ServerElement, EdgeVertexType
{
  private static final long  serialVersionUID = 88826735;

  public static final String JSON_ATTRIBUTES  = "attributes";

  public static final String JSON_CODE        = "code";

  public BusinessType()
  {
    super();
  }

  @Override
  public void apply()
  {
    super.apply();
  }

  @Override
  public void delete()
  {
    super.delete();
  }

  @Override
  public GraphOrganization getOrganization()
  {
    return GraphOrganization.get((String) this.getObjectValue(BusinessEdgeType.ORGANIZATION));
  }

  public MdVertexDAOIF getMdVertexDAO()
  {
    return MdVertexDAO.get(this.getMdVertexOid());
  }

  public LocalizedValue getLabel()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(BusinessType.DISPLAYLABEL));
  }

  public Map<String, AttributeType> getAttributeMap()
  {
    RegistryAttributeTypeConverter converter = new RegistryAttributeTypeConverter();

    MdVertexDAOIF mdVertex = this.getMdVertexDAO();

    return mdVertex.definesAttributes().stream().filter(attr -> {
      return !attr.isSystem() && !attr.definesAttribute().equals(BusinessType.SEQ);
    }).map(attr -> converter.build(attr)).collect(Collectors.toMap(AttributeType::getName, attr -> attr));
  }

  public AttributeType getAttribute(String name)
  {
    RegistryAttributeTypeConverter converter = new RegistryAttributeTypeConverter();

    MdVertexDAOIF mdVertex = this.getMdVertexDAO();
    MdAttributeConcreteDAOIF mdAttribute = (MdAttributeConcreteDAOIF) mdVertex.definesAttribute(name);

    return converter.build(mdAttribute);
  }

  public void setLabelAttribute(String name)
  {
    MdVertexDAOIF mdVertex = this.getMdVertexDAO();
    MdAttributeConcreteDAOIF mdAttribute = (MdAttributeConcreteDAOIF) mdVertex.definesAttribute(name);

    this.setLabelAttributeId(mdAttribute.getOid());
  }

  @Override
  public boolean isGeoObjectType()
  {
    return false;
  }

  @Override
  public MdVertexDAOIF toGeoObjectType()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isBusinessType()
  {
    return true;
  }

  @Override
  public BusinessType toBusinessType()
  {
    return this;
  }

  public String getOrganizationGraphId()
  {
    return this.getObjectValue(ORGANIZATION);
  }

  public ServerOrganization getServerOrganization()
  {
    return ServerOrganization.getByGraphId(this.getOrganizationGraphId());
  }
}
