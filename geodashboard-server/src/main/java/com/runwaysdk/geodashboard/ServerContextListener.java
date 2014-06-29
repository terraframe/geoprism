package com.runwaysdk.geodashboard;

import com.runwaysdk.generation.loader.Reloadable;

public interface ServerContextListener extends Reloadable
{
  public void startup();

  public void shutdown();
}
