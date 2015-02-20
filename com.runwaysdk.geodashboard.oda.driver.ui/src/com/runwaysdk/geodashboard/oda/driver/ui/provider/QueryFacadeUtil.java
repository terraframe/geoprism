package com.runwaysdk.geodashboard.oda.driver.ui.provider;

import org.json.JSONObject;

import com.runwaysdk.geodashboard.oda.driver.QueryFacade;

public class QueryFacadeUtil
{
  public static String getDashboardQueryText()
  {
    JSONObject object = new JSONObject();
    object.put(QueryFacade.ACTION, QueryFacade.QUERIES);

    return object.toString();
  }

  public static String getSupportedAggregationQueryText(String queryId)
  {
    JSONObject parameters = new JSONObject();
    parameters.put(QueryFacade.QUERY_ID, queryId);

    JSONObject object = new JSONObject();
    object.put(QueryFacade.ACTION, QueryFacade.AGGREGATIONS);
    object.put(QueryFacade.PARAMETERS, parameters);

    return object.toString();
  }

  public static String getValuesQueryText(String queryId, String aggregation)
  {
    JSONObject parameters = new JSONObject();
    parameters.put(QueryFacade.QUERY_ID, queryId);

    if (aggregation != null && aggregation.length() > 0)
    {
      parameters.put(QueryFacade.AGGREGATION, aggregation);
    }

    JSONObject object = new JSONObject();
    object.put(QueryFacade.ACTION, QueryFacade.QUERY);
    object.put(QueryFacade.PARAMETERS, parameters);

    return object.toString();
  }

  public static String getQueryIdFromQueryText(String queryText)
  {
    JSONObject object = new JSONObject(queryText);
    JSONObject parameters = object.getJSONObject(QueryFacade.PARAMETERS);

    String type = parameters.getString(QueryFacade.QUERY_ID);

    return type;
  }

  public static String getAggregationFromQueryText(String queryText)
  {
    JSONObject object = new JSONObject(queryText);
    JSONObject parameters = object.getJSONObject(QueryFacade.PARAMETERS);

    if (parameters.has(QueryFacade.AGGREGATION))
    {
      String aggregation = parameters.getString(QueryFacade.AGGREGATION);

      return aggregation;
    }

    return null;
  }
}
