package com.runwaysdk.geodashboard.gis.geoserver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.geoserver.GeoserverFacade;
import com.runwaysdk.geodashboard.gis.GISImportLoggerIF;
import com.runwaysdk.geodashboard.gis.MockLogger;
import com.runwaysdk.geodashboard.gis.persist.AllLayerType;
import com.runwaysdk.geodashboard.gis.persist.DashboardLayer;
import com.runwaysdk.geodashboard.gis.persist.DashboardMap;
import com.runwaysdk.geodashboard.gis.persist.DashboardStyle;
import com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle;
import com.runwaysdk.geodashboard.gis.persist.HasLayer;
import com.runwaysdk.geodashboard.gis.persist.HasStyle;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqual;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThanOrEqual;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardLessThanOrEqual;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardOr;
import com.runwaysdk.geodashboard.gis.shapefile.ShapeFileImporter;
import com.runwaysdk.geodashboard.gis.sld.SLDMapVisitor;
import com.runwaysdk.gis.StrategyInitializer;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdAttributeInteger;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.system.metadata.MdBusiness;

public class GeoserverTest
{
  private static final String         TEST_PACKAGE   = "com.test.geodashboard";

  private static final String         TYPE_NAME      = "StateInfo";

  private static final String         STATE_INFO     = TEST_PACKAGE + "." + TYPE_NAME;

  private static final String         TEST_SHAPEFILE = "src/test/resources/shapefile/states.shp";

  private static final String         SLD_SCHEMA     = "src/test/resources/StyledLayerDescriptor.xsd";

  private static boolean        LOCAL;

  private static final File           xsd            = new File(SLD_SCHEMA);

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

  private static Universal            country;

  private static GeoEntity            usa;

  private static MdBusiness           stateInfo;

  private static String               stateInfoId;

  private static MdAttributeInteger   rank;

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
    LOCAL = !GeoserverFacade.geoserverExists();
    
    metadataSetup();
    dataSetup();
  }

  @Transaction
  private static void metadataSetup()
  {
    StrategyInitializer.startUp();

    stateInfo = new MdBusiness();
    stateInfo.setTypeName(TYPE_NAME);
    stateInfo.setPackageName(TEST_PACKAGE);
    stateInfo.apply();

    rank = new MdAttributeInteger();
    rank.setDefiningMdClass(stateInfo);
    rank.setAttributeName("rank");
    rank.getDisplayLabel().setDefaultValue("Alphabetic Rank");
    rank.apply();

    MdBusiness geoentityMd = MdBusiness.getMdBusiness(GeoEntity.CLASS);

    geoentity = new MdAttributeReference();
    geoentity.setDefiningMdClass(stateInfo);
    geoentity.setMdBusiness(geoentityMd);
    geoentity.setAttributeName("geoentity");
    geoentity.apply();

    country = new Universal();
    country.getDisplayLabel().setDefaultValue("Country");
    country.getDescription().setDefaultValue("Country");
    country.setUniversalId("country");
    country.apply();

    state = new Universal();
    state.getDisplayLabel().setDefaultValue("State");
    state.getDescription().setDefaultValue("State");
    state.setUniversalId("state");
    state.apply();

    state.addAllowedIn(country).apply();

    usa = new GeoEntity();
    usa.getDisplayLabel().setDefaultValue("USA");
    usa.setGeoId("USA0000");
    usa.setUniversal(country);
    usa.apply();

  }

  private static void dataSetup()
  {
    try
    {
      File shapefile = new File(TEST_SHAPEFILE);
      URL url = shapefile.toURI().toURL();

      GISImportLoggerIF logger = new MockLogger();

      ShapeFileImporter importer = new ShapeFileImporter(url);
      importer.setUniversalId(state.getId());
      importer.setName("STATE_NAME");
      importer.setId("STATE_NAME");
      importer.run(logger);

      // Create an alphabetic rank for each state
      QueryFactory f = new QueryFactory();
      GeoEntityQuery q = new GeoEntityQuery(f);

      q.WHERE(q.getUniversal().EQ(state));
      q.ORDER_BY_ASC(q.getDisplayLabel().getDefaultLocale());

      OIterator<? extends GeoEntity> iter = q.getIterator();
      try
      {
        int count = 0;
        while (iter.hasNext())
        {
          GeoEntity ge = iter.next();

          Business si = BusinessFacade.newBusiness(STATE_INFO);
          si.setValue(rank.getAttributeName(), Integer.toString(count++));
          si.setValue(geoentity.getAttributeName(), ge.getId());
          si.apply();
        }
      }
      finally
      {
        iter.close();
      }
    }
    catch (Throwable e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @AfterClass
  @Request
  @Transaction
  public static void classTeardown()
  {
    try
    {
      MdBusiness.get(stateInfo.getId()).delete();
      Universal.get(country.getId()).delete();
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

  private void validate(String sld) throws SAXException
  {
    SchemaFactory f = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = f.newSchema(xsd);
    Validator v = schema.newValidator();

    InputStream stream;
    try
    {
      stream = new ByteArrayInputStream(sld.getBytes("UTF-8"));
      Source s = new StreamSource(stream);
      v.validate(s);
    }
    catch (UnsupportedEncodingException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (IOException e)
    {
      throw new ProgrammingErrorException(e);
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

      if (LOCAL)
      {
        try
        {
          validate(sld);
        }
        catch (SAXException e)
        {
          Assert.fail(e.getLocalizedMessage());
        }
      }
      else
      {
        GeoserverFacade.publishStyle(styleName, sld);

        if (!GeoserverFacade.styleExists(styleName))
        {
          Assert.fail("The style [" + styleName + "] was not created.");
        }
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

      if (LOCAL)
      {
        try
        {
          validate(sld);
        }
        catch (SAXException e)
        {
          Assert.fail(e.getLocalizedMessage());
        }
      }
      else
      {
        GeoserverFacade.publishStyle(styleName, sld);

        if (!GeoserverFacade.styleExists(styleName))
        {
          Assert.fail("The style [" + styleName + "] was not created.");
        }
      }
    }
    finally
    {
      map.delete();
    }
  }

  /**
   * Tests creating a composite condition.
   */
  @Test
  @Request
  public void createCompositePointSLD()
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

      DashboardGreaterThanOrEqual gte = new DashboardGreaterThanOrEqual();
      gte.setComparisonValue("0");
      gte.apply();

      DashboardLessThanOrEqual lte = new DashboardLessThanOrEqual();
      lte.setComparisonValue("10");
      lte.apply();

      DashboardOr or = new DashboardOr();
      or.setLeftCondition(gte);
      or.setRightCondition(lte);
      or.apply();

      DashboardThematicStyle style = new DashboardThematicStyle();
      style.setMdAttribute(rank);
      style.setName("Style 1");
      style.setStyleCondition(or);
      style.apply();

      HasStyle hasStyle = layer.addHasStyle(style);
      hasStyle.apply();

      SLDMapVisitor visitor = new SLDMapVisitor();
      map.accepts(visitor);
      String sld = visitor.getSLD(layer);

      String styleName = layer.getKeyName();

      if (LOCAL)
      {
        try
        {
          validate(sld);
        }
        catch (SAXException e)
        {
          Assert.fail(e.getLocalizedMessage());
        }
      }
      else
      {
        GeoserverFacade.publishStyle(styleName, sld);

        if (!GeoserverFacade.styleExists(styleName))
        {
          Assert.fail("The style [" + styleName + "] was not created.");
        }
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

      DashboardEqual eq = new DashboardEqual();
      eq.setComparisonValue("5");
      eq.setParentCondition(null);
      eq.setRootCondition(null);
      eq.apply();

      DashboardThematicStyle style = new DashboardThematicStyle();
      style.setMdAttribute(rank);
      style.setName("Style 1");
      style.setStyleCondition(eq);
      style.apply();

      HasStyle hasStyle = layer.addHasStyle(style);
      hasStyle.apply();

      SLDMapVisitor visitor = new SLDMapVisitor();
      map.accepts(visitor);
      String sld = visitor.getSLD(layer);

      String styleName = layer.getKeyName();

      if (LOCAL)
      {
        try
        {
          validate(sld);
        }
        catch (SAXException e)
        {
          Assert.fail(e.getLocalizedMessage());
        }
      }
      else
      {
        GeoserverFacade.publishStyle(styleName, sld);

        if (!GeoserverFacade.styleExists(styleName))
        {
          Assert.fail("The style [" + styleName + "] was not created.");
        }
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
  public void createThematicPolygonSLD()
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

      DashboardEqual eq = new DashboardEqual();
      eq.setComparisonValue("5");
      eq.setParentCondition(null);
      eq.setRootCondition(null);
      eq.apply();

      DashboardThematicStyle style = new DashboardThematicStyle();
      style.setMdAttribute(rank);
      style.setName("Style 1");
      style.setStyleCondition(eq);
      style.apply();

      HasStyle hasStyle = layer.addHasStyle(style);
      hasStyle.apply();

      SLDMapVisitor visitor = new SLDMapVisitor();
      map.accepts(visitor);
      String sld = visitor.getSLD(layer);

      String styleName = layer.getKeyName();

      if (LOCAL)
      {
        try
        {
          validate(sld);
        }
        catch (SAXException e)
        {
          Assert.fail(e.getLocalizedMessage());
        }
      }
      else
      {
        GeoserverFacade.publishStyle(styleName, sld);

        if (!GeoserverFacade.styleExists(styleName))
        {
          Assert.fail("The style [" + styleName + "] was not created.");
        }
      }
    }
    finally
    {
      map.delete();
    }
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

  /**
   * Creates a point layer.
   */
  @Test
  @Request
  @Transaction
  public void createPointLayer()
  {

  }

  //
  // @Test
  // @Request
  // @Transaction
  // public void createManyPointLayers()
  // {
  // Assert.fail("Not implemented");
  // }
  //
  // @Test
  // @Request
  // @Transaction
  // public void createManyPolygonLayers()
  // {
  // Assert.fail("Not implemented");
  //
  // }
  //
  // @Test
  // @Request
  // @Transaction
  // public void createManyMixedLayers()
  // {
  //
  // Assert.fail("Not implemented");
  // }
  //
  // @Test
  // @Request
  // @Transaction
  // public void testRemoveLayer()
  // {
  // Assert.fail("Not implemented");
  // }
  //
  // @Test
  // @Request
  // @Transaction
  // public void testRemoveStyle()
  // {
  // Assert.fail("Not implemented");
  // }
  //
  // /**
  // * Creates a polygon layer.
  // */
  // @Test
  // @Request
  // @Transaction
  // public void createPolygonLayer()
  // {
  // junit.framework.Assert.fail("Not Implemented");
  // }
}
