package com.runwayskd.geodashboard.etl;

import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.Universal;

public class TargetFieldDerived extends TargetFieldCoordinate implements TargetFieldIF
{
  private GeoEntity country;

  private Universal universal;

  public GeoEntity getCountry()
  {
    return country;
  }

  public void setCountry(GeoEntity country)
  {
    this.country = country;
  }

  public Universal getUniversal()
  {
    return universal;
  }

  public void setUniversal(Universal universal)
  {
    this.universal = universal;
  }

  @Override
  public Object getValue(MdAttributeConcreteDAOIF mdAttribute, Transient source)
  {
    return this.country.getId();
  }

}
