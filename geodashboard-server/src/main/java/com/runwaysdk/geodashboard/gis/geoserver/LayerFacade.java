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
