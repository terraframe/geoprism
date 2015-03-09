package com.runwaysdk.geodashboard.gis.persist;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.Locale;

import org.json.JSONObject;

import com.runwaysdk.geodashboard.SessionParameterFacade;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.Style;
import com.runwaysdk.geodashboard.gis.sld.SLDMapVisitor;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.system.metadata.MdAttributeChar;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdAttributeTerm;
import com.runwaysdk.system.metadata.MdAttributeText;
import com.runwaysdk.system.metadata.MdAttributeVirtual;

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

  /**
   * @see com.runwaysdk.business.Entity#apply()
   */
  @Override
  public void apply()
  {
    super.apply();
  }

  @Override
  public String getName()
  {
    return SessionParameterFacade.get(this.getId());
  }

  public void setName(String value)
  {
    SessionParameterFacade.put(this.getId(), value);
  }

  public String generateSLD()
  {
    DashboardLayer layer = this.getContainingLayer();

    SLDMapVisitor visitor = new SLDMapVisitor();
    layer.accepts(visitor);
    String sld = visitor.getSLD(layer);

    return sld;
  }

  public DashboardLayer getContainingLayer()
  {
    return this.getAllContainingLayer().getAll().get(0);
  }

  public static String[] getSortedFonts()
  {
    GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
    Locale locale = Session.getCurrentLocale();

    Font[] fonts = e.getAllFonts(); // Get the fonts
    String[] localizedFonts = new String[fonts.length];

    for (int i = 0; i < fonts.length; i++)
    {
      localizedFonts[i] = fonts[i].getFontName(locale);
    }

    Arrays.sort(localizedFonts);

    return localizedFonts;
  }

  public JSONObject toJSON()
  {
    // try {
    // JSONObject json = new JSONObject();
    //
    // return json;
    // }
    // catch (JSONException ex)
    // {
    // String msg = "Could not properly form DashboardStyle [" + this.toString() +
    // "] into valid JSON to send back to the client.";
    // throw new ProgrammingErrorException(msg, ex);
    // }

    // Its okay to throw this here (for now) because this method is overridden with an implementation in
    // DashboardThematicStyle
    throw new UnsupportedOperationException();
  }

  public static AggregationTypeQuery getSortedAggregations(String thematicAttributeId)
  {
    AggregationTypeQuery q;

    MdAttributeConcrete mdAttrConcrete = ( (MdAttributeVirtual) MdAttribute.get(thematicAttributeId) ).getMdAttributeConcrete();
    if (mdAttrConcrete instanceof MdAttributeTerm || mdAttrConcrete instanceof MdAttributeText || mdAttrConcrete instanceof MdAttributeChar)
    {
      QueryFactory f = new QueryFactory();
      q = new AggregationTypeQuery(f);
      q.WHERE(q.getEnumName().EQ(AllAggregationType.MINORITY.name()));
      q.OR(q.getEnumName().EQ(AllAggregationType.MAJORITY.name()));
      // // We are currently not supporting distribution but want to leave this hear for future implementation
      // q.OR(q.getEnumName().EQ(AllAggregationType.DISTRIBUTION.name()));
      q.ORDER_BY_ASC(q.getDisplayLabel().localize());
    }
    else
    {
      QueryFactory f = new QueryFactory();
      q = new AggregationTypeQuery(f);
      q.WHERE(q.getEnumName().EQ(AllAggregationType.SUM.name()));
      q.OR(q.getEnumName().EQ(AllAggregationType.AVG.name()));
      q.OR(q.getEnumName().EQ(AllAggregationType.MIN.name()));
      q.OR(q.getEnumName().EQ(AllAggregationType.MAX.name()));
      q.ORDER_BY_ASC(q.getDisplayLabel().localize());
    }

    return q;
  }

}
