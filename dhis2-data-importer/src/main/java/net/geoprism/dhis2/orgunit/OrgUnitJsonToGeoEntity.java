package net.geoprism.dhis2.orgunit;

import java.util.Locale;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.InvalidIdException;
import com.runwaysdk.gis.geometry.GeometryHelper;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.Universal;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import net.geoprism.dhis2.DHIS2DataImporter;

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
    
    geo.setGeoId(json.getString("code"));
    
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
      parent = GeoEntity.searchByGeoId(json.getString("parent"));
    }
    catch(InvalidIdException e)
    {
      throw new RuntimeException("The DHIS2 child OrgUnit [" + json.toString() + "] references a parent [" + json.getString("parent") + "] but that GeoEntity does not exist.", e);
    }
    
    geo.addLocatedIn(parent).apply();
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
      uni = importer.getUniversalByLevel(level);
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      throw new UnsupportedOperationException("Unable to find matching universal for OrgUnit [" + json + "]. We calculated a universal level of [" + level + "] but no universal exists at that level.", e);
    }
    
    geo.setUniversal(uni);
  }
  
  private void setLocales()
  {
    JSONArray translations = json.getJSONArray("translations");
    
    for (int i = 0; i < translations.length(); ++i)
    {
      JSONObject translation = translations.getJSONObject(i);
      
//      String property = translation.getString("property");
      String locale = translation.getString("locale");
      String value = translation.getString("value");
      
      Locale localeAsLocale = LocaleUtils.toLocale(locale);
      
      geo.getDisplayLabel().setValue(localeAsLocale, value);
    }
  }
  
  private void setGeometry()
  {
    String featureType = json.getString("featureType");
    
    if (featureType == null || featureType.equals("NONE")) { return; }
    
    JSONArray coordinates = json.getJSONArray("coordinates");
    
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
      Polygon polygon = jsonToPolygon(coordinates);
      
      geo.setGeoPoint(this.geometryHelper.getGeoPoint(polygon));
      geo.setGeoMultiPolygon(geometryHelper.getGeoMultiPolygon(polygon));
      geo.setWkt(polygon.toText());
    }
    else if (featureType.equals("MULTI_POLYGON"))
    {
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
