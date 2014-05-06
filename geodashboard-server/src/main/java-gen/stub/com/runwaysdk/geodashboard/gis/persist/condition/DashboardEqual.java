package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.Equal;

public class DashboardEqual extends DashboardEqualBase implements com.runwaysdk.generation.loader.Reloadable, Equal
{
  private static final long serialVersionUID = 1213634698;
  
  public DashboardEqual()
  {
    super();
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);    
  }

}
