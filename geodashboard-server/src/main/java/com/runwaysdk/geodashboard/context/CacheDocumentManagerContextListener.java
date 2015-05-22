package com.runwaysdk.geodashboard.context;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.report.CacheDocumentManager;

public class CacheDocumentManagerContextListener implements Reloadable, ServerContextListener
{
  @Override
  public void startup()
  {
    CacheDocumentManager.start();
  }

  @Override
  public void shutdown()
  {
    CacheDocumentManager.stop();
  }

}
