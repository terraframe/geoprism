package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.ServerElement;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.view.TypeClass;
import net.geoprism.registry.view.TypeInfo;

public class BusinessType extends BusinessTypeBase implements ServerElement
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
  public TypeInfo getTypeInfo()
  {
    return new TypeInfo(TypeClass.BUSINESS_TYPE, this.getCode());
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

  public void setLabelAttribute(String name)
  {
    MdVertexDAOIF mdVertex = this.getMdVertexDAO();
    MdAttributeConcreteDAOIF mdAttribute = (MdAttributeConcreteDAOIF) mdVertex.definesAttribute(name);

    this.setLabelAttributeId(mdAttribute.getOid());
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
