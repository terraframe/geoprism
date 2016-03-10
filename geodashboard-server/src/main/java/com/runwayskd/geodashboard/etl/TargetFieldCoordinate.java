/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwayskd.geodashboard.etl;

import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.gis.geometry.GeometryHelper;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public abstract class TargetFieldCoordinate extends TargetField implements TargetFieldIF
{
  private String          latitudeSourceAttributeName;

  private String          longitudeSourceAttributeName;

  private String          latitudeLabel;

  private String          longitudeLabel;

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

  public String getLatitudeLabel()
  {
    return latitudeLabel;
  }

  public void setLatitudeLabel(String latitudeLabel)
  {
    this.latitudeLabel = latitudeLabel;
  }

  public String getLongitudeLabel()
  {
    return longitudeLabel;
  }

  public void setLongitudeLabel(String longitudeLabel)
  {
    this.longitudeLabel = longitudeLabel;
  }

  public GeometryFactory getGeometryFactory()
  {
    return geometryFactory;
  }

  public GeometryHelper getGeometryHelper()
  {
    return geometryHelper;
  }

  protected Coordinate getCoordinate(MdAttributeConcreteDAOIF mdAttribute, Transient source)
  {
    Double latitude = new Double(source.getValue(this.getLatitudeSourceAttributeName()));
    Double longitude = new Double(source.getValue(this.getLongitudeSourceAttributeName()));

    if (latitude != null && longitude != null)
    {
      return new Coordinate(longitude, latitude);
    }

    return null;
  }
}
