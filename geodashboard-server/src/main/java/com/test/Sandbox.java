package com.test;

<<<<<<< Updated upstream
import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.geodashboard.gis.model.Layer;
import com.runwaysdk.geodashboard.gis.model.Map;
import com.runwaysdk.geodashboard.gis.model.Style;
import com.runwaysdk.geodashboard.gis.model.condition.And;
import com.runwaysdk.geodashboard.gis.model.condition.Equal;
import com.runwaysdk.geodashboard.gis.model.condition.Or;
import com.runwaysdk.geodashboard.gis.model.impl.AndImpl;
import com.runwaysdk.geodashboard.gis.model.impl.EqualImpl;
import com.runwaysdk.geodashboard.gis.model.impl.LayerImpl;
import com.runwaysdk.geodashboard.gis.model.impl.MapImpl;
import com.runwaysdk.geodashboard.gis.model.impl.OrImpl;
import com.runwaysdk.geodashboard.gis.model.impl.StyleImpl;
import com.runwaysdk.geodashboard.gis.model.impl.ThematicStyleImpl;
import com.runwaysdk.geodashboard.gis.sld.SLDMapVisitor;
import com.runwaysdk.geodashboard.gis.sld.WellKnownName;

public class Sandbox
{
  public static void main(String[] args)
  {
    
    Map map = new MapImpl("My Map");
    
    Layer layer1 = new LayerImpl("Layer 1");
    layer1.setVirtual(true);
    layer1.setFeatureType(FeatureType.POINT);
    map.addLayer(layer1);
    
    Style style1 = new StyleImpl();
    style1.setName("Style 1.1");
    
    // point
    style1.setPointSize(3);
    style1.setPointStroke("#000000");
    style1.setPointFill("#fffeee");
    style1.setPointStrokeWidth(1);
    style1.setPointOpacity(0.4);
    style1.setPointRotation(6);
    style1.setPointStrokeWidth(8);
    style1.setPointWellKnownName(WellKnownName.STANDARD.CIRCLE.getSymbol());
    // polygon
    style1.setPolygonFill("#eeeeee");
    style1.setPolygonStroke("#000000");
    style1.setPolygonStrokeWidth(4);
    
    layer1.addStyle(style1);    
    

    ThematicStyleImpl style2 = new ThematicStyleImpl();
    style2.setAttribute("testAttribute");
    // point
    style2.setName("Style 1.2");
    style2.setPointFill("#999eee");
    style2.setPointSize(1);
    style2.setPointStroke("#008800");
    style2.setPointStrokeWidth(2);
    style2.setPointOpacity(1.0);
    style2.setPointRotation(3);
    style2.setPointStrokeWidth(2);
    // polygon
    style2.setPolygonFill("#efefef");
    style2.setPolygonStroke("#ff0000");
    style2.setPolygonStrokeWidth(3);
    style2.setPointWellKnownName(WellKnownName.STANDARD.SQUARE.getSymbol());

    layer1.addStyle(style2);    
    
    
    Equal a = new EqualImpl();
    a.setValue("1");

    Equal b = new EqualImpl();
    b.setValue("2");

    Or or1 = new OrImpl();
    or1.setThematicStyle(style2);
    or1.setLeftCondition(a);
    or1.setRightCondition(b);

    Equal c = new EqualImpl();
    c.setValue("8");
    
    Equal d = new EqualImpl();
    d.setValue("9");
    
    Or or2 = new OrImpl();
    or2.setThematicStyle(style2);
    or2.setLeftCondition(c);
    or2.setRightCondition(d);
    
    And and = new AndImpl();
    and.setThematicStyle(style2);
    and.setLeftCondition(or1);
    and.setRightCondition(or2);
    
    style2.setCondition(and);
    
    SLDMapVisitor visitor = new SLDMapVisitor();
    map.accepts(visitor);
    
    System.out.println(visitor.getSLD());
//    
//    Map map2 = new MapBuilder("My Map")
//    
//      .layer("Layer 1").composite(true).featureType(FeatureType.POINT)
//        .style("Default Style").pointSize(3).pointStrokeWidth(5).pointRotation(5)
//        .add()
//      
//      .layer("Layer 2").featureType(FeatureType.POLYGON)
//        .tStyle("Thematic Style").attribute("foo")
//        .add()
//      
//    .build();
//
//    SLDMapVisitor visitor2 = new SLDMapVisitor();
//    map2.accepts(visitor2);
//    System.out.println(visitor2.getSLD());
  }
=======
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.org.eclipse.jdt.core.dom.Initializer;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.constants.GeoserverProperties;
import com.runwaysdk.gis.mapping.gwc.SeedRequest;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.ConfigurationException;
import com.runwaysdk.util.FileIO;

import java.util.ResourceBundle;

public class Sandbox
{

  private ResourceBundle                bundle;

  private static GeoServerRESTPublisher publisher;

  private static GeoServerRESTReader    reader;
  
  public static final int               SRS_CODE    = 4326;

  public static final String            SRS         = "EPSG:" + SRS_CODE;

  public static final String            GEOM_COLUMN = "geom";

  public static int                     MINX_INDEX  = 0;

  public static int                     MINY_INDEX  = 1;

  public static int                     MAXX_INDEX  = 2;

  public static int                     MAXY_INDEX  = 3;
  
  private static Log         log              = LogFactory.getLog(Sandbox.class);

  static class GeoserverProps
  {
    private static String localPath;

    private static String adminUser;

    private static String adminPassword;

    public GeoserverProps()
    {
      this.localPath = "http://127.0.0.1:8443/geoserver";
      this.adminUser = "admin";
      this.adminPassword = "geoserver";
    }

    public static String getLocalPath()
    {
      return localPath;
    }

    public void setLocalPath(String localPath)
    {
      this.localPath = localPath;
    }

    public static String getAdminUser()
    {
      return adminUser;
    }

    public void setAdminUser(String adminUser)
    {
      this.adminUser = adminUser;
    }

    public static String getAdminPassword()
    {
      return adminPassword;
    }

    public void setAdminPassword(String adminPassword)
    {
      this.adminPassword = adminPassword;
    }

  }

  /**
   * Returns the Geoserver REST publisher.
   * 
   * @return
   */
  public static synchronized GeoServerRESTPublisher getPublisher()
  {
    if (publisher == null)
    {
      publisher = new GeoServerRESTPublisher(GeoserverProps.getLocalPath(),
          GeoserverProps.getAdminUser(), GeoserverProps.getAdminPassword());
    }

    return publisher;
  }

  /**
   * Returns the Geoserver REST reader.
   */
  public static synchronized GeoServerRESTReader getReader()
  {
    if (reader == null)
    {
      try
      {
        reader = new GeoServerRESTReader(GeoserverProps.getLocalPath(), GeoserverProps.getAdminUser(),
            GeoserverProps.getAdminPassword());
      }
      catch (MalformedURLException e)
      {
        // We don't know if this is being called via client or server code, so
        // log
        // the error and throw an NPE to the calling code for its error handling
        // mechanism.
        String msg = "The " + GeoserverProperties.class.getSimpleName() + "."
            + GeoServerRESTReader.class.getSimpleName() + " is null.";
        LogFactory.getLog(GeoserverProperties.class.getClass()).error(msg, e);

        throw new NullPointerException(msg);
      }
    }

    return reader;
  }

  /**
   * Checks if the given style exists in geoserver.
   * 
   * @param styleName
   * @return
   */
  public static boolean styleExists(String styleName)
  {
    return getReader().getSLD(styleName) != null;
  }

  /**
   * Checks if the given layer exists in Geoserver.
   * 
   * @param layer
   * @return
   */
  @SuppressWarnings("deprecation")
  public static boolean layerExists(String layer)
  {
    return getReader().getLayer(layer) != null;
  }

  /**
   * Gets all layers declared in Geoserver for the workspace.
   * 
   * @return
   */
  public static List<String> getLayers()
  {
    return getReader().getLayers().getNames();
  }

  public static void refresh()
  {
    if (getPublisher().reload())
    {
      // log.info("Reloaded geoserver.");
      System.out.println("Reloaded geoserver.");
    }
    else
    {
      // log.warn("Failed to reload geoserver.");
      System.out.println("Failed to reload geoserver.");
    }
  }

   /**
   * Checks if the cache directory exists. This method does not check what
   tiles
   * or zoom levels have been cached.
   *
   * @param cacheName
   * @return
   */
   public static boolean cacheExists(String cacheName)
   {
   String cacheDir = GeoserverProperties.getGeoserverGWCDir() + cacheName;
   File cache = new File(cacheDir);
   return cache.exists();
   }

  /**
   * Gets all styles declared in Geoserver for the workspace.
   */
  public static List<String> getStyles()
  {
    return getReader().getStyles().getNames();
  }

  /**
   * Removes the style defined in Geoserver, including the .sld and .xml file
   * artifacts.
   * 
   * @param styleName
   *          The name of the style to delete.
   */
  public static void removeStyle(String styleName)
  {
    if (styleExists(styleName))
    {
      if (getPublisher().removeStyle(styleName, true))
      {
        // log.info("Removed the SLD [" + styleName + "].");
        System.out.println("Removed the SLD [" + styleName + "].");
      }
      else
      {
        // log.warn("Failed to remove the SLD [" + styleName + "].");
        System.out.println("Failed to remove the SLD [" + styleName + "].");
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
          // log.info("Deleted the file [" + sld + "].");
          System.out.println("Deleted the file [" + sld + "].");
        }
        else
        {
          // log.warn("Failed to delete the file [" + sld + "].");
          System.out.println("Failed to delete the file [" + sld + "].");
        }
      }
      else
      {
        // log.info("The file [" + sld + "] does not exist.");
        System.out.println("The file [" + sld + "] does not exist.");
      }

      // remove the xml
      File xml = new File(stylePath + styleName + ".xml");
      if (xml.exists())
      {
        boolean deleted = sld.delete();
        if (deleted)
        {
          // log.info("Deleted the file [" + xml + "].");
          System.out.println("Deleted the file [" + xml + "].");
        }
        else
        {
          // log.warn("Failed to delete the file [" + xml + "].");
          System.out.println("Failed to delete the file [" + xml + "].");
        }
      }
      else
      {
        // log.info("The file [" + xml + "] does not exist.");
        System.out.println("The file [" + xml + "] does not exist.");
      }

    }
  }
  
  /**
   * Calculates the bounding box of a specific layer.
   * 
   * @param views
   * @return double[] {minx, miny, maxx, maxy}
   */
  public static double[] getBBOX(String viewName)
  {
    List<String> view = new LinkedList<String>();
    view.add(viewName);

    return getBBOX(view);
  }
  
  /**
   * Calculates the bounding box of all the layers.
   * 
   * @param views
   * @return double[] {minx, miny, maxx, maxy}
   */
  public static double[] getBBOX(List<String> views)
  {
    // collect all the views and extend the bounding box
    ValueQuery union = new ValueQuery(new QueryFactory());
    if (views.size() == 1)
    {
      String view = views.get(0);
      union.SELECT(union.aSQLClob(GEOM_COLUMN, GEOM_COLUMN, GEOM_COLUMN));
      union.FROM(view, view);
    }
    else if (views.size() > 1)
    {
      ValueQuery[] unionVQs = new ValueQuery[views.size()];
      for (int i = 0; i < unionVQs.length; i++)
      {
        String view = views.get(i);
        ValueQuery vq = new ValueQuery(union.getQueryFactory());
        vq.SELECT(vq.aSQLClob(GEOM_COLUMN, GEOM_COLUMN, GEOM_COLUMN));
        vq.FROM(view, view);

        unionVQs[i] = vq;
      }

      union.UNION_ALL(unionVQs);
    }
    else
    {
      // TODO throw better exception or a message
      throw new ProgrammingErrorException("The map has no layers");
    }

    ValueQuery collected = new ValueQuery(union.getQueryFactory());
    collected.SELECT(collected.aSQLAggregateClob("collected", "st_collect(" + GEOM_COLUMN + ")",
        "collected"));
    collected.FROM("(" + union.getSQL() + ")", "unioned");

    ValueQuery outer = new ValueQuery(union.getQueryFactory());
    outer.SELECT(union.aSQLAggregateDouble("minx", "st_xmin(collected)"),
        union.aSQLAggregateDouble("miny", "st_ymin(collected)"),
        union.aSQLAggregateDouble("maxx", "st_xmax(collected)"),
        union.aSQLAggregateDouble("maxy", "st_ymax(collected)"));

    outer.FROM("(" + collected.getSQL() + ")", "collected");

    OIterator<? extends ValueObject> iter = outer.getIterator();
    ValueObject o;
    try
    {
      o = iter.next();
    }
    finally
    {
      iter.close();
    }

    double[] bbox = new double[4];
    bbox[MINX_INDEX] = Double.parseDouble(o.getValue("minx"));
    bbox[MINY_INDEX] = Double.parseDouble(o.getValue("miny"));
    bbox[MAXX_INDEX] = Double.parseDouble(o.getValue("maxx"));
    bbox[MAXY_INDEX] = Double.parseDouble(o.getValue("maxy"));

    return bbox;
  }

  /**
   * Adds a database view and publishes the layer if necessary.
   * 
   * @param layer
   */
  public static void publishLayer(String layer, String styleName)
  {

    // create the layer if it does not exist
    if (layerExists(layer))
    {
//      log.info("The layer [" + layer + "] already exists in geoserver.");
      System.out.println("The layer [" + layer + "] already exists in geoserver.");
      return;
    }
    else
    {
      double[] bbox = getBBOX(layer);

      double minX = bbox[MINX_INDEX];
      double minY = bbox[MINY_INDEX];
      double maxX = bbox[MAXX_INDEX];
      double maxY = bbox[MAXY_INDEX];

      GSFeatureTypeEncoder fte = new GSFeatureTypeEncoder();
      fte.setEnabled(true);
      fte.setName(layer);
      fte.setSRS(SRS);
      fte.setTitle(layer);
      fte.addKeyword(layer);
      fte.setNativeBoundingBox(minX, minY, maxX, maxY, SRS);
      fte.setLatLonBoundingBox(minX, minY, maxX, maxY, SRS);

      GSLayerEncoder le = new GSLayerEncoder();
      le.setDefaultStyle(styleName);
      le.setEnabled(true);

      if (getPublisher().publishDBLayer(GeoserverProperties.getWorkspace(), GeoserverProperties.getStore(), fte, le))
      {
//        log.info("Created the layer [" + layer + "] in geoserver.");
        System.out.println("Failed to create the layer [" + layer + "] in geoserver.");
        return;
      }
      else
      {
//        log.warn("Failed to create the layer [" + layer + "] in geoserver.");
        System.out.println("Failed to create the layer [" + layer + "] in geoserver.");
        return;
      }
      
    }
  }
  
  /**
   * Removes the layer from geoserver.
   * 
   * @param layer
   * @return
   */
  public static void removeLayer(String layer)
  {
    String workspace = GeoserverProperties.getWorkspace();
    if (getPublisher().removeLayer(workspace, layer))
    {
      log.info("Removed the layer for [" + layer + "].");
    }
    else
    {
      log.warn("Failed to remove the layer for [" + layer + "].");
    }
  }
  

  public static void main(String[] args) throws MalformedURLException
  {

    GeoserverProps props = new GeoserverProps();
    
    

     GeoServerRESTReader reader = new
     GeoServerRESTReader(props.getLocalPath(), props.getAdminUser(),
     props.getAdminPassword());
     
//     System.out.println(reader.getWorkspaceNames());
//     
//     System.out.println(reader.getResource(reader.getLayer("poi")));

     GeoServerRESTPublisher publisher = new
     GeoServerRESTPublisher(props.getLocalPath(), props.getAdminUser(),
     props.getAdminPassword());

    //
    // System.out.println(getReader().getSLD("burg"));


//    System.out.println(Sandbox.getLayers());
    
//    System.out.println("Layer exists = " + Sandbox.layerExists("poi"));
    
    System.out.println("Style exists = " + Sandbox.styleExists("poi"));
    
    if(Sandbox.layerExists("poi")){
      System.out.println("Layer exists = " + Sandbox.layerExists("poi_test"));
      System.out.println("Now lets remove it");
      
//      Sandbox.removeLayer("poi_test");
    }
    else{
      Sandbox.publishLayer("poi", "poi");
    }

//    Sandbox.refresh();

  }

>>>>>>> Stashed changes
}
