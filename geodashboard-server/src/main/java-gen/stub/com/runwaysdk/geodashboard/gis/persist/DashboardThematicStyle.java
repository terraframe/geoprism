package com.runwaysdk.geodashboard.gis.persist;

import com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;

public class DashboardThematicStyle extends DashboardThematicStyleBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1178596850;
  
  public DashboardThematicStyle()
  {
    super();
  }
  
  @Override
  public void delete()
  {
    DashboardCondition cond = this.getStyleCondition();
    
    super.delete();
    
    cond.delete();
  }
  
}
