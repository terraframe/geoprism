package com.runwaysdk.geodashboard.gis.geoserver;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.AttributeWrapper;
import com.runwaysdk.geodashboard.Dashboard;
import com.runwaysdk.geodashboard.DashboardAttributes;
import com.runwaysdk.geodashboard.DashboardMetadata;
import com.runwaysdk.geodashboard.MetadataWrapper;
import com.runwaysdk.geodashboard.gis.GISImportLoggerIF;
import com.runwaysdk.geodashboard.gis.MockLogger;
import com.runwaysdk.geodashboard.gis.shapefile.ShapeFileImporter;
import com.runwaysdk.gis.StrategyInitializer;
import com.runwaysdk.logging.LogLevel;
import com.runwaysdk.logging.RunwayLogUtil;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdAttributeDouble;
import com.runwaysdk.system.metadata.MdAttributeInteger;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.system.metadata.MdAttributeVirtual;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdView;
import com.runwaysdk.util.FileIO;

/**
 * Simple class to be run as a way to load test data into the system.
 * 
 * @author justin
 * 
 */
public class GeoserverTestSetup
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

  protected static MdAttributeDouble    ratio;

  protected static MdAttributeReference geoentityRef;

  protected static final Log            log            = LogFactory.getLog(GeoserverTest.class);

  static
  {
    if (consoleDebug)
    {
      BasicConfigurator.configure();
      RunwayLogUtil.convertLogLevelToLevel(LogLevel.ERROR);
    }
  }

  @Request
  public static void main(String[] args) throws Throwable
  {
    try
    {
      StrategyInitializer.startUp();
      
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

    Dashboard dashboard = new Dashboard();
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
  }

  @Transaction
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

          Integer rankI = ++count; // 1-based starting index
          Double ratioD = rankI / total;

          Business si = BusinessFacade.newBusiness(STATE_INFO);
          si.setValue(rank.getAttributeName(), rankI.toString());
          si.setValue(ratio.getAttributeName(), ratioD.toString());
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
}
