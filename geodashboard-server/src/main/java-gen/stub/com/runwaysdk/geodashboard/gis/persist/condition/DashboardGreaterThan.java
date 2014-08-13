package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;
import com.runwaysdk.geodashboard.gis.model.condition.GreaterThan;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeNumber;

public class DashboardGreaterThan extends DashboardGreaterThanBase implements com.runwaysdk.generation.loader.Reloadable, GreaterThan
{
  private static final long serialVersionUID = 815122248;
  
  public DashboardGreaterThan()
  {
    super();
  }
  
  @Override
  public com.runwaysdk.query.Condition asRunwayQuery(Attribute attr) {
    return ((AttributeNumber)attr).GT(this.getComparisonValue());
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);    
  }
}
