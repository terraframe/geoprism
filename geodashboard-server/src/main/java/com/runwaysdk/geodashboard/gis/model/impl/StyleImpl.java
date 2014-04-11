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
  
}
