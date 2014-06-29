package com.runwaysdk.geodashboard;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverInitializer;
import com.runwaysdk.geodashboard.gis.persist.DashboardMap;
import com.runwaysdk.gis.StrategyInitializer;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.scheduler.SchedulerManager;

public class ServerInitializer implements Reloadable
{

  private static final Log log = LogFactory.getLog(ServerInitializer.class);

  @Request
  public static void initialize()
  {
    StrategyInitializer.startUp();
    log.debug("COMLPETE: StrategyInitializer.startUp()");
    
    SchedulerManager.start();
    log.debug("COMLPETE: SchedulerManager.start();");

    SessionEntry.deleteAll();
    log.debug("COMLPETE: SessionEntry.deleteAll();");

    GeoserverInitializer.setup();
    log.debug("COMLPETE: GeoserverInitializer.setup();");
    
    DashboardMap.cleanup();
    log.debug("COMLPETE: DashboardMap.cleanup();");
  }

  @Request
  public static void destroy()
  {
    StrategyInitializer.shutDown();
    
    SchedulerManager.shutdown();
    
    SessionEntry.deleteAll();
  }
}
