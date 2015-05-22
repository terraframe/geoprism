package com.runwaysdk.geodashboard.context;

import com.runwaysdk.generation.loader.Reloadable;

public interface ServerContextListener extends Reloadable
{
  public void startup();

  public void shutdown();
}
