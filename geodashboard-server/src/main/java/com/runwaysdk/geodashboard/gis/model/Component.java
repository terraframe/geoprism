package com.runwaysdk.geodashboard.gis.model;

public interface Component
{
  public void accepts(MapVisitor visitor);
  
  public String getId();
  
  public void setName(String name);
  
  public String getName();
}
