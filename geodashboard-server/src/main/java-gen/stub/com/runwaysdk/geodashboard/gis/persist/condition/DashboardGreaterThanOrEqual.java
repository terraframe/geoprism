package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.GreaterThanOrEqual;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeDate;
import com.runwaysdk.query.AttributeNumber;
import com.runwaysdk.query.ValueQuery;

public class DashboardGreaterThanOrEqual extends DashboardGreaterThanOrEqualBase implements com.runwaysdk.generation.loader.Reloadable, GreaterThanOrEqual
{
  private static final long serialVersionUID = -1887751967;

  public DashboardGreaterThanOrEqual()
  {
    super();
  }

  @Override
  public void restrictQuery(ValueQuery query, Attribute attr)
  {
    if (attr instanceof AttributeNumber)
    {
      query.AND( ( (AttributeNumber) attr ).GE(this.getComparisonValue()));
    }
    else if (attr instanceof AttributeDate)
    {
      query.AND( ( (AttributeDate) attr ).GE(this.getComparisonValueAsDate()));
    }
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }
}
