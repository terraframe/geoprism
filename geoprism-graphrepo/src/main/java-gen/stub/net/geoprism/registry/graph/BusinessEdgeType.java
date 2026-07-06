package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.GraphTypeDTO;

import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;

import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.EdgeType;
import net.geoprism.registry.model.ServerElement;
import net.geoprism.registry.view.BusinessEdgeTypeView;
import net.geoprism.registry.view.TypeClass;

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

  public LocalizedValue getDescriptionLV()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(BusinessEdgeType.DESCRIPTION));
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
    final GraphTypeDTO dto = new GraphTypeDTO(TypeClass.BUSINESS_EDGE.getCode(), this.getCode(), this.getLabel(), getDescriptionLV());
    dto.setChildType(this.getIsChildGeoObject() ? BusinessEdgeTypeView.GEO_OBJECT_TYPE : this.getChildType().getTypeName());
    dto.setParentType(this.getIsParentGeoObject() ? BusinessEdgeTypeView.GEO_OBJECT_TYPE : this.getParentType().getTypeName());

    return dto;
  }

  @Override
  public TypeClass getSourceType()
  {
    return this.getIsParentGeoObject() ? TypeClass.GEO_OBJECT_TYPE : TypeClass.BUSINESS_TYPE;
  }

  @Override
  public TypeClass getTargetType()
  {
    return this.getIsChildGeoObject() ? TypeClass.GEO_OBJECT_TYPE : TypeClass.BUSINESS_TYPE;
  }

}
