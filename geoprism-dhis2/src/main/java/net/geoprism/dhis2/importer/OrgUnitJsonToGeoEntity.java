/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.dhis2.importer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.gis.geometry.GeometryHelper;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.Synonym;
import com.runwaysdk.system.gis.geo.Universal;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class OrgUnitJsonToGeoEntity
{
  private static final Logger logger = LoggerFactory.getLogger(OrgUnitJsonToGeoEntity.class);
  
  private DHIS2DataImporter importer;
  
  private GeometryFactory geometryFactory;

  private GeometryHelper geometryHelper;
  
  private JSONObject json;
  
  private GeoEntity geo;
  
  private String countryOrgUnitId;
  
  private boolean skipMe = false;
  
  public static ArrayList<JSONObject> countryGeos = new ArrayList<JSONObject>();
  
  public OrgUnitJsonToGeoEntity(DHIS2DataImporter importer, GeometryFactory geometryFactory, GeometryHelper geometryHelper, JSONObject orgUnit, String countryOrgUnitId)
  {
    this.geometryFactory = geometryFactory;
    
    this.geometryHelper = geometryHelper;
    
    this.json = orgUnit;
    
    this.geo = new GeoEntity();
    
    this.importer = importer;
    
    this.countryOrgUnitId = countryOrgUnitId;
  }
  
  public void apply()
  {
    String oid = json.getString("oid");
    
    geo.setGeoId(oid);
    
    if (!setUniversal())
    {
      skipMe = true;
      return;
    }
    
    setName();
    
    setGeometry();
    
    setLocales();
    
    geo.apply();
    
    // Create a synonym that is the geo oid and also the code
//    Synonym synonym = new Synonym();
//    synonym.getDisplayLabel().setValue(json.getString("code"));
//    Synonym.create(synonym, geo.getOid());
    
    Synonym synonym2 = new Synonym();
    synonym2.getDisplayLabel().setValue(json.getString("oid"));
    Synonym.create(synonym2, geo.getOid());
    
    
    // Akros has some weird naming conventions. We're matching here based on like 'vil Mulala SCHOOL'. In this case we'll extract 'Mulala'.
    String prefixPattern = "^([a-z]{0,4}) ?(\\w*) ?(.*)$";
    Pattern pattern = Pattern.compile(prefixPattern);
    Matcher matcher = pattern.matcher(json.getString("name"));
    if (matcher.find() && matcher.group(2) != null && !matcher.group(2).equals(""))
    {
      String synonymText = matcher.group(2);
      
      Synonym synonym = new Synonym();
      synonym.getDisplayLabel().setValue(synonymText);
      Synonym.create(synonym, geo.getOid());
    }
  }
  
  private void setName()
  {
    String name = json.getString("name");
    
    geo.getDisplayLabel().setValue(name);
  }
  
  public void applyLocatedIn()
  {
    if (skipMe) { return; }
    
    String oid = json.getString("oid");
    
    GeoEntity parent;
    try
    {
      if (json.has("parent"))
      {
        // DHIS2 woes: Kinda dumb to have a JSONObject that will always only have an oid in it.
        parent = GeoEntity.getByKey(json.getJSONObject("parent").getString("oid"));
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
  
  private boolean setUniversal()
  {
    String path = json.getString("path");
    
    if (!path.startsWith("/"))
    {
      path = "/" + path;
    }
    
    int level = StringUtils.countMatches(path, "/") - 1;
    
    if (level == 0 && countryOrgUnitId != null && !json.getString("oid").equals(countryOrgUnitId))
    {
      return false;
    }
    
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
    
    return true;
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
    try
    {
      if (json.has("featureType") && json.has("coordinates"))
      {
        String featureType = json.getString("featureType");
        
        if (featureType == null || featureType.equals("NONE") || featureType.equals("")) { return; }
        
        JSONArray coordinates = new JSONArray(json.getString("coordinates")); // DHIS2 woes: Their coordinates are wrapped in quotes (its a string). We want to parse it as JSON.
        
        if (coordinates == null || coordinates.equals("NONE") || coordinates.equals("")) { return; }
        
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
    }
    catch (JSONException e)
    {
      logger.warn("Problem importing geometries for Org unit [" + geo.getDisplayLabel() + " : " + geo.getGeoId() + "]");
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
