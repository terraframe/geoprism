package com.runwaysdk.geodashboard.gis.geoserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.MdAttributeDateUtil;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.AttributeWrapper;
import com.runwaysdk.geodashboard.Dashboard;
import com.runwaysdk.geodashboard.DashboardAttributes;
import com.runwaysdk.geodashboard.DashboardMetadata;
import com.runwaysdk.geodashboard.DashboardQuery;
import com.runwaysdk.geodashboard.MetadataWrapper;
import com.runwaysdk.geodashboard.gis.GISImportLoggerIF;
import com.runwaysdk.geodashboard.gis.MockLogger;
import com.runwaysdk.geodashboard.gis.persist.AllAggregationType;
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
import com.runwaysdk.geodashboard.gis.sld.SLDValidator;
import com.runwaysdk.gis.StrategyInitializer;
import com.runwaysdk.logging.LogLevel;
import com.runwaysdk.logging.RunwayLogUtil;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdAttributeDate;
import com.runwaysdk.system.metadata.MdAttributeDouble;
import com.runwaysdk.system.metadata.MdAttributeInteger;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.system.metadata.MdAttributeVirtual;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.system.metadata.MdView;
import com.runwaysdk.system.metadata.Metadata;
import com.runwaysdk.util.FileIO;




  /**
   * Simple class to be run as a way to load test data into the system.
   * 
   * @author justin
   * 
   */
  public class GeoserverTest
  {
    protected static final String         TEST_PACKAGE   = "com.test.geodashboard";

    protected static final String         TYPE_NAME      = "StateInfo";

    protected static final String         VIEW_NAME      = "StateInfoView";

    protected static final String         STATE_INFO     = TEST_PACKAGE + "." + TYPE_NAME;

    protected static final String         TEST_SHAPEFILE = "src/test/resources/shapefile/states.shp";

    protected static final String         SLD_SCHEMA     = "src/test/resources/StyledLayerDescriptor.xsd";

    protected static final String         USA_WKT        = "src/test/resources/USA_WKT.txt";

    protected static int                  stateCount     = 0;

    protected static boolean              GEOSERVER_RUNNING;

    protected static final File           xsd            = new File(SLD_SCHEMA);

    protected static final boolean        consoleDebug   = true;

    protected static Universal            state;

    protected static Universal            country;

    protected static GeoEntity            usa;

    protected static MdBusiness           stateInfo;

    protected static MdView               stateInfoView;

    protected static String               stateInfoId;

    protected static MdAttributeInteger   rank;
    
    protected static MdAttributeDate date;

    protected static MdAttributeDouble    ratio;

    protected static MdAttributeReference geoentityRef;
    
    protected static Dashboard dashboard;
    
    private static boolean keepData = true;

    protected static final Log            log            = LogFactory.getLog(GeoserverTest.class);

    static
    {
      if (consoleDebug)
      {
        BasicConfigurator.configure();
        RunwayLogUtil.convertLogLevelToLevel(LogLevel.ERROR);
      }
    }

    public static void main(String[] args) throws Throwable
    {
      try
      {
        log.debug("Executing GeoserverTest.main() to load test data (no teardown)");

        GEOSERVER_RUNNING = GeoserverFacade.geoserverExists();

        metadataSetup();
        dataSetup();
      }
      catch (Throwable t)
      {
        log.error("Unable to invoke classSetup()", t);
        throw new RuntimeException(t);
      }
    }


  public
  @Rule
   TestName                     name           = new TestName();

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

  @Request
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
    stateInfo.getDisplayLabel().setDefaultValue("State Information");
    stateInfo.apply();
    stateInfoId = stateInfo.getId();

    rank = new MdAttributeInteger();
    rank.setDefiningMdClass(stateInfo);
    rank.setAttributeName("rank");
    rank.getDisplayLabel().setDefaultValue("Alphabetic Rank");
    rank.apply();
    
    date = new MdAttributeDate();
    date.setDefiningMdClass(stateInfo);
    date.setAttributeName("studyDate");
    date.getDisplayLabel().setDefaultValue("Study Date");
    date.apply();
    
    // ratio = rank/total (linear relationship...higher rank = 
    ratio = new MdAttributeDouble();
    ratio.setDefiningMdClass(stateInfo);
    ratio.setAttributeName("ratio");
    ratio.setDatabaseLength(4);
    ratio.setDatabaseDecimal(2);
    ratio.getDisplayLabel().setDefaultValue("Alphabetic Ratio");
    ratio.apply();

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
    
    country.addLink(Universal.getRoot(), AllowedIn.CLASS);
    
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
    usa.addLink(GeoEntity.getRoot(), LocatedIn.CLASS);

    // create the link to the dashboard with an MdView
    stateInfoView = new MdView();
    stateInfoView.setTypeName(VIEW_NAME);
    stateInfoView.getDisplayLabel().setDefaultValue("State Statistics");
    stateInfoView.setPackageName(TEST_PACKAGE);
    stateInfoView.apply();
    
    MdAttributeVirtual virtualRank = new MdAttributeVirtual();
    virtualRank.setAttributeName("viewRank");
    virtualRank.setMdAttributeConcrete(rank);
    virtualRank.setDefiningMdView(stateInfoView);
    virtualRank.getDisplayLabel().setDefaultValue("Education Rank");
    virtualRank.apply();

    MdAttributeVirtual virtualRatio = new MdAttributeVirtual();
    virtualRatio.setAttributeName("viewRatio");
    virtualRatio.setMdAttributeConcrete(ratio);
    virtualRatio.setDefiningMdView(stateInfoView);
    virtualRatio.getDisplayLabel().setDefaultValue("Crime Rate");
    virtualRatio.apply();

    MdAttributeVirtual virtualDate = new MdAttributeVirtual();
    virtualDate.setAttributeName("studyDate");
    virtualDate.setMdAttributeConcrete(date);
    virtualDate.setDefiningMdView(stateInfoView);
    virtualDate.getDisplayLabel().setDefaultValue("Study Date");
    virtualDate.apply();
    
    dashboard = new Dashboard();
    dashboard.getDisplayLabel().setDefaultValue("Test Dashboard");
    dashboard.apply();
    
    
    MetadataWrapper mWrapper = new MetadataWrapper();
    mWrapper.setWrappedMdClass(stateInfoView);
    mWrapper.apply();
    
    DashboardMetadata dm = dashboard.addMetadata(mWrapper);
    dm.setListOrder(0);
    dm.apply();
    
    // rank
    AttributeWrapper aWrapper = new AttributeWrapper();
    aWrapper.setWrappedMdAttribute(virtualRank);
    aWrapper.apply();
    
    DashboardAttributes da = mWrapper.addAttributeWrapper(aWrapper);
    da.setListOrder(0);
    da.apply();

    // ratio
    AttributeWrapper aWrapper2 = new AttributeWrapper();
    aWrapper2.setWrappedMdAttribute(virtualRatio);
    aWrapper2.apply();

    DashboardAttributes da2 = mWrapper.addAttributeWrapper(aWrapper2);
    da2.setListOrder(1);
    da2.apply();
    
    // date
    AttributeWrapper aWrapper3 = new AttributeWrapper();
    aWrapper3.setWrappedMdAttribute(virtualDate);
    aWrapper3.apply();
    
    DashboardAttributes da3 = mWrapper.addAttributeWrapper(aWrapper3);
    da3.setListOrder(2);
    da3.apply();
    
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
      
      double total = q.getCount();
      
      
      try
      {
        int count = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

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

          // add a day to the date
          cal.roll(Calendar.DATE, true);
          Date current = cal.getTime();
          String dateStr = MdAttributeDateUtil.getTypeUnsafeValue(current);
          
          Integer rankI = ++count; // 1-based starting index
          Double ratioD = rankI/total;
          
          Business si = BusinessFacade.newBusiness(STATE_INFO);
          si.setValue(rank.getAttributeName(), rankI.toString());
          si.setValue(ratio.getAttributeName(), ratioD.toString());
          si.setValue(geoentityRef.getAttributeName(), ge.getId());
          si.setValue(date.getAttributeName(), dateStr);
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
    if(keepData){
      log.debug("Skipping teardown process.");      
    }
    else
    {
      log.debug("Starting teardown process.");      
      metadataTeardown();
      StrategyInitializer.shutDown();
    }
  }

  @Transaction
  private static void metadataTeardown()
  {
    try
    {
      dashboard.delete();
      
      stateInfoView.delete();
      
      // Delete all generated views
      DashboardMap.cleanup();

      MdBusiness.get(stateInfo.getId()).delete();
      Universal.get(country.getId()).delete();
      Universal.get(state.getId()).delete();
      GeoEntity.get(usa.getId()).delete();
    }
    catch (Throwable t)
    {
      log.error("metadataTeardown", t);
      throw new RuntimeException(t);
    }
  }
  
  private void validate(String sld)
  {
    try
    {
      new SLDValidator().validate(sld);
    }
    catch(Throwable t)
    {
      log.error(sld);
      Assert.fail(t.getLocalizedMessage());
    }
  }

  /**
   * Creates styling for a point layer.
   */
  //@Test
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

      validate(sld);

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
  //@Test
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

      validate(sld);

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
  //@Test
  @Request
  @Transaction
  public void createCompositePointSLD()
  {
    DashboardMap map = new DashboardMap();
    map.setName("Test Map");
    map.apply();

    try
    {
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

      validate(sld);

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
  //@Test
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

      validate(sld);

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
  //@Test
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

      validate(sld);

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
  //@Test
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
      layer.apply();

      Assert.fail("A Layer was able to reference a non-GeoEntity attribute.");
    }
    catch (ProgrammingErrorException e)
    {
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
  //@Test
  @Request
  @Transaction
  public void createPointLayer()
  {
    DashboardMap map = null;
    String viewName = null;

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

      ValueQuery v = layer.asValueQuery();

      // This query should have all states in it
      QueryFactory checkF = new QueryFactory();
      GeoEntityQuery checkGE = new GeoEntityQuery(checkF);
      checkGE.WHERE(checkGE.getUniversal().EQ(layer.getUniversal()));

      Assert.assertEquals(stateCount, checkGE.getCount());
      Assert.assertEquals(checkGE.getCount(), v.getCount());

      viewName = layer.getViewName();
      String sldName = layer.getSLDName();
      boolean dbViewCreated = false;

      try
      {
        Database.createView(viewName, v.getSQL());
        dbViewCreated = true;

        if (GEOSERVER_RUNNING)
        {
          if (GeoserverFacade.publishLayer(viewName, sldName))
          {
            // geoserver purportedly added the layer but query it just in case
            if (!GeoserverFacade.layerExists(viewName)
                || !GeoserverFacade.getLayers().contains(viewName))
            {
              Assert.fail("Published the view [" + viewName + "] with style [" + sldName
                  + "] but it could not be found.");
            }
          }
          else
          {
            Assert.fail("Could not publish view [" + viewName + "] with style [" + sldName + "]");
          }
        }
      }
      finally
      {
        if (dbViewCreated)
        {
          List<String> views = new LinkedList<String>();
          views.add(viewName);
          Database.dropViews(views);
        }
      }
    }
    finally
    {
      map.delete();
    }
  }

  //@Test
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
      style.addAggregationType(AllAggregationType.SUM);
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

 
  /**
   * Tests the interpolate SLD code for a bubble map.
   */
  @Test
  @Request
  @Transaction
  public void createGradientSLD()
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
      layer.addLayerType(AllLayerType.GRADIENT);
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
      
      validate(sld);
    }
    finally
    {
      map.delete();
    }
  }
  
  /**
   * Tests the interpolate SLD code for a bubble map.
   */
  @Test
  @Request
  @Transaction
  public void createBubbleSLD()
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

      validate(sld);
    }
    finally
    {
      map.delete();
    }
  }

  /**
   * Creates a polygon layer.
   */
  //@Test
  @Request
  @Transaction
  public void createPolygonLayer()
  {
    DashboardMap map = null;
    String viewName = null;

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

      ValueQuery v = layer.asValueQuery();

      // This query should have all states in it
      QueryFactory checkF = new QueryFactory();
      GeoEntityQuery checkGE = new GeoEntityQuery(checkF);
      checkGE.WHERE(checkGE.getUniversal().EQ(layer.getUniversal()));

      Assert.assertEquals(stateCount, checkGE.getCount());
      Assert.assertEquals(checkGE.getCount(), v.getCount());

      viewName = layer.getViewName();
      String sldName = layer.getSLDName();
      boolean dbViewCreated = false;
      
      try
      {
        Database.createView(viewName, v.getSQL());
        dbViewCreated = true;

        if (GEOSERVER_RUNNING)
        {
          if (GeoserverFacade.publishLayer(viewName, sldName))
          {
            // geoserver purportedly added the layer but query it just in case
            if (!GeoserverFacade.layerExists(viewName)
                || !GeoserverFacade.getLayers().contains(viewName))
            {
              Assert.fail("Published the view [" + viewName + "] with style [" + sldName
                  + "] but it could not be found.");
            }
          }
          else
          {
            Assert.fail("Could not publish view [" + viewName + "] with style [" + sldName + "]");
          }
        }
      }
      finally
      {
        if (dbViewCreated)
        {
          List<String> views = new LinkedList<String>();
          views.add(viewName);
          Database.dropViews(views);
        }
      }
    }
    finally
    {
      map.delete();
    }  
  }
  
  /**
   * Ensures that a dashboard retrieves the proper metadata
   */
  //@Test
  @Request
  public void testDashboardMetadata()
  {
    DashboardQuery q = Dashboard.getSortedDashboards();
    
    Assert.assertEquals(1, q.getCount());

    OIterator<? extends Dashboard> iter = q.getIterator();
    
    try
    {
      Dashboard dashboard = iter.next();
      
      MdClass[] mds = dashboard.getSortedTypes();
      Assert.assertEquals(1, mds.length);
      
      MdView fetched = (MdView) mds[0];

      
      Assert.assertEquals(stateInfoView.getId(), fetched.getId());
    }
    finally
    {
      iter.close();
    }
  }
  
  //@Test
  @Request
  public void testMapJSON() throws JSONException
  {
    // //
    // // This needs a test for point data in addition to the current polygon.
    // //

    DashboardMap map = null;

    try
    {     

      map = new DashboardMap();
      map.setName("Test Map");
      map.apply();
      
//      session.addDashboardMap(map).apply();
//      map.addSessionEntry(session).apply();

      DashboardLayer layer = new DashboardLayer();
      layer.setName("Layer 1");
      layer.setUniversal(state);
      layer.addLayerType(AllLayerType.BASIC);
      layer.setVirtual(true);
      layer.setGeoEntity(geoentityRef);
      layer.apply();

      DashboardLayer layer2 = new DashboardLayer();
      layer2.setName("Layer 2");
      layer2.setUniversal(state);
      layer2.addLayerType(AllLayerType.BASIC);
      layer2.setVirtual(true);
      layer2.setGeoEntity(geoentityRef);
      layer2.apply();

      HasLayer hasLayer = map.addHasLayer(layer);
      hasLayer.setLayerIndex(0);
      hasLayer.apply();

      HasLayer hasLayer2 = map.addHasLayer(layer2);
      hasLayer2.setLayerIndex(1);
      hasLayer2.apply();

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

      DashboardThematicStyle style2 = new DashboardThematicStyle();
      style2.setMdAttribute(rank);
      style2.setName("demo2");
      style2.setStyleCondition(eq);
      style2.setPolygonFill("#FF0000");
      style2.apply();

      HasStyle hasStyle = layer.addHasStyle(style);
      hasStyle.apply();

      HasStyle hasStyle2 = layer2.addHasStyle(style2);
      hasStyle2.apply();

      ValueQuery v = layer.asValueQuery();
      ValueQuery v2 = layer2.asValueQuery();

      // This query should have all states in it
      QueryFactory checkF = new QueryFactory();
      GeoEntityQuery checkGE = new GeoEntityQuery(checkF);
      checkGE.WHERE(checkGE.getUniversal().EQ(layer.getUniversal()));

      Database.createView(layer.getViewName(), v.getSQL());
      Database.createView(layer2.getViewName(), v2.getSQL());   
      
      // just for demo... remove all geoserverfacade code before commit
      GeoserverFacade.publishWorkspace();
      GeoserverFacade.publishStore();
      GeoserverFacade.publishLayer(layer.getViewName(), "polygon");
      GeoserverFacade.publishLayer(layer2.getViewName(), "demo");

      String json = map.getMapJSON();
      JSONObject mapJsonObj = new JSONObject(json);
      
      System.out.println(json);

      Assert.assertEquals(map.getAllHasLayer().getAll().size(), mapJsonObj.getJSONArray("layers")
          .length());
      Assert.assertEquals(mapJsonObj.getString("mapName"), "Test Map");

      DashboardLayer[] fetched = map.getOrderedLayers();
      
      System.out.println(fetched.length);
      
//      if (GEOSERVER_RUNNING)
//      {
////        Assert.fail("Not implemented.");
//      }
    }
    catch(JSONException ex)
    {
      log.error(name.getMethodName(), ex);
      Assert.fail(ex.getLocalizedMessage());
    }
    finally
    {
      map.delete();
    }
  }
  
  @Transaction
  private DashboardMap testNoLayerException_trans()
  {
    DashboardMap map = new DashboardMap();
    map.setName("broken test");
    map.apply();
    
    return map;
  }
}
