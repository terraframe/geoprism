package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.system.gis.geo.GeometryType;

import net.geoprism.registry.model.ValueNodeStrategy;
import net.geoprism.registry.model.ValueStrategy;
import net.geoprism.registry.model.VertexValueStrategy;

public class AttributeGeometryType extends AttributeGeometryTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1252972775;

  public AttributeGeometryType()
  {
    super();
  }

  @Override
  public AttributeType toDTO()
  {
    org.commongeoregistry.adapter.metadata.AttributeGeometryType dto = new org.commongeoregistry.adapter.metadata.AttributeGeometryType(this.getCode(), getLocalizedLabel(), getLocalizedDescription(), isAppliedToDb(), isNew(), isAppliedToDb());
    
    this.populate(dto);
    
    return dto;
  }

  @Override
  public ValueStrategy getStrategy()
  {
    if (!this.getIsChangeOverTime())
    {
      return new VertexValueStrategy(this);
    }
    else
    {
      return new ValueNodeStrategy(this, getGeometryMdVertex(), AttributeGeometryValue.VALUE);
    }
  }

  protected MdVertexDAOIF getGeometryMdVertex()
  {
    if (this.getGeometryType().equals(GeometryType.POINT.name()) || this.getGeometryType().equals(GeometryType.MULTIPOINT.name()))
    {
      return MdVertexDAO.getMdVertexDAO(AttributePointValue.CLASS);
    }
    else if (this.getGeometryType().equals(GeometryType.LINE.name()) || this.getGeometryType().equals(GeometryType.MULTILINE.name()))
    {
      return MdVertexDAO.getMdVertexDAO(AttributeLineValue.CLASS);
    }
    else if (this.getGeometryType().equals(GeometryType.POLYGON.name()) || this.getGeometryType().equals(GeometryType.MULTIPOLYGON.name()))
    {
      return MdVertexDAO.getMdVertexDAO(AttributePolygonValue.CLASS);
    }
    else if (this.getGeometryType().equals(GeometryType.SHAPE.name()))
    {
      return MdVertexDAO.getMdVertexDAO(AttributeShapeValue.CLASS);
    }

    throw new UnsupportedOperationException("Unsupported geometry type [" + this.getGeometryType() + "]");
  }

}
