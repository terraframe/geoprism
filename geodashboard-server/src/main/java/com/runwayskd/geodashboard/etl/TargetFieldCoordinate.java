package com.runwayskd.geodashboard.etl;

import com.runwaysdk.gis.geometry.GeometryHelper;
import com.vividsolutions.jts.geom.GeometryFactory;

public abstract class TargetFieldCoordinate extends TargetField implements TargetFieldIF
{
  private String          latitudeSourceAttributeName;

  private String          longitudeSourceAttributeName;

  private GeometryFactory geometryFactory;

  private GeometryHelper  geometryHelper;

  public TargetFieldCoordinate()
  {
    this.geometryFactory = new GeometryFactory();
    this.geometryHelper = new GeometryHelper();
  }

  public String getLatitudeSourceAttributeName()
  {
    return latitudeSourceAttributeName;
  }

  public void setLatitudeSourceAttributeName(String latitudeSourceAttributeName)
  {
    this.latitudeSourceAttributeName = latitudeSourceAttributeName;
  }

  public String getLongitudeSourceAttributeName()
  {
    return longitudeSourceAttributeName;
  }

  public void setLongitudeSourceAttributeName(String longitudeSourceAttributeName)
  {
    this.longitudeSourceAttributeName = longitudeSourceAttributeName;
  }

  public GeometryFactory getGeometryFactory()
  {
    return geometryFactory;
  }

  public GeometryHelper getGeometryHelper()
  {
    return geometryHelper;
  }

}
