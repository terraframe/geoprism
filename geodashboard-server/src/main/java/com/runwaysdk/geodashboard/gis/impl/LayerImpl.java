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

import com.runwaysdk.geodashboard.gis.model.FeatureStrategy;
import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.geodashboard.gis.model.Layer;
import com.runwaysdk.geodashboard.gis.model.Style;

public abstract class LayerImpl extends ComponentImpl implements Layer
{
  private List<Style>             styles;

  private String                  name;

  private Boolean                 virtual;

  private FeatureType             featureType;

  private Boolean                 displayInLegend;

  private FeatureStrategy         featureStrategy;
  
  private Integer                 pointFixedSize;
  
  private Boolean                 pointFixed;

  public LayerImpl()
  {
    this.name = null;
    this.styles = new LinkedList<Style>();

    this.virtual = false;
  }

  @Override
  public void setName(String name)
  {
    this.name = name;
  }

  @Override
  public String getName()
  {
    return name;
  }

  public void addStyle(Style style)
  {
    this.styles.add(style);
  }

  @Override
  public List<Style> getStyles()
  {
    return this.styles;
  }

  public void setVirtual(Boolean virtual)
  {
    this.virtual = virtual;
  }

  @Override
  public Boolean getVirtual()
  {
    return this.virtual;
  }


  public void setFeatureType(FeatureType featureType)
  {
    this.featureType = featureType;
  }

  @Override
  public FeatureType getFeatureType()
  {
    return this.featureType;
  }

  @Override
  public Boolean getDisplayInLegend()
  {
    return this.displayInLegend;
  }

  public void setDisplayInLegend(Boolean display)
  {
    this.displayInLegend = display;
  }

  @Override
  public FeatureStrategy getFeatureStrategy()
  {
    return featureStrategy;
  }

  public void setFeatureStrategy(FeatureStrategy featureStrategy)
  {
    this.featureStrategy = featureStrategy;
  }

}
