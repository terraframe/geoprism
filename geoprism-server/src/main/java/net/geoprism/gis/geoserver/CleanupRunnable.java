/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.gis.geoserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class CleanupRunnable implements Runnable
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
        CleanupFacade.cleanupUnusedLayers();
        CleanupFacade.cleanupUnusedFiles();
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
