package com.runwaysdk.geodashboard;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverInitializer;
import com.runwaysdk.gis.StrategyInitializer;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.scheduler.SchedulerManager;

public class ServerInitializer implements Reloadable
{

  private static final Log log = LogFactory.getLog(ServerInitializer.class);

  @Request
  public static void initialize()
  {
    try
    {
      StrategyInitializer.startUp();
      log.debug("COMLPETE: StrategyInitializer.startUp()");
      
      SchedulerManager.start();
      log.debug("COMLPETE: SchedulerManager.start();");

      SessionEntry.deleteAll();
      log.debug("COMLPETE: SessionEntry.deleteAll();");

      GeoserverInitializer.setup();
      log.debug("COMLPETE: GeoserverInitializer.setup();");
    }
    catch (Throwable t)
    {
      log.fatal("Could not initialize server.", t);
    }
  }

  @Request
  public static void destroy()
  {
    int tasks = 0;
    try
    {
      StrategyInitializer.tearDown();
      
      tasks++;
      SchedulerManager.shutdown();
      
      tasks++;
      SessionEntry.deleteAll();
    }
    catch (Throwable t)
    {
      log.fatal("Cannot start up. Failed on task [" + tasks + "].", t);
    }
  }
}
