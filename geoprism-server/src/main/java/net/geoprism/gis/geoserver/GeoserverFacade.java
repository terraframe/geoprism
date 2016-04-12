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
package net.geoprism.gis.geoserver;

import java.io.File;
import java.util.List;

import com.runwaysdk.generation.loader.Reloadable;

public class GeoserverFacade implements Reloadable
{
  public static final String      GEOM_COLUMN = "geom";

  public static int               MINX_INDEX  = 0;

  public static int               MINY_INDEX  = 1;

  public static int               MAXX_INDEX  = 2;

  public static int               MAXY_INDEX  = 3;

  private static GeoserverService service     = new GeoserverRestService();

  public synchronized static GeoserverService getService()
  {
    return service;
  }

  public synchronized static void setService(GeoserverService _service)
  {
    service = _service;
  }

  public static void refresh()
  {
    getService().refresh();
  }

  public static void removeStore()
  {
    getService().removeStore();
  }

  public static void removeWorkspace()
  {
    getService().removeWorkspace();
  }

  public static void publishWorkspace()
  {
    getService().publishWorkspace();
  }

  /**
   * Checks if Geoserver is available.
   * 
   * @return
   */
  public static boolean geoserverExists()
  {
    return getService().geoserverExists();
  }

  public static void publishStore()
  {
    getService().publishStore();
  }

  /**
   * Checks if the given style exists in geoserver.
   * 
   * @param styleName
   * @return
   */
  public static boolean styleExists(String styleName)
  {
    return getService().styleExists(styleName);
  }

  /**
   * Checks if the cache directory exists. This method does not check what tiles or zoom levels have been cached.
   * 
   * @param cacheName
   * @return
   */
  public static boolean cacheExists(String cacheName)
  {
    return getService().cacheExists(cacheName);
  }

  /**
   * Gets all layers declared in Geoserver for the workspace.
   * 
   * @return
   */
  public static List<String> getLayers()
  {
    return getService().getLayers();
  }

  /**
   * Gets all styles declared in Geoserver for the workspace.
   */
  public static List<String> getStyles()
  {
    return getService().getStyles();
  }

  /**
   * Removes the style defined in Geoserver, including the .sld and .xml file artifacts.
   * 
   * @param styleName
   *          The name of the style to delete.
   */
  public static void removeStyle(String styleName)
  {
    getService().removeStyle(styleName);
  }

  public static boolean publishStyle(String styleName, String body, boolean force)
  {
    return getService().publishStyle(styleName, body, force);
  }

  /**
   * Publishes the SQL of the given name with the XML body.
   * 
   * @param styleName
   * @param body
   */
  public static boolean publishStyle(String styleName, String body)
  {
    return getService().publishStyle(styleName, body);
  }

  public static void pushUpdates(GeoserverBatch batch)
  {
    getService().pushUpdates(batch);
  }

  /**
   * Adds a database view and publishes the layer if necessary.
   * 
   * @param layer
   */
  public static boolean publishLayer(String layer, String styleName)
  {
    return getService().publishLayer(layer, styleName);
  }

  /**
   * Removes the layer from geoserver.
   * 
   * @param layer
   * @return
   */
  public static void removeLayer(String layer)
  {
    getService().removeLayer(layer);
  }

  public static void publishCache(String layer)
  {
    getService().publishCache(layer);
  }

  /**
   * Returns a list of all cache directories.
   * 
   * @return
   */
  public static File[] getCaches()
  {
    return getService().getCaches();
  }

  public static void removeCache(File cache)
  {
    getService().removeCache(cache);
  }

  /**
   * Removes all cache files and directories.
   * 
   * @param cacheName
   */
  public static void removeCache(String cacheName)
  {
    getService().removeCache(cacheName);
  }

  /**
   * Checks if the given layer exists in Geoserver.
   * 
   * @param layer
   * @return
   */
  public static boolean layerExists(String layer)
  {
    return getService().layerExists(layer);
  }

  /**
   * Checks if the given layer has a database view.
   * 
   * @param layer
   * @return
   */
  public static boolean viewExists(String viewName)
  {
    return getService().viewExists(viewName);
  }

  /**
   * Calculates the bounding box of all the layers.
   * 
   * @param views
   * @return double[] {minx, miny, maxx, maxy}
   */
  public static double[] getBBOX(String... views)
  {
    return getService().getBBOX(views);
  }

  /**
   * Calculates the bounding box of all the layers.
   * 
   * @param views
   * @return double[] {minx, miny, maxx, maxy}
   */
  public static double[] getExpandedBBOX(List<String> views, double expandVal)
  {
    return getService().getExpandedBBOX(views, expandVal);
  }
  
}
