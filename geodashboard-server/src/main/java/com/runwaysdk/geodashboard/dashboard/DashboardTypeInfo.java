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
package com.runwaysdk.geodashboard.dashboard;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.system.gis.geo.GeoNode;

public class DashboardTypeInfo implements Reloadable
{
  private List<String>        attributes;

  private List<String>        dashboardAttributes;

  private Integer             index;

  private Map<String, String> labels;

  private List<GeoNode>       nodes;

  private String              type;

  private String              universal;

  public DashboardTypeInfo(String _type, Integer _index, String _universal)
  {
    this.type = _type;
    this.index = _index;
    this.universal = _universal;
    this.labels = new HashMap<String, String>();
    this.nodes = new LinkedList<GeoNode>();
    this.attributes = new LinkedList<String>();
    this.dashboardAttributes = new LinkedList<String>();
  }

  public void add(String... attributeNames)
  {
    for (String attributeName : attributeNames)
    {
      this.dashboardAttributes.add(attributeName);
    }

    this.addViewOnlyAttribute(attributeNames);
  }

  public void addViewOnlyAttribute(String... attributeNames)
  {
    for (String attributeName : attributeNames)
    {
      this.attributes.add(attributeName);
    }
  }

  public String getType()
  {
    return type;
  }

  public String getUniversal()
  {
    return universal;
  }

  public Integer getIndex()
  {
    return index;
  }

  public void setIndex(Integer _index)
  {
    this.index = _index;
  }

  public List<String> getDashboardAttribute()
  {
    return dashboardAttributes;
  }

  public List<String> getAttributes()
  {
    return attributes;
  }

  public void setLabel(String _attributeName, String _label)
  {
    this.labels.put(_attributeName, _label);
  }

  public String getLabel(String _attributeName)
  {
    return this.labels.get(_attributeName);
  }

  public void addGeoNode(GeoNode node)
  {
    this.nodes.add(node);
  }

  public List<GeoNode> getNodes()
  {
    return nodes;
  }

  public boolean isDashboardAttribute(String attributeName)
  {
    return this.dashboardAttributes.contains(attributeName);
  }

}
