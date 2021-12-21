/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.gis.geoserver;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.session.Request;

import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import net.geoprism.PluginUtil;

/**
 * Maintains an initialization thread, which exits upon initial server setup, and a general cleanup thread
 * which runs indefinitely.
 * 
 * @author rrowlands
 */
public class GeoserverInitializer implements UncaughtExceptionHandler
{
  private static boolean               initialized = false;

  private static final ReentrantLock   lock        = new ReentrantLock();

  private static final Logger          initLogger     = LoggerFactory.getLogger(GeoserverInitializer.class);

  private static CleanupThread cleanupThread;

  private static InitializerThread initializerThread;

  public static class InitializerThread extends Thread
  {

    private static final Logger logger = LoggerFactory.getLogger(InitializerThread.class);

    public InitializerThread()
    {
      super();
    }

    @Override
    public void run()
    {
      GeoServerRESTReader reader = GeoserverProperties.getReader();

      while (!Thread.interrupted())
      {
        try
        {
          lock.lock();

          logger.debug("Attempting to check existence of geoserver");

          if (reader.existGeoserver())
          {
            logger.debug("Geoserver available.");

            runInRequest();

            initialized = true;
            logger.debug("Geoserver initialized.");
            return; // we are done here
          }
          else
          {
            try
            {
              logger.debug("Waiting for Geoserver to start.");
              Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
              return;
            }
          }
        }
        catch (Throwable t)
        {
          // we couldn't hit the application correctly, so log the error
          // and quit the loop to avoid excessive logging
          logger.error("Unable to start the application.", t);
          return;
        }
        finally
        {
          lock.unlock();
        }
      }
    }
    
    @Request
    private void runInRequest()
    {
      logger.info("Deleting ALL geoserver data and republshing workspace and store.");
      
      // To prevent a problem if the database connection information of the
      // datastore ever changes we must delete and recreate the store and workspace.
      if (GeoserverFacade.workspaceExists())
      {
        GeoserverFacade.removeWorkspace();
        GeoserverFacade.removeStore();
      }

      GeoserverFacade.publishWorkspace();
      GeoserverFacade.publishStore();
      
      Collection<GeoserverInitializerIF> initializers = PluginUtil.getGeoserverInitializers();
      for (GeoserverInitializerIF initializer : initializers)
      {
        initializer.initialize();
      }
    }

  }

  public static boolean isInitialized()
  {
    try
    {
      lock.lock();

      return initialized;
    }
    finally
    {
      lock.unlock();
    }
  }

  public static void startup()
  {
    GeoserverInitializer init = new GeoserverInitializer();

    try
    {
      initLogger.debug("Attempting to initialize context.");

      // create another thread to avoid blocking the one starting the webapps.
      initializerThread = new InitializerThread();
      initializerThread.setUncaughtExceptionHandler(init);
      initializerThread.setDaemon(true);
      initializerThread.start();

      initLogger.debug("Context initialized...[" + GeoserverInitializer.class + "] started.");
    }
    catch (Throwable t)
    {
      initLogger.error("Could not initialize context.", t);
    }

    // Start the mapping database view cleanup thread
    cleanupThread = new CleanupThread();
    cleanupThread.setUncaughtExceptionHandler(init);
    cleanupThread.setDaemon(true);
    cleanupThread.start();
  }

  /**
   * Log the error.
   */
  @Override
  public void uncaughtException(Thread t, Throwable e)
  {
    initLogger.error("Exception occurred in thread [" + t.getName() + "].", e);
  }

  public static void shutdown()
  {
    if (initializerThread != null)
    {
      initializerThread.interrupt();
    }
    
    if (cleanupThread != null)
    {
      cleanupThread.interrupt();
    }
  }

}