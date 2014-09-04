package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.Equal;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.ValueQuery;

public class DashboardEqual extends DashboardEqualBase implements com.runwaysdk.generation.loader.Reloadable, Equal
{
  private static final long serialVersionUID = 1213634698;
  
  public DashboardEqual()
  {
    super();
  }
  
  @Override
  public void restrictQuery(ValueQuery query, Attribute attr) {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);    
  }

}
