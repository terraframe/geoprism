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

import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.SingleActor;

public class DashboardState extends DashboardStateBase 
{
  private static final long serialVersionUID = 1598060270;

  public DashboardState()
  {
    super();
  }

  public DashboardState clone(Dashboard dashboard)
  {
    DashboardState clone = new DashboardState();
    clone.setDashboard(dashboard);
    clone.setGeoprismUser(this.getGeoprismUser());
    clone.setConditions(this.getConditions());
    clone.setMapThumbnail(this.getMapThumbnail());
    clone.apply();

    return clone;
  }

  public static DashboardState getDashboardState(Dashboard dashboard, SingleActor user)
  {
    DashboardStateQuery query = new DashboardStateQuery(new QueryFactory());
    query.WHERE(query.getDashboard().EQ(dashboard));

    if (user != null)
    {
      query.AND(query.getGeoprismUser().EQ(user));
    }
    else
    {
      query.AND(query.getGeoprismUser().EQ((String) null));
    }

    OIterator<? extends DashboardState> it = query.getIterator();

    try
    {
      if (it.hasNext())
      {
        return it.next();
      }

      return null;
    }
    finally
    {
      it.close();
    }
  }
}
