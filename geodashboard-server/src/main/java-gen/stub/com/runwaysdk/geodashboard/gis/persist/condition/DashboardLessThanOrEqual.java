package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.LessThanOrEqual;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeNumber;
import com.runwaysdk.query.ValueQuery;

public class DashboardLessThanOrEqual extends DashboardLessThanOrEqualBase implements
    com.runwaysdk.generation.loader.Reloadable, LessThanOrEqual
{
  private static final long serialVersionUID = 1496821676;

  public DashboardLessThanOrEqual()
  {
    super();
  }
  
  @Override
  public void restrictQuery(ValueQuery query, Attribute attr) {
    if (attr instanceof AttributeNumber) {
      query.AND(((AttributeNumber)attr).LE(this.getComparisonValue()));
    }
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

}
