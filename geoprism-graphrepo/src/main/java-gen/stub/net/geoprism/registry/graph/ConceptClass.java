package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.ServerOrganization;

public class ConceptClass extends ConceptClassBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -694947919;

  public ConceptClass()
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

  public String getOrganizationGraphId()
  {
    return this.getObjectValue(ORGANIZATION);
  }

  public ServerOrganization getServerOrganization()
  {
    return ServerOrganization.getByGraphId(this.getOrganizationGraphId());
  }

}
