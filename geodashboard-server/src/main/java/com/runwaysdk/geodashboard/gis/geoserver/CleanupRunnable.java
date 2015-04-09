package com.runwaysdk.geodashboard.gis.geoserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.generation.loader.Reloadable;

public class CleanupRunnable implements Runnable, Reloadable
{
  private static final Log log = LogFactory.getLog(CleanupRunnable.class);

  @Override
  public void run()
  {
    try
    {
      LayerFacade.cleanupUnusedLayers();
    }
    catch (Exception e)
    {
      log.error(e.getMessage(), e);

      e.printStackTrace();
    }
  }

}
