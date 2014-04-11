package com.runwaysdk.geodashboard.gis.model.impl;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.IsBetween;

public class IsBetweenImpl extends ConditionImpl implements IsBetween
{
  
  private String lowerBound;
  
  private String upperBound;
  
  public IsBetweenImpl()
  {
    super();
  }
  
  @Override
  public void setLowerBound(String lowerBound)
  {
    this.lowerBound = lowerBound;
  }
  
  @Override
  public void setUpperBound(String upperBound)
  {
    this.upperBound = upperBound;
  }
  
  @Override
  public String getLowerBound()
  {
    return lowerBound;
  }
  
  @Override
  public String getUpperBound()
  {
    return upperBound;
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);    
  }


}
