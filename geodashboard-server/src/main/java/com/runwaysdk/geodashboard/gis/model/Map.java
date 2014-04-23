package com.runwaysdk.geodashboard.gis.model;

import java.util.List;

public interface Map extends Component
{

  public List<? extends Layer> getLayers();
  
}
