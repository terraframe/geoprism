package com.runwaysdk.geodashboard.gis.persist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.geodashboard.geoserver.GeoserverFacade;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.gis.model.Layer;
import com.runwaysdk.geodashboard.gis.model.Map;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.query.OIterator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

public class DashboardMap extends DashboardMapBase implements
    com.runwaysdk.generation.loader.Reloadable, Map
{
  private static final long serialVersionUID = 861649895;

  public DashboardMap()
  {
    super();
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public List<? extends Layer> getLayers()
  {
    return this.getAllHasLayer().getAll();
  }

  public String getMapJSON()
  {
    try
    {
//      List<DashboardLayer> layerList = new ArrayList<DashboardLayer>();
      
      JSONObject mapJSON = new JSONObject();
      mapJSON.put("mapName", this.getName());
      
      JSONArray mapBBox = getMapLayersBBox();       
      mapJSON.put("bbox", mapBBox);
      
      JSONArray layers = new JSONArray();
      mapJSON.put("layers", layers);

      OIterator<? extends DashboardLayer> iter = this.getAllHasLayer();
//      List<? extends DashboardLayer> layerList = iter.getAll();
//      JSONArray mapBBox = getMapLayersBBox(layerList);       
//      mapJSON.put("bbox", mapBBox);
      try
      {
        while (iter.hasNext())
        {
          DashboardLayer layer = iter.next();
                  
//          layerList.add(layer);

          JSONObject layerObj = new JSONObject();
          layerObj.put("viewName", layer.getViewName());
          layerObj.put("sldName", layer.getSLDName());
          layerObj.put("layerName", layer.getName());
          layers.put(layerObj);
        }
        
//        JSONArray mapBBox = getMapLayersBBox(layerList);       
//        mapJSON.put("bbox", mapBBox);
        
        System.out.println(mapJSON.toString(4));
        return mapJSON.toString();
      }
      finally
      {
        iter.close();
      }
    }
    catch (JSONException ex)
    {
      throw new ProgrammingErrorException(ex);
    }
  }
  
  public JSONArray getMapLayersBBox()
  {
    
    OIterator<? extends DashboardLayer> iter = this.getAllHasLayer();
    List<? extends DashboardLayer> layerList = iter.getAll();
    
    System.out.println(layerList.size() + "layers.size()");
    
    JSONArray bboxArr = new JSONArray();
    ResultSet resultSet = null;
    String[] layerNames = null;

    if (layerList.size() > 0)
    {
      layerNames = new String[layerList.size()];
      String sql;

      if (layerList.size() == 1)
      {

        // This needs to get the 1st (only) layer in the list and that layers viewname and layername
         DashboardLayer layer = layerList.get(0);
         String viewName = layer.getViewName(); 
         layerNames[0] = layer.getName();

         sql = "SELECT ST_AsText(ST_Extent(" + viewName + "." + GeoserverFacade.GEOM_COLUMN + ")) AS bbox FROM " + viewName;
      }
      else
      {
        // More than one layer so union the geometry columns
        sql = "SELECT ST_AsText(ST_Extent(geo_v)) AS bbox FROM (\n";

        for (int i = 0; i < layerList.size(); i++)
        {
          DashboardLayer layer = layerList.get(i);
          String viewName = layer.getViewName(); 
          layerNames[i] = layer.getName();

          sql += "(SELECT " + GeoserverFacade.GEOM_COLUMN + " AS geo_v FROM " + viewName + ") \n";

          if (i != layerList.size() - 1)
          {
            sql += "Union \n";
          }
        }

        sql += ") bbox_union";
      }

      resultSet = Database.query(sql);
    }
    try
    {
      if (resultSet.next())
      {

        String bbox = resultSet.getString("bbox");
        if (bbox != null)
        {
          Pattern p = Pattern.compile("POLYGON\\(\\((.*)\\)\\)");
          Matcher m = p.matcher(bbox);

          if (m.matches())
          {
            String coordinates = m.group(1);
            List<Coordinate> coords = new LinkedList<Coordinate>();

            for (String c : coordinates.split(","))
            {
              String[] xAndY = c.split(" ");
              double x = Double.valueOf(xAndY[0]);
              double y = Double.valueOf(xAndY[1]);

              coords.add(new Coordinate(x, y));
            }

            Envelope e = new Envelope(coords.get(0), coords.get(2));

            try
            {
              bboxArr.put(e.getMinX());
              bboxArr.put(e.getMinY());
              bboxArr.put(e.getMaxX());
              bboxArr.put(e.getMaxY());
            }
            catch (JSONException ex)
            {
              throw new ProgrammingErrorException(ex);
            }
          }
          else
          {
            // There will not be a match if there is a single point geo
            // entity.
            // In this case, return the x,y coordinates to OpenLayers.

            p = Pattern.compile("POINT\\((.*)\\)");
            m = p.matcher(bbox);
            if (m.matches())
            {
              String c = m.group(1);
              String[] xAndY = c.split(" ");
              double x = Double.valueOf(xAndY[0]);
              double y = Double.valueOf(xAndY[1]);

              try
              {
                bboxArr.put(x);
                bboxArr.put(y);
              }
              catch (JSONException ex)
              {
                throw new ProgrammingErrorException(ex);
              }
            }
            else
            {
              String error = "The database view(s) [" + StringUtils.join(layerNames, ",")
                  + "] could not be used to create a valid bounding box";
              // throw new GeoServerReloadException(error);
            }
          }
        }
      }

      return bboxArr;
    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }

    // Some problem occured and the bbox couldn't be calculated.
    // Just return the African defaults
    try
    {
      bboxArr.put(36.718452);
      bboxArr.put(-17.700377000000003);
      bboxArr.put(36.938452);
      bboxArr.put(-17.480376999999997);
    }
    catch (JSONException ex)
    {
      throw new ProgrammingErrorException(ex);
    }

    return bboxArr;
  }

  @Transaction
  public void delete()
  {
    for(DashboardLayer layer : this.getAllHasLayer())
    {
      layer.delete();
    }
    
    super.delete();
  }
}
