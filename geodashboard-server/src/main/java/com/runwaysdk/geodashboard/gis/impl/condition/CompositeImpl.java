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
  
  @Override
  public void setRightCondition(Condition condition)
  {
    condition.setParentCondition(this);
    this.rightCondition = condition;    
    this.rightCondition.setThematicStyle(this.getThematicStyle());    
  }
  
  @Override
  public void setLeftCondition(Condition condition)
  {
    condition.setParentCondition(this);
    this.leftCondition = condition;    
    this.leftCondition.setThematicStyle(this.getThematicStyle());    
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
