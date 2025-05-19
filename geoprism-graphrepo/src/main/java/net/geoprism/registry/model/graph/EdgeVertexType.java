package net.geoprism.registry.model.graph;

import com.runwaysdk.dataaccess.MdVertexDAOIF;

import net.geoprism.registry.BusinessType;

public interface EdgeVertexType
{
  public boolean isGeoObjectType();

  public MdVertexDAOIF toGeoObjectType();

  public boolean isBusinessType();

  public BusinessType toBusinessType();

  public String getCode();
}
