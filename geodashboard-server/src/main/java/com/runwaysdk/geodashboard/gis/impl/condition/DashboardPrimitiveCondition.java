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
package com.runwaysdk.geodashboard.gis.impl.condition;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.report.ReportProviderUtil;

public abstract class DashboardPrimitiveCondition extends DashboardAttributeCondition implements Reloadable
{
  public static final String COMPARISONVALUE = "value";

  private String             comparisonValue;

  public DashboardPrimitiveCondition(String mdAttributeId, String comparisonValue)
  {
    super(mdAttributeId);

    this.comparisonValue = comparisonValue;
  }

  public String getComparisonValue()
  {
    return comparisonValue;
  }

  public void setComparisonValue(String comparisonValue)
  {
    this.comparisonValue = comparisonValue;
  }

  public abstract String getOperation();

  public Date getComparisonValueAsDate()
  {
    return ReportProviderUtil.parseDate(this.getComparisonValue());
  }

  public Boolean getComparisonValueAsBoolean()
  {
    return new Boolean(this.getComparisonValue());
  }

  @Override
  public JSONObject getJSON()
  {
    try
    {
      JSONObject object = new JSONObject();
      object.put(TYPE_KEY, CONDITION_TYPE);
      object.put(MD_ATTRIBUTE_KEY, this.getMdAttributeId());
      object.put(OPERATION_KEY, this.getOperation());
      object.put(VALUE_KEY, this.getComparisonValue());

      return object;
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }
}
