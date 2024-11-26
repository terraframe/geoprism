/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.registry.model;

import org.commongeoregistry.adapter.constants.GeometryType;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdVertexDAOIF;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.GeometrySizeException;
import net.geoprism.registry.GeometryTypeException;
import net.geoprism.registry.graph.AttributeGeometryType;

public class GeometryValueNodeStrategy extends ValueNodeStrategy
{

  public GeometryValueNodeStrategy(AttributeGeometryType type, MdVertexDAOIF nodeVertex, String nodeAttribute)
  {
    super(type, nodeVertex, nodeAttribute);
  }

  @Override
  public AttributeGeometryType getType()
  {
    return (AttributeGeometryType) super.getType();
  }

  public boolean isValidGeometry(Geometry geometry)
  {
    if (geometry != null)
    {
      GeometryType type = getGeometryType();

      if (type.equals(GeometryType.LINE) && ! ( geometry instanceof LineString ))
      {
        return false;
      }
      else if (type.equals(GeometryType.MULTILINE) && ! ( geometry instanceof MultiLineString ))
      {
        return false;
      }
      else if (type.equals(GeometryType.POINT) && ! ( geometry instanceof Point ))
      {
        return false;
      }
      else if (type.equals(GeometryType.MULTIPOINT) && ! ( geometry instanceof MultiPoint ))
      {
        return false;
      }
      else if (type.equals(GeometryType.POLYGON) && ! ( geometry instanceof Polygon ))
      {
        return false;
      }
      else if (type.equals(GeometryType.MULTIPOLYGON) && ! ( geometry instanceof MultiPolygon ))
      {
        return false;
      }

      return true;
    }

    return true;
  }

  protected GeometryType getGeometryType()
  {
    String geometryType = this.getType().getGeometryType();

    if(geometryType.equalsIgnoreCase(com.runwaysdk.system.gis.geo.GeometryType.SHAPE.name())) {
      return GeometryType.MIXED;
    }
    return GeometryType.valueOf(geometryType);
  }

  @Override
  protected void setNodeValue(VertexObject node, Object value, Boolean validate)
  {
    if (value instanceof Geometry)
    {
      Geometry geometry = (Geometry) value;

      // Validate the geometry type
      if (geometry != null && geometry.getNumPoints() > GeoprismProperties.getMaxNumberOfPoints())
      {
        throw new GeometrySizeException();
      }

      if (!this.isValidGeometry(geometry))
      {
        GeometryTypeException ex = new GeometryTypeException();
        ex.setActualType(geometry.getGeometryType());
        ex.setExpectedType(this.getGeometryType().name());

        throw ex;
      }
    }

    super.setNodeValue(node, value, validate);
  }
}
