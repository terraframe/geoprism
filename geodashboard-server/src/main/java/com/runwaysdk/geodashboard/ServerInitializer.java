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
    int tasks = 0;
    try
    {
      StrategyInitializer.startUp();

      tasks++;
      SchedulerManager.start();

      tasks++;
      SessionEntry.deleteAll();

      tasks++;
      GeoserverInitializer.setup();
    }
    catch (Throwable t)
    {
      log.fatal("Cannot start up. Failed on task [" + tasks + "].", t);
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
