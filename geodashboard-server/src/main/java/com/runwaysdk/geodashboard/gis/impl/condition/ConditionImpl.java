package com.runwaysdk.geodashboard.gis.impl.condition;

import com.runwaysdk.geodashboard.gis.impl.ComponentImpl;
import com.runwaysdk.geodashboard.gis.model.ThematicLayer;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;

public abstract class ConditionImpl extends ComponentImpl implements Condition
{
  private ThematicLayer thematicLayer;

  public ConditionImpl()
  {
    super();
  }

  public void setThematicLayer(ThematicLayer thematicLayer)
  {
    this.thematicLayer = thematicLayer;
  }

  @Override
  public ThematicLayer getThematicLayer()
  {
    return this.thematicLayer;
  }

}
