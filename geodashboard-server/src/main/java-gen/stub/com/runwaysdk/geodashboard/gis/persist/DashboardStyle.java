package com.runwaysdk.geodashboard.gis.persist;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Locale;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.Style;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;

public class DashboardStyle extends DashboardStyleBase implements
    com.runwaysdk.generation.loader.Reloadable, Style
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

  public static String[] getSortedFonts()
  {
    GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
    Locale locale = Session.getCurrentLocale();
    
    Font[] fonts = e.getAllFonts(); // Get the fonts
    String[] localizedFonts = new String[fonts.length];

    for(int i=0; i<fonts.length; i++)
    {
      localizedFonts[i] = fonts[i].getFontName(locale);
    }
    
    return localizedFonts;
  }

  public static AggregationTypeQuery getSortedAggregations()
  {
    QueryFactory f = new QueryFactory();
    AggregationTypeQuery q = new AggregationTypeQuery(f);

    q.ORDER_BY_ASC(q.getDisplayLabel().localize());

    return q;

    // AllAggregationType[] types = AllAggregationType.values();
    // AggregationType[] aggs = new AggregationType[types.length];
    //
    // Arrays.sort(types); // sort alphabetically
    //
    // for(int i=0; i<types.length; i++)
    // {
    // aggs[i] = AggregationType.get(types[i].getId());
    // }
    //
    // return aggs;
  }

}
