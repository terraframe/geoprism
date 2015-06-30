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
package com.runwaysdk.geodashboard.gis.persist;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.ThematicStyle;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;

public class DashboardThematicStyle extends DashboardThematicStyleBase implements Reloadable, ThematicStyle
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
  public Condition getCondition()
  {
    return this.getStyleCondition();
  }

  public JSONObject toJSON()
  {
    try
    {
      JSONObject json = new JSONObject();
      json.put("bubbleContinuousSize", this.getBubbleContinuousSize());

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

    DashboardCondition cond = this.getStyleCondition();

    if (cond != null)
    {
      cond.delete();
    }
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

      this.setBubbleContinuousSize(tSource.getBubbleContinuousSize());
      this.setPointFixed(tSource.getPointFixed());
      this.setPointMaxSize(tSource.getPointMaxSize());
      this.setPointMinSize(tSource.getPointMinSize());
      this.setPolygonMaxFill(tSource.getPolygonMaxFill());
      this.setPolygonMinFill(tSource.getPolygonMinFill());
      this.addSecondaryAggregationType(tSource.getSecondaryAttributeAggregationMethod());
      this.setSecondaryAttribute(tSource.getSecondaryAttribute());
      this.setSecondaryCategories(tSource.getSecondaryCategories());
      this.setStyleCategories(tSource.getStyleCategories());
      this.setStyleCondition(tSource.getStyleCondition());
    }
  }
}
