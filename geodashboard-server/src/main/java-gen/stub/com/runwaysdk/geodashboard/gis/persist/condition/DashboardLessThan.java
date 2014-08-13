package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.LessThan;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeNumber;

public class DashboardLessThan extends DashboardLessThanBase implements com.runwaysdk.generation.loader.Reloadable, LessThan
{
  private static final long serialVersionUID = -1060927779;
  
  public DashboardLessThan()
  {
    super();
  }
  
  @Override
  public com.runwaysdk.query.Condition asRunwayQuery(Attribute attr) {
    return ((AttributeNumber)attr).LT(this.getComparisonValue());
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this); 
  }
  
}
