package com.runwaysdk.geodashboard.gis.model;

import java.util.List;

public interface Map extends Component
{
  public void addLayer(Layer layer);
  
  public List<Layer> getLayers();
  
}
