package com.runwaysdk.geodashboard.oda.driver.ui.provider;

import org.json.JSONObject;

import com.runwaysdk.geodashboard.oda.driver.QueryFacade;

public class QueryFacadeUtil
{
  public static String getDashboardQueryText()
  {
    JSONObject object = new JSONObject();
    object.put(QueryFacade.ACTION, QueryFacade.TYPES);

    return object.toString();
  }

  public static String getValuesQueryText(String type)
  {
    JSONObject parameters = new JSONObject();
    parameters.put(QueryFacade.TYPE, type);

    JSONObject object = new JSONObject();
    object.put(QueryFacade.ACTION, QueryFacade.QUERY);
    object.put(QueryFacade.PARAMETERS, parameters);

    return object.toString();
  }

  public static String getTypeFromQueryText(String queryText)
  {
    JSONObject object = new JSONObject(queryText);
    JSONObject parameters = object.getJSONObject(QueryFacade.PARAMETERS);

    String type = parameters.getString(QueryFacade.TYPE);

    return type;
  }
}
