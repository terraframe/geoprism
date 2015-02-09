package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.dataaccess.transaction.AbortIfProblem;
import com.runwaysdk.geodashboard.gis.model.condition.Composite;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;

public abstract class DashboardComposite extends DashboardCompositeBase implements
    com.runwaysdk.generation.loader.Reloadable, Composite
{
  private static final long serialVersionUID = -287417231;

  public DashboardComposite()
  {
    super();
  }
  
  @Override
  public void restrictQuery(QueryFactory factory, ValueQuery query, Attribute attr) {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public void apply()
  {
    // a new instance will have the root and parent set to itself
    boolean isNew = this.isNew();

    super.apply();
    
    if(isNew || isModified(LEFTCONDITION) || isModified(RIGHTCONDITION))
    {
      setConditionReferences();
    }
  }
  
  /**
   * Sets the back-references to the conditions.
   */
  @AbortIfProblem
  private void setConditionReferences()
  {
    DashboardCondition root = this.getRootCondition();
    
    DashboardCondition left = this.getLeftCondition();
    left.appLock();
    left.setParentCondition(this);
    
    if(root != null)
    {
      left.setRootCondition(root);
    }
    else
    {
      left.setRootCondition(this);
    }
    
    left.apply();

    DashboardCondition right = this.getRightCondition();
    right.appLock();
    right.setParentCondition(this);
    
    if(root != null)
    {
      right.setRootCondition(root);
    }
    else
    {
      right.setRootCondition(this);
    }
    
    right.apply();
  }

  @Override
  public void delete()
  {
    super.delete();

    DashboardCondition left = this.getLeftCondition();
    DashboardCondition right = this.getRightCondition();

    left.delete();
    right.delete();
  }
}
