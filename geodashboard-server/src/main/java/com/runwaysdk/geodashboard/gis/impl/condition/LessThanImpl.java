package com.runwaysdk.geodashboard.gis.impl.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.LessThan;

public class LessThanImpl extends PrimitiveImpl implements LessThan
{
  public LessThanImpl()
  {
    super();
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
    
  }
}
