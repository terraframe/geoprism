package com.runwaysdk.geodashboard;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.persist.DashboardMap;

public class DashboardMapContextListener implements Reloadable, ServerContextListener
{

  @Override
  public void startup()
  {
    DashboardMap.cleanup();
  }

  @Override
  public void shutdown()
  {
    // DO NOTHING
  }

}
