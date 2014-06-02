package com.runwaysdk.geodashboard.gis.persist;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.Style;
import com.runwaysdk.query.QueryFactory;

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
  
  public static AggregationTypeQuery getSortedAggregations()
  {
    QueryFactory f = new QueryFactory();
    AggregationTypeQuery q = new AggregationTypeQuery(f);
    
    q.ORDER_BY_ASC(q.getDisplayLabel().localize());
    
    return q;
    
//    AllAggregationType[] types = AllAggregationType.values();
//    AggregationType[] aggs = new AggregationType[types.length];
//    
//    Arrays.sort(types); // sort alphabetically
//    
//    for(int i=0; i<types.length; i++)
//    {
//      aggs[i] = AggregationType.get(types[i].getId());
//    }
//    
//    return aggs;
  }

}
