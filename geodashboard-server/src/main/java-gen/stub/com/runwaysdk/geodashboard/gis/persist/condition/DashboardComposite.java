package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.geodashboard.gis.model.condition.Composite;

public abstract class DashboardComposite extends DashboardCompositeBase implements com.runwaysdk.generation.loader.Reloadable, Composite
{
  private static final long serialVersionUID = -287417231;
  
  public DashboardComposite()
  {
    super();
  }
  
  @Override
  public void delete()
  {
    DashboardCondition left = this.getLeftCondition();
    DashboardCondition right = this.getRightCondition();
    
    super.delete();
    
    left.delete();
    right.delete();
  }
}
