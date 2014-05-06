package com.runwaysdk.geodashboard.gis.impl.condition;

import com.runwaysdk.geodashboard.gis.model.condition.Composite;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;

public abstract class CompositeImpl extends ConditionImpl implements Composite
{
  private Condition leftCondition;
  
  private Condition rightCondition;
  
  public CompositeImpl()
  {
    super();
  }
  
  public void setRightCondition(Condition condition)
  {
    this.rightCondition = condition;    
  }
  
  public void setLeftCondition(Condition condition)
  {
    this.leftCondition = condition;    
  }
  
  @Override
  public Condition getLeftCondition()
  {
    return leftCondition;
  }
  
  @Override
  public Condition getRightCondition()
  {
    return rightCondition;
  }
}
