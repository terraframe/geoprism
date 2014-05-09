package com.runwaysdk.geodashboard.gis.impl.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.GreaterThan;

public class GreaterThanImpl extends PrimitiveImpl implements GreaterThan
{
  
  public GreaterThanImpl()
  {
    super();
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
    
  }

}
