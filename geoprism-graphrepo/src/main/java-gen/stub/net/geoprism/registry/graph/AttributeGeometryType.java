package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.gis.geo.GeometryType;

import net.geoprism.registry.model.GeometryValueNodeStrategy;
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
  @Transaction
  public void apply()
  {
    if (!this.getIsChangeOverTime())
    {
      throw new UnsupportedOperationException();
    }

    super.apply();
  }

  @Override
  public AttributeType toDTO()
  {
    org.commongeoregistry.adapter.metadata.AttributeGeometryType dto = new org.commongeoregistry.adapter.metadata.AttributeGeometryType(this.getCode(), getLocalizedLabel(), getLocalizedDescription(), getIsDefault(), isNew(), getUnique());

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
      return new GeometryValueNodeStrategy(this, getGeometryMdVertex(), AttributeGeometryValue.VALUE);
    }
  }

  protected MdVertexDAOIF getGeometryMdVertex()
  {
    if (this.getGeometryType().equals(GeometryType.POINT.name()) || this.getGeometryType().equals(GeometryType.MULTIPOINT.name()))
    {
      return MdVertexDAO.getMdVertexDAO(AttributeMultiPointValue.CLASS);
    }
    else if (this.getGeometryType().equals(GeometryType.LINE.name()) || this.getGeometryType().equals(GeometryType.MULTILINE.name()))
    {
      return MdVertexDAO.getMdVertexDAO(AttributeMultiLineValue.CLASS);
    }
    else if (this.getGeometryType().equals(GeometryType.POLYGON.name()) || this.getGeometryType().equals(GeometryType.MULTIPOLYGON.name()))
    {
      return MdVertexDAO.getMdVertexDAO(AttributeMultiPolygonValue.CLASS);
    }
    else if (this.getGeometryType().equals(GeometryType.SHAPE.name()))
    {
      return MdVertexDAO.getMdVertexDAO(AttributeShapeValue.CLASS);
    }

    throw new UnsupportedOperationException("Unsupported geometry type [" + this.getGeometryType() + "]");
  }

}
