package com.runwaysdk.geodashboard;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.generation.loader.LoaderDecorator;

public class SessionEntryManager
{

  private static final Log log = LogFactory.getLog(SessionEntryManager.class);
  
  /**
   * Initializes the SessionEntry objects and whatever those components require.
   */
//  @Request
  public static void initialize()
  {
    // use reflection to avoid Reloadable being infectious
    try
    {
      LoaderDecorator.load("com.runwaysdk.geodashboard.SessionEntry").getMethod("deleteAll").invoke(null);
    }
    catch(Throwable t)
    {
      log.error("Unable to call SessionEntry.initialize()", t);
      throw new RuntimeException(t);
    }
  }
  
  /**
   * Destroy the SessionEntry objects and whatever those components require.
   */
//  @Request
  public static void destroy()
  {
    // use reflection to avoid Reloadable being infectious
    try
    {
      LoaderDecorator.load("com.runwaysdk.geodashboard.SessionEntry").getMethod("deleteAll").invoke(null);
    }
    catch(Throwable t)
    {
      log.error("Unable to call SessionEntry.destroy()", t);
      throw new RuntimeException(t);
    }
  }
  
}
