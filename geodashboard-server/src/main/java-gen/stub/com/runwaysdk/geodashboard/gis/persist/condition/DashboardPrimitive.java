package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.geodashboard.gis.model.condition.Primitive;

public abstract class DashboardPrimitive extends DashboardPrimitiveBase implements com.runwaysdk.generation.loader.Reloadable, Primitive
{
  private static final long serialVersionUID = -1224425442;
  
  public DashboardPrimitive()
  {
    super();
  }
  
  @Override
  public Object getValue()
  {
    return this.getComparisonValue();
  }
  
  @Override
  public DashboardComposite getParentCondition()
  {
    return this.getParentCondition();
  }
  
  @Override
  public DashboardComposite getRootCondition()
  {
    return this.getRootCondition();
  }
}
