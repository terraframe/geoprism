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
package net.geoprism.data;

import org.json.JSONArray;
import org.json.JSONException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class GeometrySerializationUtil {
  
  private GeometryFactory geometryFactory;
  
  public GeometrySerializationUtil(GeometryFactory geometryFactory)
  {
    this.geometryFactory = geometryFactory;
  }
  
  public Coordinate jsonToCoordinate(JSONArray jsonPoint) throws JSONException
  {
    double x = jsonPoint.getDouble(0);
    double y = jsonPoint.getDouble(1);
    
    Coordinate coord = new Coordinate(x, y);
    
    return coord;
  }
  
  public Polygon jsonToPolygon(JSONArray jsonPoly) throws JSONException
  {
    Coordinate[] coords = new Coordinate[jsonPoly.length()];
    for (int i = 0; i < jsonPoly.length(); ++i)
    {
      JSONArray jsonPoint = jsonPoly.getJSONArray(i);
      
      coords[i] = jsonToCoordinate(jsonPoint);
    }
    
    return geometryFactory.createPolygon(coords);
  }
  
  public MultiPolygon jsonToMultiPolygon(JSONArray jsonMultiPoly) throws JSONException
  {
    Polygon[] polygons = new Polygon[jsonMultiPoly.length()];
    for (int i = 0; i < jsonMultiPoly.length(); ++i)
    {
      JSONArray jsonPoly = jsonMultiPoly.getJSONArray(i);
      
      polygons[i] = jsonToPolygon(jsonPoly);
    }
    
    return geometryFactory.createMultiPolygon(polygons);
  }
}
