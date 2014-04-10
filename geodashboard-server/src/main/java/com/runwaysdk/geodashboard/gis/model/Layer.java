package com.runwaysdk.geodashboard.gis.model;

import java.util.List;


public interface Layer extends Component
{
  public String getName();
  
  public void addStyle(Style style);
  
  public List<Style> getStyles();
  
  public Boolean getVirtual();
  
  public void setVirtual(Boolean virtual);
  
  public void setFeatureType(FeatureType featureType);
  
  public FeatureType getFeatureType();
}
