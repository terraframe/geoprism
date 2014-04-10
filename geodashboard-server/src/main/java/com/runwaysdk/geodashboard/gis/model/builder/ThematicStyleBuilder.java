package com.runwaysdk.geodashboard.gis.model.builder;

import com.runwaysdk.geodashboard.gis.model.ThematicStyle;
import com.runwaysdk.geodashboard.gis.model.impl.ThematicStyleImpl;

public class ThematicStyleBuilder extends StyleBuilder
{
  private ThematicStyle style;

  public ThematicStyleBuilder(LayerBuilder layerBuilder, String name)
  {
    super(layerBuilder);
    this.style = new ThematicStyleImpl();
    this.style.setName(name);
  }
  
  @Override
  public ThematicStyleBuilder pointFill(String fill)
  {
    // TODO Auto-generated method stub
    return (ThematicStyleBuilder) super.pointFill(fill);
  }
  
  @Override
  public ThematicStyleBuilder pointOpacity(Double opacity)
  {
    // TODO Auto-generated method stub
    return (ThematicStyleBuilder) super.pointOpacity(opacity);
  }
  
  @Override
  public ThematicStyleBuilder pointRotation(Integer rotation)
  {
    // TODO Auto-generated method stub
    return (ThematicStyleBuilder) super.pointRotation(rotation);
  }
  
  @Override
  public ThematicStyleBuilder pointSize(Integer size)
  {
    // TODO Auto-generated method stub
    return (ThematicStyleBuilder) super.pointSize(size);
  }
  
  @Override
  public ThematicStyleBuilder pointStroke(String stroke)
  {
    // TODO Auto-generated method stub
    return (ThematicStyleBuilder) super.pointStroke(stroke);
  }
  
  @Override
  public ThematicStyleBuilder pointStrokeWidth(Integer width)
  {
    // TODO Auto-generated method stub
    return (ThematicStyleBuilder) super.pointStrokeWidth(width);
  }
  
  @Override
  public ThematicStyleBuilder pointWellKnownName(String name)
  {
    // TODO Auto-generated method stub
    return (ThematicStyleBuilder) super.pointWellKnownName(name);
  }
  
  @Override
  public ThematicStyleBuilder polygonFill(String fill)
  {
    // TODO Auto-generated method stub
    return (ThematicStyleBuilder) super.polygonFill(fill);
  }
  
  @Override
  public ThematicStyleBuilder polygonStroke(String stroke)
  {
    // TODO Auto-generated method stub
    return (ThematicStyleBuilder) super.polygonStroke(stroke);
  }
  
  @Override
  public ThematicStyleBuilder polygonStrokeWidth(Integer width)
  {
    // TODO Auto-generated method stub
    return (ThematicStyleBuilder) super.polygonStrokeWidth(width);
  }

  public ThematicStyle getStyle()
  {
    return style;
  }

  public ThematicStyleBuilder attribute(String attribute)
  {
    this.getStyle().setAttribute(attribute);
    return this;
  }
  
}
