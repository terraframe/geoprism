package com.runwaysdk.geodashboard.gis.impl.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.GreaterThanOrEqual;

public class GreaterThanOrImpl extends PrimitiveImpl implements GreaterThanOrEqual
{
  public GreaterThanOrImpl()
  {
    super();
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);    
  }
}
