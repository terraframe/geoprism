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
    return GeometryType.valueOf(this.getType().getGeometryType());
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
