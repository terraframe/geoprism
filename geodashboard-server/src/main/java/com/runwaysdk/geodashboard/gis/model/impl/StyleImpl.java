package com.runwaysdk.geodashboard.gis.model.impl;

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

  private String polygonFillOpacity;

  private Integer labelHaloWidth;
  
  
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
  public void enableValue(Boolean enable)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Boolean enabledValue()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setValueFont(String font)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String getValueFont()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setValueSize(Integer size)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Integer getValueSize()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setValueColor(String color)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String getValueColor()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setValueHalo(String halo)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String getValueHalo()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setValueHaloWidth(Integer width)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Integer getValueHaloWidth()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void enableLabel(Boolean enable)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Boolean enabledLabel()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setLabelFont(String font)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String getLabelFont()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setLabelSize(Integer size)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Integer getLabelSize()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setLabelColor(String color)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String getLabelColor()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setLabelHalo(String halo)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String getLabelHalo()
  {
    // TODO Auto-generated method stub
    return null;
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
  public String getPolygonFillOpacity()
  {
    return this.polygonFillOpacity;
  }

  @Override
  public void setPolygonFillOpacity(String opacity)
  {
    this.polygonFillOpacity = opacity;
  }
  
}
