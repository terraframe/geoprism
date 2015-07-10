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
package com.runwaysdk.geodashboard.gis.geoserver;

import java.util.List;

import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.SessionParameterFacade;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverInitializer.SessionPredicate;
import com.runwaysdk.geodashboard.gis.persist.DashboardLayer;
import com.runwaysdk.geodashboard.util.Iterables;
import com.runwaysdk.session.Request;

public class LayerFacade implements Reloadable
{
  @Request
  public static void cleanupUnusedLayers()
  {
    cleanupUnusedLayers_Transaction();
  }

  @Transaction
  private static void cleanupUnusedLayers_Transaction()
  {
    List<String> viewNames = Database.getViewsByPrefix(DashboardLayer.DB_VIEW_PREFIX);

    new Iterables<String>().remove(viewNames, new SessionPredicate());

    for (String viewName : viewNames)
    {
      // First remove the geoserver layer
      GeoserverFacade.removeLayer(viewName);

      // Clear the session map
      String sessionId = DashboardLayer.getSessionId(viewName);
      SessionParameterFacade.clear(sessionId);
    }

    // Second delete the database views
    Database.dropViews(viewNames);
  }

}
