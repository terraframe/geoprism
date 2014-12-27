package com.runwaysdk.geodashboard.gis.impl;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.ThematicStyle;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;

public class ThematicStyleImpl extends StyleImpl implements ThematicStyle
{
  private String    attribute;

  private Condition condition;

  private String    polygonMinFill;

  private String    polygonMaxFill;

  private Integer   pointMinSize;

  private Integer   pointMaxSize;
  
  private String    styleCategory1;
  
  private String    styleCategory2;

  private String    styleCategory3;
  
  private String    styleCategory4;
  
  private String    styleCategory5;
  
  private String    styleCategoryFill1;
  
  private String    styleCategoryFill2;

  private String    styleCategoryFill3;
  
  private String    styleCategoryFill4;
  
  private String    styleCategoryFill5;

  public ThematicStyleImpl()
  {
    super();

    this.attribute = null;
    this.condition = null;
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
  public String getStyleCategory1()
  {
    return styleCategory1;
  }

  @Override
  public String getStyleCategoryFill1()
  {
    return styleCategoryFill1;
  }

  @Override
  public String getStyleCategory2()
  {
    return styleCategory2;
  }

  @Override
  public String getStyleCategoryFill2()
  {
    return styleCategoryFill2;
  }

  @Override
  public String getStyleCategory3()
  {
    return styleCategory3;
  }

  @Override
  public String getStyleCategoryFill3()
  {
    return styleCategoryFill3;
  }

  @Override
  public String getStyleCategory4()
  {
    return styleCategory4;
  }

  @Override
  public String getStyleCategoryFill4()
  {
    return styleCategoryFill4;
  }

  @Override
  public String getStyleCategory5()
  {
    return styleCategory5;
  }

  @Override
  public String getStyleCategoryFill5()
  {
    return styleCategoryFill5;
  }
}
