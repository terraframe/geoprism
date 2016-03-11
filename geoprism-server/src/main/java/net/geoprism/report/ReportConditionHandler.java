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
package net.geoprism.report;

import net.geoprism.dashboard.condition.DashboardCondition;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.ValueQuery;

public class ReportConditionHandler implements ReportConditionHandlerIF, Reloadable
{
  private String                  type;

  private ValueQuery              vQuery;

  private GeneratedComponentQuery query;

  public ReportConditionHandler(String _type, ValueQuery _vQuery, GeneratedComponentQuery _query)
  {
    this.type = _type;
    this.vQuery = _vQuery;
    this.query = _query;
  }

  @Override
  public void handleCondition(DashboardCondition condition)
  {
    condition.restrictQuery(this.type, this.vQuery, this.query);
  }
}
