package com.runwaysdk.geodashboard.gis.model.impl;

import com.runwaysdk.geodashboard.gis.model.Component;

public abstract class ComponentImpl implements Component
{
  public ComponentImpl()
  {
    super();
  }
  
  @Override
  public String getName()
  {
    return this.getId();
  }
  
  @Override
  public String getId()
  {
    return String.valueOf(this.hashCode());
  }
}
