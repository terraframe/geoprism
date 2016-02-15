package com.runwayskd.geodashboard.etl;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

public class TargetFieldPoint extends TargetFieldCoordinate implements TargetFieldIF
{
  @Override
  public Object getValue(MdAttributeConcreteDAOIF mdAttribute, Transient source)
  {
    Double latitude = new Double(source.getValue(this.getLatitudeSourceAttributeName()));
    Double longitude = new Double(source.getValue(this.getLongitudeSourceAttributeName()));

    if (latitude != null && longitude != null)
    {
      Coordinate coord = new Coordinate(longitude, latitude);
      Point point = this.getGeometryFactory().createPoint(coord);

      return this.getGeometryHelper().getGeoPoint(point);
    }

    return null;
  }

}
