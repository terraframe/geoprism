package com.runwaysdk.geodashboard;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.gis.StrategyInitializer;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.scheduler.SchedulerManager;

public class ServerInitializer implements Reloadable
{
  @Request
  public static void initialize()
  {
    StrategyInitializer.startUp();
    SchedulerManager.start();
    SessionEntry.deleteAll();
  }

  @Request
  public static void destroy()
  {
    StrategyInitializer.tearDown();
    SchedulerManager.shutdown();
    SessionEntry.deleteAll();
  }
}
