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
package net.geoprism.gis.geoserver;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTStoreManager;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import net.geoprism.dashboard.DashboardStyle;
import net.geoprism.dashboard.layer.DashboardLayer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.gis.mapping.gwc.SeedRequest;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.ConfigurationException;
import com.runwaysdk.util.FileIO;

public class GeoserverRestService implements GeoserverService, Reloadable
{
  public final int    SRS_CODE   = 4326;

  public final String SRS        = "EPSG:" + SRS_CODE;

  public int          MINX_INDEX = 0;

  public int          MINY_INDEX = 1;

  public int          MAXX_INDEX = 2;

  public int          MAXY_INDEX = 3;

  private Log         log        = LogFactory.getLog(GeoserverFacade.class);

  /**
   * Checks if a given File is a cache directory for the workspace.
   */
  private class CacheFilter implements FileFilter, Reloadable
  {
    @Override
    public boolean accept(File file)
    {
      String workspace = GeoserverProperties.getWorkspace();

      return file.isDirectory() && file.getName().startsWith(workspace);
    }
  }

  public void refresh()
  {
    if (GeoserverProperties.getPublisher().reload())
    {
      log.info("Reloaded geoserver.");
    }
    else
    {
      log.warn("Failed to reload geoserver.");
    }
  }

  public void removeStore()
  {
    if (GeoserverProperties.getPublisher().removeDatastore(GeoserverProperties.getWorkspace(), GeoserverProperties.getStore(), true))
    {
      log.info("Removed the datastore [" + GeoserverProperties.getStore() + "].");
    }
    else
    {
      log.warn("Failed to remove the datastore [" + GeoserverProperties.getStore() + "].");
    }
  }

  public void removeWorkspace()
  {
    if (GeoserverProperties.getPublisher().removeWorkspace(GeoserverProperties.getWorkspace(), true))
    {
      log.info("Removed the workspace [" + GeoserverProperties.getWorkspace() + "].");
    }
    else
    {
      log.warn("Failed to remove the workspace [" + GeoserverProperties.getWorkspace() + "].");
    }
  }

  public void publishWorkspace()
  {
    try
    {
      GeoServerRESTPublisher publisher = GeoserverProperties.getPublisher();
      
      // IMPORTANT: The URI must match the namespace of the store or rendering doesn't work
      if (publisher.createWorkspace(GeoserverProperties.getWorkspace(), new URI(GeoserverProperties.getWorkspace())))
      {
        log.info("Created the workspace [" + GeoserverProperties.getWorkspace() + "].");
      }
      else
      {
        log.warn("Failed to create the workspace [" + GeoserverProperties.getWorkspace() + "].");
      }
    }
    catch (URISyntaxException e)
    {
      throw new ConfigurationException("The URI [" + GeoserverProperties.getWorkspace() + "] is invalid.", e);
    }
  }

  /**
   * Checks if Geoserver is available.
   * 
   * @return
   */
  public boolean geoserverExists()
  {
    String path = GeoserverProperties.getLocalPath();

    if (GeoserverProperties.getReader().existGeoserver())
    {
      log.info("A geoserver instance is running at [" + path + "].");
      return true;
    }
    else
    {
      log.warn("A geoserver instance is NOT running at [" + path + "].");
      return false;
    }
  }

  public void publishStore()
  {
    String dbSchema = DatabaseProperties.getNamespace().length() != 0 ? DatabaseProperties.getNamespace() : "public";
    String workspace = GeoserverProperties.getWorkspace();

    GSPostGISDatastoreEncoder encoder = new GSPostGISDatastoreEncoder(GeoserverProperties.getStore());
    encoder.setDatabase(DatabaseProperties.getDatabaseName());
    encoder.setUser(DatabaseProperties.getUser());
    encoder.setPassword(DatabaseProperties.getPassword());
    encoder.setName(GeoserverProperties.getStore());
    encoder.setHost(DatabaseProperties.getServerName());
    encoder.setPort(DatabaseProperties.getPort());
    encoder.setSchema(dbSchema);
    encoder.setNamespace(workspace);
    encoder.setEnabled(true);
    encoder.setMaxConnections(10);
    encoder.setMinConnections(1);
    encoder.setFetchSize(1000);
    encoder.setConnectionTimeout(20);
    encoder.setValidateConnections(true);
    encoder.setLooseBBox(true);
    encoder.setExposePrimaryKeys(true);

    GeoServerRESTStoreManager manager = GeoserverProperties.getManager();
    
    if (manager.create(workspace, encoder))
    {
      log.info("Published the store [" + GeoserverProperties.getStore() + "].");
    }
    else
    {
      log.warn("Failed to publish the store [" + GeoserverProperties.getStore() + "].");
    }
  }
  
  /**
   * Checks if the given workspace exists.
   * 
   * @return
   */
  public boolean workspaceExists()
  {
    GeoServerRESTReader reader = GeoserverProperties.getReader();

    return reader.existsWorkspace(GeoserverProperties.getWorkspace());
  }

  /**
   * Checks if the given style exists in geoserver.
   * 
   * @param styleName
   * @return
   */
  public boolean styleExists(String styleName)
  {
    GeoServerRESTReader reader = GeoserverProperties.getReader();

    return reader.existsStyle(styleName);
  }

  /**
   * Checks if the cache directory exists. This method does not check what tiles or zoom levels have been cached.
   * 
   * @param cacheName
   * @return
   */
  public boolean cacheExists(String cacheName)
  {
    String cacheDir = GeoserverProperties.getGeoserverGWCDir() + cacheName;
    File cache = new File(cacheDir);
    return cache.exists();
  }

  /**
   * Gets all layers declared in Geoserver for the workspace.
   * 
   * @return
   */
  public List<String> getLayers()
  {
    GeoServerRESTReader reader = GeoserverProperties.getReader();

    return reader.getLayers().getNames();
  }

  /**
   * Gets all styles declared in Geoserver for the workspace.
   */
  public List<String> getStyles()
  {
    return GeoserverProperties.getReader().getStyles().getNames();
  }

  /**
   * Removes the style defined in Geoserver, including the .sld and .xml file artifacts.
   * 
   * @param styleName
   *          The name of the style to delete.
   */
  public void removeStyle(String styleName)
  {
    if (styleExists(styleName))
    {
      if (GeoserverProperties.getPublisher().removeStyle(styleName, true))
      {
        log.info("Removed the SLD [" + styleName + "].");
      }
      else
      {
        log.warn("Failed to remove the SLD [" + styleName + "].");
      }

      // There are problems with Geoserver not removing the SLD artifacts,
      // so make sure those are gone
      String stylePath = GeoserverProperties.getGeoserverSLDDir();

      // remove the sld
      File sld = new File(stylePath + styleName + ".sld");
      if (sld.exists())
      {
        boolean deleted = sld.delete();
        if (deleted)
        {
          log.info("Deleted the file [" + sld + "].");
        }
        else
        {
          log.warn("Failed to delete the file [" + sld + "].");
        }
      }
      else
      {
        log.info("The file [" + sld + "] does not exist.");
      }

      // remove the xml
      File xml = new File(stylePath + styleName + ".xml");
      if (xml.exists())
      {
        boolean deleted = sld.delete();
        if (deleted)
        {
          log.info("Deleted the file [" + xml + "].");
        }
        else
        {
          log.warn("Failed to delete the file [" + xml + "].");
        }
      }
      else
      {
        log.info("The file [" + xml + "] does not exist.");
      }
    }
  }

  public boolean publishStyle(String styleName, String body, boolean force)
  {
    GeoServerRESTPublisher publisher = GeoserverProperties.getPublisher();

    if (force && styleExists(styleName))
    {
      publisher.removeStyle(styleName, true);
    }

    if (publisher.publishStyle(body, styleName))
    {
      log.info("Published the SLD [" + styleName + "].");
      return true;
    }
    else
    {
      log.warn("Failed to publish the SLD [" + styleName + "].");
      return false;
    }
  }

  /**
   * Publishes the SQL of the given name with the XML body.
   * 
   * @param styleName
   * @param body
   */
  public boolean publishStyle(String styleName, String body)
  {
    return publishStyle(styleName, body, true);
  }

  public void pushUpdates(GeoserverBatch batch)
  {
    ArrayList<String> layersToDrop = batch.getLayersToDrop();
    ArrayList<String> stylesToDrop = batch.getStylesToDrop();
    ArrayList<DashboardLayer> layersToPublish = batch.getLayersToPublish();

    for (String layerName : layersToDrop)
    {
      removeLayer(layerName);
    }

    for (String styleName : stylesToDrop)
    {
      removeStyle(styleName);
    }

    // GeoServer will say these layers already exist if we don't refresh here.
    if (layersToDrop.size() > 0 && layersToPublish.size() > 0)
    {
      refresh();
    }

    for (DashboardLayer layer : layersToPublish)
    {
      List<? extends DashboardStyle> styles = layer.getStyles();
      for (int i = 0; i < styles.size(); ++i)
      {
        DashboardStyle style = styles.get(i);
        publishStyle(style.getName(), style.generateSLD(), false);
      }

      publishLayer(layer.getViewName(), layer.getViewName());
    }
  }

  /**
   * Adds a database view and publishes the layer if necessary.
   * 
   * @param layer
   */
  public boolean publishLayer(String layer, String styleName)
  {
    double[] bbox = getBBOX(layer);

    GSFeatureTypeEncoder fte = new GSFeatureTypeEncoder();
    fte.setEnabled(true);
    fte.setName(layer);
    fte.setSRS(SRS);
    fte.setTitle(layer);
    fte.addKeyword(layer);

    if (bbox != null)
    {
      double minX = bbox[MINX_INDEX];
      double minY = bbox[MINY_INDEX];
      double maxX = bbox[MAXX_INDEX];
      double maxY = bbox[MAXY_INDEX];

      fte.setNativeBoundingBox(minX, minY, maxX, maxY, SRS);
      fte.setLatLonBoundingBox(minX, minY, maxX, maxY, SRS);
    }

    GSLayerEncoder le = new GSLayerEncoder();
    le.setDefaultStyle(styleName);
    le.setEnabled(true);

    if (GeoserverProperties.getPublisher().publishDBLayer(GeoserverProperties.getWorkspace(), GeoserverProperties.getStore(), fte, le))
    {
      log.debug("Created the layer [" + layer + "] in geoserver.");
      return true;
    }
    else
    {
      log.error("Failed to create the layer [" + layer + "] in geoserver.");
      return false;
    }
  }

  /**
   * Removes the layer from geoserver.
   * 
   * @param layer
   * @return
   */
  public void removeLayer(String layer)
  {
    if (GeoserverFacade.layerExists(layer))
    {
      String workspace = GeoserverProperties.getWorkspace();
      GeoServerRESTPublisher publisher = GeoserverProperties.getPublisher();

      if (publisher.removeLayer(workspace, layer))
      {
        log.info("Removed the layer for [" + layer + "].");
      }
      else
      {
        log.warn("Failed to remove the layer for [" + layer + "].");
      }
    }
  }

  public void publishCache(String layer)
  {
    SeedRequest request = new SeedRequest(layer);
    if (request.doRequest())
    {
      log.info("Started seeding layer [" + layer + "].");
    }
    else
    {
      log.warn("Could not seed layer [" + layer + "]. Response code [" + request.getCode() + "].");
    }
  }

  /**
   * Returns a list of all cache directories.
   * 
   * @return
   */
  public File[] getCaches()
  {
    File cacheRoot = new File(GeoserverProperties.getGeoserverGWCDir());
    return cacheRoot.listFiles(new CacheFilter());
  }

  public void removeCache(File cache)
  {
    if (cache.exists())
    {
      try
      {
        FileIO.deleteDirectory(cache);

        if (cache.exists())
        {
          log.info("Deleted the cache [" + cache + "].");
        }
        else
        {
          log.warn("Failed to delete the cache [" + cache + "].");
        }
      }
      catch (IOException e)
      {
        log.error("Error deleting the cache [" + cache + "].", e);
      }
    }
    else
    {
      log.info("The cache [" + cache + "] does not exist.");
    }
  }

  /**
   * Removes all cache files and directories.
   * 
   * @param cacheName
   */
  public void removeCache(String cacheName)
  {
    String cacheDir = GeoserverProperties.getGeoserverGWCDir() + cacheName;
    removeCache(new File(cacheDir));
  }

  /**
   * Checks if the given layer exists in Geoserver.
   * 
   * @param layer
   * @return
   */
  public boolean layerExists(String layer)
  {
    GeoServerRESTReader reader = GeoserverProperties.getReader();

    boolean exists = reader.existsStyle(layer);

    return exists;
  }

  /**
   * Checks if the given layer has a database view.
   * 
   * @param layer
   * @return
   */
  public boolean viewExists(String viewName)
  {
    // create the view if the view exists in the pg metadata
    ValueQuery check = new ValueQuery(new QueryFactory());

    check.SELECT(check.aSQLAggregateInteger("ct", "count(*)"));
    check.FROM("pg_views", "pg_views");
    check.WHERE(check.aSQLCharacter("viewname", "viewname").EQ(viewName));

    OIterator<? extends ValueObject> iter = check.getIterator();
    try
    {
      ValueObject vo = iter.next();
      if (Integer.valueOf(vo.getValue("ct")) == 1)
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    finally
    {
      iter.close();
    }
  }

  /**
   * Calculates the bounding box of all the layers.
   * 
   * @param views
   * @return double[] {minx, miny, maxx, maxy}
   */
  public double[] getBBOX(String... views)
  {
    // collect all the views and extend the bounding box
    ValueQuery union = new ValueQuery(new QueryFactory());
    if (views.length == 1)
    {
      String view = views[0];
      union.SELECT(union.aSQLClob(GeoserverFacade.GEOM_COLUMN, GeoserverFacade.GEOM_COLUMN, GeoserverFacade.GEOM_COLUMN));
      union.FROM(view, view);
    }
    else if (views.length > 1)
    {
      ValueQuery[] unionVQs = new ValueQuery[views.length];

      for (int i = 0; i < unionVQs.length; i++)
      {
        String view = views[i];
        ValueQuery vq = new ValueQuery(union.getQueryFactory());
        vq.SELECT(vq.aSQLClob(GeoserverFacade.GEOM_COLUMN, GeoserverFacade.GEOM_COLUMN, GeoserverFacade.GEOM_COLUMN));
        vq.FROM(view, view);

        unionVQs[i] = vq;
      }

      union.UNION_ALL(unionVQs);
    }
    else
    {
      throw new MapLayerException("The map has no layers");
    }

    ValueQuery collected = new ValueQuery(union.getQueryFactory());
    collected.SELECT(collected.aSQLAggregateClob("collected", "st_collect(" + GeoserverFacade.GEOM_COLUMN + ")", "collected"));
    collected.FROM("(" + union.getSQL() + ")", "unioned");

    ValueQuery outer = new ValueQuery(union.getQueryFactory());
    outer.SELECT(union.aSQLAggregateDouble("minx", "st_xmin(collected)"), union.aSQLAggregateDouble("miny", "st_ymin(collected)"), union.aSQLAggregateDouble("maxx", "st_xmax(collected)"), union.aSQLAggregateDouble("maxy", "st_ymax(collected)"));

    outer.FROM("(" + collected.getSQL() + ")", "collected");

    OIterator<? extends ValueObject> iter = outer.getIterator();

    try
    {
      ValueObject o = iter.next();

      double[] bbox = new double[4];
      bbox[MINX_INDEX] = Double.parseDouble(o.getValue("minx"));
      bbox[MINY_INDEX] = Double.parseDouble(o.getValue("miny"));
      bbox[MAXX_INDEX] = Double.parseDouble(o.getValue("maxx"));
      bbox[MAXY_INDEX] = Double.parseDouble(o.getValue("maxy"));

      return bbox;
    }
    catch (Exception e)
    {
      return null;
      // throw new NoLayerDataException();
    }
    finally
    {
      iter.close();
    }
  }

  @Override
  public double[] getExpandedBBOX(List<String> views, double expandVal)
  {
    // collect all the views and extend the bounding box
    ValueQuery union = new ValueQuery(new QueryFactory());
    if (views.size() == 1)
    {
      String view = views.iterator().next();
      union.SELECT(union.aSQLClob(GeoserverFacade.GEOM_COLUMN, GeoserverFacade.GEOM_COLUMN, GeoserverFacade.GEOM_COLUMN));
      union.FROM(view, view);
    }
    else if (views.size() > 1)
    {
      ValueQuery[] unionVQs = new ValueQuery[views.size()];

      for (int i = 0; i < unionVQs.length; i++)
      {
        String view = views.get(i);
        ValueQuery vq = new ValueQuery(union.getQueryFactory());
        vq.SELECT(vq.aSQLClob(GeoserverFacade.GEOM_COLUMN, GeoserverFacade.GEOM_COLUMN, GeoserverFacade.GEOM_COLUMN));
        vq.FROM(view, view);

        unionVQs[i] = vq;
      }

      union.UNION_ALL(unionVQs);
    }
    else
    {
      throw new MapLayerException("The map has no layers");
    }

    ValueQuery collected = new ValueQuery(union.getQueryFactory());
    collected.SELECT(collected.aSQLAggregateClob("collected", "ST_Expand(ST_Collect(" + GeoserverFacade.GEOM_COLUMN + "), " + expandVal + ")", "collected"));
    collected.FROM("(" + union.getSQL() + ")", "unioned");

    ValueQuery outer = new ValueQuery(union.getQueryFactory());
    outer.SELECT(union.aSQLAggregateDouble("minx", "st_xmin(collected)"), union.aSQLAggregateDouble("miny", "st_ymin(collected)"), union.aSQLAggregateDouble("maxx", "st_xmax(collected)"), union.aSQLAggregateDouble("maxy", "st_ymax(collected)"));

    outer.FROM("(" + collected.getSQL() + ")", "collected");

    OIterator<? extends ValueObject> iter = outer.getIterator();

    try
    {
      ValueObject o = iter.next();

      double[] bbox = new double[4];
      bbox[MINX_INDEX] = Double.parseDouble(o.getValue("minx"));
      bbox[MINY_INDEX] = Double.parseDouble(o.getValue("miny"));
      bbox[MAXX_INDEX] = Double.parseDouble(o.getValue("maxx"));
      bbox[MAXY_INDEX] = Double.parseDouble(o.getValue("maxy"));

      return bbox;
    }
    catch (Exception e)
    {
      return null;
      // throw new NoLayerDataException();
    }
    finally
    {
      iter.close();
    }
  }

}
