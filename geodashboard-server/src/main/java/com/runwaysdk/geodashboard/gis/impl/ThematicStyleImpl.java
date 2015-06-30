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
import com.runwaysdk.geodashboard.gis.model.condition.Condition;
import com.runwaysdk.geodashboard.gis.persist.AllAggregationType;

public class ThematicStyleImpl extends StyleImpl implements ThematicStyle
{
  private Condition          condition;

  private String             polygonMinFill;

  private String             polygonMaxFill;

  private Integer            pointMinSize;

  private Integer            pointMaxSize;

  private Integer            pointFixedSize;

  private Boolean            pointFixed;

  private String             styleCategories;

  private Boolean            bubbleContinuousSize;

  private MdAttributeDAOIF   secondaryAttribute;

  private AllAggregationType secondaryAttributeAggregationMethod;

  private JSONArray          secondaryAttributeCategories;

  public ThematicStyleImpl()
  {
    super();

    this.condition = null;
  }

  @Override
  public Condition getCondition()
  {
    return this.condition;
  }

  public void setCondition(Condition condition)
  {
    this.condition = condition;
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public Integer getPointMinSize()
  {
    return pointMinSize;
  }

  @Override
  public void setPointMinSize(Integer pointMinSize)
  {
    this.pointMinSize = pointMinSize;
  }

  @Override
  public Integer getPointMaxSize()
  {
    return pointMaxSize;
  }

  @Override
  public void setPointMaxSize(Integer pointMaxSize)
  {
    this.pointMaxSize = pointMaxSize;
  }

  @Override
  public String getPolygonMinFill()
  {
    return this.polygonMinFill;
  }

  @Override
  public void setPolygonMinFill(String polygonMinFill)
  {
    this.polygonMinFill = polygonMinFill;
  }

  @Override
  public String getPolygonMaxFill()
  {
    return polygonMaxFill;
  }

  @Override
  public void setPolygonMaxFill(String fill)
  {
    this.polygonMaxFill = fill;
  }

  @Override
  public String getStyleCategories()
  {
    return styleCategories;
  }

  @Override
  public Boolean getBubbleContinuousSize()
  {
    return this.bubbleContinuousSize;
  }

  @Override
  public Integer getPointFixedSize()
  {
    return this.pointFixedSize;
  }

  @Override
  public Boolean getPointFixed()
  {
    return this.pointFixed;
  }

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
}
