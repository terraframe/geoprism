/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
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
    object.put(QueryFacade.ACTION, QueryFacade.GET_AGGREGATIONS);
    object.put(QueryFacade.PARAMETERS, parameters);

    return object.toString();
  }

  public static String getGeoNodeQueryText(String queryId)
  {
    JSONObject parameters = new JSONObject();
    parameters.put(QueryFacade.QUERY_ID, queryId);

    JSONObject object = new JSONObject();
    object.put(QueryFacade.ACTION, QueryFacade.GET_GEO_NODE);
    object.put(QueryFacade.PARAMETERS, parameters);

    return object.toString();
  }

  public static String getDefaultGeoIdQueryText(String text)
  {
    JSONObject parameters = new JSONObject();
    parameters.put(QueryFacade.TEXT, text);

    JSONObject object = new JSONObject();
    object.put(QueryFacade.ACTION, QueryFacade.GET_ENTITY);
    object.put(QueryFacade.PARAMETERS, parameters);

    return object.toString();
  }

  public static String buildQueryText(String queryId, String aggregation, String defaultGeoId, String geoNodeId)
  {
    JSONObject parameters = new JSONObject();
    parameters.put(QueryFacade.QUERY_ID, queryId);
    parameters.put(QueryFacade.DEFAULT_GEO_ID, defaultGeoId);
    parameters.put(QueryFacade.GEO_NODE_ID, geoNodeId);

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

  public static String getDefaultGeoIdFromQueryText(String queryText)
  {
    JSONObject object = new JSONObject(queryText);
    JSONObject parameters = object.getJSONObject(QueryFacade.PARAMETERS);

    if (parameters.has(QueryFacade.DEFAULT_GEO_ID))
    {
      String aggregation = parameters.getString(QueryFacade.DEFAULT_GEO_ID);

      return aggregation;
    }

    return null;
  }

  public static String getGeoNodeIdFromQueryText(String queryText)
  {
    JSONObject object = new JSONObject(queryText);
    JSONObject parameters = object.getJSONObject(QueryFacade.PARAMETERS);

    if (parameters.has(QueryFacade.GEO_NODE_ID))
    {
      String geoNodeId = parameters.getString(QueryFacade.GEO_NODE_ID);

      return geoNodeId;
    }

    return null;
  }
}
