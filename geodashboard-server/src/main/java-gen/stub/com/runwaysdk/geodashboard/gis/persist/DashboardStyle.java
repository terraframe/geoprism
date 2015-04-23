package com.runwaysdk.geodashboard.gis.persist;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.geodashboard.SessionParameterFacade;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.Style;
import com.runwaysdk.geodashboard.gis.sld.SLDMapVisitor;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.system.metadata.MdAttributeChar;
import com.runwaysdk.system.metadata.MdAttributeCharacter;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdAttributeDecimal;
import com.runwaysdk.system.metadata.MdAttributeDouble;
import com.runwaysdk.system.metadata.MdAttributeFloat;
import com.runwaysdk.system.metadata.MdAttributeInteger;
import com.runwaysdk.system.metadata.MdAttributeLong;
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
      q.WHERE(q.getEnumName().EQ(AllAggregationType.MAJORITY.name()));
      q.OR(q.getEnumName().EQ(AllAggregationType.MINORITY.name()));
      // // We are currently not supporting distribution but want to leave this hear for future implementation
      // q.OR(q.getEnumName().EQ(AllAggregationType.DISTRIBUTION.name()));
      // q.ORDER_BY_ASC(q.getDisplayLabel().localize());
    }
    else
    {
      QueryFactory f = new QueryFactory();
      q = new AggregationTypeQuery(f);
      q.WHERE(q.getEnumName().EQ(AllAggregationType.SUM.name()));
      q.OR(q.getEnumName().EQ(AllAggregationType.AVG.name()));
      q.OR(q.getEnumName().EQ(AllAggregationType.MIN.name()));
      q.OR(q.getEnumName().EQ(AllAggregationType.MAX.name()));
      // q.ORDER_BY_ASC(q.getDisplayLabel().localize());
    }

    return q;
  }

  public static String getAggregationJSON()
  {
    try
    {
      JSONArray textOptions = DashboardStyle.getOptions(AllAggregationType.MAJORITY, AllAggregationType.MINORITY);
      JSONArray numberOptions = DashboardStyle.getOptions(AllAggregationType.SUM, AllAggregationType.AVG, AllAggregationType.MIN, AllAggregationType.MAX);

      JSONObject mapping = new JSONObject();
      mapping.put(MdAttributeTerm.CLASS, textOptions);
      mapping.put(MdAttributeText.CLASS, textOptions);
      mapping.put(MdAttributeCharacter.CLASS, textOptions);

      mapping.put(MdAttributeInteger.CLASS, numberOptions);
      mapping.put(MdAttributeLong.CLASS, numberOptions);
      mapping.put(MdAttributeDecimal.CLASS, numberOptions);
      mapping.put(MdAttributeDouble.CLASS, numberOptions);
      mapping.put(MdAttributeFloat.CLASS, numberOptions);

      return mapping.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private static JSONArray getOptions(AllAggregationType... types) throws JSONException
  {
    JSONArray options = new JSONArray();

    for (AllAggregationType type : types)
    {
      String value = type.name();
      String label = type.getDisplayLabel();

      JSONObject option = new JSONObject();
      option.put("value", value);
      option.put("label", label);

      options.put(option);
    }

    return options;
  }

  public DashboardStyle clone()
  {
    DashboardStyle clone = new DashboardStyle();
    clone.populate(clone);
    clone.apply();

    return clone;
  }

  protected void populate(DashboardStyle source)
  {
    this.setValueSize(source.getValueSize());
    this.setValueHaloWidth(source.getValueHaloWidth());
    this.setValueHalo(source.getValueHalo());
    this.setValueFont(source.getValueFont());
    this.setValueColor(source.getValueColor());
    this.setPolygonStrokeWidth(source.getPolygonStrokeWidth());
    this.setPolygonStrokeOpacity(source.getPolygonStrokeOpacity());
    this.setPolygonStroke(source.getPolygonStroke());
    this.setPolygonFillOpacity(source.getPolygonFillOpacity());
    this.setPolygonFill(source.getPolygonFill());
    this.setPointWellKnownName(source.getPointWellKnownName());
    this.setPointStrokeWidth(source.getPointStrokeWidth());
    this.setPointStrokeOpacity(source.getPointStrokeOpacity());
    this.setPointSize(source.getPointSize());
    this.setPointRotation(source.getPointRotation());
    this.setPointOpacity(source.getPointOpacity());
    this.setPointFill(source.getPointFill());
    this.setLineStrokeWidth(source.getLineStrokeWidth());
    this.setLineStrokeCap(source.getLineStrokeCap());
    this.setLineStroke(source.getLineStroke());
    this.setLineOpacity(source.getLineOpacity());
    this.setLabelSize(source.getLabelSize());
    this.setLabelHaloWidth(source.getLabelHaloWidth());
    this.setLabelHalo(source.getLabelHalo());
    this.setLabelFont(source.getLabelFont());
    this.setLabelColor(source.getLabelColor());
  }
}
