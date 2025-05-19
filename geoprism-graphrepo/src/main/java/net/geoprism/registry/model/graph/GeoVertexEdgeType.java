package net.geoprism.registry.model.graph;

import com.runwaysdk.dataaccess.MdVertexDAOIF;

import net.geoprism.registry.BusinessType;

public class GeoVertexEdgeType implements EdgeVertexType
{
  private MdVertexDAOIF mdVertex;

  public GeoVertexEdgeType(MdVertexDAOIF mdVertex)
  {
    this.mdVertex = mdVertex;
  }
  
  @Override
  public String getCode()
  {
    return "GEO_OBJECT";
  }

  @Override
  public boolean isGeoObjectType()
  {
    return true;
  }

  @Override
  public MdVertexDAOIF toGeoObjectType()
  {
    return this.mdVertex;
  }

  @Override
  public boolean isBusinessType()
  {
    return false;
  }

  @Override
  public BusinessType toBusinessType()
  {
    throw new UnsupportedOperationException();
  }

}
