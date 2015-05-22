package com.runwaysdk.geodashboard.context;

import com.runwaysdk.generation.loader.Reloadable;

public class ServerContextListenerInfo implements Reloadable
{
  private String className;

  public ServerContextListenerInfo(String className)
  {
    this.className = className;
  }

  public String getClassName()
  {
    return className;
  }
}
