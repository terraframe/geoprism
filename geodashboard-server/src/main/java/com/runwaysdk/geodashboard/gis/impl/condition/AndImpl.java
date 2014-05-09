package com.runwaysdk.geodashboard.gis.impl.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.And;

public class AndImpl extends CompositeImpl implements And
{
  public AndImpl()
  {
    super();
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this); 
    
    this.getLeftCondition().accepts(visitor);
    
    this.getRightCondition().accepts(visitor);
  }
}
