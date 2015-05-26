package com.runwaysdk.geodashboard.gis.model;

import java.util.List;


public interface Layer extends Component
{
  public String getName();
  
  public Boolean getDisplayInLegend();
  
  public List<? extends Style> getStyles();
  
  public Boolean getVirtual();
  
  public FeatureType getFeatureType();
  
  public FeatureStrategy getFeatureStrategy();
}
