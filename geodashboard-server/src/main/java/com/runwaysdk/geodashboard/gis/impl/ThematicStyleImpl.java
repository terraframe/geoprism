package com.runwaysdk.geodashboard.gis.impl;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.ThematicStyle;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;

public class ThematicStyleImpl extends StyleImpl implements ThematicStyle
{
  private Condition condition;

  private String    polygonMinFill;

  private String    polygonMaxFill;

  private Integer   pointMinSize;

  private Integer   pointMaxSize;

  private Integer   pointFixedSize;

  private Boolean   pointFixed;

  private String    styleCategories;

  private Boolean   bubbleContinuousSize;

  public ThematicStyleImpl()
  {
    super();

    this.condition = null;
  }

  @Override
  public Condition getCondition()
  {
    return this.condition;
  }

  public void setCondition(Condition condition)
  {
    this.condition = condition;
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public Integer getPointMinSize()
  {
    return pointMinSize;
  }

  @Override
  public void setPointMinSize(Integer pointMinSize)
  {
    this.pointMinSize = pointMinSize;
  }

  @Override
  public Integer getPointMaxSize()
  {
    return pointMaxSize;
  }

  @Override
  public void setPointMaxSize(Integer pointMaxSize)
  {
    this.pointMaxSize = pointMaxSize;
  }

  @Override
  public String getPolygonMinFill()
  {
    return this.polygonMinFill;
  }

  @Override
  public void setPolygonMinFill(String polygonMinFill)
  {
    this.polygonMinFill = polygonMinFill;
  }

  @Override
  public String getPolygonMaxFill()
  {
    return polygonMaxFill;
  }

  @Override
  public void setPolygonMaxFill(String fill)
  {
    this.polygonMaxFill = fill;
  }

  @Override
  public String getStyleCategories()
  {
    return styleCategories;
  }

  @Override
  public Boolean getBubbleContinuousSize()
  {
    return this.bubbleContinuousSize;
  }

  @Override
  public Integer getPointFixedSize()
  {
    return this.pointFixedSize;
  }

  @Override
  public Boolean getPointFixed()
  {
    return this.pointFixed;
  }
}
