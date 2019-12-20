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

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.MdAttributeCharDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeNumberDAO;

import net.geoprism.dashboard.condition.DashboardCondition;
import net.geoprism.dashboard.condition.DashboardEqualCondition;
import net.geoprism.dashboard.condition.DashboardGreaterThanCondition;

public class MdAttributeView extends MdAttributeViewBase 
{
  private static final long serialVersionUID = 1311378616;

  public MdAttributeView()
  {
    super();
  }

  public JSONObject toJSON(DashboardCondition condition) throws JSONException
  {
    JSONObject object = new JSONObject();
    object.put("attributeType", this.getAttributeType());
    object.put("mdAttributeId", this.getMdAttributeId());
    object.put("attributeName", this.getAttributeName());
    object.put("label", this.getDisplayLabel());

    if (condition != null)
    {
      object.put("filter", condition.getJSON());
    }
    else
    {
      MdAttributeConcreteDAOIF mdAttributeConcrete = MdAttributeDAO.get(this.getMdAttributeId()).getMdAttributeConcrete();

      JSONObject filter = new JSONObject();

      if (mdAttributeConcrete instanceof MdAttributeNumberDAO)
      {
        filter.put(DashboardCondition.OPERATION_KEY, DashboardGreaterThanCondition.OPERATION);
      }
      else if (mdAttributeConcrete instanceof MdAttributeCharDAOIF)
      {
        filter.put(DashboardCondition.OPERATION_KEY, DashboardEqualCondition.OPERATION);
      }

      object.put("filter", filter);
    }

    return object;
  }
}
