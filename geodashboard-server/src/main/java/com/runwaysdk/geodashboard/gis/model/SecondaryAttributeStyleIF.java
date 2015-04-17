package com.runwaysdk.geodashboard.gis.model;

import org.json.JSONArray;
import org.json.JSONException;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.persist.AllAggregationType;

public interface SecondaryAttributeStyleIF extends Reloadable
{
  public static final String KEY   = "key";

  public static final String COLOR = "color";

  public MdAttributeDAOIF getMdAttributeDAO();

  public AllAggregationType getAggregationMethod();

  public JSONArray getCategoriesAsJSON() throws JSONException;
}
