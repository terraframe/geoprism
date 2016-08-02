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
package net.geoprism.dashboard;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;

import net.geoprism.MappableClass;
import net.geoprism.MappableClassQuery;
import net.geoprism.dashboard.condition.DashboardCondition;
import net.geoprism.dashboard.layer.DashboardLayer;
import net.geoprism.dashboard.layer.DashboardReferenceLayer;
import net.geoprism.dashboard.layer.DashboardThematicLayer;
import net.geoprism.dashboard.layer.HasLayer;
import net.geoprism.dashboard.layer.HasLayerQuery;
import net.geoprism.dashboard.query.MdAttributeViewPredicate;
import net.geoprism.data.etl.excel.ValueQueryExcelExporter;
import net.geoprism.gis.geoserver.GeoserverBatch;
import net.geoprism.gis.geoserver.GeoserverFacade;
import net.geoprism.gis.geoserver.GeoserverProperties;
import net.geoprism.gis.wrapper.MapVisitor;
import net.geoprism.ontology.GeoEntityUtil;
import net.geoprism.util.Iterables;
import net.geoprism.util.Predicate;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WMSUtils;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.request.GetMapRequest;
import org.geotools.data.wms.response.GetMapResponse;
import org.geotools.ows.ServiceException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.logging.LogLevel;
import com.runwaysdk.query.F;
import com.runwaysdk.query.MAX;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableGeometry;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdBusiness;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

public class DashboardMap extends DashboardMapBase implements com.runwaysdk.generation.loader.Reloadable, net.geoprism.gis.wrapper.Map
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
  public List<? extends DashboardLayer> getLayers()
  {
    return this.getAllHasLayer().getAll();
  }

  @Override
  public String refresh(String state)
  {
    DashboardCondition[] conditions = DashboardCondition.getConditionsFromState(state);

    GeoserverBatch batch = new GeoserverBatch();

    List<? extends DashboardLayer> layers = this.getLayers();

    for (DashboardLayer layer : layers)
    {
      batch.addLayerToDrop(layer);

      this.generateSessionViewName(layer);

      layer.setConditions(Arrays.asList(conditions));
      layer.publish(batch);
    }

    GeoserverFacade.pushUpdates(batch);

    return getMapJSON("republish=false");
  }

  @Override
  public InputStream exportLayerData(String state, String layerId)
  {
    DashboardCondition[] conditions = DashboardCondition.getConditionsFromState(state);

    DashboardLayer layer = DashboardLayer.get(layerId);
    layer.setConditions(Arrays.asList(conditions));

    ValueQuery query = layer.getViewQuery();

    Predicate<Selectable> predicate = new Predicate<Selectable>()
    {
      @Override
      public boolean evaulate(Selectable selectable)
      {
        if ( ( selectable instanceof SelectableGeometry ) || selectable.getMdAttributeIF().isSystem())
        {
          return false;
        }

        return true;
      }
    };

    /*
     * Export the query to an excel file and pipe it back to the controller
     */
    PipedInputStream istream = new PipedInputStream();

    Thread thread = new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          PipedOutputStream ostream = new PipedOutputStream(istream);

          try
          {
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            workbook.setCompressTempFiles(true);

            ValueQueryExcelExporter exporter = new ValueQueryExcelExporter(query, layer.getName(), predicate, workbook);
            exporter.write(ostream);
          }
          finally
          {
            ostream.close();
          }
        }
        catch (IOException e)
        {
          log.error(e);
        }
      }
    });
    thread.setDaemon(true);
    thread.start();

    // Check for exceptions
    return istream;
  }

  @Transaction
  private void makeBiz()
  {
    try
    {
      MdBusiness biz = new MdBusiness();
      biz.setValue(MdBusinessInfo.NAME, "ClassLoaderTestiesBiz");
      biz.setValue(MdBusinessInfo.PACKAGE, "com.test.cltest");
      biz.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "mdBusiness Set Test");
      biz.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Set mdBusiness Attributes Test");
      biz.apply();
    }
    catch (DuplicateDataException e)
    {

    }
  }

  private void generateSessionViewName(DashboardLayer layer)
  {
    // Generate a new database view name for the layer. This viewName is
    // specific to a user's session.
    String viewName = layer.generateViewName();

    // Update the stored viewName for the session
    layer.setViewName(viewName);

    // Update the corresponding style name to link with the view name
    DashboardStyle style = layer.getStyles().get(0);
    style.setName(layer.getViewName());
  }

  /**
   * MdMethod
   * 
   * Invoked after the user reorders a layer via drag+drop in the dashboard viewer.
   * 
   * @return The JSON representation of the current DashboardMap.
   */
  @Override
  @Transaction
  public java.lang.String orderLayers(java.lang.String[] layerIds)
  {
    if (layerIds == null || layerIds.length == 0)
    {
      throw new RuntimeException("Unable to order layers, the layerIds array is either null or empty.");
    }

    HasLayerQuery q = new HasLayerQuery(new QueryFactory());
    q.WHERE(q.parentId().EQ(this.getId()));
    q.AND(q.childId().IN(layerIds));

    OIterator<? extends HasLayer> iter = q.getIterator();

    try
    {
      while (iter.hasNext())
      {
        HasLayer rel = iter.next();
        rel.appLock();
        rel.setLayerIndex(ArrayUtils.indexOf(layerIds, rel.getChildId()) + 1);
        rel.apply();
      }
    }
    finally
    {
      iter.close();
    }

    this.reorderLayers();

    return "";
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
    hsQ.WHERE(hsQ.parentId().EQ(this.getId()));
    hsQ.ORDER_BY_ASC(hsQ.getLayerIndex());

    OIterator<? extends HasLayer> iter = hsQ.getIterator();

    try
    {
      List<DashboardLayer> layers = new LinkedList<DashboardLayer>();
      while (iter.hasNext())
      {
        layers.add(iter.next().getChild());
      }

      return layers.toArray(new DashboardLayer[layers.size()]);
    }
    finally
    {
      iter.close();
    }
  }

  /**
   * Returns the reference layer options.
   * 
   * @return
   */
  public JSONArray getReferenceLayersJSON()
  {
    Map<String, Integer> indices = this.getDashboard().getUniversalIndices();

    JSONArray array = new JSONArray();

    List<? extends DashboardLayer> layers = this.getAllHasLayer().getAll();

    for (DashboardLayer layer : layers)
    {
      if (layer instanceof DashboardReferenceLayer)
      {
        DashboardReferenceLayer referenceLayer = (DashboardReferenceLayer) layer;

        array.put(referenceLayer.toJSON(indices));
      }
    }

    return array;
  }

  /**
   * Republishes all layers to GeoServer.
   * 
   */
  public void publishAllLayers(DashboardLayer[] orderedLayers)
  {
    GeoserverBatch batch = new GeoserverBatch();

    for (DashboardLayer layer : orderedLayers)
    {
      batch.addLayerToDrop(layer);

      this.generateSessionViewName(layer);

      layer.publish(batch);
    }

    GeoserverFacade.pushUpdates(batch);
  }

  /**
   * MdMethod
   */
  @com.runwaysdk.logging.Log(level = LogLevel.DEBUG)
  public String getMapJSON(String config)
  {
    try
    {
      DashboardLayer[] orderedLayers = this.getOrderedLayers();

      JSONObject mapJSON = new JSONObject();
      JSONArray layers = new JSONArray();

      mapJSON.put("mapName", this.getName());

      ArrayList<DashboardThematicLayer> orderedTLayers = new ArrayList<DashboardThematicLayer>();

      for (int i = 0; i < orderedLayers.length; i++)
      {
        if (orderedLayers[i] instanceof DashboardThematicLayer)
        {
          DashboardThematicLayer tLayer = (DashboardThematicLayer) orderedLayers[i];
          orderedTLayers.add(tLayer);
        }
      }

      // Convert from ListArray to Array for Thematic Layers
      DashboardThematicLayer[] orderedTLayersArr = new DashboardThematicLayer[orderedTLayers.size()];

      for (int i = 0; i < orderedTLayers.size(); i++)
      {
        orderedTLayersArr[i] = orderedTLayers.get(i);
      }

      if (config == null || !config.equals("republish=false"))
      {
        publishAllLayers(orderedLayers);
      }

      for (int i = 0; i < orderedTLayersArr.length; i++)
      {
        JSONObject object = orderedTLayersArr[i].toJSON();
        object.put("index", i);

        layers.put(object);
      }
      mapJSON.put("layers", layers);

      mapJSON.put("activeBaseMap", this.getActiveBaseMap());

      //
      // TODO: Resolve the situation where a reference layer is saved and loaded
      // compared to the results of getAvailableReferenceLayers()
      //
      JSONArray refLayerOptions = this.getReferenceLayersJSON();
      mapJSON.put("refLayers", refLayerOptions);

      JSONArray mapBBox = getMapLayersBBox(orderedTLayersArr);

      mapJSON.put("bbox", mapBBox);

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

  public JSONArray getExpandedMapLayersBBox(DashboardLayer[] layers, double expandVal)
  {
    JSONArray bboxArr = new JSONArray();
    Dashboard dashboard = this.getDashboard();

    if (layers.length > 0)
    {
      List<String> views = new LinkedList<String>();

      for (DashboardLayer layer : layers)
      {
        views.add(layer.getViewName());
      }

      try
      {
        double[] bbox = GeoserverFacade.getExpandedBBOX(views, expandVal);

        if (bbox != null)
        {
          bboxArr.put(bbox[0]);
          bboxArr.put(bbox[1]);
          bboxArr.put(bbox[2]);
          bboxArr.put(bbox[3]);
        }
        else
        {
          return null;
        }
      }
      catch (JSONException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }
    else if (dashboard != null)
    {
      List<GeoEntity> countries = dashboard.getCountries();

      MdBusinessDAOIF mdClass = (MdBusinessDAOIF) MdBusinessDAO.getMdBusinessDAO(GeoEntity.CLASS);
      MdAttributeDAOIF mdAttributeGeom = mdClass.definesAttribute(GeoEntity.GEOMULTIPOLYGON);
      MdAttributeDAOIF mdAttributeId = mdClass.definesAttribute(GeoEntity.ID);

      String tableName = mdClass.getTableName();
      String geoColumnName = mdAttributeGeom.getColumnName();
      String idColumnName = mdAttributeId.getColumnName();

      StringBuffer sql = new StringBuffer();
      sql.append("SELECT ST_AsText(ST_Expand(ST_Collect(" + tableName + "." + geoColumnName + "), " + expandVal + ")) AS bbox");
      sql.append(" FROM " + tableName);

      boolean first = true;

      for (GeoEntity country : countries)
      {
        if (first)
        {
          sql.append(" WHERE " + tableName + "." + idColumnName + "= '" + country.getId() + "'");

          first = false;
        }
        else
        {
          sql.append(" OR " + tableName + "." + idColumnName + "= '" + country.getId() + "'");
        }
      }

      ResultSet resultSet = Database.query(sql.toString());
      bboxArr = this.formatBBox(resultSet);
    }

    return bboxArr;
  }

  public JSONArray getMapLayersBBox(DashboardLayer[] layers)
  {
    Dashboard dashboard = this.getDashboard();

    if (dashboard != null)
    {
      List<GeoEntity> countries = dashboard.getCountries();

      if (countries.size() > 0)
      {

        MdBusinessDAOIF mdClass = (MdBusinessDAOIF) MdBusinessDAO.getMdBusinessDAO(GeoEntity.CLASS);
        MdAttributeDAOIF mdAttributeGeom = mdClass.definesAttribute(GeoEntity.GEOMULTIPOLYGON);
        MdAttributeDAOIF mdAttributeId = mdClass.definesAttribute(GeoEntity.ID);

        String tableName = mdClass.getTableName();
        String geoColumnName = mdAttributeGeom.getColumnName();
        String idColumnName = mdAttributeId.getColumnName();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ST_AsText(ST_Extent(" + tableName + "." + geoColumnName + ")) AS bbox");
        sql.append(" FROM " + tableName);

        boolean first = true;

        for (GeoEntity country : countries)
        {
          if (first)
          {
            sql.append(" WHERE " + tableName + "." + idColumnName + "= '" + country.getId() + "'");

            first = false;
          }
          else
          {
            sql.append(" OR " + tableName + "." + idColumnName + "= '" + country.getId() + "'");
          }
        }

        ResultSet resultSet = Database.query(sql.toString());
        return this.formatBBox(resultSet);
      }

    }

    /*
     * There are geometries in the map, use World defaults
     */
    try
    {
      JSONArray bboxArr = new JSONArray();

      bboxArr.put(-179.0);
      bboxArr.put(-89.0);
      bboxArr.put(179.0);
      bboxArr.put(89.0);

      return bboxArr;
    }
    catch (JSONException ex)
    {
      throw new ProgrammingErrorException(ex);
    }
  }

  /**
   * Format the bounding box result set returned from a PostGIS database query
   * 
   * @param resultSet
   * @return
   */
  private JSONArray formatBBox(ResultSet resultSet)
  {
    JSONArray bboxArr = new JSONArray();
    Dashboard dashboard = this.getDashboard();

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
            String error = "The countries of dashboard [" + dashboard.getDisplayLabel().getValue() + "] could not be used to create a valid bounding box";

            throw new ProgrammingErrorException(error);
          }
        }
      }
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

  @Override
  public Universal[] getUniversalAggregations(String mdAttributeId)
  {
    MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(mdAttributeId);
    MdClassDAOIF mdClass = mdAttribute.definedByClass();

    MappableClassQuery query = new MappableClassQuery(new QueryFactory());
    query.WHERE(query.getWrappedMdClass().EQ(mdClass));

    OIterator<? extends MappableClass> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        MappableClass mClass = iterator.next();

        List<? extends Universal> lowests = mClass.getAllUniversal().getAll();

        List<Term> universals = new LinkedList<Term>();

        for (Universal lowest : lowests)
        {
          Universal root = Universal.getRoot();

          universals.addAll(GeoEntityUtil.getOrderedAncestors(root, lowest, AllowedIn.CLASS));
        }

        return universals.toArray(new Universal[universals.size()]);
      }
      else
      {
        String message = "An unwrapped MdAttribute exists as part of a Dashboard.  This should never happen.";

        throw new ProgrammingErrorException(message);
      }
    }
    finally
    {
      iterator.close();
    }

  }

  public static MdAttributeView[] getSecondaryAttributes(String mapId, String mdAttributeId)
  {
    DashboardMap dashboardMap = DashboardMap.get(mapId);
    Dashboard dashboard = dashboardMap.getDashboard();

    MdAttributeDAOIF mdAttributeDAOIF = MdAttributeDAO.get(mdAttributeId);
    MdClassDAOIF mdClass = mdAttributeDAOIF.definedByClass();
    MdAttributeViewPredicate predicate = new MdAttributeViewPredicate(mdAttributeDAOIF);

    OIterator<? extends MetadataWrapper> iterator = dashboard.getAllMetadata();

    try
    {
      while (iterator.hasNext())
      {
        MetadataWrapper wrapper = iterator.next();

        if (mdClass.getId().equals(wrapper.getWrappedMdClassId()))
        {
          List<MdAttributeView> attributes = new LinkedList<MdAttributeView>(Arrays.asList(wrapper.getSortedAttributes()));

          new Iterables<MdAttributeView>().remove(attributes, predicate);

          return attributes.toArray(new MdAttributeView[attributes.size()]);
        }
      }
    }
    finally
    {
      iterator.close();
    }

    return new MdAttributeView[] {};
  }

  /**
   * Generate an image replicating the users map in the browser.
   * 
   * @outFileFormat - Allowed types (png, gif, jpg, bmp)
   * @mapBounds - JSON constructed as {"bottom":"VALUE", "top":"VALUE", "left":"VALUE", "right":"VALUE"}
   * @mapSize - JSON constructed as {"width":"VALUE", "height":"VALUE"}
   * @activeBaseMap = JSON constructed as {"LAYER_SOURCE_TYPE":"VALUE"}
   */
  @Override
  public InputStream generateMapImageExport(String outFileFormat, String mapBounds, String mapSize, String activeBaseMap)
  {
    InputStream inStream = null;
    int width;
    int height;

    // Get dimensions of the map window (<div>)
    try
    {
      JSONObject mapSizeObj = new JSONObject(mapSize);
      width = mapSizeObj.getInt("width");
      height = mapSizeObj.getInt("height");
    }
    catch (JSONException e)
    {
      String error = "Could not parse map size.";
      throw new ProgrammingErrorException(error, e);
    }

    // Setup the base canvas to which we will add layers and map elements
    BufferedImage base = null;
    Graphics mapBaseGraphic = null;
    try
    {
      if (outFileFormat.toLowerCase().equals("png") || outFileFormat.toLowerCase().equals("gif"))
      {
        base = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      }
      else if (outFileFormat.equals("jpg") || outFileFormat.toLowerCase().equals("bmp"))
      {
        base = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      }

      // Create the base canvas that all other map elements will be draped on top of
      mapBaseGraphic = base.getGraphics();
      mapBaseGraphic.setColor(Color.white);
      mapBaseGraphic.fillRect(0, 0, width, height);
      mapBaseGraphic.drawImage(base, 0, 0, null);

      // Ordering the layers from the default map
      DashboardLayer[] orderedLayers = this.getOrderedLayers();

      // Add layers to the base canvas
      BufferedImage layerCanvas = getLayersExportCanvas(width, height, orderedLayers, mapBounds);

      // Get base map
      String baseType = null;
      try
      {
        JSONObject activeBaseObj = new JSONObject(activeBaseMap);
        baseType = activeBaseObj.getString("LAYER_SOURCE_TYPE");
      }
      catch (JSONException e)
      {
        String error = "Could not active base map JSON.";
        throw new ProgrammingErrorException(error, e);
      }

      // Get bounds of the map
      if (baseType.length() > 0)
      {
        String bottom;
        String top;
        String right;
        String left;
        try
        {
          JSONObject mapBoundsObj = new JSONObject(mapBounds);
          bottom = mapBoundsObj.getString("bottom");
          top = mapBoundsObj.getString("top");
          right = mapBoundsObj.getString("right");
          left = mapBoundsObj.getString("left");
        }
        catch (JSONException e)
        {
          String error = "Could not parse map bounds.";
          throw new ProgrammingErrorException(error, e);
        }

        BufferedImage baseMapImage = this.getBaseMapCanvas(width, height, left, bottom, right, top, baseType);

        if (baseMapImage != null)
        {
          mapBaseGraphic.drawImage(baseMapImage, 0, 0, null);
        }
      }

      // Offset the layerCanvas so that it is center
      int widthOffset = (int) ( ( width - layerCanvas.getWidth() ) / 2 );
      int heightOffset = (int) ( ( height - layerCanvas.getHeight() ) / 2 );

      mapBaseGraphic.drawImage(layerCanvas, widthOffset, heightOffset, null);

      // Add legends to the base canvas
      BufferedImage legendCanvas = getLegendExportCanvas(width, height);
      mapBaseGraphic.drawImage(legendCanvas, 0, 0, null);
    }
    finally
    {
      ByteArrayOutputStream outStream = null;
      try
      {
        outStream = new ByteArrayOutputStream();
        ImageIO.write(base, outFileFormat, outStream);
        inStream = new ByteArrayInputStream(outStream.toByteArray());
      }
      catch (IOException e)
      {
        String error = "Could not write map image to the output stream.";
        throw new ProgrammingErrorException(error, e);
      }
      finally
      {
        if (outStream != null)
        {
          try
          {
            outStream.close();
          }
          catch (IOException e)
          {
            String error = "Could not close stream.";
            throw new ProgrammingErrorException(error, e);
          }
        }
      }

      if (mapBaseGraphic != null)
      {
        mapBaseGraphic.dispose();
      }
    }

    return inStream;
  }

  public BufferedImage getBaseMapCanvas(int mapWidth, int mapHeight, String left, String bottom, String right, String top)
  {
    String baseType = null;
    try
    {
      JSONObject activeBaseObj = new JSONObject(this.getActiveBaseMap());
      baseType = activeBaseObj.getString("LAYER_SOURCE_TYPE");
    }
    catch (JSONException e)
    {
      String error = "Could not active base map JSON.";
      throw new ProgrammingErrorException(error, e);
    }

    BufferedImage baseMapImage = this.getBaseMapCanvas(mapWidth, mapHeight, left, bottom, right, top, baseType);

    return baseMapImage;
  }

  public BufferedImage getBaseMapCanvas(int mapWidth, int mapHeight, String left, String bottom, String right, String top, String baseType)
  {
    BufferedImage image = null;

    if (baseType.toLowerCase().equals("osm"))
    {
      // TODO: Only add base map based on user settings (i.e. if OSM is enabled)
      URL url = null;
      String wmsLayerName = "osm";
      try
      {
        //
        // Currently we are using the WMS service from http://irs.gis-lab.info/ because most web services are offered as
        // Tiled Map Services (TMS) which are not directly consumable by geotools.
        //
        url = new URL("http://irs.gis-lab.info/?layers=" + wmsLayerName + "&VERSION=1.1.1&Request=GetCapabilities&Service=WMS");

        //
        // BACKUP SERVICE
        // Currently we are using the WMS service from http://ows.terrestris.de/dienste.html#wms
        // because this one http://irs.gis-lab.info/ is un-reliable and most web services are offered as
        // Tiled Map Services (TMS) which are not directly consumable by geotools.
        //
        // wmsLayerName = "osm-wms";
        // url = new
        // URL("http://ows.terrestris.de/osm/service?layers="+wmsLayerName+"&styles=&VERSION=1.1.1&Request=GetCapabilities&Service=WMS");

      }
      catch (MalformedURLException e)
      {
        // will not happen
        String error = "The URL is not formed correctly.";
        throw new ProgrammingErrorException(error, e);
      }

      WebMapServer wms = null;
      try
      {
        wms = new WebMapServer(url);
      }
      catch (IOException e)
      {
        // There was an error communicating with the server
        // For example, the server is down
        String error = "There was a problem communicating with the base map server.";
        throw new ProgrammingErrorException(error, e);
      }
      catch (ServiceException e)
      {
        // The server returned a ServiceException (unusual in this case)
        String error = "The server returned a ServiceException.";
        throw new ProgrammingErrorException(error, e);
      }

      GetMapRequest request = wms.createGetMapRequest();
      request.setFormat("image/png");
      request.setDimensions(mapWidth, mapHeight); // sets the dimensions of the image to be returned from the server
      request.setTransparent(true);
      request.setSRS("EPSG:4326");
      request.setBBox(left + "," + bottom + "," + right + "," + top);

      WMSCapabilities capabilities = wms.getCapabilities();

      for (Layer layer : WMSUtils.getNamedLayers(capabilities))
      {
        if (layer.getName().toLowerCase().trim().equals(wmsLayerName))
        {
          request.addLayer(layer);
        }
      }

      GetMapResponse response = null;
      try
      {
        System.out.println(request.getFinalURL());
        response = (GetMapResponse) wms.issueRequest(request);
      }
      catch (ServiceException e)
      {
        String error = "The server returned a ServiceException.";
        throw new ProgrammingErrorException(error, e);
      }
      catch (IOException e)
      {
        String error = "There was a problem communicating with the base map server.";
        throw new ProgrammingErrorException(error, e);
      }

      try
      {
        image = ImageIO.read(response.getInputStream());
      }
      catch (IOException e)
      {
        String error = "Could not read the response to an image.";
        throw new ProgrammingErrorException(error, e);
      }
    }

    return image;
  }

  /**
   * Builds a combined image layer of all the layers in a saved map.
   * 
   * @mapWidth
   * @mapHeight
   * @orderedLayers
   * @mapBounds - expects json object {"bottom":"VALUE", "top":"VALUE", "left":"VALUE", "right":"VALUE"}
   */
  public BufferedImage getLayersExportCanvas(int mapWidth, int mapHeight, DashboardLayer[] orderedLayers, String mapBounds)
  {
    String bottom;
    String top;
    String right;
    String left;
    String processingFormat = "png"; // needed to allow transparency on each overlay before combining to a single
                                     // map/format
    Graphics mapBaseGraphic = null;
    BufferedImage base = null;

    try
    {
      base = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_ARGB);
      mapBaseGraphic = base.getGraphics();
      mapBaseGraphic.drawImage(base, 0, 0, null);

      // Get bounds of the map
      try
      {
        JSONObject mapBoundsObj = new JSONObject(mapBounds);
        bottom = mapBoundsObj.getString("bottom");
        top = mapBoundsObj.getString("top");
        right = mapBoundsObj.getString("right");
        left = mapBoundsObj.getString("left");
      }
      catch (JSONException e)
      {
        String error = "Could not parse map bounds.";
        throw new ProgrammingErrorException(error, e);
      }

      // Generates map overlays and combines them into a single map image
      for (DashboardLayer layer : orderedLayers)
      {

        Graphics2D newOverlayBaseGraphic = null;
        Graphics2D mapLayerGraphic2d = null;

        String layersString = GeoserverProperties.getWorkspace() + ":" + layer.getViewName();

        StringBuffer requestURL = new StringBuffer();
        requestURL.append(GeoserverProperties.getLocalPath() + "/wms?");
        requestURL.append("LAYERS=" + layersString);
        requestURL.append("&");
        requestURL.append("STYLES="); // there are no geoserver styles being added. sld's are used instead
        requestURL.append("&");
        requestURL.append("SRS=EPSG%3A4326");
        requestURL.append("&");
        requestURL.append("TRANSPARENT=true");
        requestURL.append("&");
        requestURL.append("ISBASELAYER=false"); // in the browser the baselayer prop is set for the 1st layer in the
                                                // map.
        requestURL.append("&");
        requestURL.append("SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&EXCEPTIONS=application%2Fvnd.ogc.se_inimage");
        requestURL.append("&");
        requestURL.append("FORMAT=image%2F" + processingFormat);
        requestURL.append("&");
        requestURL.append("BBOX=" + left + "," + bottom + "," + right + "," + top);
        requestURL.append("&");
        requestURL.append("WIDTH=" + Integer.toString(mapWidth));
        requestURL.append("&");
        requestURL.append("HEIGHT=" + Integer.toString(mapHeight));

        try
        {
          BufferedImage layerImg = this.getImageFromGeoserver(requestURL.toString());
          BufferedImage newOverlayBase = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_ARGB);

          newOverlayBaseGraphic = newOverlayBase.createGraphics();

          // Add transparency to the layerGraphic
          // This is set in JavaScript in the app so we are replicating browser side transparency settings that are
          // applied to the whole layer
          AlphaComposite thisLayerComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.getLayerOpacity(layer));
          mapLayerGraphic2d = layerImg.createGraphics();
          newOverlayBaseGraphic.setComposite(thisLayerComposite);

          // Add the current layerGraphic to the base image
          newOverlayBaseGraphic.drawImage(layerImg, 0, 0, null);
          mapBaseGraphic.drawImage(newOverlayBase, 0, 0, null);

        }
        finally
        {
          if (newOverlayBaseGraphic != null)
          {
            newOverlayBaseGraphic.dispose();
          }

          if (mapLayerGraphic2d != null)
          {
            mapLayerGraphic2d.dispose();
          }
        }
      }
    }
    finally
    {
      mapBaseGraphic.dispose();
    }

    return base;
  }

  private float getLayerOpacity(DashboardLayer layer)
  {
    List<? extends DashboardStyle> styles = layer.getStyles();

    if (styles.size() > 0)
    {
      DashboardStyle style = styles.get(0);
      String featureType = layer.getFeatureType().toString();

      if (featureType == "POINT")
      {
        return style.getPointOpacity().floatValue();
      }
      else if (featureType == "POLYGON")
      {
        return style.getPolygonFillOpacity().floatValue();
      }
    }

    return (float) 1.0; // return no transparency
  }

  /**
   * Builds an image layer of all the layers in a SavedMap.
   * 
   * @mapWidth
   * @mapHeight
   */
  private BufferedImage getLegendExportCanvas(int mapWidth, int mapHeight)
  {
    int padding = 2;
    BufferedImage base = null;
    Graphics mapBaseGraphic = null;
    Color innerBackgroundColor = Color.darkGray;
    Color outerBorderColor = Color.black;
    int legendTopPlacement = 0;
    int legendLeftPlacement = 0;
    int widestLegend = 0;
    int legendXPosition = 0;
    int legendYPosition = 0;

    List<? extends DashboardLayer> layers = this.getAllHasLayer().getAll();

    try
    {
      base = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_ARGB);
      mapBaseGraphic = base.getGraphics();
      mapBaseGraphic.drawImage(base, 0, 0, null);

      // Generates map overlays and combines them into a single map image
      for (DashboardLayer layer : layers)
      {
        if (layer.getDisplayInLegend())
        {
          Graphics2D titleBaseGraphic = null;
          Graphics2D iconGraphic = null;

          String requestURL = getLegendURL(layer);

          try
          {
            // handle color graphics and categories
            BufferedImage titleBase = getLegendTitleImage(layer);
            titleBaseGraphic = titleBase.createGraphics();
            int paddedTitleWidth = titleBase.getWidth();
            int paddedTitleHeight = titleBase.getHeight();

            BufferedImage icon = getImageFromGeoserver(requestURL);
            int iconHeight = icon.getHeight();
            int iconWidth = icon.getWidth();
            int paddedIconWidth = iconWidth + ( padding * 2 );
            int paddedIconHeight = iconHeight + ( padding * 2 );

            int fullWidth = paddedIconWidth + paddedTitleWidth;
            int fullHeight;
            if (paddedIconHeight >= paddedTitleHeight)
            {
              fullHeight = paddedIconHeight;
            }
            else
            {
              fullHeight = paddedTitleHeight;
            }

            DashboardLegend legend = layer.getDashboardLegend();
            if (legend.getGroupedInLegend())
            {
              if (legendTopPlacement + fullHeight >= mapHeight)
              {
                legendLeftPlacement = widestLegend + legendLeftPlacement + padding;
                legendTopPlacement = 0; // reset so 2nd column legends start at the top row
              }
              legendXPosition = legendLeftPlacement + padding;
              legendYPosition = legendTopPlacement + padding;
            }
            else
            {
              legendXPosition = (int) Math.round((double) legend.getLegendXPosition());
              legendYPosition = (int) Math.round((double) legend.getLegendYPosition());
            }

            BufferedImage legendBase = new BufferedImage(fullWidth + ( padding * 2 ), fullHeight + ( padding * 2 ), BufferedImage.TYPE_INT_ARGB);
            Graphics2D legendBaseGraphic = legendBase.createGraphics();
            legendBaseGraphic.setColor(innerBackgroundColor);
            legendBaseGraphic.fillRect(0, 0, fullWidth, fullHeight);
            legendBaseGraphic.setColor(outerBorderColor);
            legendBaseGraphic.setStroke(new BasicStroke(5));
            legendBaseGraphic.drawRect(0, 0, fullWidth, fullHeight);

            legendBaseGraphic.drawImage(icon, padding, padding, paddedIconWidth, paddedIconHeight, null);
            legendBaseGraphic.drawImage(titleBase, paddedIconWidth + ( padding * 2 ), ( fullHeight / 2 ) - ( paddedTitleHeight / 2 ), paddedTitleWidth, paddedTitleHeight, null);
            mapBaseGraphic.drawImage(legendBase, legendXPosition, legendYPosition, fullWidth, fullHeight, null);

            if (legend.getGroupedInLegend())
            {
              legendTopPlacement = legendTopPlacement + fullHeight + padding;
            }

            if (fullWidth > widestLegend)
            {
              widestLegend = fullWidth;
            }
          }
          finally
          {
            if (titleBaseGraphic != null)
            {
              titleBaseGraphic.dispose();
            }

            if (iconGraphic != null)
            {
              iconGraphic.dispose();
            }
          }
        }
      }
    }
    finally
    {
      mapBaseGraphic.dispose();
    }

    return base;
  }

  private String getLegendURL(DashboardLayer layer)
  {
    String layerString = GeoserverProperties.getWorkspace() + ":" + layer.getViewName();

    StringBuffer requestURL = new StringBuffer();
    requestURL.append(GeoserverProperties.getLocalPath() + "/wms?");
    requestURL.append("REQUEST=GetLegendGraphic");
    requestURL.append("&");
    requestURL.append("VERSION=1.0.0");
    requestURL.append("&");
    requestURL.append("FORMAT=image/png&amp;WIDTH=25&amp;HEIGHT=25");
    requestURL.append("&");
    requestURL.append("&TRANSPARENT=true&LEGEND_OPTIONS=fontName:Arial;fontAntiAliasing:true;fontColor:0xececec;fontSize:11;fontStyle:bold;");

    DashboardStyle style = layer.getStyles().get(0);
    boolean contSize = true;
    if (style instanceof DashboardThematicStyle)
    {
      DashboardThematicStyle tStyle = (DashboardThematicStyle) style;
      contSize = tStyle.getBubbleContinuousSize();
    }

    // forcing labels for gradient for instances where only one feature is mapped which geoserver hides labels by
    // default
    if (layer.getFeatureStrategy().toString() == "GRADIENT" || layer.getFeatureStrategy().toString() == "CATEGORY")
    {
      requestURL.append("forceLabels:on;");
    }
    else if (layer.getFeatureStrategy().toString() == "BUBBLE" && layer.getLayerType().toString() == "BASIC")
    {
      // The label should be hidden when mapping bubbles against a text or term attribute.
      requestURL.append("forceLabels:off;");
    }
    else if (layer.getFeatureStrategy().toString() == "BUBBLE" && contSize && layer.getLayerType().toString() != "BASIC")
    {
      // The label should be displayed when mapping continuous size bubbles against anything other than a text or term
      // attribute.
      requestURL.append("forceLabels:on;");
    }

    requestURL.append("&");
    requestURL.append("LAYER=" + layerString);

    return requestURL.toString();
  }

  private BufferedImage getLegendTitleImage(DashboardLayer layer)
  {

    FontMetrics fm;
    int textWidth;
    int textHeight;
    int textBoxHorizontalPadding = 4;
    int textBoxVerticalPadding = 4;
    int borderWidth = 2;
    int paddedTitleHeight;
    int paddedTitleWidth;
    int titleLeftPadding = textBoxHorizontalPadding;
    BufferedImage newLegendTitleBase;
    Graphics2D newLegendTitleBaseGraphic = null;

    try
    {
      // Build the Font object
      Font titleFont = new Font(layer.getName(), Font.BOLD, 14);

      // Build variables for base legend graphic construction
      try
      {
        newLegendTitleBase = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        newLegendTitleBaseGraphic = newLegendTitleBase.createGraphics();

        newLegendTitleBaseGraphic.setFont(titleFont);

        fm = newLegendTitleBaseGraphic.getFontMetrics();
        textHeight = fm.getHeight();
        textWidth = fm.stringWidth(layer.getName());

        paddedTitleWidth = textWidth + ( textBoxHorizontalPadding * 2 ) + ( borderWidth * 2 );

        paddedTitleHeight = textHeight + ( textBoxVerticalPadding * 2 ) + ( borderWidth * 2 );
      }
      finally
      {
        // dispose of temporary graphics context
        if (newLegendTitleBaseGraphic != null)
        {
          newLegendTitleBaseGraphic.dispose();
        }
      }

      titleLeftPadding = ( ( paddedTitleWidth / 2 ) - ( ( textWidth + ( textBoxHorizontalPadding * 2 ) + ( borderWidth * 2 ) ) / 2 ) ) + textBoxHorizontalPadding;

      newLegendTitleBase = new BufferedImage(paddedTitleWidth, paddedTitleHeight, BufferedImage.TYPE_INT_ARGB);
      newLegendTitleBaseGraphic = newLegendTitleBase.createGraphics();
      newLegendTitleBaseGraphic.drawImage(newLegendTitleBase, 0, 0, null);

      newLegendTitleBaseGraphic.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
      newLegendTitleBaseGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      newLegendTitleBaseGraphic.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
      newLegendTitleBaseGraphic.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
      newLegendTitleBaseGraphic.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
      newLegendTitleBaseGraphic.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      newLegendTitleBaseGraphic.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      newLegendTitleBaseGraphic.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
      newLegendTitleBaseGraphic.setFont(titleFont);

      // draw title text
      fm = newLegendTitleBaseGraphic.getFontMetrics();
      newLegendTitleBaseGraphic.setColor(Color.WHITE);
      newLegendTitleBaseGraphic.drawString(layer.getName(), titleLeftPadding, fm.getAscent() + textBoxVerticalPadding);

      newLegendTitleBaseGraphic.drawImage(newLegendTitleBase, 0, 0, null);
    }
    finally
    {
      if (newLegendTitleBaseGraphic != null)
      {
        newLegendTitleBaseGraphic.dispose();
      }
    }

    return newLegendTitleBase;
  }

  private BufferedImage getImageFromGeoserver(String _requestURL)
  {
    for (int i = 0; i < 10; i++)
    {
      // Make the getMap or getLegendGraphic request to geoserver for this layer
      // and return a byte[] of the returned image
      byte[] response = this.requestGeoserverImage(_requestURL);

      ByteArrayInputStream istream = new ByteArrayInputStream(response);

      try
      {
        // Convert the response into an image.
        // Note that the reponse may not be a valid image
        // If this is the case then ImageIO.read will return
        // a null BufferedImage.
        BufferedImage image = ImageIO.read(istream);

        if (image != null)
        {
          return image;
        }
        else
        {
          // Wait a couple seconds and try again
          try
          {
            Thread.sleep(2000);
          }
          catch (InterruptedException e)
          {
            // Do nothing
          }
        }
      }
      catch (IOException e)
      {
        String error = "Could not read the map request image from the map server.";
        throw new ProgrammingErrorException(error, e);
      }
      finally
      {
        try
        {
          istream.close();
        }
        catch (IOException e)
        {
          String error = "Could not close stream.";
          throw new ProgrammingErrorException(error, e);
        }
      }
    }

    throw new ProgrammingErrorException("Error creating layers image");
  }

  /**
   * Makes a getMap request to geoserver and returns the response as a ByteArrayOutputStream
   * 
   * @throws NoSuchAlgorithmException
   * 
   * @requestURL = geoserver getMap() or getLegendGraphic() request url
   */
  private byte[] requestGeoserverImage(String requestURL)
  {
    InputStream inStream = null;
    ByteArrayOutputStream outStream = null;
    CloseableHttpResponse response = null;
    CloseableHttpClient client = null;
    try
    {
      KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

      // StorePath and StorePass must be set for the systems keystore
      trustStore.load(new FileInputStream(GeoserverProperties.getGeoserverKeystorePath()), GeoserverProperties.getGeoserverKeystorePass().toCharArray());

      SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(trustStore, GeoserverProperties.getGeoserverKeystorePass().toCharArray()).loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();

      //
      // TODO: socket factory load once ever
      //
      SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

      client = HttpClients.custom()
      // Allow all hostnames regardless of what is specified in the certificate
          .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
          // Use the provided SSL socket factory
          .setSSLSocketFactory(factory)
          // Set a default cookie store which will be used in all of the requests
          .setDefaultCookieStore(new BasicCookieStore()).build();
      HttpGet method = new HttpGet(requestURL);
      response = client.execute(method);
      inStream = response.getEntity().getContent();
      outStream = new ByteArrayOutputStream();
      // Copy the input stream to the output stream
      IOUtils.copy(inStream, outStream);
    }
    catch (MalformedURLException e)
    {
      String error = "The URL is not formated correctly.";
      throw new ProgrammingErrorException(error, e);
    }
    catch (IOException e)
    {
      String error = "Could not make the request to the map server.";
      throw new ProgrammingErrorException(error, e);
    }
    catch (KeyStoreException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (KeyManagementException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (NoSuchAlgorithmException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (CertificateException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (UnrecoverableKeyException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    finally
    {
      if (inStream != null)
      {
        try
        {
          inStream.close();
        }
        catch (IOException e)
        {
          String error = "Could not close stream.";
          throw new ProgrammingErrorException(error, e);
        }
      }

      if (outStream != null)
      {
        try
        {
          outStream.close();
        }
        catch (IOException e)
        {
          String error = "Could not close stream.";
          throw new ProgrammingErrorException(error, e);
        }
      }
      if (response != null)
      {
        try
        {
          response.close();
        }
        catch (IOException e)
        {
          String error = "Could not close stream.";
          throw new ProgrammingErrorException(error, e);
        }
      }
      if (client != null)
      {
        try
        {
          client.close();
        }
        catch (IOException e)
        {
          String error = "Could not close stream.";
          throw new ProgrammingErrorException(error, e);
        }
      }
    }

    return outStream.toByteArray();
  }

  public Map<String, Integer> calculateLayerIndices()
  {
    Map<String, Integer> uIndexes = this.getDashboard().getUniversalIndices();

    DashboardLayer[] layers = this.getOrderedLayers();

    int index = Collections.max(uIndexes.values()) + 1;

    Map<String, Integer> indices = new HashMap<String, Integer>();

    for (DashboardLayer layer : layers)
    {
      if (layer instanceof DashboardReferenceLayer)
      {
        DashboardReferenceLayer referenceLayer = (DashboardReferenceLayer) layer;

        Integer universalIndex = uIndexes.get(referenceLayer.getUniversal().getId());

        indices.put(layer.getId(), universalIndex);
      }
      else
      {
        indices.put(layer.getId(), index++);
      }
    }

    return indices;
  }

  public int getMaxIndex()
  {
    ValueQuery vQuery = new ValueQuery(new QueryFactory());
    HasLayerQuery query = new HasLayerQuery(vQuery);

    MAX selectable = F.MAX(query.getLayerIndex());
    selectable.setColumnAlias(HasLayer.LAYERINDEX);
    selectable.setUserDefinedAlias(HasLayer.LAYERINDEX);

    vQuery.SELECT(selectable);
    vQuery.WHERE(query.getParent().EQ(this));

    OIterator<ValueObject> it = vQuery.getIterator();

    try
    {
      ValueObject object = it.next();

      String value = object.getValue(HasLayer.LAYERINDEX);
      if (value != null && value.trim().length() > 0)
      {
        return new Integer(value);
      }

      return 0;
    }
    finally
    {
      it.close();
    }
  }

  public void reorderLayers()
  {
    /*
     * Update the indexes of all of the existing layers. We must reorder all of the layer indexes such that the
     * reference layers are on the bottom depending on their order referenced universal in the universal tree. The
     * thematic layers will be on top based up their relative indexing between other thematic layers. If this layer is a
     * new thematic layer then it will be on top.
     */
    Map<String, Integer> indices = this.calculateLayerIndices();
    List<? extends HasLayer> relationships = this.getAllHasLayerRel().getAll();

    for (HasLayer relationship : relationships)
    {
      Integer index = indices.get(relationship.getChildId());

      relationship.appLock();
      relationship.setLayerIndex(index);
      relationship.apply();
    }
  }

}
