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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



public class DashboardTypeInfo 
{
  private List<String>        attributes;

  private List<String>        dashboardAttributes;

  private Integer             index;

  private Map<String, String> labels;

  private String              type;

  public DashboardTypeInfo(String _type, Integer _index)
  {
    this.type = _type;
    this.index = _index;
    this.labels = new HashMap<String, String>();
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

  public boolean isDashboardAttribute(String attributeName)
  {
    return this.dashboardAttributes.contains(attributeName);
  }

}
