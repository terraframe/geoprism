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
package com.runwaysdk.geodashboard.gis.impl;

import org.json.JSONArray;
import org.json.JSONException;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.ThematicStyle;
import com.runwaysdk.geodashboard.gis.persist.AllAggregationType;

public class ThematicStyleImpl extends StyleImpl implements ThematicStyle
{
  private String             gradientPolygonMinFill;

  private String             gradientPolygonMaxFill;

  private String             gradientPolygonStroke;

  private Double             gradientPolygonFillOpacity;

  private Integer            gradientPolygonStrokeWidth;

  private Double             gradientPolygonStrokeOpacity;

  private Integer            pointMinSize;

  private Integer            pointMaxSize;

  private Integer            gradientPointSize;

  private String             gradientPointMinFill;

  private String             gradientPointMaxFill;

  private Double             gradientPointFillOpacity;

  private String             gradientPointStroke;

  private Integer            gradientPointStrokeWidth;

  private Double             gradientPointStrokeOpacity;

  private String             gradientPointWellKnownName;

  private Integer            categoryPointSize;

  private String             categoryPolygonStyles;

  private String             categoryPolygonStroke;

  private Double             categoryPolygonFillOpacity;

  private Integer            categoryPolygonStrokeWidth;

  private Double             categoryPolygonStrokeOpacity;

  private String             categoryPointWellKnownName;

  private Boolean            bubbleContinuousSize;

  private String             bubbleStroke;

  private Integer            bubbleStrokeWidth;

  private Integer            bubbleSize;

  private String             bubbleWellKnownName;

  private String             bubbleFill;

  private Double             bubbleOpacity;

  private Integer            bubbleRotation;

  private Double             bubbleStrokeOpacity;

  private MdAttributeDAOIF   secondaryAttribute;

  private AllAggregationType secondaryAttributeAggregationMethod;

  private JSONArray          secondaryAttributeCategories;

  public ThematicStyleImpl()
  {
    super();
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public Integer getBubbleMinSize()
  {
    return pointMinSize;
  }

  @Override
  public Double getBubbleStrokeOpacity()
  {
    return this.bubbleStrokeOpacity;
  }

  public void setBubbleStrokeOpacity(Double bubbleStrokeOpacity)
  {
    this.bubbleStrokeOpacity = bubbleStrokeOpacity;
  }

  public void setBubbleSize(Integer bubbleSize)
  {
    this.bubbleSize = bubbleSize;
  }

  public void setBubbleStroke(String bubbleStroke)
  {
    this.bubbleStroke = bubbleStroke;
  }

  public void setBubbleStrokeWidth(Integer bubbleStrokeWidth)
  {
    this.bubbleStrokeWidth = bubbleStrokeWidth;
  }

  public void setBubbleWellKnownName(String bubbleWellKnownName)
  {
    this.bubbleWellKnownName = bubbleWellKnownName;
  }

  @Override
  public Integer getBubbleSize()
  {
    return bubbleSize;
  }

  @Override
  public String getBubbleStroke()
  {
    return bubbleStroke;
  }

  @Override
  public Integer getBubbleStrokeWidth()
  {
    return bubbleStrokeWidth;
  }

  @Override
  public String getBubbleWellKnownName()
  {
    return bubbleWellKnownName;
  }

  @Override
  public void setBubbleFill(String fill)
  {
    this.bubbleFill = fill;
  }

  @Override
  public String getBubbleFill()
  {
    return this.bubbleFill;
  }

  @Override
  public void setBubbleOpacity(Double opacity)
  {
    this.bubbleOpacity = opacity;
  }

  @Override
  public Double getBubbleOpacity()
  {
    return this.bubbleOpacity;
  }

  @Override
  public void setBubbleRotation(Integer rotation)
  {
    this.bubbleRotation = rotation;
  }

  @Override
  public Integer getBubbleRotation()
  {
    return this.bubbleRotation;
  }

  @Override
  public void setBubbleMinSize(Integer pointMinSize)
  {
    this.pointMinSize = pointMinSize;
  }

  @Override
  public Integer getBubbleMaxSize()
  {
    return pointMaxSize;
  }

  @Override
  public void setBubbleMaxSize(Integer pointMaxSize)
  {
    this.pointMaxSize = pointMaxSize;
  }

  @Override
  public String getGradientPolygonMinFill()
  {
    return this.gradientPolygonMinFill;
  }

  public void setGradientPolygonMinFill(String gradientPolygonMinFill)
  {
    this.gradientPolygonMinFill = gradientPolygonMinFill;
  }

  public String getGradientPolygonMaxFill()
  {
    return gradientPolygonMaxFill;
  }

  public void setGradientPolygonMaxFill(String fill)
  {
    this.gradientPolygonMaxFill = fill;
  }

  public String getCategoryPolygonStyles()
  {
    return categoryPolygonStyles;
  }

  @Override
  public Boolean getBubbleContinuousSize()
  {
    return this.bubbleContinuousSize;
  }

  // @Override
  // public Integer getPointRadius()
  // {
  // return this.pointRadius;
  // }

  @Override
  public MdAttributeDAOIF getSecondaryAttributeDAO()
  {
    return this.secondaryAttribute;
  }

  public void setSecondaryAttribute(MdAttributeDAOIF secondaryAttribute)
  {
    this.secondaryAttribute = secondaryAttribute;
  }

  @Override
  public AllAggregationType getSecondaryAttributeAggregationMethod()
  {
    return this.secondaryAttributeAggregationMethod;
  }

  public void setSecondaryAttributeAggregationMethod(AllAggregationType secondaryAttributeAggregationMethod)
  {
    this.secondaryAttributeAggregationMethod = secondaryAttributeAggregationMethod;
  }

  @Override
  public JSONArray getSecondaryAttributeCategoriesAsJSON() throws JSONException
  {
    return this.secondaryAttributeCategories;
  }

  public void setSecondaryAttributeCategories(JSONArray secondaryAttributeCategories)
  {
    this.secondaryAttributeCategories = secondaryAttributeCategories;
  }

  public String getGradientPolygonStroke()
  {
    return gradientPolygonStroke;
  }

  public void setGradientPolygonStroke(String gradientPolygonStroke)
  {
    this.gradientPolygonStroke = gradientPolygonStroke;
  }

  public Double getGradientPolygonFillOpacity()
  {
    return gradientPolygonFillOpacity;
  }

  public void setGradientPolygonFillOpacity(Double gradientPolygonFillOpacity)
  {
    this.gradientPolygonFillOpacity = gradientPolygonFillOpacity;
  }

  public Integer getGradientPolygonStrokeWidth()
  {
    return gradientPolygonStrokeWidth;
  }

  public void setGradientPolygonStrokeWidth(Integer gradientPolygonStrokeWidth)
  {
    this.gradientPolygonStrokeWidth = gradientPolygonStrokeWidth;
  }

  public Double getGradientPolygonStrokeOpacity()
  {
    return gradientPolygonStrokeOpacity;
  }

  public void setGradientPolygonStrokeOpacity(Double gradientPolygonStrokeOpacity)
  {
    this.gradientPolygonStrokeOpacity = gradientPolygonStrokeOpacity;
  }

  public String getCategoryPolygonStroke()
  {
    return categoryPolygonStroke;
  }

  public void setCategoryPolygonStroke(String categoryPolygonStroke)
  {
    this.categoryPolygonStroke = categoryPolygonStroke;
  }

  public Double getCategoryPolygonFillOpacity()
  {
    return categoryPolygonFillOpacity;
  }

  public void setCategoryPolygonFillOpacity(Double categoryPolygonFillOpacity)
  {
    this.categoryPolygonFillOpacity = categoryPolygonFillOpacity;
  }

  public Integer getCategoryPolygonStrokeWidth()
  {
    return categoryPolygonStrokeWidth;
  }

  public void setCategoryPolygonStrokeWidth(Integer categoryPolygonStrokeWidth)
  {
    this.categoryPolygonStrokeWidth = categoryPolygonStrokeWidth;
  }

  public Double getCategoryPolygonStrokeOpacity()
  {
    return categoryPolygonStrokeOpacity;
  }

  public void setCategoryPolygonStrokeOpacity(Double categoryPolygonStrokeOpacity)
  {
    this.categoryPolygonStrokeOpacity = categoryPolygonStrokeOpacity;
  }

  public String getGradientPointMaxFill()
  {
    return gradientPointMaxFill;
  }

  public void setGradientPointMaxFill(String gradientPointMaxFill)
  {
    this.gradientPointMaxFill = gradientPointMaxFill;
  }

  public String getGradientPointMinFill()
  {
    return gradientPointMinFill;
  }

  public void setGradientPointMinFill(String gradientPointMinFill)
  {
    this.gradientPointMinFill = gradientPointMinFill;
  }

  public Double getGradientPointFillOpacity()
  {
    return gradientPointFillOpacity;
  }

  public void setGradientPointFillOpacity(Double gradientPointFillOpacity)
  {
    this.gradientPointFillOpacity = gradientPointFillOpacity;
  }

  public String getGradientPointStroke()
  {
    return gradientPointStroke;
  }

  public void setGradientPointStroke(String gradientPointStroke)
  {
    this.gradientPointStroke = gradientPointStroke;
  }

  public Integer getGradientPointStrokeWidth()
  {
    return gradientPointStrokeWidth;
  }

  public void setGradientPointStrokeWidth(Integer gradientPointStrokeWidth)
  {
    this.gradientPointStrokeWidth = gradientPointStrokeWidth;
  }

  public Double getGradientPointStrokeOpacity()
  {
    return gradientPointStrokeOpacity;
  }

  public void setGradientPointStrokeOpacity(Double gradientPointStrokeOpacity)
  {
    this.gradientPointStrokeOpacity = gradientPointStrokeOpacity;
  }

  public Integer getGradientPointSize()
  {
    return gradientPointSize;
  }

  public void setGradientPointSize(Integer gradientPointSize)
  {
    this.gradientPointSize = gradientPointSize;
  }

  public Integer getCategoryPointSize()
  {
    return categoryPointSize;
  }

  public void setCategoryPointSize(Integer categoryPointSize)
  {
    this.categoryPointSize = categoryPointSize;
  }

  public String getGradientPointWellKnownName()
  {
    return gradientPointWellKnownName;
  }

  public void setGradientPointWellKnownName(String gradientPointWellKnownName)
  {
    this.gradientPointWellKnownName = gradientPointWellKnownName;
  }

  public String getCategoryPointWellKnownName()
  {
    return categoryPointWellKnownName;
  }

  public void setCategoryPointWellKnownName(String categoryPointWellKnownName)
  {
    this.categoryPointWellKnownName = categoryPointWellKnownName;
  }
}
