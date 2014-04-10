package com.runwaysdk.geodashboard.gis;

public interface GISImportLoggerIF
{
  public void log(String featureId, Throwable t);

  public void log(String featureId, String message);

  public boolean hasLogged();

  public void close();
}
