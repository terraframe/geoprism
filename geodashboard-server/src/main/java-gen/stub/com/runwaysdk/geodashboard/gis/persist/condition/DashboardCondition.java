package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.geodashboard.gis.model.ThematicStyle;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;
import com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle;
import com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyleQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public abstract class DashboardCondition extends DashboardConditionBase implements com.runwaysdk.generation.loader.Reloadable, Condition
{
  private static final long serialVersionUID = -1287604192;
  
  public DashboardCondition()
  {
    super();
  }
  
  @Override
  public String getName()
  {
    return this.getClass().getSimpleName();
  }
  
  @Override
  public ThematicStyle getThematicStyle()
  {
    QueryFactory f = new QueryFactory();
    DashboardThematicStyleQuery q = new DashboardThematicStyleQuery(f);
    
    DashboardCondition root = this.getRootCondition();
    q.WHERE(q.getStyleCondition().EQ(root));
    
    OIterator<? extends DashboardThematicStyle> iter = q.getIterator();
    
    try
    {
      // There will always be one result
      return iter.next();
    }
    finally
    {
      iter.close();
    }
  }
}
