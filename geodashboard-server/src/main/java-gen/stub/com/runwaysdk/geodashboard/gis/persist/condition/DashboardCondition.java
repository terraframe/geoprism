package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.geodashboard.gis.model.ThematicStyle;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;

public abstract class DashboardCondition extends DashboardConditionBase implements com.runwaysdk.generation.loader.Reloadable, Condition
{
  private static final long serialVersionUID = -1287604192;
  
  public DashboardCondition()
  {
    super();
  }
  
  @Override
  public String getName()
  {
    return this.getClass().getSimpleName();
  }
  
  @Override
  public DashboardCondition getParentCondition()
  {
    return this.getParentCondition();
  }

  @Override
  public DashboardCondition getRootCondition()
  {
    return this.getParentCondition();
  }
  
  @Override
  public ThematicStyle getThematicStyle()
  {
    return this.getStyleReference();
  }
}
