/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.dashboard;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.gis.wrapper.MapVisitor;
import net.geoprism.gis.wrapper.ThematicStyle;


public class DashboardThematicStyle extends DashboardThematicStyleBase implements ThematicStyle
{
  private static final long serialVersionUID = -1178596850;

  public DashboardThematicStyle()
  {
    super();
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public JSONObject toJSON()
  {
    try
    {
      JSONObject json = new JSONObject();
      json.put("bubbleContinuousSize", this.getBubbleContinuousSize());

      JSONObject dashboardLayerJSON = super.toJSON();
      for (String key : JSONObject.getNames(dashboardLayerJSON))
      {
        json.put(key, dashboardLayerJSON.get(key));
      }

      json.put("bubbleFill", this.getBubbleFill());
      json.put("bubbleMaxSize", this.getBubbleMaxSize());
      json.put("bubbleMinSize", this.getBubbleMinSize());
      json.put("bubbleOpacity", this.getBubbleOpacity());
      json.put("bubbleRotation", this.getBubbleRotation());
      json.put("bubbleSize", this.getBubbleSize());
      json.put("bubbleStroke", this.getBubbleStroke());
      json.put("bubbleStrokeOpacity", this.getBubbleStrokeOpacity());
      json.put("bubbleStrokeWidth", this.getBubbleStrokeWidth());
      json.put("bubbleWellKnownName", this.getBubbleWellKnownName());
      json.put("categoryPointFillOpacity", this.getCategoryPointFillOpacity());
      json.put("categoryPointSize", this.getCategoryPointSize());
      json.put("categoryPointStroke", this.getCategoryPointStroke());
      json.put("categoryPointStrokeOpacity", this.getCategoryPointStrokeOpacity());
      json.put("categoryPointStrokeWidth", this.getCategoryPointStrokeWidth());
      json.put("categoryPointStyles", this.getCategoryPointStyles());
      json.put("categoryPointWellKnownName", this.getCategoryPointWellKnownName());
      json.put("categoryPolygonFillOpacity", this.getCategoryPolygonFillOpacity());
      json.put("categoryPolygonStroke", this.getCategoryPolygonStroke());
      json.put("categoryPolygonStrokeOpacity", this.getCategoryPolygonStrokeOpacity());
      json.put("categoryPolygonStrokeWidth", this.getCategoryPolygonStrokeWidth());
      json.put("categoryPolygonStyles", this.getCategoryPolygonStyles());
      json.put("gradientPointFillOpacity", this.getGradientPointFillOpacity());
      json.put("gradientPointMaxFill", this.getGradientPointMaxFill());
      json.put("gradientPointMinFill", this.getGradientPointMinFill());
      json.put("gradientPointSize", this.getGradientPointSize());
      json.put("gradientPointStroke", this.getGradientPointStroke());
      json.put("gradientPointStrokeOpacity", this.getGradientPointStrokeOpacity());
      json.put("gradientPointStrokeWidth", this.getGradientPointStrokeWidth());
      json.put("gradientPointWellKnownName", this.getGradientPointWellKnownName());
      json.put("gradientPolygonFillOpacity", this.getGradientPolygonFillOpacity());
      json.put("gradientPolygonMaxFill", this.getGradientPolygonMaxFill());
      json.put("gradientPolygonMinFill", this.getGradientPolygonMinFill());
      json.put("gradientPolygonStroke", this.getGradientPolygonStroke());
      json.put("gradientPolygonStrokeOpacity", this.getGradientPolygonStrokeOpacity());
      json.put("gradientPolygonStrokeWidth", this.getGradientPolygonStrokeWidth());
      json.put("secondaryAggregationType", this.getSecondaryAggregationType());
      json.put("secondaryAttribute", this.getSecondaryAttributeId());
      json.put("secondaryCategories", this.getSecondaryCategories());
      json.put("numGradientPolygonCategories", this.getNumGradientPolygonCategories());
      json.put("numGradientPointCategories", this.getNumGradientPointCategories());
      json.put("numBubbleSizeCategories", this.getNumBubbleSizeCategories());
      
      json.put("aggregationMap", DashboardStyle.getAggregationJSON());

      return json;
    }
    catch (JSONException ex)
    {
      String msg = "Could not properly form DashboardStyle [" + this.toString() + "] into valid JSON to send back to the client.";
      throw new ProgrammingErrorException(msg, ex);
    }
  }

  @Override
  @Transaction
  public void delete()
  {
    super.delete();
  }

  @Override
  public AllAggregationType getSecondaryAttributeAggregationMethod()
  {
    List<AllAggregationType> aggregations = this.getSecondaryAggregationType();

    if (aggregations.size() > 0)
    {
      return aggregations.get(0);
    }

    return null;
  }

  @Override
  public MdAttributeDAOIF getSecondaryAttributeDAO()
  {
    String mdAttributeId = this.getSecondaryAttributeId();

    if (mdAttributeId != null && mdAttributeId.length() > 0)
    {
      return MdAttributeDAO.get(mdAttributeId);
    }

    return null;
  }

  @Override
  public JSONArray getSecondaryAttributeCategoriesAsJSON() throws JSONException
  {
    if (this.getSecondaryCategories() != null && this.getSecondaryCategories().length() > 0)
    {
      return new JSONArray(this.getSecondaryCategories());
    }

    return null;
  }

  @Override
  public DashboardStyle clone()
  {
    DashboardThematicStyle clone = new DashboardThematicStyle();
    clone.populate(this);
    clone.apply();

    return clone;
  }

  @Override
  protected void populate(DashboardStyle source)
  {
    super.populate(source);

    if (source instanceof DashboardThematicStyle)
    {
      DashboardThematicStyle tSource = (DashboardThematicStyle) source;

      // Bubble
      this.setBubbleContinuousSize(tSource.getBubbleContinuousSize());
      this.setBubbleFill(tSource.getBubbleFill());
      this.setBubbleMaxSize(tSource.getBubbleMaxSize());
      this.setBubbleMinSize(tSource.getBubbleMinSize());
      this.setBubbleOpacity(tSource.getBubbleOpacity());
      this.setBubbleRotation(tSource.getBubbleRotation());
      this.setBubbleSize(tSource.getBubbleSize());
      this.setBubbleStroke(tSource.getBubbleStroke());
      this.setBubbleStrokeOpacity(tSource.getBubbleStrokeOpacity());
      this.setBubbleStrokeWidth(tSource.getBubbleStrokeWidth());
      this.setBubbleWellKnownName(tSource.getBubbleWellKnownName());

      // Category Point
      this.setCategoryPointFillOpacity(tSource.getCategoryPointFillOpacity());
      this.setCategoryPointSize(tSource.getCategoryPointSize());
      this.setCategoryPointStroke(tSource.getCategoryPointStroke());
      this.setCategoryPointStrokeOpacity(tSource.getCategoryPointStrokeOpacity());
      this.setCategoryPointStrokeWidth(tSource.getCategoryPointStrokeWidth());
      this.setCategoryPointStyles(tSource.getCategoryPointStyles());
      this.setCategoryPointWellKnownName(tSource.getCategoryPointWellKnownName());

      // Category Polygon
      this.setCategoryPolygonFillOpacity(tSource.getCategoryPolygonFillOpacity());
      this.setCategoryPolygonStroke(tSource.getCategoryPolygonStroke());
      this.setCategoryPolygonStrokeOpacity(tSource.getCategoryPolygonStrokeOpacity());
      this.setCategoryPolygonStrokeWidth(tSource.getCategoryPolygonStrokeWidth());
      this.setCategoryPolygonStyles(tSource.getCategoryPolygonStyles());

      // Gradient Point
      this.setGradientPointSize(tSource.getGradientPointSize());
      this.setGradientPointWellKnownName(tSource.getGradientPointWellKnownName());
      this.setGradientPointFillOpacity(tSource.getGradientPointFillOpacity());
      this.setGradientPointMaxFill(tSource.getGradientPointMaxFill());
      this.setGradientPointMinFill(tSource.getGradientPointMinFill());
      this.setGradientPointStroke(tSource.getGradientPointStroke());
      this.setGradientPointStrokeOpacity(tSource.getGradientPointStrokeOpacity());
      this.setGradientPointStrokeWidth(tSource.getGradientPointStrokeWidth());

      // Gradient Polygon
      this.setGradientPolygonFillOpacity(tSource.getGradientPolygonFillOpacity());
      this.setGradientPolygonStroke(tSource.getGradientPolygonStroke());
      this.setGradientPolygonMaxFill(tSource.getGradientPolygonMaxFill());
      this.setGradientPolygonMinFill(tSource.getGradientPolygonMinFill());
      this.setGradientPolygonStrokeOpacity(tSource.getGradientPolygonStrokeOpacity());
      this.setGradientPolygonStrokeWidth(tSource.getGradientPolygonStrokeWidth());

      // this.setPointRadius(tSource.getPointRadius());
      this.setBasicPointSize(tSource.getBasicPointSize());

      // Secondary attributes
      this.addSecondaryAggregationType(tSource.getSecondaryAttributeAggregationMethod());
      this.setSecondaryAttribute(tSource.getSecondaryAttribute());
      this.setSecondaryCategories(tSource.getSecondaryCategories());
    }
  }
}