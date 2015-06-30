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
package com.runwaysdk.geodashboard.gis.model;

import org.json.JSONArray;
import org.json.JSONException;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;
import com.runwaysdk.geodashboard.gis.persist.AllAggregationType;

public interface ThematicStyle extends Style
{
  public static final String VAL   = "val";

  public static final String COLOR = "color";

  public Condition getCondition();

  public Integer getPointMinSize();

  public void setPointMinSize(Integer size);

  public Integer getPointMaxSize();

  public void setPointMaxSize(Integer size);

  public String getPolygonMinFill();

  public void setPolygonMinFill(String fill);

  public String getPolygonMaxFill();

  public void setPolygonMaxFill(String fill);

  public String getStyleCategories();

  public Boolean getBubbleContinuousSize();

  public Integer getPointFixedSize();

  public Boolean getPointFixed();

  public MdAttributeDAOIF getSecondaryAttributeDAO();

  public AllAggregationType getSecondaryAttributeAggregationMethod();

  public JSONArray getSecondaryAttributeCategoriesAsJSON() throws JSONException;
}
