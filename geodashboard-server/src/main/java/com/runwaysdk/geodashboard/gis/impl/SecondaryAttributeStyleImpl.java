package com.runwaysdk.geodashboard.gis.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.model.SecondaryAttributeStyleIF;
import com.runwaysdk.geodashboard.gis.persist.AllAggregationType;

public class SecondaryAttributeStyleImpl implements Reloadable, SecondaryAttributeStyleIF
{
  private MdClassDAOIF       mdClass;

  private String             attributeName;

  private AllAggregationType aggregation;

  private JSONArray          categories;

  public SecondaryAttributeStyleImpl(MdClassDAOIF mdClass, String attributeName, AllAggregationType aggregation, JSONArray categories)
  {
    this.mdClass = mdClass;
    this.attributeName = attributeName;
    this.aggregation = aggregation;
    this.categories = categories;
  }

  @Override
  public MdAttributeDAOIF getMdAttributeDAO()
  {
    return this.mdClass.definesAttribute(this.attributeName);
  }

  @Override
  public AllAggregationType getSingleAggregationType()
  {
    return this.aggregation;
  }

  @Override
  public JSONArray getCategoriesAsJSON() throws JSONException
  {
    return this.categories;
  }

  public static JSONObject createCategory(String key, String color)
  {
    try
    {
      JSONObject object = new JSONObject();
      object.put(SecondaryAttributeStyleIF.KEY, key);
      object.put(SecondaryAttributeStyleIF.COLOR, color);

      return object;
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

}
