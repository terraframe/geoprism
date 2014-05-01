package com.runwaysdk.geodashboard.gis.impl.condition;

import com.runwaysdk.geodashboard.gis.impl.ComponentImpl;
import com.runwaysdk.geodashboard.gis.model.ThematicStyle;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;

public abstract class ConditionImpl extends ComponentImpl implements Condition
{
  private ThematicStyle thematicStyle;
  
  private Condition rootCondition;
  
  private Condition parentCondition;
  
  public ConditionImpl()
  {
    super();
    this.thematicStyle = null;
    this.parentCondition = null;
    this.rootCondition = null;
  }
  
  @Override
  public Condition getParentCondition()
  {
    return parentCondition;
  }
  
  @Override
  public Condition getRootCondition()
  {
    return this.rootCondition;
  }
  
  public void setParentCondition(Condition parentCondition)
  {
    this.parentCondition = parentCondition;
  }
  
  public void setThematicStyle(ThematicStyle thematicStyle)
  {
    this.thematicStyle = thematicStyle;
  }
  
  @Override
  public ThematicStyle getThematicStyle()
  {
    return this.thematicStyle;
  }

}
