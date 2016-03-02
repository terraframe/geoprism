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
import java.util.LinkedList;
import java.util.List;

public class NullGeoserverService implements GeoserverService
{

  @Override
  public void refresh()
  {

  }

  @Override
  public void removeStore()
  {

  }

  @Override
  public void removeWorkspace()
  {

  }

  @Override
  public void publishWorkspace()
  {

  }

  @Override
  public boolean geoserverExists()
  {

    return true;
  }

  @Override
  public void publishStore()
  {

  }

  @Override
  public boolean styleExists(String styleName)
  {

    return true;
  }

  @Override
  public boolean cacheExists(String cacheName)
  {

    return true;
  }

  @Override
  public List<String> getLayers()
  {

    return new LinkedList<String>();
  }

  @Override
  public List<String> getStyles()
  {

    return new LinkedList<String>();
  }

  @Override
  public void removeStyle(String styleName)
  {

  }

  @Override
  public boolean publishStyle(String styleName, String body, boolean force)
  {

    return true;
  }

  @Override
  public boolean publishStyle(String styleName, String body)
  {

    return true;
  }

  @Override
  public void pushUpdates(GeoserverBatch batch)
  {

  }

  @Override
  public boolean publishLayer(String layer, String styleName)
  {

    return true;
  }

  @Override
  public void removeLayer(String layer)
  {

  }

  @Override
  public void publishCache(String layer)
  {

  }

  @Override
  public File[] getCaches()
  {

    return new File[0];
  }

  @Override
  public void removeCache(File cache)
  {

  }

  @Override
  public void removeCache(String cacheName)
  {

  }

  @Override
  public boolean layerExists(String layer)
  {

    return false;
  }

  @Override
  public boolean viewExists(String viewName)
  {

    return false;
  }

  @Override
  public double[] getBBOX(String... views)
  {
    return new double[] { 0, 0, 0, 0 };
  }

}
