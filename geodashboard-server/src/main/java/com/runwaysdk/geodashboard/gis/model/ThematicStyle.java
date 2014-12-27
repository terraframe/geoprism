package com.runwaysdk.geodashboard.gis.model;

import com.runwaysdk.geodashboard.gis.model.condition.Condition;


public interface ThematicStyle extends Style
{
  public String getAttribute();
  
  public Condition getCondition();
  
  public Integer getPointMinSize();
  
  public void setPointMinSize(Integer size);
  
  public Integer getPointMaxSize();
  
  public void setPointMaxSize(Integer size);
  
  public String getPolygonMinFill();
  
  public void setPolygonMinFill(String fill);
  
  public String getPolygonMaxFill();

  public void setPolygonMaxFill(String fill);
  
  public String getStyleCategory1();
  public String getStyleCategoryFill1();
  
  public String getStyleCategory2();
  public String getStyleCategoryFill2();
  
  public String getStyleCategory3();
  public String getStyleCategoryFill3();
  
  public String getStyleCategory4();
  public String getStyleCategoryFill4();
  
  public String getStyleCategory5();
  public String getStyleCategoryFill5();
  
}
