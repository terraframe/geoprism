package com.runwaysdk.geodashboard.gis.impl;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.Style;

public class StyleImpl extends ComponentImpl implements Style
{
  private String name;
  
  private String polygonStroke;
  private Integer polygonStrokeWidth;
  private String polygonFill;
  
  private String pointStroke;
  private Integer pointStrokeWidth;
  private Integer pointSize;
  private String pointWellKnownName;

  private String pointFill;

  private Double pointOpacity;

  private Integer pointRotation;

  private Double polygonFillOpacity;

  private Integer labelHaloWidth;

  private Boolean enableValue;

  private String valueHalo;

  private String valueColor;

  private Double pointStrokeOpacity;
  
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
  
  @Override
  public Double getPointStrokeOpacity()
  {
    return this.pointStrokeOpacity;
  }
  
  public void setPointStrokeOpacity(Double pointStrokeOpacity)
  {
    this.pointStrokeOpacity = pointStrokeOpacity;
  }
  
  public void setPointSize(Integer pointSize)
  {
    this.pointSize = pointSize;
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
  public Integer getPointSize()
  {
    return pointSize;
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
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
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
  
}
