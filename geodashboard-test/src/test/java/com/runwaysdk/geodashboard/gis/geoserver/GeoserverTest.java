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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.attributes.InvalidReferenceException;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTable;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.geodashboard.geoserver.GeoserverFacade;
import com.runwaysdk.geodashboard.gis.GISImportLoggerIF;
import com.runwaysdk.geodashboard.gis.MockLogger;
import com.runwaysdk.geodashboard.gis.model.FeatureType;
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
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.EntityQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdAttributeInteger;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.system.metadata.Metadata;
import com.runwaysdk.util.FileIO;

public class GeoserverTest
{
  private static final String         TEST_PACKAGE   = "com.test.geodashboard";

  private static final String         TYPE_NAME      = "StateInfo";

  private static final String         STATE_INFO     = TEST_PACKAGE + "." + TYPE_NAME;

  private static final String         TEST_SHAPEFILE = "src/test/resources/shapefile/states.shp";

  private static final String         SLD_SCHEMA     = "src/test/resources/StyledLayerDescriptor.xsd";

  private static final String         USA_WKT        = "src/test/resources/USA_WKT.txt";

  private static int                  stateCount     = 0;

  private static boolean              GEOSERVER_RUNNING;

  private static final File           xsd            = new File(SLD_SCHEMA);

  /*
   * static {
   * 
   * javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new
   * javax.net.ssl.HostnameVerifier() {
   * 
   * public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession)
   * { if (hostname.equals("localhost")) { return true; } else if
   * (hostname.equals("127.0.0.1")) { return true; } return false; } }); }
   */

  private static Universal            state;

  private static Universal            country;

  private static GeoEntity            usa;

  private static MdBusiness           stateInfo;

  private static String               stateInfoId;

  private static MdAttributeInteger   rank;

  private static MdAttributeReference geoentityRef;

  public static void main(String[] args) throws Throwable
  {
    String url = "https://localhost:8443/geoserver/wms/reflect?layers=topp:states_2&format=image/png";

    testWMS(url);
  }

  public static void testWMS(String url) throws Exception
  {

    URL obj = new URL(url);
    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

    con.setRequestMethod("GET");

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

    System.out.println(response.toString());

  }

  @BeforeClass
  @Request
  public static void classSetup()
  {
    GEOSERVER_RUNNING = GeoserverFacade.geoserverExists();

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
    stateInfoId = stateInfo.getId();

    rank = new MdAttributeInteger();
    rank.setDefiningMdClass(stateInfo);
    rank.setAttributeName("rank");
    rank.getDisplayLabel().setDefaultValue("Alphabetic Rank");
    rank.apply();

    MdBusiness geoentityMd = MdBusiness.getMdBusiness(GeoEntity.CLASS);

    geoentityRef = new MdAttributeReference();
    geoentityRef.setDefiningMdClass(stateInfo);
    geoentityRef.setMdBusiness(geoentityMd);
    geoentityRef.setAttributeName("geoentity");
    geoentityRef.apply();

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

    state.addLink(country, AllowedIn.CLASS).apply();

    usa = new GeoEntity();
    usa.getDisplayLabel().setDefaultValue("USA");
    usa.setGeoId("USA0000");
    usa.setUniversal(country);

    try
    {
      String wkt = FileIO.readString(USA_WKT);
      usa.setWkt(wkt);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }

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

          // Set the state as a child of USA (and remove anything else)
          OIterator<? extends LocatedIn> liIter = ge.getAllLocatedInRel();
          try
          {
            while (liIter.hasNext())
            {
              liIter.next().delete();
            }
          }
          finally
          {
            liIter.close();
          }

          ge.addLink(usa, LocatedIn.CLASS).apply();

          Business si = BusinessFacade.newBusiness(STATE_INFO);
          si.setValue(rank.getAttributeName(), Integer.toString(count++));
          si.setValue(geoentityRef.getAttributeName(), ge.getId());
          si.apply();
        }

        stateCount = count;
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
  public static void classTeardown()
  {
    metadataTeardown();
    StrategyInitializer.tearDown();
  }

  @Transaction
  private static void metadataTeardown()
  {
    MdBusiness.get(stateInfo.getId()).delete();
    Universal.get(country.getId()).delete();
    Universal.get(state.getId()).delete();
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
      layer.setUniversal(state);
      layer.addLayerType(AllLayerType.BUBBLE);
      layer.setVirtual(true);
      layer.setGeoEntity(geoentityRef);
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

      try
      {
        validate(sld);
      }
      catch (SAXException e)
      {
        Assert.fail(e.getLocalizedMessage());
      }

      if (GEOSERVER_RUNNING)
      {
        GeoserverFacade.publishStyle(styleName, sld);

        if (!GeoserverFacade.styleExists(styleName))
        {
          Assert.fail("The style [" + styleName + "] was not created.");
        }

        GeoserverFacade.removeStyle(styleName);
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
      layer.setUniversal(state);
      layer.addLayerType(AllLayerType.BASIC);
      layer.setVirtual(true);
      layer.setGeoEntity(geoentityRef);
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

      try
      {
        validate(sld);
      }
      catch (SAXException e)
      {
        Assert.fail(e.getLocalizedMessage());
      }

      if (GEOSERVER_RUNNING)
      {
        GeoserverFacade.publishStyle(styleName, sld);

        if (!GeoserverFacade.styleExists(styleName))
        {
          Assert.fail("The style [" + styleName + "] was not created.");
        }

        GeoserverFacade.removeStyle(styleName);
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
  @Transaction
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
      layer.setUniversal(state);
      layer.addLayerType(AllLayerType.BUBBLE);
      layer.setVirtual(true);
      layer.setGeoEntity(geoentityRef);
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

      DashboardOr and = new DashboardOr();
      and.setLeftCondition(gte);
      and.setRightCondition(lte);
      and.apply();

      DashboardThematicStyle style = new DashboardThematicStyle();
      style.setMdAttribute(rank);
      style.setName("Style 1");
      style.setStyleCondition(and);
      style.apply();

      HasStyle hasStyle = layer.addHasStyle(style);
      hasStyle.apply();

      SLDMapVisitor visitor = new SLDMapVisitor();
      map.accepts(visitor);
      String sld = visitor.getSLD(layer);

      String styleName = layer.getKeyName();

      try
      {
        validate(sld);
      }
      catch (SAXException e)
      {
        Assert.fail(e.getLocalizedMessage());
      }

      if (GEOSERVER_RUNNING)
      {
        GeoserverFacade.publishStyle(styleName, sld);

        if (!GeoserverFacade.styleExists(styleName))
        {
          Assert.fail("The style [" + styleName + "] was not created.");
        }

        GeoserverFacade.removeStyle(styleName);
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
      layer.setUniversal(state);
      layer.addLayerType(AllLayerType.BUBBLE);
      layer.setVirtual(true);
      layer.setGeoEntity(geoentityRef);
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

      try
      {
        validate(sld);
      }
      catch (SAXException e)
      {
        Assert.fail(e.getLocalizedMessage());
      }

      if (GEOSERVER_RUNNING)
      {
        GeoserverFacade.publishStyle(styleName, sld);

        if (!GeoserverFacade.styleExists(styleName))
        {
          Assert.fail("The style [" + styleName + "] was not created.");
        }

        GeoserverFacade.removeStyle(styleName);
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
    DashboardMap map = null;

    try
    {

      map = new DashboardMap();
      map.setName("Test Map");
      map.apply();

      DashboardLayer layer = new DashboardLayer();
      layer.setName("Layer 1");
      layer.setUniversal(state);
      layer.addLayerType(AllLayerType.BASIC);
      layer.setVirtual(true);
      layer.setGeoEntity(geoentityRef);
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

      try
      {
        validate(sld);
      }
      catch (SAXException e)
      {
        Assert.fail(e.getLocalizedMessage());
      }

      if (GEOSERVER_RUNNING)
      {
        GeoserverFacade.publishStyle(styleName, sld);

        if (!GeoserverFacade.styleExists(styleName))
        {
          Assert.fail("The style [" + styleName + "] was not created.");
        }

        GeoserverFacade.removeStyle(styleName);
      }
    }
    finally
    {
      map.delete();
    }
  }

  /**
   * Tests that a Layer can only reference an MdAttributeReference that points
   * to GeoEntity.
   */
  @Test
  @Request
  @Transaction
  public void testInvalidLayerGeoEntityReference()
  {
    DashboardLayer layer = null;
    try
    {
      MdBusinessDAOIF md = MdBusinessDAO.get(stateInfoId);
      MdAttributeReferenceDAOIF createdBy = (MdAttributeReferenceDAOIF) md
          .definesAttribute(Metadata.CREATEDBY);

      layer = new DashboardLayer();
      layer.setName("Layer 1");
      layer.setUniversal(state);
      layer.setGeoEntity(MdAttributeReference.get(createdBy.getId()));
      layer.addLayerType(AllLayerType.BUBBLE);
      layer.setVirtual(true);
      layer.setGeoEntity(geoentityRef);
      layer.apply();

      Assert.fail("A Layer was able to reference a non-GeoEntity attribute.");
    }
    catch (InvalidReferenceException e)
    {
      System.out.println(e.getLocalizedMessage());
      // this is expected
    }
    finally
    {
      if (layer != null && layer.isAppliedToDB())
      {
        layer.delete();
      }
    }
  }

  /**
   * Creates a point layer.
   */
  @Test
  @Request
  @Transaction
  public void createPointLayer()
  {
    DashboardMap map = null;

    try
    {

      map = new DashboardMap();
      map.setName("Test Map");
      map.apply();

      DashboardLayer layer = new DashboardLayer();
      layer.setName("Layer 1");
      layer.setUniversal(state);
      layer.setGeoEntity(geoentityRef);
      layer.addLayerType(AllLayerType.BUBBLE);
      layer.setVirtual(true);
      layer.setGeoEntity(geoentityRef);
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
      
      ValueQuery v = layer.asValueQuery();

      // This query should have all states in it
      QueryFactory checkF = new QueryFactory();
      GeoEntityQuery checkGE = new GeoEntityQuery(checkF);
      checkGE.WHERE(checkGE.getUniversal().EQ(layer.getUniversal()));

      Assert.assertEquals(stateCount, checkGE.getCount());
      Assert.assertEquals(checkGE.getCount(), v.getCount());

      if (GEOSERVER_RUNNING)
      {
        Assert.fail("Not implemented.");
      }

    }
    finally
    {
      map.delete();
    }
  }

  @Test
  @Request
  @Transaction
  public void createPointLayerHigherUniversal()
  {
    DashboardMap map = null;

    try
    {

      map = new DashboardMap();
      map.setName("Test Map");
      map.apply();

      DashboardLayer layer = new DashboardLayer();
      layer.setName("Layer 1");
      layer.setUniversal(country);
      layer.setGeoEntity(geoentityRef);
      layer.addLayerType(AllLayerType.BUBBLE);
      layer.setVirtual(true);
      layer.setGeoEntity(geoentityRef);
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

      ValueQuery v = layer.asValueQuery();

      // This query should have all states in it
      QueryFactory checkF = new QueryFactory();
      GeoEntityQuery checkGE = new GeoEntityQuery(checkF);
      checkGE.WHERE(checkGE.getUniversal().EQ(layer.getUniversal()));
      System.out.println(v.getSQL());

      Assert.assertEquals(1, checkGE.getCount());
      Assert.assertEquals(checkGE.getCount(), v.getCount());

      if (GEOSERVER_RUNNING)
      {
        Assert.fail("Not implemented.");
      }

    }
    finally
    {
      map.delete();
    }
  }

  @Test
  @Request
  @Transaction
  public void createManyPointLayers()
  {
    Assert.fail("Not implemented");
  }

  @Test
  @Request
  @Transaction
  public void createManyPolygonLayers()
  {
    Assert.fail("Not implemented");

  }

  @Test
  @Request
  @Transaction
  public void createManyMixedLayers()
  {

    Assert.fail("Not implemented");
  }

  @Test
  @Request
  @Transaction
  public void testRemoveLayer()
  {
    Assert.fail("Not implemented");
  }

  @Test
  @Request
  @Transaction
  public void testRemoveStyle()
  {
    Assert.fail("Not implemented");
  }

  /**
   * Creates a polygon layer.
   */
  @Test
  @Request
  @Transaction
  public void createPolygonLayer()
  {
    junit.framework.Assert.fail("Not Implemented");
  }

  @Test
  @Request
  @Transaction
  public void testMapJSON()
  {
    // String json = new DashboardMap().getMapJSON();
    //
    // JSONObject obj = new JSONObject(json);
    //
    // Assert.assertEquals(obj.getString("mapName"), "Justin's Map");
  }
}
