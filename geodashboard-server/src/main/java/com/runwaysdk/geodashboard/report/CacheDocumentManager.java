package com.runwaysdk.geodashboard.report;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.axis.encoding.Base64;

import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.util.FileIO;

public class CacheDocumentManager implements Runnable
{

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
            String sessionId = new String(Base64.decode(file.getName()));

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
