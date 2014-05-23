package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.And;

public class DashboardAnd extends DashboardAndBase implements com.runwaysdk.generation.loader.Reloadable, And
{
  private static final long serialVersionUID = 377632172;
  
  public DashboardAnd()
  {
    super();
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);    
  }
}
