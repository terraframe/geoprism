package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.LessThanOrEqual;

public class DashboardLessThanOrEqual extends DashboardLessThanOrEqualBase implements
    com.runwaysdk.generation.loader.Reloadable, LessThanOrEqual
{
  private static final long serialVersionUID = 1496821676;

  public DashboardLessThanOrEqual()
  {
    super();
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

}
