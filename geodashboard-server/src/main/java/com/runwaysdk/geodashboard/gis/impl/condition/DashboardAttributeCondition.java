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

import com.runwaysdk.generation.loader.Reloadable;

public abstract class DashboardAttributeCondition extends DashboardCondition implements Reloadable
{

  /**
   * Condition type for restricting on an attribute
   */
  public static final String CONDITION_TYPE   = "ATTRIBUTE_CONDITION";

  /**
   * Magic value for the json attribute name which specifies the id of the MdAttribute
   */
  public static final String MD_ATTRIBUTE_KEY = "mdAttribute";

  private String             mdAttributeId;

  public DashboardAttributeCondition(String mdAttributeId)
  {
    super();

    this.mdAttributeId = mdAttributeId;
  }

  public String getMdAttributeId()
  {
    return mdAttributeId;
  }

  public void setMdAttributeId(String definingMdAttributeId)
  {
    this.mdAttributeId = definingMdAttributeId;
  }

  @Override
  public String getJSONKey()
  {
    return this.getMdAttributeId();
  }
}
