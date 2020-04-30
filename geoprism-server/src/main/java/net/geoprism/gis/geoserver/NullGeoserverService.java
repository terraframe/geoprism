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
package net.geoprism.gis.geoserver;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.geotools.data.ows.WMSCapabilities;

public class NullGeoserverService implements GeoserverService
{

  @Override
  public WMSCapabilities getCapabilities(String layer)
  {
    return null;
  }

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
  public void forceRemoveLayer(String layer)
  {

  }

  @Override
  public void forceRemoveLayer(String workspace, String layer)
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
  public boolean layerExists(String workspace, String layer)
  {
    return false;
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
  public boolean workspaceExists()
  {
    return false;
  }

  @Override
  public double[] getBBOX(String... views)
  {
    return new double[] { 0, 0, 0, 0 };
  }

  @Override
  public double[] getExpandedBBOX(List<String> views, double expandVal)
  {
    return new double[] { 0, 0, 0, 0 };
  }

  @Override
  public boolean publishS3GeoTIFF(String storeName, String url)
  {
    return true;
  }

  @Override
  public void removeCoverageStore(String storeName)
  {

  }
  
  @Override
  public void removeCoverageStore(String workspace, String storeName)
  {
    
  }

//  @Override
//  public void publishS3GeoTIFF(String storeName, String url)
//  {
//    
//  }

  @Override
  public void publishGeoTiff(String storeName, File geoTiff)
  {
    // TODO Auto-generated method stub
  }

  @Override
  public boolean workspaceExists(String workspace)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void removeWorkspace(String workspace)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void publishWorkspace(String workspace)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeStore(String workspace)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void publishStore(String workspace)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void publishGeoTiff(String workspace, String storeName, File geoTiff)
  {
    // TODO Auto-generated method stub

  }

}
