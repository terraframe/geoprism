package com.runwaysdk.geodashboard.gis.persist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverFacade;
import com.runwaysdk.geodashboard.gis.model.Layer;
import com.runwaysdk.geodashboard.gis.model.Map;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.sld.SLDMapVisitor;
import com.runwaysdk.logging.LogLevel;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

public class DashboardMap extends DashboardMapBase implements
    com.runwaysdk.generation.loader.Reloadable, Map
{
  private static Log        log              = LogFactory.getLog(DashboardMap.class);

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

  /**
   * Returns the layers this map defines in the proper order.
   * 
   * @return
   */
  public DashboardLayer[] getOrderedLayers()
  {
    QueryFactory f = new QueryFactory();

    HasLayerQuery hsQ = new HasLayerQuery(f);
    DashboardLayerQuery layerQ = new DashboardLayerQuery(f);

    hsQ.WHERE(hsQ.parentId().EQ(this.getId()));
    layerQ.WHERE(layerQ.containingMap(hsQ));
    layerQ.WHERE(layerQ.getLayerEnabled().EQ(true));
    hsQ.ORDER_BY_ASC(hsQ.getLayerIndex());

    OIterator<? extends DashboardLayer> iter = layerQ.getIterator();

    try
    {
      List<DashboardLayer> layers = new LinkedList<DashboardLayer>();
      while (iter.hasNext())
      {
        layers.add(iter.next());
      }

      return layers.toArray(new DashboardLayer[layers.size()]);
    }
    finally
    {
      iter.close();
    }
  }

  @com.runwaysdk.logging.Log(level=LogLevel.DEBUG)
  public String getMapJSON()
  {
    try
    {
      
      DashboardLayer[] orderedLayers = this.getOrderedLayers();
      
      log.debug("Looping over layers for map ["+this.toString()+"] and removing artifacts.");
      
      for(DashboardLayer layer : orderedLayers)
      {
        String layerName = layer.getViewName();
        
        // remove the layer
        try
        {
          GeoserverFacade.removeLayer(layerName);
          log.debug("Deleting layer ["+layerName+"].");
        }
        catch(Throwable t)
        {
          log.error("Error deleting layer ["+layerName+"].", t);
          
        }
        
        // remove the style
        try
        {
          GeoserverFacade.removeStyle(layerName);
          log.debug("Deleting style ["+layerName+"].");
        }
        catch(Throwable t)
        {
          log.error("Error deleting style ["+layerName+"].", t);
        }
      }
      
      // now that we've deleted all the map artifacts refresh geoserver
      // so any read/write operations will use accurate date
      GeoserverFacade.refresh();
      
      /**
       * All layers returned in order and if they're enabled
       */
      for(DashboardLayer layer : orderedLayers)
      {
        String layerName = layer.getViewName();
        ValueQuery vq = layer.asValueQuery();
        try
        {
          Database.dropView(layerName, vq.getSQL(), false);
        }
        catch(Throwable t)
        {
          log.warn("Dropping/creating view ["+layerName+"].", t);
        }
        finally
        {
          Database.createView(layerName, vq.getSQL());
        }
        
        try
        {
          SLDMapVisitor visitor = new SLDMapVisitor();
          this.accepts(visitor);
          String sld = visitor.getSLD(layer);
          
          if(!GeoserverFacade.publishStyle(layerName, sld))
          {
            log.error("Failure publishing style ["+layerName+"].");
          }
        }
        catch(Throwable t)
        {
          log.error("Error publishing style ["+layerName+"].", t);
        }
        
        try
        {
          if(!GeoserverFacade.publishLayer(layerName, layerName))
          {
            log.error("Failure publishing layer ["+layerName+"].");
          }
        }
        catch(Throwable t)
        {
          log.error("Error publishing layer ["+layerName+"].", t);
        }
      }

      JSONObject mapJSON = new JSONObject();
      mapJSON.put("mapName", this.getName());

      JSONArray mapBBox = getMapLayersBBox();
      mapJSON.put("bbox", mapBBox);

      JSONArray layers = new JSONArray();
      mapJSON.put("layers", layers);

      List<DashboardLayer> addedLayers = new LinkedList<DashboardLayer>();

      for (int i = 0; i < orderedLayers.length; i++)
      {
        DashboardLayer layer = orderedLayers[i];
        if (true /* test if layer is valid--1+ rows and valid geoms */)
        {
          JSONObject layerObj = new JSONObject();
          layerObj.put("viewName", layer.getViewName());
          layerObj.put("sldName", layer.getSLDName());
          layerObj.put("layerName", layer.getName());
          layerObj.put("layerId", layer.getId());
          layers.put(layerObj);

          addedLayers.add(layer);
        }
      }

      if (log.isDebugEnabled())
      {
        log.debug("JSON for map [" + this + "]:\n" + mapJSON.toString(4));
      }

      return mapJSON.toString();
    }
    catch (JSONException ex)
    {
      log.error("Could not properly form map [" + this + "] into valid JSON to send back to the client.");
      throw new ProgrammingErrorException(ex);
    }
  }

  @Override
  public String getName()
  {
    return super.getName();
  }

  public JSONArray getMapLayersBBox()
  {
    return this.getMapLayersBBox(this.getOrderedLayers());
  }

  public JSONArray getMapLayersBBox(DashboardLayer[] layers)
  {
    JSONArray bboxArr = new JSONArray();
    ResultSet resultSet = null;
    String[] layerNames = null;

    if (layers.length > 0)
    {
      layerNames = new String[layers.length];
      String sql;

      if (layers.length == 1)
      {
        // This needs to get the 1st (only) layer in the list and that layers
        // viewname and layername
        DashboardLayer layer = layers[0];
        String viewName = layer.getViewName();
        layerNames[0] = layer.getName();

        sql = "SELECT ST_AsText(ST_Extent(" + viewName + "." + GeoserverFacade.GEOM_COLUMN
            + ")) AS bbox FROM " + viewName;
      }
      else
      {
        // More than one layer so union the geometry columns
        sql = "SELECT ST_AsText(ST_Extent(geo_v)) AS bbox FROM (\n";

        for (int i = 0; i < layers.length; i++)
        {
          DashboardLayer layer = layers[i];
          String viewName = layer.getViewName();
          layerNames[i] = layer.getName();

          sql += "(SELECT " + GeoserverFacade.GEOM_COLUMN + " AS geo_v FROM " + viewName + ") \n";

          if (i != layers.length - 1)
          {
            sql += "UNION ALL\n";
          }
        }

        sql += ") bbox_union";
      }

      resultSet = Database.query(sql);

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
                throw new ProgrammingErrorException(error);
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
    }
    else
    {
      // There are no layers in the map so return the Cambodian defaults
      try
      {
        bboxArr.put(99.60205078124999);
        bboxArr.put(10.28249130152419);
        bboxArr.put(111.33544921874999);
        bboxArr.put(14.764259178591587);
      }
      catch (JSONException ex)
      {
        throw new ProgrammingErrorException(ex);
      }
    }

    return bboxArr;
  }

  @Transaction
  public void delete()
  {
    for (DashboardLayer layer : this.getAllHasLayer())
    {
      layer.delete();
    }

    super.delete();
  }
  
  @Override
  public String toString()
  {
    return String.format("[%s] = %s", this.getClassDisplayLabel(), this.getName());
  }
  
  /**
   * Cleans old map artifacts.
   * 
   * TODO although it's not a problem to blindly remove layers/styles, there might
   * be a better way to save unchanged resources and avoid costly regeneration. 
   */
  public static void cleanup()
  {
    // Delete all generated views
    List<String> viewNames = Database.getViewsByPrefix(DashboardLayer.DB_VIEW_PREFIX);
    Database.dropViews(viewNames);
  }
}
