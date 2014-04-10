package com.runwaysdk.geodashboard.gis.model;

import com.runwaysdk.geodashboard.gis.model.condition.Condition;

public interface ThematicStyle extends Style
{
  public void setAttribute(String attribute);
  
  public String getAttribute();
  
  public void setCondition(Condition condition);
  
  public Condition getCondition();
}
