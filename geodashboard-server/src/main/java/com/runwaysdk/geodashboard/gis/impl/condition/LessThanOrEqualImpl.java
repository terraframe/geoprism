package com.runwaysdk.geodashboard.gis.impl.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.LessThanOrEqual;

public class LessThanOrEqualImpl extends PrimitiveImpl implements LessThanOrEqual
{
  public LessThanOrEqualImpl()
  {
    super();
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);    
  }
}
