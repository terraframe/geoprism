/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.report;

import java.io.File;
import java.util.Base64;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.util.FileIO;

public class CacheDocumentManager implements Runnable
{
  private static final Log                      log           = LogFactory.getLog(CacheDocumentManager.class);

  /**
   * Executer responsible for running the cleanup thread
   */
  private static final ScheduledExecutorService executor      = Executors.newSingleThreadScheduledExecutor();

  /**
   * Interval time in minutes
   */
  public static final long                      INTERVAL_TIME = 5;

  @Override
  public void run()
  {
    try
    {
      String[] directories = new String[] { BirtConstants.CACHE_DIR, BirtConstants.IMGS_DIR };

      for (String dir : directories)
      {
        File directory = new File(dir);

        File[] files = directory.listFiles();

        if (files != null)
        {
          for (File file : files)
          {
            String sessionId = new String(Base64.getDecoder().decode(file.getName()));

            if (!SessionFacade.containsSession(sessionId))
            {
              FileIO.deleteDirectory(file);
            }
          }
        }
      }
    }
    catch (Exception e)
    {
      log.error(e);
    }
  }

  public static void start()
  {
    executor.scheduleWithFixedDelay(new CacheDocumentManager(), 0, INTERVAL_TIME, TimeUnit.MINUTES);
  }

  public static void stop()
  {
    executor.shutdown();
  }
}
