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
package net.geoprism.data.etl;

import net.geoprism.data.etl.TargetBinding;
import net.geoprism.data.etl.TargetFieldDerivedBinding;

import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdAttribute;
import com.vividsolutions.jts.geom.Coordinate;

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
//    SELECT ge.id
//    FROM geo_entity AS ge
//    WHERE ST_Within(ST_GeomFromText('SRID=4326;POINT(105.811345313737 11.5276163335363)'), ge.geo_multi_polygon)
//    AND ge.universal = 'i78va0eqzts6n0epfmqwr2l7wqos7v0si1vpa2tywfkq0wgqelwt6ay8b49cnbch';
    
    Coordinate coordinate = super.getCoordinate(mdAttribute, source);
    
    // collect all the views and extend the bounding box
    QueryFactory factory = new QueryFactory();
    
//    ValueQuery vQuery = new ValueQuery(factory);
//    vQuery.aS
//    
//    GeoEntityQuery geQuery = new GeoEntityQuery(factory);
//    geQuery.WHERE(geQuery.getUniversal().EQ(universal));
//    geQuery.AND(geQuery.);
    


    return this.country.getId();
  }
  
  @Override
  public void persist(TargetBinding binding)
  {
    MdAttribute latitudeAttribute = MdAttribute.getByKey(binding.getSourceView().definesType() + "." + this.getLatitudeSourceAttributeName());
    MdAttribute longitudeAttribute = MdAttribute.getByKey(binding.getSourceView().definesType() + "." + this.getLongitudeSourceAttributeName());
    MdAttribute targetAttribute = MdAttribute.getByKey(this.getKey());

    TargetFieldDerivedBinding field = new TargetFieldDerivedBinding();
    field.setTarget(binding);
    field.setTargetAttribute(targetAttribute);
    field.setLatitudeAttribute(latitudeAttribute);
    field.setLongitudeAttribute(longitudeAttribute);
    field.setGeoEntity(this.country);
    field.setUniversal(this.getUniversal());
    field.setColumnLabel(this.getLabel());
    field.apply();
  }
}
