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

import java.util.HashMap;

import com.runwaysdk.geodashboard.gis.model.AttributeType;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.ThematicLayer;

public class ThematicLayerImpl extends LayerImpl implements ThematicLayer
{
  private String                  attribute;

  private AttributeType           attributeType;

  private HashMap<String, Double> layerMinMax;

  public HashMap<String, Double> getLayerMinMax(String attribute)
  {
    return layerMinMax;
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public String getAttribute()
  {
    return this.attribute;
  }

  public void setAttribute(String attribute)
  {
    this.attribute = attribute;
  }

  @Override
  public AttributeType getAttributeType()
  {
    return this.attributeType;
  }

  public void setAttributeType(AttributeType attributeType)
  {
    this.attributeType = attributeType;
  }
}
