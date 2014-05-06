package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.LessThan;

public class DashboardLessThan extends DashboardLessThanBase implements com.runwaysdk.generation.loader.Reloadable, LessThan
{
  private static final long serialVersionUID = -1060927779;
  
  public DashboardLessThan()
  {
    super();
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this); 
  }
  
}
