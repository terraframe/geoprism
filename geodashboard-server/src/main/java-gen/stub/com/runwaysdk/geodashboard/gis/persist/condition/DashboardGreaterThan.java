package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.GreaterThan;

public class DashboardGreaterThan extends DashboardGreaterThanBase implements com.runwaysdk.generation.loader.Reloadable, GreaterThan
{
  private static final long serialVersionUID = 815122248;
  
  public DashboardGreaterThan()
  {
    super();
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);    
  }
}
