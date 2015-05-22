package com.runwaysdk.geodashboard.context;

import com.runwaysdk.facade.RemoteAdapterServer;
import com.runwaysdk.generation.loader.Reloadable;

public class RMIContextListener implements Reloadable, ServerContextListener
{
  @Override
  public void startup()
  {
    RemoteAdapterServer.startServer();
  }

  @Override
  public void shutdown()
  {
    RemoteAdapterServer.stopServer();
  }
}
