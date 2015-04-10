package com.runwaysdk.geodashboard.gis.geoserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.generation.loader.Reloadable;

public class CleanupRunnable implements Runnable, Reloadable
{
  /**
   * Delay in milliseconds. Set to 5 minutes
   */
  private static final int DELAY = 1000 * 60 * 5;

  private static final Log log   = LogFactory.getLog(CleanupRunnable.class);

  private boolean          running;

  public CleanupRunnable()
  {
    this.running = true;
  }

  public synchronized boolean isRunning()
  {
    return running;
  }

  public synchronized void setRunning(boolean running)
  {
    this.running = running;
  }

  @Override
  public void run()
  {
    while (this.isRunning())
    {
      try
      {
        Thread.sleep(DELAY);
      }
      catch (InterruptedException e)
      {
      }

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

  public void shutdown()
  {
    this.setRunning(false);
  }

}
