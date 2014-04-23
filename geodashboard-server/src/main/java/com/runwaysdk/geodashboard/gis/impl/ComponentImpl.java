package com.runwaysdk.geodashboard.gis.impl;

import com.runwaysdk.geodashboard.gis.model.Component;

public abstract class ComponentImpl implements Component
{
  private String name;
  
  public ComponentImpl()
  {
    super();
    this.name = this.getId(); // default id
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  @Override
  public String getName()
  {
    return this.name;
  }
  
  @Override
  public String getId()
  {
    return String.valueOf(this.hashCode());
  }
}
