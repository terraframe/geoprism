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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class CleanupThread extends Thread
{
  /**
   * Delay in milliseconds. Set to 5 minutes
   */
  private static final int DELAY = 1000 * 60 * 5;

  private static final Logger logger   = LoggerFactory.getLogger(CleanupThread.class);

  public CleanupThread()
  {
    
  }

  @Override
  public void run()
  {
    while (!Thread.interrupted())
    {
      try
      {
        Thread.sleep(DELAY);
      }
      catch (InterruptedException e)
      {
        return;
      }

      try
      {
        CleanupFacade.cleanupUnusedLayers();
        CleanupFacade.cleanupUnusedFiles();
      }
      catch (Exception e)
      {
        logger.error("Cleanup runnable encountered an error.", e);
      }
    }
  }

}
