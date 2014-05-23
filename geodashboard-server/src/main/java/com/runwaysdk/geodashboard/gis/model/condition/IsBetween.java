package com.runwaysdk.geodashboard.gis.model.condition;

public interface IsBetween extends Condition
{
  public void setLowerBound(String string);
  
  public String getLowerBound();
  
  public void setUpperBound(String string);

  public String getUpperBound();
}
