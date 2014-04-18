package com.runwaysdk.geodashboard.gis.model;

import java.util.List;


public interface Layer extends Component
{
  public String getName();
  
  public Boolean displayInLegend();
  
  public void setDisplayInLegend(Boolean display);
  
  public void addStyle(Style style);
  
  public List<Style> getStyles();
  
  public Boolean getVirtual();
  
  public void setVirtual(Boolean virtual);
  
  public void setFeatureType(FeatureType featureType);
  
  public FeatureType getFeatureType();
}
