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
package com.runwaysdk.geodashboard.gis.persist.condition;

import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.geodashboard.Dashboard;
import com.runwaysdk.geodashboard.gis.model.ThematicLayer;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.ValueQuery;

public abstract class DashboardCondition extends DashboardConditionBase implements com.runwaysdk.generation.loader.Reloadable, Condition
{
  private static final long  serialVersionUID = -1287604192;

  /**
   * Magic value for the json attribute name which specifies the operation type
   */
  public static final String OPERATION_KEY    = "operation";

  /**
   * Magic value for the json attribute name which specifies the value
   */
  public static final String VALUE_KEY        = "value";

  /**
   * Magic value for the json attribute name which specifies the value
   */
  public static final String TYPE_KEY         = "type";

  public abstract void restrictQuery(ValueQuery query, Attribute attr);

  public abstract JSONObject getJSON();

  public abstract String getJSONKey();

  protected abstract DashboardCondition newInstance();

  public DashboardCondition()
  {
    super();
  }

  @Override
  public String getName()
  {
    return this.getClass().getSimpleName();
  }

  @Override
  public String toString()
  {
    return "[" + getName() + "] - " + this.getId();
  }

  @Override
  public ThematicLayer getThematicLayer()
  {
    String msg = "The condition [" + this.getName() + "] with condition [" + this + "] is not referenced by a layer.";
    throw new ProgrammingErrorException(msg);
  }

  public DashboardCondition clone(Dashboard dashboard)
  {
    DashboardCondition clone = this.newInstance();
    clone.setDashboard(dashboard);
    clone.populate(this);
    clone.apply();

    return clone;
  }

  protected void populate(DashboardCondition source)
  {
    this.setGeodashboardUser(source.getGeodashboardUser());
  }
}
