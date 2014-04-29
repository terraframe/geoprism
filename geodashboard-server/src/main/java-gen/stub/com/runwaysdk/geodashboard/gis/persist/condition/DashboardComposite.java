package com.runwaysdk.geodashboard.gis.persist.condition;

public abstract class DashboardComposite extends DashboardCompositeBase implements com.runwaysdk.generation.loader.Reloadable
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
