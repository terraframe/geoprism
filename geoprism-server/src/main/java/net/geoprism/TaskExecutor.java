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
package net.geoprism;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Wrapper around an ExecutorService to provide a means to perform non-time critical tasks. These tasks include deletion
 * of database views for layers that no longer exist, server side generation of thumbnail images, etc...
 * 
 * @author jsmethie
 */
public class TaskExecutor
{
  /**
   * Number of threads to use for task execution
   */
  private static final int       NUM_THREADS = 2;

  private static ExecutorService executor;

  static
  {
    executor = Executors.newFixedThreadPool(NUM_THREADS);
  }

  public static void addTask(Runnable task)
  {
    executor.submit(task);
  }

  public static void shutdown()
  {
    try
    {
      executor.shutdown();
      executor.awaitTermination(5, TimeUnit.MINUTES);
    }
    catch (InterruptedException e)
    {

      // Task Interrupted
    }
    finally
    {
      executor.shutdownNow();
    }
  }
}
