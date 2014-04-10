package com.runwaysdk.geodashboard.gis.model.impl;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.IsNull;

public class IsNullImpl extends PrimitiveImpl implements IsNull
{
  public IsNullImpl()
  {
    super();
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);    
  }
}
