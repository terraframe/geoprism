package com.runwaysdk.geodashboard.gis.model.condition;



public interface Primitive extends Condition
{
  public void setValue(Object value);
  
  public Object getValue();
}
