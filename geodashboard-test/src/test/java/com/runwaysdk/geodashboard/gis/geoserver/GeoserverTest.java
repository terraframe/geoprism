package com.runwaysdk.geodashboard.gis.geoserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.geoserver.GeoserverFacade;
import com.runwaysdk.geodashboard.gis.impl.LayerImpl;
import com.runwaysdk.geodashboard.gis.impl.MapImpl;
import com.runwaysdk.geodashboard.gis.impl.StyleImpl;
import com.runwaysdk.geodashboard.gis.impl.ThematicStyleImpl;
import com.runwaysdk.geodashboard.gis.impl.condition.AndImpl;
import com.runwaysdk.geodashboard.gis.impl.condition.EqualImpl;
import com.runwaysdk.geodashboard.gis.impl.condition.OrImpl;
import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.geodashboard.gis.model.Style;
import com.runwaysdk.geodashboard.gis.model.condition.And;
import com.runwaysdk.geodashboard.gis.model.condition.Equal;
import com.runwaysdk.geodashboard.gis.model.condition.Or;
import com.runwaysdk.geodashboard.gis.persist.AllLayerType;
import com.runwaysdk.geodashboard.gis.persist.DashboardLayer;
import com.runwaysdk.geodashboard.gis.persist.DashboardMap;
import com.runwaysdk.geodashboard.gis.persist.DashboardStyle;
import com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle;
import com.runwaysdk.geodashboard.gis.persist.HasLayer;
import com.runwaysdk.geodashboard.gis.persist.HasStyle;
import com.runwaysdk.geodashboard.gis.sld.SLDMapVisitor;
import com.runwaysdk.geodashboard.gis.sld.WellKnownName;
import com.runwaysdk.gis.StrategyInitializer;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdAttributeInteger;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.system.metadata.MdBusiness;

public class GeoserverTest
{
  private static final String         TEST_PACKAGE = "com.test.geodashboard";

  // prevent ssl errors from localhost requests
  static
  {
    //
    // for localhost testing only
    //
    javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier()
    {

      public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession)
      {
        if (hostname.equals("localhost"))
        {
          return true;
        }
        else if (hostname.equals("127.0.0.1"))
        {
          return true;
        }
        return false;
      }
    });
  }

  private static Universal            state;

  private static MdBusiness           stateInfo;

  private static String               stateInfoId;

  private static MdAttributeInteger   population;

  private static MdAttributeReference geoentity;

  public static void main(String[] args) throws Throwable
  {
    String url = "https://localhost:8443/geoserver/wms/reflect?layers=topp:states_2&format=image/png";

    testWMS(url);
  }

  @BeforeClass
  @Request
  public static void classSetup()
  {
    StrategyInitializer.startUp();

    try
    {
      stateInfo = new MdBusiness();
      stateInfo.setTypeName("StateInfo");
      stateInfo.setPackageName(TEST_PACKAGE);
      stateInfo.apply();

      population = new MdAttributeInteger();
      population.setDefiningMdClass(stateInfo);
      population.setAttributeName("population");
      population.apply();

      MdBusiness geoentityMd = MdBusiness.getMdBusiness(GeoEntity.CLASS);

      geoentity = new MdAttributeReference();
      geoentity.setDefiningMdClass(stateInfo);
      geoentity.setMdBusiness(geoentityMd);
      geoentity.setAttributeName("geoentity");
      geoentity.apply();

      state = new Universal();
      state.getDisplayLabel().setDefaultValue("State");
      state.getDescription().setDefaultValue("State");
      state.setUniversalId("state");
      state.apply();
    }
    catch (Throwable t)
    {
      t.printStackTrace(System.out);
      throw new RuntimeException(t);
    }
  }

  @AfterClass
  @Request
  public static void classTeardown()
  {
    try
    {
      MdBusiness.get(stateInfo.getId()).delete();
      Universal.get(state.getId()).delete();
    }
    catch (Throwable t)
    {
      t.printStackTrace(System.out);
      throw new RuntimeException(t);
    }
    finally
    {
      StrategyInitializer.tearDown();
    }

  }



  /**
   * Creates styling for a point layer.
   */
  @Test
  @Request
  @Transaction
  public void createPointSLD()
  {
    DashboardMap map = null;

    try
    {

      map = new DashboardMap();
      map.setName("Test Map");
      map.apply();

      DashboardLayer layer = new DashboardLayer();
      layer.setName("Layer 1");
      layer.addLayerType(AllLayerType.BUBBLE);
      layer.setVirtual(true);
      layer.apply();

      HasLayer hasLayer = map.addHasLayer(layer);
      hasLayer.setLayerIndex(0);
      hasLayer.apply();

      DashboardStyle style = new DashboardStyle();
      style.setName("Style 1");
      style.apply();

      HasStyle hasStyle = layer.addHasStyle(style);
      hasStyle.apply();

      SLDMapVisitor visitor = new SLDMapVisitor();
      map.accepts(visitor);
      String sld = visitor.getSLD(layer);

      String styleName = layer.getKeyName();

      GeoserverFacade.publishStyle(styleName, sld);

      if (!GeoserverFacade.styleExists(styleName))
      {
        Assert.fail("The style [" + styleName + "] was not created.");
      }
    }
    finally
    {
      map.delete();
    }
  }

  /**
   * Creates styling for a polygon layer.
   */
  @Test
  @Request
  @Transaction
  public void createPolygonSLD()
  {
    DashboardMap map = null;

    try
    {

      map = new DashboardMap();
      map.setName("Test Map");
      map.apply();

      DashboardLayer layer = new DashboardLayer();
      layer.setName("Layer 1");
      layer.addLayerType(AllLayerType.BASIC);
      layer.setVirtual(true);
      layer.apply();

      HasLayer hasLayer = map.addHasLayer(layer);
      hasLayer.setLayerIndex(0);
      hasLayer.apply();

      DashboardStyle style = new DashboardStyle();
      style.setName("Style 1");
      style.apply();

      HasStyle hasStyle = layer.addHasStyle(style);
      hasStyle.apply();

      SLDMapVisitor visitor = new SLDMapVisitor();
      map.accepts(visitor);
      String sld = visitor.getSLD(layer);

      String styleName = layer.getKeyName();

      GeoserverFacade.publishStyle(styleName, sld);

      if (!GeoserverFacade.styleExists(styleName))
      {
        Assert.fail("The style [" + styleName + "] was not created.");
      }
    }
    finally
    {
      map.delete();
    }
  }

  /**
   * Creates thematic styling for a point layer.
   */
  @Test
  @Request
  @Transaction
  public void createThematicPointSLD()
  {
    DashboardMap map = null;

    try
    {

      map = new DashboardMap();
      map.setName("Test Map");
      map.apply();

      DashboardLayer layer = new DashboardLayer();
      layer.setName("Layer 1");
      layer.addLayerType(AllLayerType.BUBBLE);
      layer.setVirtual(true);
      layer.apply();

      HasLayer hasLayer = map.addHasLayer(layer);
      hasLayer.setLayerIndex(0);
      hasLayer.apply();

      DashboardThematicStyle style = new DashboardThematicStyle();
      style.setMdAttribute(population);
      style.setName("Style 1");
      style.apply();

      HasStyle hasStyle = layer.addHasStyle(style);
      hasStyle.apply();

      SLDMapVisitor visitor = new SLDMapVisitor();
      map.accepts(visitor);
      String sld = visitor.getSLD(layer);

      String styleName = layer.getKeyName();

      GeoserverFacade.publishStyle(styleName, sld);

      if (!GeoserverFacade.styleExists(styleName))
      {
        Assert.fail("The style [" + styleName + "] was not created.");
      }
    }
    finally
    {
      map.delete();
    }
  }

  /**
   * Creates thematic styling for a polygon layer.
   */
  @Test
  @Request
  @Transaction
  public void createThematicPolygonSLD()
  {
    junit.framework.Assert.fail("Not Implemented");
  }

  // HTTPS GET request
  public static void testWMS(String url) throws Exception
  {

    URL obj = new URL(url);
    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

    // optional default is GET
    con.setRequestMethod("GET");

    // add request header
    con.setRequestProperty("User-Agent",
        "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

    int responseCode = con.getResponseCode();
    System.out.println("\nSending 'GET' request to URL : " + url);
    System.out.println("Response Code : " + responseCode);

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ( ( inputLine = in.readLine() ) != null)
    {
      response.append(inputLine);
    }
    in.close();

    // print result
    System.out.println(response.toString());

  }
//
//  /**
//   * Creates a point layer.
//   */
//  @Test
//  @Request
//  @Transaction
//  public void createPointLayer()
//  {
//    Assert.fail("Not implemented");
//  }
//
//  @Test
//  @Request
//  @Transaction
//  public void createManyPointLayers()
//  {
//    Assert.fail("Not implemented");
//  }
//
//  @Test
//  @Request
//  @Transaction
//  public void createManyPolygonLayers()
//  {
//    Assert.fail("Not implemented");
//
//  }
//
//  @Test
//  @Request
//  @Transaction
//  public void createManyMixedLayers()
//  {
//
//    Assert.fail("Not implemented");
//  }
//
//  @Test
//  @Request
//  @Transaction
//  public void testRemoveLayer()
//  {
//    Assert.fail("Not implemented");
//  }
//
//  @Test
//  @Request
//  @Transaction
//  public void testRemoveStyle()
//  {
//    Assert.fail("Not implemented");
//  }
//
//  /**
//   * Creates a polygon layer.
//   */
//  @Test
//  @Request
//  @Transaction
//  public void createPolygonLayer()
//  {
//    junit.framework.Assert.fail("Not Implemented");
//  }
}
