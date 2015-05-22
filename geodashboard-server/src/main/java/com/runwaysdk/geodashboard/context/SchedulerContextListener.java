package com.runwaysdk.geodashboard.context;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.system.scheduler.SchedulerManager;

public class SchedulerContextListener implements ServerContextListener, Reloadable
{

  @Override
  public void startup()
  {
    SchedulerManager.start();
  }

  @Override
  public void shutdown()
  {
    SchedulerManager.shutdown();
  }

}
