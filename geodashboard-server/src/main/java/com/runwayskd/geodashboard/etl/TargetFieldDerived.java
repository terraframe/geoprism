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

import java.util.Map;
import java.util.Set;

import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;
import com.runwaysdk.query.Condition;
import com.runwaysdk.query.Join;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.SelectableGeometry;
import com.runwaysdk.query.Visitor;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdAttribute;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTWriter;

public class TargetFieldDerived extends TargetFieldCoordinate implements TargetFieldIF
{
  public static class ST_WITHIN extends Condition
  {
    private Geometry           geometry;

    private SelectableGeometry selectable;

    private WKTWriter          writer;

    private int                srid;

    public ST_WITHIN(Geometry geometry, SelectableGeometry selectable)
    {
      this.geometry = geometry;
      this.selectable = selectable;
      this.writer = new WKTWriter();
      this.srid = ( (MdAttributeGeometryDAOIF) this.selectable.getMdAttributeIF() ).getSRID();
    }

    @Override
    public Set<Join> getJoinStatements()
    {
      return null;
    }

    @Override
    public Map<String, String> getFromTableMap()
    {
      return null;
    }

    /**
     * Visitor to traverse the query object structure.
     * 
     * @param visitor
     */
    public void accept(Visitor visitor)
    {
      visitor.visit(this);
    }

    /**
     * Returns the SQL representation of this condition.
     *
     * @return SQL representation of this condition.
     */
    public String getSQL()
    {
      String statementSQL1 = "ST_GeomFromText('SRID=" + this.srid + ";" + this.writer.write(this.geometry) + "')";
      String statementSQL2 = this.selectable.getSQL();

      return "ST_Within(" + statementSQL1 + ", " + statementSQL2 + ")";
    }
  }

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
    // SELECT ge.id
    // FROM geo_entity AS ge
    // WHERE ST_Within(ST_GeomFromText('SRID=4326;POINT(105.811345313737 11.5276163335363)'), ge.geo_multi_polygon)
    // AND ge.universal = 'i78va0eqzts6n0epfmqwr2l7wqos7v0si1vpa2tywfkq0wgqelwt6ay8b49cnbch';

    Coordinate coord = super.getCoordinate(mdAttribute, source);

    if (coord != null)
    {
      Point point = this.getGeometryFactory().createPoint(coord);

      GeoEntityQuery query = new GeoEntityQuery(new QueryFactory());
      query.WHERE(query.getUniversal().EQ(this.universal));
      query.AND(new ST_WITHIN(point, query.getGeoMultiPolygon()));

      OIterator<? extends GeoEntity> it = query.getIterator();

      try
      {
        if (it.hasNext())
        {
          GeoEntity entity = it.next();

          return entity.getId();
        }
      }
      finally
      {
        it.close();
      }
    }

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
