/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.oda.driver;

import java.util.LinkedHashMap;
import java.util.Map;

import net.geoprism.report.BirtConstants;
import net.geoprism.report.PairViewDTO;
import net.geoprism.report.RemoteQuery;
import net.geoprism.report.RemoteQueryIF;
import net.geoprism.report.ReportItemDTO;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.ClientRequestIF;

public class QueryFacade
{
  /*
   * JSON contants
   */

  /**
   * Mapping to the action which should be executed
   */
  public static final String ACTION           = "ACTION";

  /**
   * Mapping to the parameters used by the action
   */
  public static final String PARAMETERS       = "PARAMETERS";

  /*
   * Actions supported by the driver
   */

  /**
   * Query for the potential data set queries
   */
  public static final String QUERIES          = "QUERIES";

  /**
   * Execute a query for a data set type to get actual values
   */
  public static final String QUERY            = "QUERY";

  /**
   * Execute a query to get all of the geo nodes associated with a query
   */
  public static final String GET_GEO_NODE     = "GEO_NODE";

  /**
   * Execute a query to get all of the geo entities associated with a query
   */
  public static final String GET_ENTITY       = "ENTITY";

  /**
   * Execute a query to get all of the supported aggregations associated with a query
   */
  public static final String GET_AGGREGATIONS = "AGGREGATIONS";

  /*
   * Parameter constants
   */

  /**
   * TYPE parameter used in the QUERY action
   */
  public static final String QUERY_ID         = "TYPE";

  /**
   * Geo entity text parameter
   */
  public static final String TEXT             = "TEXT";

  /*
   * Query text parameters
   */

  public static final String DEFAULT_GEO_ID   = BirtConstants.DEFAULT_GEO_ID;

  public static final String GEO_NODE_ID      = BirtConstants.GEO_NODE_ID;

  public static final String AGGREGATION      = BirtConstants.AGGREGATION;

  public IResultSet invoke(ClientRequestIF request, String query, Map<String, Object> parameters, boolean queryMetadata) throws OdaException
  {
    if (query != null)
    {
      try
      {
        JSONObject object = new JSONObject(query);
        String action = object.getString(ACTION);

        if (action.equals(QUERIES))
        {
          PairViewDTO[] results = ReportItemDTO.getQueriesForReporting(request);

          return new ArrayResultSet(results);
        }
        else if (action.equals(GET_ENTITY))
        {
          JSONObject params = object.getJSONObject(QueryFacade.PARAMETERS);

          String text = params.getString(QueryFacade.TEXT);

          return this.getDefaultGeoIds(request, text);
        }
        else if (action.equals(GET_AGGREGATIONS))
        {
          JSONObject params = object.getJSONObject(QueryFacade.PARAMETERS);

          String queryId = params.getString(QueryFacade.QUERY_ID);

          PairViewDTO[] results = ReportItemDTO.getSupportedAggregation(request, queryId);

          return new ArrayResultSet(results);
        }
        else if (action.equals(GET_GEO_NODE))
        {
          JSONObject params = object.getJSONObject(QueryFacade.PARAMETERS);

          String queryId = params.getString(QueryFacade.QUERY_ID);

          PairViewDTO[] results = ReportItemDTO.getSupportedGeoNodes(request, queryId);

          return new ArrayResultSet(results);
        }
        else if (action.equals(QUERY))
        {
          JSONObject params = object.getJSONObject(QueryFacade.PARAMETERS);

          String queryId = params.getString(QueryFacade.QUERY_ID);
          String aggregation = null;
          String defaultGeoId = null;
          String geoNodeId = null;

          if (params.has(BirtConstants.AGGREGATION))
          {
            aggregation = params.getString(BirtConstants.AGGREGATION);
          }

          if (params.has(BirtConstants.DEFAULT_GEO_ID))
          {
            defaultGeoId = params.getString(BirtConstants.DEFAULT_GEO_ID);
          }

          if (params.has(BirtConstants.GEO_NODE_ID))
          {
            geoNodeId = params.getString(BirtConstants.GEO_NODE_ID);
          }

          JSONObject context = getContext(parameters);
          context.put(BirtConstants.AGGREGATION, aggregation);
          context.put(BirtConstants.DEFAULT_GEO_ID, defaultGeoId);
          context.put(BirtConstants.GEO_NODE_ID, geoNodeId);

          return this.executeQuery(request, queryMetadata, queryId, context);
        }

        throw new OdaException("Unknown query action [" + action + "]");
      }
      catch (JSONException e)
      {
        throw new OdaException(e);
      }
    }

    throw new OdaException("Unable to execute an empty query");
  }

  private IResultSet executeQuery(ClientRequestIF request, boolean queryMetadata, String queryId, JSONObject context)
  {
    if (queryMetadata)
    {
      String json = ReportItemDTO.getMetadataForReporting(request, queryId, context.toString());
      RemoteQueryIF results = RemoteQuery.deserialize(json);

      return new ComponentQueryResultSet(results);
    }
    else
    {
      return new BlockQueryResultSet(request, queryId, context.toString());
    }
  }

  private IResultSet getDefaultGeoIds(ClientRequestIF request, String text)
  {
    PairViewDTO[] results = ReportItemDTO.getGeoEntitySuggestions(request, text, 10);

    return new ArrayResultSet(results);
  }

  private JSONObject getContext(Map<String, Object> parameters)
  {
    if (parameters.containsKey("context"))
    {
      String value = (String) parameters.get("context");

      if (value != null && value.length() > 0)
      {
        try
        {
          return new JSONObject(value);
        }
        catch (JSONException e)
        {
          throw new RuntimeException(e);
        }
      }
    }

    return new JSONObject();
  }

  public String getQueryId(String query) throws OdaException
  {
    try
    {
      JSONObject object = new JSONObject(query);
      String action = object.getString(ACTION);

      if (action.equals(QUERY))
      {
        JSONObject params = object.getJSONObject(QueryFacade.PARAMETERS);

        String queryId = params.getString(QueryFacade.QUERY_ID);

        return queryId;
      }

      throw new OdaException("Unable to determine the query oid from the query JSON [" + query + "]");
    }
    catch (JSONException e)
    {
      throw new OdaException(e);
    }
  }

  public String getAggregation(String query) throws OdaException
  {
    try
    {
      JSONObject object = new JSONObject(query);
      String action = object.getString(ACTION);

      if (action.equals(QUERY))
      {
        JSONObject params = object.getJSONObject(QueryFacade.PARAMETERS);

        if (params.has(BirtConstants.AGGREGATION))
        {
          return params.getString(BirtConstants.AGGREGATION);
        }

        return null;
      }

      throw new OdaException("Unable to determine the query oid from the query JSON [" + query + "]");
    }
    catch (JSONException e)
    {
      throw new OdaException(e);
    }
  }

  public IParameterMetaData getParameterMetaData(String query) throws OdaException
  {
    if (query != null)
    {
      try
      {
        JSONObject object = new JSONObject(query);
        String action = object.getString(ACTION);

        if (action.equals(QUERIES))
        {
          return new ParameterMetaData();
        }
        else if (action.equals(QUERY))
        {
          LinkedHashMap<String, IParameter> map = new LinkedHashMap<String, IParameter>();
          map.put("context", new StringParameter("context", IParameterMetaData.parameterNullable));

          return new ParameterMetaData(map);
        }

        throw new OdaException("Unknown query action [" + action + "]");
      }
      catch (JSONException e)
      {
        throw new OdaException(e);
      }
    }

    throw new OdaException("Unable to execute an empty query");
  }
}
