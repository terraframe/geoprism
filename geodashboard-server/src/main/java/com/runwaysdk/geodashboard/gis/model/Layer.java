package com.runwaysdk.geodashboard.gis.model;

import java.util.List;
import java.util.HashMap;


public interface Layer extends Component
{
  public String getName();
  
  public Boolean getDisplayInLegend();
  
  public List<? extends Style> getStyles();
  
  public Boolean getVirtual();
  
  public FeatureType getFeatureType();
  
  public FeatureStrategy getFeatureStrategy();
  
  public HashMap<String, Double> getLayerMinMax(String attribute);

//  public void setDisplayInLegend(Boolean display);
//  public void setFeatureType(FeatureType featureType);
//  public void setVirtual(Boolean virtual);
//  public void addStyle(Style style);
}
