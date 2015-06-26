package com.runwaysdk.geodashboard.gis.impl;

import org.json.JSONArray;
import org.json.JSONException;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.ThematicStyle;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;
import com.runwaysdk.geodashboard.gis.persist.AllAggregationType;

public class ThematicStyleImpl extends StyleImpl implements ThematicStyle
{
  private Condition          condition;

  private String             gradientPolygonMinFill;
  private String             gradientPolygonMaxFill;
  
  private Integer            pointMinSize;
  private Integer            pointMaxSize;
//  private Integer            pointRadius;

  private String             categoryPolygonStyles;

  private Boolean            bubbleContinuousSize;
  private String bubbleStroke;
  private Integer bubbleStrokeWidth;
  private Integer bubbleSize;
  private String bubbleWellKnownName;
  private String bubbleFill;
  private Double bubbleOpacity;
  private Integer bubbleRotation;
  private Double bubbleStrokeOpacity;

  private MdAttributeDAOIF   secondaryAttribute;
  private AllAggregationType secondaryAttributeAggregationMethod;
  private JSONArray          secondaryAttributeCategories;

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
  public Integer getBubbleMinSize()
  {
    return pointMinSize;
  }
  
  @Override
  public Double getBubbleStrokeOpacity()
  {
    return this.bubbleStrokeOpacity;
  }
  
  public void setBubbleStrokeOpacity(Double bubbleStrokeOpacity)
  {
    this.bubbleStrokeOpacity = bubbleStrokeOpacity;
  }
  
  public void setBubbleSize(Integer bubbleSize)
  {
    this.bubbleSize = bubbleSize;
  }
  
  public void setBubbleStroke(String bubbleStroke)
  {
    this.bubbleStroke = bubbleStroke;
  }
  
  public void setBubbleStrokeWidth(Integer bubbleStrokeWidth)
  {
    this.bubbleStrokeWidth = bubbleStrokeWidth;
  }
  
  public void setBubbleWellKnownName(String bubbleWellKnownName)
  {
    this.bubbleWellKnownName = bubbleWellKnownName;
  }
  
  @Override
  public Integer getBubbleSize()
  {
    return bubbleSize;
  }
  
  @Override
  public String getBubbleStroke()
  {
    return bubbleStroke;
  }
  
  @Override
  public Integer getBubbleStrokeWidth()
  {
    return bubbleStrokeWidth;
  }
  
  @Override
  public String getBubbleWellKnownName()
  {
    return bubbleWellKnownName;
  }
  
  @Override
  public void setBubbleFill(String fill)
  {
    this.bubbleFill = fill;    
  }

  @Override
  public String getBubbleFill()
  {
    return this.bubbleFill;
  }

  @Override
  public void setBubbleOpacity(Double opacity)
  {
    this.bubbleOpacity = opacity;    
  }

  @Override
  public Double getBubbleOpacity()
  {
    return this.bubbleOpacity;
  }

  @Override
  public void setBubbleRotation(Integer rotation)
  {
    this.bubbleRotation = rotation;    
  }

  @Override
  public Integer getBubbleRotation()
  {
    return this.bubbleRotation;
  }

  @Override
  public void setBubbleMinSize(Integer pointMinSize)
  {
    this.pointMinSize = pointMinSize;
  }

  @Override
  public Integer getBubbleMaxSize()
  {
    return pointMaxSize;
  }

  @Override
  public void setBubbleMaxSize(Integer pointMaxSize)
  {
    this.pointMaxSize = pointMaxSize;
  }

  @Override
  public String getGradientPolygonMinFill()
  {
    return this.gradientPolygonMinFill;
  }

  public void setGradientPolygonMinFill(String gradientPolygonMinFill)
  {
    this.gradientPolygonMinFill = gradientPolygonMinFill;
  }

  
  public String getGradientPolygonMaxFill()
  {
    return gradientPolygonMaxFill;
  }

  public void setGradientPolygonMaxFill(String fill)
  {
    this.gradientPolygonMaxFill = fill;
  }

  public String getCategoryPolygonStyles()
  {
    return categoryPolygonStyles;
  }

  @Override
  public Boolean getBubbleContinuousSize()
  {
    return this.bubbleContinuousSize;
  }

//  @Override
//  public Integer getPointRadius()
//  {
//    return this.pointRadius;
//  }

  @Override
  public MdAttributeDAOIF getSecondaryAttributeDAO()
  {
    return this.secondaryAttribute;
  }

  public void setSecondaryAttribute(MdAttributeDAOIF secondaryAttribute)
  {
    this.secondaryAttribute = secondaryAttribute;
  }

  @Override
  public AllAggregationType getSecondaryAttributeAggregationMethod()
  {
    return this.secondaryAttributeAggregationMethod;
  }

  public void setSecondaryAttributeAggregationMethod(AllAggregationType secondaryAttributeAggregationMethod)
  {
    this.secondaryAttributeAggregationMethod = secondaryAttributeAggregationMethod;
  }

  @Override
  public JSONArray getSecondaryAttributeCategoriesAsJSON() throws JSONException
  {
    return this.secondaryAttributeCategories;
  }

  public void setSecondaryAttributeCategories(JSONArray secondaryAttributeCategories)
  {
    this.secondaryAttributeCategories = secondaryAttributeCategories;
  }
}
