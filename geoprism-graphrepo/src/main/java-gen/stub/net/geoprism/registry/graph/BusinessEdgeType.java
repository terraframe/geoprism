package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.GraphTypeDTO;

import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;

import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.model.EdgeType;
import net.geoprism.registry.model.ServerElement;
import net.geoprism.registry.model.VertexComponentType;
import net.geoprism.registry.view.BusinessEdgeTypeView;

public class BusinessEdgeType extends BusinessEdgeTypeBase implements ServerElement, EdgeType
{
  @SuppressWarnings("unused")
  private static final long  serialVersionUID = -1808640970;

  public static final String JSON_LABEL       = "label";

  public BusinessEdgeType()
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
  protected String buildKey()
  {
    return this.getCode();
  }

  public LocalizedValue getLabel()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(BusinessEdgeType.DISPLAYLABEL));
  }

  public MdEdgeDAOIF getMdEdgeDAO()
  {
    return MdEdgeDAO.get(this.getMdEdgeOid());
  }

  @Override
  public GraphOrganization getOrganization()
  {
    return GraphOrganization.get((String) this.getObjectValue(BusinessEdgeType.ORGANIZATION));
  }

  @Override
  public GraphTypeDTO toDTO()
  {
    final GraphTypeDTO dto = new GraphTypeDTO(EdgeType.BUSINESS_EDGE_TYPE, this.getCode(), this.getLabel(), getLocalizedDescription());
    dto.setChildType(this.getIsChildGeoObject() ? BusinessEdgeTypeView.GEO_OBJECT_TYPE : this.getChildType().getTypeName());
    dto.setParentType(this.getIsParentGeoObject() ? BusinessEdgeTypeView.GEO_OBJECT_TYPE : this.getParentType().getTypeName());

    return dto;
  }

  public LocalizedValue getLocalizedDescription()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(BusinessEdgeType.DESCRIPTION));
  }

  @Override
  public VertexComponentType getSourceType()
  {
    return this.getIsParentGeoObject() ? VertexComponentType.GEO_OBJECT : VertexComponentType.BUSINESS;
  }

  @Override
  public VertexComponentType getTargetType()
  {
    return this.getIsChildGeoObject() ? VertexComponentType.GEO_OBJECT : VertexComponentType.BUSINESS;
  }

}
