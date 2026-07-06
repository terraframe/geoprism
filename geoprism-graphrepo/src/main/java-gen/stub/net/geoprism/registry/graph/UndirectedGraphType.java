package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.GraphTypeDTO;

import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;

import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.GraphType;
import net.geoprism.registry.model.ServerElement;
import net.geoprism.registry.model.graph.GraphStrategy;
import net.geoprism.registry.model.graph.UndirectedGraphStrategy;
import net.geoprism.registry.view.TypeClass;

public class UndirectedGraphType extends UndirectedGraphTypeBase implements GraphType, ServerElement
{
  private static final long  serialVersionUID = -1097845938;

  public static final String JSON_LABEL       = "label";

  public UndirectedGraphType()
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

  public MdEdgeDAOIF getMdEdgeDAO()
  {
    return MdEdgeDAO.get(this.getMdEdgeOid());
  }

  @Override
  protected String buildKey()
  {
    return this.getCode();
  }

  @Override
  public LocalizedValue getLabel()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(DISPLAYLABEL));
  }

  @Override
  public LocalizedValue getDescriptionLV()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(DESCRIPTION));
  }

  @Override
  public GraphTypeDTO toDTO()
  {
    final GraphTypeDTO dto = new GraphTypeDTO(TypeClass.UNDIRECTED_GRAPH.getCode(), this.getCode(), this.getLabel(), this.getDescriptionLV());

    return dto;
  }

  @Override
  public GraphStrategy getStrategy()
  {
    return new UndirectedGraphStrategy(this);
  }

}
