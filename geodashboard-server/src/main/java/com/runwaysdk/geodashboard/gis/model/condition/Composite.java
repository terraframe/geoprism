package com.runwaysdk.geodashboard.gis.model.condition;


public interface Composite extends Condition
{
  public Condition getLeftCondition();
  
  public void setLeftCondition(Condition condition);
  
  public Condition getRightCondition();

  public void setRightCondition(Condition condition);
}
