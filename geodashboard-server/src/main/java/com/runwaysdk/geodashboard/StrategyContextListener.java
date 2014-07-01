package com.runwaysdk.geodashboard;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.gis.StrategyInitializer;

public class StrategyContextListener implements ServerContextListener, Reloadable
{
  @Override
  public void startup()
  {
    StrategyInitializer.startUp();
  }

  @Override
  public void shutdown()
  {
    StrategyInitializer.shutDown();
  }
}
