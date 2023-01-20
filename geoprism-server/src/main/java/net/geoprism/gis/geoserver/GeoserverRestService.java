/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.gis.geoserver;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.geotools.ows.wms.WMSCapabilities;
import org.geotools.ows.wms.WebMapServer;
import org.json.JSONArray;
import org.json.JSONObject;

import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.gis.mapping.gwc.SeedRequest;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.ConfigurationException;
import com.runwaysdk.util.FileIO;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTStoreManager;
import net.geoprism.dashboard.DashboardStyle;
import net.geoprism.dashboard.layer.DashboardLayer;

public class GeoserverRestService implements GeoserverService
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
  private class CacheFilter implements FileFilter
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

  public void publishGeoTiff(String storeName, File geoTiff)
  {
    publishGeoTiff(GeoserverProperties.getWorkspace(), storeName, geoTiff);
  }

  public void publishGeoTiff(final String workspace, String storeName, File geoTiff)
  {
    try
    {
      if (GeoserverProperties.getPublisher().publishGeoTIFF(workspace, storeName, geoTiff))
      {
//        StringBuilder xml = new StringBuilder();
//        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
//        xml.append("<StyledLayerDescriptor version=\"1.0.0\" \n" + " xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" \n" + " xmlns=\"http://www.opengis.net/sld\" \n" + " xmlns:ogc=\"http://www.opengis.net/ogc\" \n" + " xmlns:xlink=\"http://www.w3.org/1999/xlink\" \n" + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");
//        xml.append("  <!-- a Named Layer is the basic building block of an SLD document -->\n");
//        xml.append("  <NamedLayer>\n");
//        xml.append("    <Name>default_raster</Name>\n");
//        xml.append("    <UserStyle>\n");
//        xml.append("    <!-- Styles can have names, titles and abstracts -->\n");
//        xml.append("      <Title>Default Raster</Title>\n");
//        xml.append("      <Abstract>A sample style that draws a raster, good for displaying imagery</Abstract>\n");
//        xml.append("      <!-- FeatureTypeStyles describe how to render different features -->\n");
//        xml.append("      <!-- A FeatureTypeStyle for rendering rasters -->\n");
//        xml.append("      <FeatureTypeStyle>\n");
//        xml.append("        <Rule>\n");
//        xml.append("          <Name>rule1</Name>\n");
//        xml.append("          <Title>Opaque Raster</Title>\n");
//        xml.append("          <Abstract>A raster with 100% opacity</Abstract>\n");
//        xml.append("          <RasterSymbolizer>\n");
//        xml.append("            <Opacity>1.0</Opacity>\n");
//        xml.append("          </RasterSymbolizer>\n");
//        xml.append("        </Rule>\n");
//        xml.append("      </FeatureTypeStyle>\n");
//        xml.append("    </UserStyle>\n");
//        xml.append("  </NamedLayer>\n");
//        xml.append("</StyledLayerDescriptor>\n");
//
//        GeoserverProperties.getPublisher().publishStyle(xml.toString(), storeName);

        log.info("Published geo tiff [" + storeName + "], [" + geoTiff.getAbsolutePath() + "].");
      }
      else
      {
        log.warn("Failed to publish geo tiff [" + storeName + "], [" + geoTiff.getAbsolutePath() + "].");
      }
    }
    catch (Throwable t)
    {
      log.warn("Failed to publish geo tiff [" + storeName + "], [" + geoTiff.getAbsolutePath() + "].", t);
    }
  }

//  @Override
//  public void publishS3GeoTIFF(String storeName, String url)
//  {
//    try
//    {
//      S3Object s3Obj = download(url);
//      
//      File temp = Files.createTempFile("geotiff-" + storeName, ".tif").toFile();
//      IOUtils.copy(s3Obj.getObjectContent(), new FileOutputStream(temp));
//      
//      if (GeoserverProperties.getPublisher().publishGeoTIFF(GeoserverProperties.getWorkspace(), storeName, temp))
//      {
//        log.info("Published s3 geo tiff [" + storeName + "], [" + url + "].");
//      }
//      else
//      {
//        log.warn("Failed to publish s3 geo tiff [" + storeName + "], [" + url + "].");
//      }
//    }
//    catch (Throwable t)
//    {
//      log.warn("Failed to publish s3 geo tiff [" + storeName + "], [" + url + "].", t);
//    }
//  }

  public void removeCoverageStore(String storeName)
  {
    removeCoverageStore(GeoserverProperties.getWorkspace(), storeName);
  }

  public void removeCoverageStore(final String workspace, String storeName)
  {
    if (GeoserverProperties.getPublisher().removeCoverageStore(workspace, storeName, true, GeoServerRESTPublisher.Purge.ALL))
    {
      log.info("Removed the coverage store [" + GeoserverProperties.getStore() + "].");

      // This is dumb but manually delete the granule file
      String geoserverData = System.getProperty("GEOSERVER_DATA_DIR");

      if (geoserverData != null)
      {
        try
        {
          FileUtils.deleteDirectory(new File(geoserverData + "/data/" + workspace + "/" + storeName));
        }
        catch(IOException e)
        {
          log.warn("Failed to delete the data directory of the coverage store [" + storeName + "].");          
        }
      }
    }
    else
    {
      log.warn("Failed to remove the coverage store [" + storeName + "].");
    }
  }

  @Override
  public WMSCapabilities getCapabilities(String layer)
  {
    return getCapabilities(GeoserverProperties.getWorkspace(), layer);
  }

  @Override
  public WMSCapabilities getCapabilities(final String workspace, String layer)
  {
    try
    {
      String path = GeoserverProperties.getLocalPath() + "/" + workspace;

      if (layer != null)
      {
        path = path + "/" + layer;
      }

      path = path + "/wms";

      WebMapServer wms = new WebMapServer(new URL(path), 1000000);
      return wms.getCapabilities();
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public void removeStore()
  {
    removeStore(GeoserverProperties.getWorkspace());
  }

  public void removeStore(final String workspace)
  {
    if (GeoserverProperties.getPublisher().removeDatastore(workspace, GeoserverProperties.getStore(), true))
    {
      log.info("Removed the datastore [" + GeoserverProperties.getStore() + "].");
    }
    else
    {
      log.warn("Failed to remove the datastore [" + GeoserverProperties.getStore() + "].");
    }
  }

//  public void removeCoverageStore(String storeName)
//  {
//    if (GeoserverProperties.getPublisher().removeCoverageStore(GeoserverProperties.getWorkspace(), storeName, true))
//    {
//      log.info("Removed the datastore [" + GeoserverProperties.getStore() + "].");
//    }
//    else
//    {
//      log.warn("Failed to remove the datastore [" + GeoserverProperties.getStore() + "].");
//    }
//  }

  public void removeWorkspace()
  {
    removeWorkspace(GeoserverProperties.getWorkspace());
  }

  public void removeWorkspace(String workspace)
  {
    if (GeoserverProperties.getPublisher().removeWorkspace(workspace, true))
    {
      log.info("Removed the workspace [" + workspace + "].");
    }
    else
    {
      log.warn("Failed to remove the workspace [" + workspace + "].");
    }
  }

  public void publishWorkspace()
  {
    this.publishWorkspace(GeoserverProperties.getWorkspace());
  }

  @Override
  public void publishWorkspace(String workspace)
  {
    try
    {
      GeoServerRESTPublisher publisher = GeoserverProperties.getPublisher();

      // IMPORTANT: The URI must match the namespace of the store or rendering
      // doesn't work
      if (publisher.createWorkspace(workspace, new URI(workspace)))
      {
        log.info("Created the workspace [" + workspace + "].");
      }
      else
      {
        log.warn("Failed to create the workspace [" + workspace + "].");
      }
    }
    catch (URISyntaxException e)
    {
      throw new ConfigurationException("The URI [" + workspace + "] is invalid.", e);
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
    publishStore(GeoserverProperties.getWorkspace());
  }

  @Override
  public void publishStore(String workspace)
  {
    String dbSchema = DatabaseProperties.getNamespace().length() != 0 ? DatabaseProperties.getNamespace() : "public";

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
    return workspaceExists(GeoserverProperties.getWorkspace());
  }

  public boolean workspaceExists(String workspace)
  {
    GeoServerRESTReader reader = GeoserverProperties.getReader();
    return reader.existsWorkspace(workspace);
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
   * Checks if the cache directory exists. This method does not check what tiles
   * or zoom levels have been cached.
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
   * Removes the style defined in Geoserver, including the .sld and .xml file
   * artifacts.
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

    if (publisher.publishStyle(body, styleName, true))
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

    if (styleName != null && styleName.length() > 0)
    {
      le.setDefaultStyle(styleName);
      le.setEnabled(true);
    }

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
      forceRemoveLayer(workspace, layer);
    }
  }

  /**
   * Removes the layer from geoserver.
   * 
   * @param layer
   * @return
   */
  public void forceRemoveLayer(String layer)
  {
    forceRemoveLayer(GeoserverProperties.getWorkspace(), layer);
  }

  public void forceRemoveLayer(String workspace, String layer)
  {
    GeoServerRESTPublisher publisher = GeoserverProperties.getPublisher();

    if (publisher.unpublishFeatureType(workspace, GeoserverProperties.getStore(), layer))
    {
      log.info("Removed the layer for [" + layer + "].");
    }
    else
    {
      log.warn("Failed to remove the layer for [" + layer + "].");
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
  @Override
  public boolean layerExists(String layer)
  {
    return layerExists(GeoserverProperties.getWorkspace(), layer);
  }

  @Override
  public boolean layerExists(String workspace, String layer)
  {
    GeoServerRESTReader reader = GeoserverProperties.getReader();

    return reader.existsLayer(workspace, layer, true);
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

  public boolean publishS3GeoTIFF(String storeName, String url)
  {
    final String workspace = GeoserverProperties.getWorkspace();

    try
    {
      boolean success = createS3CoverageStore(workspace, storeName, url);

      if (success)
      {
        success = this.createS3Coverage(workspace, storeName, storeName);
      }

      return success;
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private boolean createS3CoverageStore(String workspace, String storeName, String url) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, UnsupportedEncodingException, IOException, ClientProtocolException
  {
    StringBuilder sbUrl = new StringBuilder(GeoserverProperties.getLocalPath()).append("/rest/workspaces/").append(workspace).append("/coveragestores");

    JSONObject wObject = new JSONObject();
    wObject.put("name", workspace);
    wObject.put("link", workspace);

    JSONObject coverageStore = new JSONObject();
    coverageStore.put("name", storeName);
    coverageStore.put("description", storeName);
    coverageStore.put("type", "S3GeoTiff");
    coverageStore.put("enabled", true);
    coverageStore.put("workspace", wObject);
    coverageStore.put("url", url);

    JSONObject params = new JSONObject();
    params.put("coverageStore", coverageStore);

    return this.post(sbUrl, params);
  }

  private boolean createUrlCoverageStore(String workspace, String storeName, String url) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, UnsupportedEncodingException, IOException, ClientProtocolException
  {
    StringBuilder sbUrl = new StringBuilder(GeoserverProperties.getLocalPath()).append("/rest/workspaces/").append(workspace).append("/coveragestores");

    JSONObject wObject = new JSONObject();
    wObject.put("name", workspace);
    wObject.put("link", workspace);

    JSONObject coverageStore = new JSONObject();
    coverageStore.put("name", storeName);
    coverageStore.put("description", storeName);
    coverageStore.put("type", "S3GeoTiff");
    coverageStore.put("enabled", true);
    coverageStore.put("workspace", wObject);
    coverageStore.put("url", url);

    JSONObject params = new JSONObject();
    params.put("coverageStore", coverageStore);

    return this.post(sbUrl, params);
  }

  private boolean createS3Coverage(String workspace, String storeName, String coverageName) throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
  {

    StringBuilder sbUrl = new StringBuilder("https://localhost:8443/geoserver").append("/rest/workspaces/").append(workspace).append("/coveragestores/");
    sbUrl.append(storeName).append("/coverages");

    JSONArray array = new JSONArray();
    array.put("WCS");
    array.put("S3GeoTiff");

    JSONObject keywords = new JSONObject();
    keywords.put("string", array);

    JSONObject coverage = new JSONObject();
    coverage.put("name", coverageName);
    coverage.put("keywords", keywords);
    coverage.put("enabled", true);

    JSONObject params = new JSONObject();
    params.put("coverage", coverage);

    return this.post(sbUrl, params);
  }

  private boolean post(StringBuilder sbUrl, JSONObject params) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, UnsupportedEncodingException, IOException, ClientProtocolException
  {
    SSLContextBuilder builder = new SSLContextBuilder();
    builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());

    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

    HttpClientBuilder cbuilder = HttpClients.custom().setSSLSocketFactory(sslsf).setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

    boolean success = false;

    try (CloseableHttpClient client = cbuilder.build())
    {
      StringEntity entity = new StringEntity(params.toString(), ContentType.APPLICATION_JSON);
      String encoding = Base64.getEncoder().encodeToString( ( GeoserverProperties.getAdminUser() + ":" + GeoserverProperties.getAdminPassword() ).getBytes("UTF-8"));

      HttpPost httpPost = new HttpPost(sbUrl.toString());
      httpPost.setEntity(entity);
      httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);

      CloseableHttpResponse response = client.execute(httpPost);

      success = response.getStatusLine().getStatusCode() == 201;
    }
    return success;
  }
}
