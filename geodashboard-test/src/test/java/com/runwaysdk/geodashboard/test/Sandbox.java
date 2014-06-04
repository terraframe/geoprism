package com.runwaysdk.geodashboard.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverFacade;
import com.runwaysdk.geodashboard.gis.model.Layer;
import com.runwaysdk.geodashboard.gis.persist.AllLayerType;
import com.runwaysdk.geodashboard.gis.persist.DashboardLayer;
import com.runwaysdk.geodashboard.gis.persist.DashboardMap;
import com.runwaysdk.geodashboard.gis.persist.DashboardStyle;
import com.runwaysdk.geodashboard.gis.persist.HasLayer;
import com.runwaysdk.geodashboard.gis.persist.HasStyle;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.UsersDTO;
import com.runwaysdk.web.WebClientSession;
import com.runwaysdk.web.json.JSONController;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

public class Sandbox
{
  @Request
  public static void main(String[] args) throws Throwable
  {
    GeoserverFacade.publishWorkspace();
    GeoserverFacade.publishStore();
  }

  @Request
  @Transaction
  public static void testGetMapJSON()
  {
    System.out.println("testMap");

    // String json = new DashboardMap().getMapJSON();

    List<Layer> bboxLayers = new LinkedList<Layer>();
    JSONArray bbox = getMapLayersBBox(bboxLayers);

    System.out.println(bbox);

    // System.out.println(json);

    // testBuildMap();

  }

  @Request
  @Transaction
  public static void testBuildMap()
  {
    System.out.println("testBuildMap");

    DashboardMap map = new DashboardMap();
    map.setName("Test Map");
    map.apply();

    DashboardLayer layer = new DashboardLayer();
    layer.setName("aa_test_data");
    layer.addLayerType(AllLayerType.BASIC);
    layer.setDisplayInLegend(false);
    layer.setVirtual(false);
    layer.setUniversal(null);
    layer.setGeoEntity(null);
    layer.apply();

    HasLayer hasLayer = map.addHasLayer(layer);
    hasLayer.setLayerIndex(0);
    hasLayer.apply();

    DashboardStyle style = new DashboardStyle();
    style.setName("polygon");
    style.setValueSize(3);
    style.setValueFont("Arial");
    style.setValueColor("red");
    style.setValueHalo("white");
    style.setValueHaloWidth(2);
    style.apply();

    HasStyle hasStyle = layer.addHasStyle(style);
    hasStyle.apply();

    // String json = map.getMapJSON();

    // String json = DashboardMap.getMapJSON(map.getId());
    // System.out.println(json);

    map.delete();

    System.out.println("all done.");

  }

  public static JSONArray getMapLayersBBox(List<Layer> layers)
  {

    JSONArray bboxArr = new JSONArray();
    ResultSet resultSet = null;
    String[] layerNames = null;

    // if (layers.size() > 0)
    if (layers.size() < 1) // for testing
    {
      layerNames = new String[layers.size()];
      String sql;

      // if (layers.size() == 1)
      if (layers.size() < 1) // for testing
      {
        // String layer = layers.get(0);
        // String viewName = layers.get;
        // layerNames[0] = layers.get(0);

        String viewName = "aa_test_data_view";

        sql = "SELECT ST_AsText(ST_Extent(" + viewName + "." + GeoserverFacade.GEOM_COLUMN
            + ")) AS bbox FROM " + viewName;
      }
      else
      {
        // More than one layer so union the geometry columns
        sql = "SELECT ST_AsText(ST_Extent(geo_v)) AS bbox FROM (\n";

        for (int i = 0; i < layers.size(); i++)
        {
          Layer layer = layers.get(i);
          String viewName = layer.getName();
          layerNames[i] = layer.getName();

          sql += "(SELECT " + GeoserverFacade.GEOM_COLUMN + " AS geo_v FROM " + viewName + ") \n";

          if (i != layers.size() - 1)
          {
            sql += "UNION \n";
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

}
