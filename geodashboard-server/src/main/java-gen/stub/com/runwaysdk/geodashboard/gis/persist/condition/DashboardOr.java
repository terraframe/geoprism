package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.Or;

public class DashboardOr extends DashboardOrBase implements com.runwaysdk.generation.loader.Reloadable, Or
{
  private static final long serialVersionUID = -359023372;
  
  public DashboardOr()
  {
    super();
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this); 
  }
  
}
