package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.LessThanOrEqual;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeNumber;

public class DashboardLessThanOrEqual extends DashboardLessThanOrEqualBase implements
    com.runwaysdk.generation.loader.Reloadable, LessThanOrEqual
{
  private static final long serialVersionUID = 1496821676;

  public DashboardLessThanOrEqual()
  {
    super();
  }
  
  @Override
  public com.runwaysdk.query.Condition asRunwayQuery(Attribute attr) {
    return ((AttributeNumber)attr).LE(this.getComparisonValue());
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

}
