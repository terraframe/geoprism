package com.runwaysdk.geodashboard.gis.impl.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.Or;

public class OrImpl extends CompositeImpl implements Or
{
  public OrImpl()
  {
    // TODO Auto-generated constructor stub
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this); 
    
    this.getLeftCondition().accepts(visitor);
    
    this.getRightCondition().accepts(visitor);
  }
}
