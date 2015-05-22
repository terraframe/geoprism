package com.runwaysdk.geodashboard.context;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverInitializer;

public class GeoserverContextListener implements Reloadable, ServerContextListener
{

  @Override
  public void startup()
  {
    GeoserverInitializer.setup();
  }

  @Override
  public void shutdown()
  {
    GeoserverInitializer.shutdown();
  }

}
