package com.runwaysdk.geodashboard.gis.model.builder;

import com.runwaysdk.geodashboard.gis.model.Style;
import com.runwaysdk.geodashboard.gis.model.impl.StyleImpl;

public class StyleBuilder
{
  private LayerBuilder lb;
  private Style style;
  
  protected StyleBuilder(LayerBuilder layerBuilder)
  {
    this.lb = layerBuilder;
  }
  
  public StyleBuilder(LayerBuilder layerBuilder, String name)
  {
    this(layerBuilder);
    style = new StyleImpl();
    layerBuilder.getLayer().addStyle(style);
  }
  
  public StyleBuilder pointSize(Integer size)
  {
    this.style.setPointSize(size);
    return this;
  }
  
  public StyleBuilder pointOpacity(Double opacity)
  {
    this.style.setPointOpacity(opacity);
    return this;
  }

  public StyleBuilder pointStrokeWidth(Integer width)
  {
    this.style.setPointStrokeWidth(width);
    return this;
  }
  
  public StyleBuilder pointRotation(Integer rotation)
  {
    this.style.setPointRotation(rotation);
    return this;
  }
  
  public StyleBuilder pointFill(String fill)
  {
    this.style.setPointFill(fill);
    return this;
  }
  
  public StyleBuilder pointStroke(String stroke)
  {
    this.style.setPointStroke(stroke);
    return this;
  }
  
  public StyleBuilder pointWellKnownName(String name)
  {
    this.style.setPointWellKnownName(name);
    return this;
  }
  
  public StyleBuilder polygonStrokeWidth(Integer width)
  {
    this.style.setPolygonStrokeWidth(width);
    return this;
  }

  public StyleBuilder polygonFill(String fill)
  {
    this.style.setPolygonFill(fill);
    return this;
  }
  
  public StyleBuilder polygonStroke(String stroke)
  {
    this.style.setPolygonStroke(stroke);
    return this;
  }
  
  protected Style getStyle()
  {
    return this.style;
  }
  
  public MapBuilder add()
  {
    return this.lb.add();
  }
}
