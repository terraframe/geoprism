package com.runwaysdk.geodashboard.gis.model.builder;

import com.runwaysdk.geodashboard.gis.model.Map;
import com.runwaysdk.geodashboard.gis.model.impl.MapImpl;

public class MapBuilder
{
  private Map map;
  
  public MapBuilder(String name)
  {
    map = new MapImpl(name);
  }
  
  Map getMap()
  {
    return map;
  }
  
  public LayerBuilder layer(String name)
  {
    return new LayerBuilder(this, name);
  }
  
  public Map build()
  {
    return map;
  }
}
