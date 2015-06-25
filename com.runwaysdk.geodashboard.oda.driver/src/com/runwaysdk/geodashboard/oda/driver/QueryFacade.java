package com.runwaysdk.geodashboard.oda.driver;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.geodashboard.report.BirtConstants;
import com.runwaysdk.geodashboard.report.PairViewDTO;
import com.runwaysdk.geodashboard.report.ReportItemDTO;

public class QueryFacade
{
  /*
   * JSON contants
   */

  /**
   * Mapping to the action which should be executed
   */
  public static final String ACTION         = "ACTION";

  /**
   * Mapping to the parameters used by the action
   */
  public static final String PARAMETERS     = "PARAMETERS";

  /*
   * Actions supported by the driver
   */

  /**
   * Query for the potential data set queries
   */
  public static final String QUERIES        = "QUERIES";

  /**
   * Query for the potential data set aggregation levels
   */
  public static final String AGGREGATIONS   = "AGGREGATIONS";

  /**
   * Query for all the geo entities
   */
  public static final String ENTITY         = "ENTITY";

  /**
   * Execute a query for a data set type to get actual values
   */
  public static final String QUERY          = "QUERY";

  /*
   * Parameter constants
   */

  /**
   * TYPE parameter used in the QUERY action
   */
  public static final String QUERY_ID       = "TYPE";

  /**
   * Aggregation parameter used in the QUERY action
   */
  public static final String AGGREGATION    = "AGGREGATION";

  /**
   * Geo entity text parameter used in the
   */
  public static final String TEXT           = "TEXT";

  public static final String DEFAULT_GEO_ID = "DEFAULT_GEO_ID";

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
        else if (action.equals(ENTITY))
        {
          JSONObject params = object.getJSONObject(QueryFacade.PARAMETERS);

          String text = params.getString(QueryFacade.TEXT);

          return this.getEntitySuggestions(request, text);
        }
        else if (action.equals(AGGREGATIONS))
        {
          JSONObject params = object.getJSONObject(QueryFacade.PARAMETERS);

          String queryId = params.getString(QueryFacade.QUERY_ID);

          PairViewDTO[] results = ReportItemDTO.getSupportedAggregation(request, queryId);

          return new ArrayResultSet(results);
        }
        else if (action.equals(QUERY))
        {
          JSONObject params = object.getJSONObject(QueryFacade.PARAMETERS);

          String queryId = params.getString(QueryFacade.QUERY_ID);
          String aggregation = null;
          String defaultGeoId = null;

          if (params.has(QueryFacade.AGGREGATION))
          {
            aggregation = params.getString(QueryFacade.AGGREGATION);
          }

          if (params.has(QueryFacade.DEFAULT_GEO_ID))
          {
            defaultGeoId = params.getString(QueryFacade.DEFAULT_GEO_ID);
          }

          JSONObject context = getContext(parameters);
          context.put(BirtConstants.AGGREGATION, aggregation);
          context.put(BirtConstants.DEFAULT_GEO_ID, defaultGeoId);

          if (queryMetadata)
          {
            ValueQueryDTO results = ReportItemDTO.getMetadataForReporting(request, queryId, context.toString());

            return new ComponentQueryResultSet(results);
          }
          else
          {
            return new BlockQueryResultSet(request, queryId, context.toString());
          }
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

  private IResultSet getEntitySuggestions(ClientRequestIF request, String text)
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
        return new JSONObject(value);
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

      throw new OdaException("Unable to determine the query id from the query JSON [" + query + "]");
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

        if (params.has(QueryFacade.AGGREGATION))
        {
          return params.getString(QueryFacade.AGGREGATION);
        }

        return null;
      }

      throw new OdaException("Unable to determine the query id from the query JSON [" + query + "]");
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
