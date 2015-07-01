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
package com.runwaysdk.geodashboard.gis.impl;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.geodashboard.gis.model.Layer;
import com.runwaysdk.geodashboard.gis.model.Map;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;

public class MapImpl extends ComponentImpl implements Map
{
  private List<Layer> layers;
  
  private String name;
  
  public MapImpl()
  {
    this.name = null;
    this.layers = new LinkedList<Layer>(); 
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  public String getName()
  {
    return name;
  }
  
  public void addLayer(Layer layer)
  {
    this.layers.add(layer);
  }
  
  @Override
  public List<Layer> getLayers()
  {
    return layers;
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }
}
