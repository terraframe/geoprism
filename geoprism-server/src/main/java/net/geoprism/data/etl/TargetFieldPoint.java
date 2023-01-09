/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.data.etl;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.GeoNodeGeometry;
import com.runwaysdk.system.gis.geo.GeoNodeGeometryQuery;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.util.IDGenerator;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;

public class TargetFieldPoint extends TargetFieldCoordinate implements TargetFieldPointIF
{
  private String oid;

  public TargetFieldPoint()
  {
    this.oid = IDGenerator.nextID();
  }

  @Override
  public FieldValue getValue(MdAttributeConcreteDAOIF mdAttribute, Transient source)
  {
    Coordinate coord = super.getCoordinate(mdAttribute, source);

    if (coord != null)
    {
      Point point = this.getGeometryFactory().createPoint(coord);

      return new FieldValue(this.getGeometryHelper().getGeoPoint(point));
    }

    return new FieldValue();
  }

  @Override
  public void persist(TargetBinding binding)
  {
    MdAttribute latitudeAttribute = MdAttribute.getByKey(binding.getSourceView().definesType() + "." + this.getLatitudeSourceAttributeName());
    MdAttribute longitudeAttribute = MdAttribute.getByKey(binding.getSourceView().definesType() + "." + this.getLongitudeSourceAttributeName());
    MdAttribute targetAttribute = MdAttribute.getByKey(this.getKey());

    TargetFieldPointBinding field = new TargetFieldPointBinding();
    field.setTarget(binding);
    field.setTargetAttribute(targetAttribute);
    field.setLatitudeAttribute(latitudeAttribute);
    field.setLongitudeAttribute(longitudeAttribute);
    field.setColumnLabel(this.getLabel());
    field.apply();
  }

  @Override
  public String getOid()
  {
    return this.oid;
  }

  public GeoNodeGeometry getNode()
  {
    GeoNodeGeometryQuery query = new GeoNodeGeometryQuery(new QueryFactory());
    query.WHERE(query.getGeometryAttribute().EQ(MdAttribute.getByKey(this.getKey())));

    OIterator<? extends GeoNodeGeometry> iterator = query.getIterator();

    try
    {
      GeoNodeGeometry node = iterator.next();

      return node;
    }
    finally
    {
      iterator.close();
    }
  }

  public TargetFieldDerivedBinding getDerivedBinding(MdAttributeReference geoEntityAttribute)
  {
    TargetFieldDerivedBindingQuery query = new TargetFieldDerivedBindingQuery(new QueryFactory());
    query.WHERE(query.getTargetAttribute().EQ(geoEntityAttribute));

    OIterator<? extends TargetFieldDerivedBinding> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        TargetFieldDerivedBinding binding = iterator.next();

        return binding;
      }

      return null;
    }
    finally
    {
      iterator.close();
    }
  }

  @Override
  public JSONObject toJSON() throws JSONException
  {
    GeoNodeGeometry node = this.getNode();

    MdAttribute identifierAttribute = node.getIdentifierAttribute();
    MdAttributeReference geoEntityAttribute = node.getGeoEntityAttribute();

    JSONObject object = new JSONObject();
    object.put("oid", this.oid);
    object.put("label", this.getLabel());
    object.put("latitude", this.getLatitudeLabel());
    object.put("longitude", this.getLongitudeLabel());
    object.put("featureLabel", identifierAttribute.getDisplayLabel().getValue());

    TargetFieldDerivedBinding binding = this.getDerivedBinding(geoEntityAttribute);

    if (binding != null)
    {
      object.put("location", "DERIVE");
      object.put("universal", binding.getUniversalId());
    }
    else
    {
      object.put("location", geoEntityAttribute.getDisplayLabel().getValue());
      object.put("universal", "");
    }

    return object;

  }
}
