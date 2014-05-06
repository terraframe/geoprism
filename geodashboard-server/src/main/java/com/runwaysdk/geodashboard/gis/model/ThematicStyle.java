package com.runwaysdk.geodashboard.gis.model;

import com.runwaysdk.geodashboard.gis.model.condition.Condition;


public interface ThematicStyle extends Style
{
  public String getAttribute();
  
  public Condition getCondition();
}
