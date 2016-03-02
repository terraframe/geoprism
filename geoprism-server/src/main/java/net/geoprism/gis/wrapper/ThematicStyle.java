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
package net.geoprism.gis.wrapper;

import org.json.JSONArray;
import org.json.JSONException;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.geodashboard.gis.persist.AllAggregationType;

public interface ThematicStyle extends Style
{
  public static final String VAL   = "val";

  public static final String COLOR = "color";

  public Integer getBubbleMinSize();
  public void setBubbleMinSize(Integer size);
  public Integer getBubbleMaxSize();
  public void setBubbleMaxSize(Integer size);
  public Integer getBubbleSize();
  public String getBubbleStroke();
  public Integer getBubbleStrokeWidth();
  public Double getBubbleStrokeOpacity();
  public String getBubbleWellKnownName();
  public void setBubbleSize(Integer size);
  public void setBubbleStroke(String stroke);
  public void setBubbleStrokeWidth(Integer width);
  public void setBubbleWellKnownName(String pointWellKnownName);
  public void setBubbleFill(String fill);
  public String getBubbleFill();
  public void setBubbleOpacity(Double opacity);
  public Double getBubbleOpacity();
  public void setBubbleRotation(Integer rotation);
  public Integer getBubbleRotation();
  public Boolean getBubbleContinuousSize();

  public String getGradientPolygonMinFill();
  public void setGradientPolygonMinFill(String fill);
  public String getGradientPolygonMaxFill();
  public void setGradientPolygonMaxFill(String fill);
  public Double getGradientPolygonFillOpacity();
  public String getGradientPolygonStroke();
  public Integer getGradientPolygonStrokeWidth();
  public Double getGradientPolygonStrokeOpacity();
  
  public Integer getGradientPointSize();
  public String getGradientPointMinFill();
  public String getGradientPointMaxFill();
  public Double getGradientPointFillOpacity();
  public String getGradientPointStroke();
  public Integer getGradientPointStrokeWidth();
  public Double getGradientPointStrokeOpacity();
  
  public Integer getCategoryPointSize();
  public String getGradientPointWellKnownName();
  
  public String getCategoryPointWellKnownName();
  
  public String getCategoryPolygonStyles();
  public Double getCategoryPolygonFillOpacity();
  public String getCategoryPolygonStroke();
  public Integer getCategoryPolygonStrokeWidth();
  public Double getCategoryPolygonStrokeOpacity();

  public MdAttributeDAOIF getSecondaryAttributeDAO();
  public AllAggregationType getSecondaryAttributeAggregationMethod();
  public JSONArray getSecondaryAttributeCategoriesAsJSON() throws JSONException;
}
