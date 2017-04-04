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
