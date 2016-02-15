package com.runwayskd.geodashboard.etl;

import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

public class TargetFieldMultiPolygon extends TargetFieldCoordinate implements TargetFieldIF
{
  @Override
  public Object getValue(MdAttributeConcreteDAOIF mdAttribute, Transient source)
  {
    Coordinate coord = super.getCoordinate(mdAttribute, source);

    if (coord != null)
    {
      Point point = this.getGeometryFactory().createPoint(coord);

      return this.getGeometryHelper().getGeoMultiPolygon(point);
    }

    return null;
  }

}
