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
package net.geoprism.dhis2.importer;

import java.sql.Savepoint;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.gis.geometry.GeometryHelper;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.Universal;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class OrgUnitJsonToGeoEntity
{
  private DHIS2DataImporter importer;
  
  private GeometryFactory geometryFactory;

  private GeometryHelper geometryHelper;
  
  private JSONObject json;
  
  private GeoEntity geo;
  
  public OrgUnitJsonToGeoEntity(DHIS2DataImporter importer, GeometryFactory geometryFactory, GeometryHelper geometryHelper, JSONObject orgUnit)
  {
    this.geometryFactory = geometryFactory;
    
    this.geometryHelper = geometryHelper;
    
    this.json = orgUnit;
    
    this.geo = new GeoEntity();
    
    this.importer = importer;
  }
  
  public void apply()
  {
    geo.getDisplayLabel().setValue(json.getString("name"));
    
    geo.setGeoId(json.getString("id"));
    
    setUniversal();
    
    setGeometry();
    
    setLocales();
    
    geo.apply();
  }
  
  public void applyLocatedIn()
  {
    GeoEntity parent;
    try
    {
      if (json.has("parent"))
      {
        // DHIS2 woes: Kinda dumb to have a JSONObject that will always only have an id in it.
        parent = GeoEntity.getByKey(json.getJSONObject("parent").getString("id"));
      }
      else
      {
        parent = GeoEntity.getRoot();
      }
    }
    catch(DataNotFoundException e)
    {
      throw new RuntimeException("The DHIS2 child OrgUnit [" + json.toString() + "] references a parent [" + json.getString("parent") + "] but that GeoEntity does not exist.", e);
    }
    
    geo.addLocatedIn(parent).apply();
  }
  
  public void swapGeoId()
  {
    geo.lock();
    geo.setGeoId(json.getString("code"));
    geo.applyInternal(false);
  }
  
  private void setUniversal()
  {
    String path = json.getString("path");
    
    if (!path.startsWith("/"))
    {
      path = "/" + path;
    }
    
    int level = StringUtils.countMatches(path, "/") - 1;
    
    Universal uni;
    try
    {
      uni = importer.getUniversalByLevel(level).getUniversal();
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      throw new UnsupportedOperationException("Unable to find matching universal for OrgUnit [" + json + "]. We calculated a universal level of [" + level + "] but no universal exists at that level.", e);
    }
    
    geo.setUniversal(uni);
  }
  
  private void setLocales()
  {
//    JSONArray translations = json.getJSONArray("translations");
//    
//    for (int i = 0; i < translations.length(); ++i)
//    {
//      JSONObject translation = translations.getJSONObject(i);
//      
//      String locale = translation.getString("locale");
//      String value = translation.getString("value");
//      
//      Locale localeAsLocale = LocaleUtils.toLocale(locale);
//      
//      
//      // The locale may not exist in the system
//      // This code throws an AttributeDoesNotExistException
//      // One solution is to query for all locales at the start of the data import, or we can just rollback a savepoint here
//      geo.getDisplayLabel().setValue(localeAsLocale, value);
//    }
  }
  
  private void setGeometry()
  {
    String featureType = json.getString("featureType");
    
    if (featureType == null || featureType.equals("NONE")) { return; }
    
    JSONArray coordinates = new JSONArray(json.getString("coordinates")); // DHIS2 woes: Their coordinates are wrapped in quotes (its a string). We want to parse it as JSON.
    
    if (featureType.equals("POINT"))
    {
      Coordinate coord = jsonToCoordinate(coordinates);
      Point point = geometryFactory.createPoint(coord);

      geo.setGeoPoint(this.geometryHelper.getGeoPoint(point));
      geo.setGeoMultiPolygon(geometryHelper.getGeoMultiPolygon(point));
      geo.setWkt(point.toText());
    }
    else if (featureType.equals("POLYGON"))
    {
      coordinates = coordinates.getJSONArray(0).getJSONArray(0); // DHIS2 woes: Why is it wrapped in so many arrays??
      
      Polygon polygon = jsonToPolygon(coordinates);
      
      geo.setGeoPoint(this.geometryHelper.getGeoPoint(polygon));
      geo.setGeoMultiPolygon(geometryHelper.getGeoMultiPolygon(polygon));
      geo.setWkt(polygon.toText());
    }
    else if (featureType.equals("MULTI_POLYGON"))
    {
      coordinates = coordinates.getJSONArray(0); // DHIS2 woes: Why is it wrapped in so many arrays??
      
      MultiPolygon multipolygon = jsonToMultiPolygon(coordinates);
      
      geo.setGeoPoint(this.geometryHelper.getGeoPoint(multipolygon));
      geo.setGeoMultiPolygon(geometryHelper.getGeoMultiPolygon(multipolygon));
      geo.setWkt(multipolygon.toText());
    }
    else
    {
      throw new UnsupportedOperationException("Unexpected featureType [" + featureType + "] on OrgUnit [" + json.toString() + "].");
    }
  }
  
  private Coordinate jsonToCoordinate(JSONArray jsonPoint)
  {
    double x = jsonPoint.getDouble(0);
    double y = jsonPoint.getDouble(1);
    
    Coordinate coord = new Coordinate(x, y);
    
    return coord;
  }
  
  private Polygon jsonToPolygon(JSONArray jsonPoly)
  {
    Coordinate[] coords = new Coordinate[jsonPoly.length()];
    for (int i = 0; i < jsonPoly.length(); ++i)
    {
      JSONArray jsonPoint = jsonPoly.getJSONArray(i);
      
      coords[i] = jsonToCoordinate(jsonPoint);
    }
    
    return geometryFactory.createPolygon(coords);
  }
  
  private MultiPolygon jsonToMultiPolygon(JSONArray jsonMultiPoly)
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
