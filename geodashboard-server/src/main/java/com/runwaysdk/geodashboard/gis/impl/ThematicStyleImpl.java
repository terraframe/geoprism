package com.runwaysdk.geodashboard.gis.impl;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.ThematicStyle;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;

public class ThematicStyleImpl extends StyleImpl implements ThematicStyle
{
  private String attribute;
  
  private Condition condition;
  
  public ThematicStyleImpl()
  {
    super();
    
    this.attribute = null;
    this.condition = null;
  }
  
  @Override
  public String getAttribute()
  {
    return this.attribute;
  }
  
  public void setAttribute(String attribute)
  {
    this.attribute = attribute;    
  }
  
  

  @Override
  public Condition getCondition()
  {
    return this.condition;
  }
  
  public void setCondition(Condition condition)
  {
    this.condition = condition;
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }
}
