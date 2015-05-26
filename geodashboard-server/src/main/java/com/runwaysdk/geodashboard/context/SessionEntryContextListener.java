package com.runwaysdk.geodashboard.context;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.SessionEntry;

public class SessionEntryContextListener implements Reloadable, ServerContextListener
{

  @Override
  public void startup()
  {
    SessionEntry.deleteAll();
  }

  @Override
  public void shutdown()
  {
    SessionEntry.deleteAll();
  }

}
