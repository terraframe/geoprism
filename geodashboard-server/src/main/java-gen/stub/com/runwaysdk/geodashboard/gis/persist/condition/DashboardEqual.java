package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;
import com.runwaysdk.geodashboard.gis.model.condition.Equal;
import com.runwaysdk.query.Attribute;

public class DashboardEqual extends DashboardEqualBase implements com.runwaysdk.generation.loader.Reloadable, Equal
{
  private static final long serialVersionUID = 1213634698;
  
  public DashboardEqual()
  {
    super();
  }
  
  @Override
  public com.runwaysdk.query.Condition asRunwayQuery(Attribute attr) {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);    
  }

}
