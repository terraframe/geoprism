/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.dashboard;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.Locale;

import net.geoprism.SessionParameterFacade;
import net.geoprism.dashboard.layer.DashboardLayer;
import net.geoprism.gis.sld.SLDMapVisitor;
import net.geoprism.gis.wrapper.MapVisitor;
import net.geoprism.gis.wrapper.Style;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
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
import com.runwaysdk.system.metadata.MdAttributeMoment;
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

  public JSONObject toJSON() throws JSONException
  {
    JSONObject json = new JSONObject();
    json.put("id", this.getId());
    json.put("basicPointSize", this.getBasicPointSize());
    json.put("enableLabel", this.getEnableLabel());
    json.put("enableValue", this.getEnableValue());
    json.put("labelColor", this.getLabelColor());
    json.put("labelFont", this.getLabelFont());
    json.put("labelHalo", this.getLabelHalo());
    json.put("labelHaloWidth", this.getLabelHaloWidth());
    json.put("labelSize", this.getLabelSize());
    json.put("lineOpacity", this.getLineOpacity());
    json.put("lineStroke", this.getLineStroke());
    json.put("lineStrokeCap", this.getLineStrokeCap());
    json.put("lineStrokeWidth", this.getLineStrokeWidth());
    json.put("pointFill", this.getPointFill());
    json.put("pointOpacity", this.getPointOpacity());
    json.put("pointRotation", this.getPointRotation());
    json.put("pointStroke", this.getPointStroke());
    json.put("pointStrokeOpacity", this.getPointStrokeOpacity());
    json.put("pointStrokeWidth", this.getPointStrokeWidth());
    json.put("pointWellKnownName", this.getPointWellKnownName());
    json.put("polygonFill", this.getPolygonFill());
    json.put("polygonFillOpacity", this.getPolygonFillOpacity());
    json.put("polygonStroke", this.getPolygonStroke());
    json.put("polygonStrokeOpacity", this.getPolygonStrokeOpacity());
    json.put("polygonStrokeWidth", this.getPolygonStrokeWidth());
    json.put("valueColor", this.getValueColor());
    json.put("valueFont", this.getValueFont());
    json.put("valueHalo", this.getValueHalo());
    json.put("valueHaloWidth", this.getValueHaloWidth());
    json.put("valueSize", this.getValueSize());
    json.put("sortedFonts", getSortedFonts());
    
    
    return json;
  }

  public static AggregationTypeQuery getSortedAggregations(String thematicAttributeId)
  {
    AggregationTypeQuery q;

    MdAttributeConcrete mdAttrConcrete = DashboardStyle.getMdAttributeConcrete(thematicAttributeId);

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
    else if (mdAttrConcrete instanceof MdAttributeMoment)
    {
      QueryFactory f = new QueryFactory();
      q = new AggregationTypeQuery(f);
      q.OR(q.getEnumName().EQ(AllAggregationType.MIN.name()));
      q.OR(q.getEnumName().EQ(AllAggregationType.MAX.name()));
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

  private static MdAttributeConcrete getMdAttributeConcrete(String thematicAttributeId)
  {
    MdAttribute mdAttribute = MdAttribute.get(thematicAttributeId);

    if (mdAttribute instanceof MdAttributeVirtual)
    {
      return ( (MdAttributeVirtual) mdAttribute ).getMdAttributeConcrete();
    }

    return (MdAttributeConcrete) mdAttribute;
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
    clone.populate(this);
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
    this.setBasicPointSize(source.getBasicPointSize());
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
    this.setEnableLabel(source.getEnableLabel());
    this.setEnableValue(source.getEnableValue());
  }
}
