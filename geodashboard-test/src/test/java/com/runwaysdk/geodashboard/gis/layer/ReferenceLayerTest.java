package com.runwaysdk.geodashboard.gis.layer;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.MdAttributeDateUtil;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.AttributeWrapper;
import com.runwaysdk.geodashboard.Dashboard;
import com.runwaysdk.geodashboard.DashboardAttributes;
import com.runwaysdk.geodashboard.DashboardMetadata;
import com.runwaysdk.geodashboard.MetadataWrapper;
import com.runwaysdk.geodashboard.gis.GISImportLoggerIF;
import com.runwaysdk.geodashboard.gis.MockLogger;
import com.runwaysdk.geodashboard.gis.persist.AllLayerType;
import com.runwaysdk.geodashboard.gis.persist.DashboardMap;
import com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayer;
import com.runwaysdk.geodashboard.gis.persist.DashboardStyle;
import com.runwaysdk.geodashboard.gis.persist.HasLayer;
import com.runwaysdk.geodashboard.gis.persist.HasStyle;
import com.runwaysdk.geodashboard.gis.shapefile.ShapeFileImporter;
import com.runwaysdk.gis.StrategyInitializer;
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
import com.runwaysdk.system.metadata.MdView;
import com.runwaysdk.util.FileIO;

public class ReferenceLayerTest
{
  protected static final String         TEST_PACKAGE   = "com.test.geodashboard";

  protected static final String         TYPE_NAME      = "StateInfo5";

  protected static final String         VIEW_NAME      = "StateInfoView5";

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

  protected static MdAttributeDate      date;

  protected static MdAttributeDouble    ratio;

  protected static MdAttributeReference geoentityRef;

  protected static Dashboard            dashboard;

  private static boolean                keepData       = true;
  

  @BeforeClass
  @Request
  public static void classStartup()
  {
    metadataSetup();
    dataSetup();
    
    System.out.println("Metadata and data is setup");
  }

  @AfterClass
  @Request
  public static void classTeardown()
  {
    System.out.println("Starting teardown process...");
    
    metadataTeardown();
    StrategyInitializer.shutDown();
  }
  
  @Transaction
  private static void metadataSetup()
  {
    System.out.println("Starting metadata setup");
    
    StrategyInitializer.startUp();
    try
    {
      stateInfo = new MdBusiness();
      stateInfo.setTypeName(TYPE_NAME);
      stateInfo.setPackageName(TEST_PACKAGE);
      stateInfo.getDisplayLabel().setDefaultValue("State Information");
      stateInfo.apply();
      stateInfoId = stateInfo.getId();
  
      rank = new MdAttributeInteger();
      rank.setDefiningMdClass(stateInfo);
      rank.setAttributeName("rank5");
      rank.getDisplayLabel().setDefaultValue("Alphabetic Rank");
      rank.apply();
  
      date = new MdAttributeDate();
      date.setDefiningMdClass(stateInfo);
      date.setAttributeName("studyDate5");
      date.getDisplayLabel().setDefaultValue("Study Date");
      date.apply();
  
      // ratio = rank/total (linear relationship...higher rank =
      ratio = new MdAttributeDouble();
      ratio.setDefiningMdClass(stateInfo);
      ratio.setAttributeName("ratio5");
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
      country.getDisplayLabel().setDefaultValue("Country5");
      country.getDescription().setDefaultValue("Country5");
      country.setUniversalId("country5");
      country.apply();
  
      country.addLink(Universal.getRoot(), AllowedIn.CLASS);
  
      state = new Universal();
      state.getDisplayLabel().setDefaultValue("State5");
      state.getDescription().setDefaultValue("State5");
      state.setUniversalId("state5");
      state.apply();
  
      state.addLink(country, AllowedIn.CLASS).apply();
  
      usa = new GeoEntity();
      usa.getDisplayLabel().setDefaultValue("USA");
      usa.setGeoId("USA00005");
      usa.setUniversal(country);
    }
    catch(Exception e)
    {
      throw new RuntimeException(e);
    }

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
    virtualRank.setAttributeName("viewRank5");
    virtualRank.setMdAttributeConcrete(rank);
    virtualRank.setDefiningMdView(stateInfoView);
    virtualRank.getDisplayLabel().setDefaultValue("Education Rank");
    virtualRank.apply();

    MdAttributeVirtual virtualRatio = new MdAttributeVirtual();
    virtualRatio.setAttributeName("viewRatio5");
    virtualRatio.setMdAttributeConcrete(ratio);
    virtualRatio.setDefiningMdView(stateInfoView);
    virtualRatio.getDisplayLabel().setDefaultValue("Crime Rate");
    virtualRatio.apply();

    MdAttributeVirtual virtualDate = new MdAttributeVirtual();
    virtualDate.setAttributeName("studyDate5");
    virtualDate.setMdAttributeConcrete(date);
    virtualDate.setDefiningMdView(stateInfoView);
    virtualDate.getDisplayLabel().setDefaultValue("Study Date");
    virtualDate.apply();

    dashboard = new Dashboard();
    dashboard.getDisplayLabel().setDefaultValue("Test Dashboard5");
    dashboard.setCountry(usa);
    dashboard.apply();

    MetadataWrapper mWrapper = new MetadataWrapper();
    mWrapper.setWrappedMdClass(stateInfoView);
    mWrapper.setUniversal(state);
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
          Double ratioD = rankI / total;

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
  
  
  @Transaction
  private static void metadataTeardown()
  {
    try
    {
      DashboardMap map = dashboard.getMap();
      
      if(map != null)
      {
        map.delete();
      }
      
      OIterator<? extends MetadataWrapper> dMeta = dashboard.getAllMetadata();
      try
      {
        while(dMeta.hasNext())
        {
          MetadataWrapper meta = dMeta.next();
          meta.delete();
        }
      }
      finally
      {
        dMeta.close();
      }
      
//      stateInfoView.delete();
      
      Dashboard.get(dashboard.getId()).delete();
      MdBusiness.get(stateInfo.getId()).delete();
      Universal.get(country.getId()).delete();
      Universal.get(state.getId()).delete();
      GeoEntity.get(usa.getId()).delete();
      
      System.out.println("Metadata and data has been removed");
    }
    catch (Throwable t)
    {
      throw new RuntimeException(t);
    }
  }

  @Test
  @Request
  public void viewQueryTest()
  {
    DashboardMap map = null;
    DashboardReferenceLayer rl = null;
    try
    {
      
      map = dashboard.getMap();
    
      rl = new DashboardReferenceLayer();
      rl.setName("testLayer");
      rl.setUniversal( state );
//      rl.setGeoEntity(geoentityRef);
      rl.addLayerType(AllLayerType.BASIC);
      rl.setDashboardMap(map);
      rl.apply();
      
      DashboardStyle style = new DashboardStyle();
      style.apply();
      
      HasStyle hasStyle = rl.addHasStyle(style);
      hasStyle.apply();
  
      HasLayer hasLayer = map.addHasLayer(rl);
      hasLayer.setLayerIndex(0);
      hasLayer.apply();
    
      ValueQuery query = rl.getViewQuery();
      
      long resultCount = query.getCount();
      
      // There are 51 states in the USA so the result should be 51.
      // If adding filter conditions this may change and should be adjusted accordingly
      assertEquals(51, resultCount);
    }
    catch (Exception e) 
    {
      throw new RuntimeException(e);
    }
    finally
    {
      if(rl != null)
      {
        rl.delete();
      }
    }
  }


//  @Test
//  @Request
//  public void testPopulator()
//  {
//    for (int i = 0; i < 100; i++)
//    {
//      BasicDashboard basic = new BasicDashboard();
//      basic.setTestChar("Test basic character : " + ( i % 2 ));
//      basic.setTestInteger(i);
//      basic.apply();
//
//      AdvancedDashboard advanced = new AdvancedDashboard();
//      advanced.setAdvancedChar("Test advanced character : " + i);
//      advanced.apply();
//    }
//  }
  
}
