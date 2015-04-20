package com.runwaysdk.geodashboard.gis.model;

import org.json.JSONArray;
import org.json.JSONException;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;
import com.runwaysdk.geodashboard.gis.persist.AllAggregationType;

public interface ThematicStyle extends Style
{
  public static final String VAL   = "val";

  public static final String COLOR = "color";

  public Condition getCondition();

  public Integer getPointMinSize();

  public void setPointMinSize(Integer size);

  public Integer getPointMaxSize();

  public void setPointMaxSize(Integer size);

  public String getPolygonMinFill();

  public void setPolygonMinFill(String fill);

  public String getPolygonMaxFill();

  public void setPolygonMaxFill(String fill);

  public String getStyleCategories();

  public Boolean getBubbleContinuousSize();

  public Integer getPointFixedSize();

  public Boolean getPointFixed();

  public MdAttributeDAOIF getSecondaryAttributeDAO();

  public AllAggregationType getSecondaryAttributeAggregationMethod();

  public JSONArray getSecondaryAttributeCategoriesAsJSON() throws JSONException;
}
