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

import java.util.HashMap;
import java.util.List;

import net.geoprism.dashboard.MetadataWrapper;

import com.runwaysdk.system.gis.geo.GeoNode;
import com.runwaysdk.system.metadata.MdClass;

public class GenericTypeProviderCache
{
  private static GenericTypeProviderCache instance;

  /**
   * List of MdClass objects which have geo nodes
   */
  private List<MdClass>                   classes;

  /**
   * Map of the GeoNode objectsz for each type
   */
  private HashMap<String, List<GeoNode>>  nodes;

  public GenericTypeProviderCache()
  {
    this.classes = MetadataWrapper.getMdClassesWithGeoNodes();
    this.nodes = new HashMap<String, List<GeoNode>>();
  }

  public List<MdClass> getMdClassesWithGeoNodes()
  {
    return classes;
  }

  public List<GeoNode> getGeoNodes(String type)
  {
    if (!this.nodes.containsKey(type))
    {
      this.nodes.put(type, MetadataWrapper.getGeoNodes(type));
    }

    return this.nodes.get(type);
  }

  public static synchronized GenericTypeProviderCache getInstance()
  {
    if (instance == null)
    {
      instance = new GenericTypeProviderCache();
    }

    return instance;
  }

}
