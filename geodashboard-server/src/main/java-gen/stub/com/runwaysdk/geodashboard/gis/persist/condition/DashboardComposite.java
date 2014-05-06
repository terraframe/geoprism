package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.dataaccess.transaction.SkipIfProblem;
import com.runwaysdk.geodashboard.gis.model.condition.Composite;

public abstract class DashboardComposite extends DashboardCompositeBase implements
    com.runwaysdk.generation.loader.Reloadable, Composite
{
  private static final long serialVersionUID = -287417231;

  public DashboardComposite()
  {
    super();
  }

  @Override
  public void apply()
  {
    super.apply();
    
    //setConditionReferences();
  }
  
  /**
   * Sets the back-references to the conditions.
   */
  @SkipIfProblem
  private void setConditionReferences()
  {
    DashboardCondition left = this.getLeftCondition();
    left.appLock();
    left.setParentCondition(this);
    left.setRootCondition(this.getRootCondition());
    left.apply();

    DashboardCondition right = this.getLeftCondition();
    right.appLock();
    right.setParentCondition(this);
    right.setRootCondition(this.getRootCondition());
    right.apply();
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
