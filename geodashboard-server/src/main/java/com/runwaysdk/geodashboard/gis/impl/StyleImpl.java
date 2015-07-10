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

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.Style;

public class StyleImpl extends ComponentImpl implements Style
{
  private String name;
  
  private String polygonStroke;
  private Integer polygonStrokeWidth;
  private String polygonFill;
  private Double polygonFillOpacity;
  
  private String pointStroke;
  private Integer pointStrokeWidth;
  private Integer basicPointSize;
  private String pointWellKnownName;
  private String pointFill;
  private Double pointOpacity;
  private Integer pointRotation;
  private Double pointStrokeOpacity;
  
  private Integer labelHaloWidth;
  private Boolean enableValue;
  private String valueHalo;
  private String valueColor;
  private Integer valueSize;
  private String valueFont;
  private Integer valueHaloWidth;
  private Boolean enableLabel;
  private String labelHalo;
  private String labelColor;
  private Integer labelSize;
  private String labelFont;
  
  
  public StyleImpl()
  {
    super();
  }

  @Override
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public Double getPointStrokeOpacity()
  {
    return this.pointStrokeOpacity;
  }
  
  public void setPointStrokeOpacity(Double pointStrokeOpacity)
  {
    this.pointStrokeOpacity = pointStrokeOpacity;
  }
  
  public void setBasicPointSize(Integer pointSize)
  {
    this.basicPointSize = pointSize;
  }
  
  public void setPointStroke(String pointStroke)
  {
    this.pointStroke = pointStroke;
  }
  
  public void setPointStrokeWidth(Integer pointStrokeWidth)
  {
    this.pointStrokeWidth = pointStrokeWidth;
  }
  
  public void setPointWellKnownName(String pointWellKnownName)
  {
    this.pointWellKnownName = pointWellKnownName;
  }
  
  public Integer getBasicPointSize()
  {
    return basicPointSize;
  }
  
  @Override
  public String getPointStroke()
  {
    return pointStroke;
  }
  
  @Override
  public Integer getPointStrokeWidth()
  {
    return pointStrokeWidth;
  }
  
  @Override
  public String getPointWellKnownName()
  {
    return pointWellKnownName;
  }
  
  @Override
  public void setPointFill(String fill)
  {
    this.pointFill = fill;    
  }

  @Override
  public String getPointFill()
  {
    return this.pointFill;
  }

  @Override
  public void setPointOpacity(Double opacity)
  {
    this.pointOpacity = opacity;    
  }

  @Override
  public Double getPointOpacity()
  {
    return this.pointOpacity;
  }

  @Override
  public void setPointRotation(Integer rotation)
  {
    this.pointRotation = rotation;    
  }

  @Override
  public Integer getPointRotation()
  {
    return this.pointRotation;
  }
  
  
  public void setPolygonFill(String polygonFill)
  {
    this.polygonFill = polygonFill;
  }
  
  public void setPolygonStroke(String polygonStroke)
  {
    this.polygonStroke = polygonStroke;
  }
  
  public void setPolygonStrokeWidth(Integer polygonStrokeWidth)
  {
    this.polygonStrokeWidth = polygonStrokeWidth;
  }
  
  @Override
  public String getPolygonFill()
  {
    return polygonFill;
  }

  @Override
  public String getPolygonStroke()
  {
    return polygonStroke;
  }
  
  @Override
  public Integer getPolygonStrokeWidth()
  {
    return polygonStrokeWidth;
  }
  
  @Override
  public Double getPolygonFillOpacity()
  {
    return this.polygonFillOpacity;
  }

  @Override
  public void setPolygonFillOpacity(Double opacity)
  {
    this.polygonFillOpacity = opacity;
  }
  
  @Override
  public Double getPolygonStrokeOpacity()
  {
    return this.polygonFillOpacity;
  }
  
  @Override
  public void setPolygonStrokeOpacity(Double opacity)
  {
    this.polygonFillOpacity = opacity;
  }
  
  
  @Override
  public void setEnableValue(Boolean enable)
  {
    this.enableValue = enable;    
  }

  @Override
  public Boolean getEnableValue()
  {
    return this.enableValue;
  }

  @Override
  public void setValueFont(String font)
  {
    this.valueFont = font;    
  }

  @Override
  public String getValueFont()
  {
    return this.valueFont;
  }

  @Override
  public void setValueSize(Integer size)
  {
    this.valueSize = size; 
  }

  @Override
  public Integer getValueSize()
  {
    return this.valueSize;
  }

  @Override
  public void setValueColor(String color)
  {
    this.valueColor = color;    
  }

  @Override
  public String getValueColor()
  {
    return this.valueColor;
  }

  @Override
  public void setValueHalo(String halo)
  {
    this.valueHalo = halo;    
  }

  @Override
  public String getValueHalo()
  {
    return this.valueHalo;
  }

  @Override
  public void setValueHaloWidth(Integer width)
  {
    this.valueHaloWidth = width;    
  }

  @Override
  public Integer getValueHaloWidth()
  {
    return this.valueHaloWidth;
  }

  @Override
  public void setEnableLabel(Boolean enable)
  {
    this.enableLabel = enable;    
  }

  @Override
  public Boolean getEnableLabel()
  {
    return this.enableLabel;
  }

  @Override
  public void setLabelFont(String font)
  {
    this.labelFont = font;    
  }

  @Override
  public String getLabelFont()
  {
    return this.labelFont;
  }

  @Override
  public void setLabelSize(Integer size)
  {
    this.labelSize = size; 
  }

  @Override
  public Integer getLabelSize()
  {
    return this.labelSize;
  }

  @Override
  public void setLabelColor(String color)
  {
    this.labelColor = color;    
  }

  @Override
  public String getLabelColor()
  {
    return this.labelColor;
  }

  @Override
  public void setLabelHalo(String halo)
  {
    this.labelHalo = halo;    
  }

  @Override
  public String getLabelHalo()
  {
    return this.labelHalo;
  }

  @Override
  public void setLabelHaloWidth(Integer width)
  {
    this.labelHaloWidth = width;    
  }
  
  @Override
  public Integer getLabelHaloWidth()
  {
    return this.labelHaloWidth;
  }
  
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

}
