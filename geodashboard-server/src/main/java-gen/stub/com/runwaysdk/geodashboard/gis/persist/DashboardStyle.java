package com.runwaysdk.geodashboard.gis.persist;

import java.util.Arrays;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.Style;

public class DashboardStyle extends DashboardStyleBase implements com.runwaysdk.generation.loader.Reloadable, Style
{
  private static final long serialVersionUID = 248809785;
  
  public DashboardStyle()
  {
    super();
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);    
  }
  
  public static AllAggregationType[] getSortedAggregations()
  {
    AllAggregationType[] types = AllAggregationType.values();
    
    Arrays.sort(types);
    
    return types;
  }

}
