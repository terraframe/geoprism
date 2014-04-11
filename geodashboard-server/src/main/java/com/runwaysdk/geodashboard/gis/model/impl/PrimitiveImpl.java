package com.runwaysdk.geodashboard.gis.model.impl;

import com.runwaysdk.geodashboard.gis.model.condition.Primitive;

public abstract class PrimitiveImpl extends ConditionImpl implements Primitive
{
  private Object value;
  
  public PrimitiveImpl()
  {
    super();
    
    this.value = null;
  }
  
  public PrimitiveImpl(Object value)
  {
    super();
    
    this.value = value;
  }
  
  @Override
  public void setValue(Object value)
  {
    this.value = value;
  }
  
  @Override
  public Object getValue()
  {
    return value;
  }
}
