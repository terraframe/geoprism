package com.runwaysdk.geodashboard.gis.model.condition;


public interface Composite extends Condition
{
  public Condition getLeftCondition();
  
  public Condition getRightCondition();
}
