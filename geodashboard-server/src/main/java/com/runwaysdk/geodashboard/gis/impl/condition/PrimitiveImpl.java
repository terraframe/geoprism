package com.runwaysdk.geodashboard.gis.impl.condition;

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
