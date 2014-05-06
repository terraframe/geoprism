package com.runwaysdk.geodashboard.gis.persist;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.ThematicStyle;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;

public class DashboardThematicStyle extends DashboardThematicStyleBase implements com.runwaysdk.generation.loader.Reloadable, ThematicStyle
{
  private static final long serialVersionUID = -1178596850;
  
  public DashboardThematicStyle()
  {
    super();
  }
  
  @Override
  public String getAttribute()
  {
    return this.getMdAttribute().getAttributeName();
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }
  
  @Override
  public Condition getCondition()
  {
    return this.getStyleCondition();
  }
//  
//  @Override
//  public void delete()
//  {
//    DashboardCondition cond = this.getStyleCondition();
//    
//    super.delete();
//    
//    if(cond != null)
//    {
//      cond.delete();
//    }
//  }
  
}
