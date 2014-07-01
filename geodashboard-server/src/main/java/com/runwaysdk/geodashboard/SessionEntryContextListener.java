package com.runwaysdk.geodashboard;

import com.runwaysdk.generation.loader.Reloadable;

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
