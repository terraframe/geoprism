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
import com.runwaysdk.geodashboard.report.ReportItemDTO;

public class QueryFacade
{
  /*
   * JSON contants
   */

  /**
   * Mapping to the action which should be executed
   */
  public static final String ACTION     = "ACTION";

  /**
   * Mapping to the parameters used by the action
   */
  public static final String PARAMETERS = "PARAMETERS";

  /*
   * Actions supported by the driver
   */

  /**
   * Query for the potential data set types
   */
  public static final String TYPES      = "TYPES";

  /**
   * Execute a query for a data set type to get actual values
   */
  public static final String QUERY      = "QUERY";

  /*
   * Parameter constants
   */

  /**
   * TYPE parameter used in the QUERY action
   */
  public static final String TYPE       = "TYPE";

  public IResultSet invoke(ClientRequestIF request, String query, Map<String, Object> parameters) throws OdaException
  {
    if (query != null)
    {
      try
      {
        JSONObject object = new JSONObject(query);
        String action = object.getString(ACTION);

        if (action.equals(TYPES))
        {
          ValueQueryDTO results = ReportItemDTO.getTypesForReporting(request);

          return new ComponentQueryResultSet(results);
        }
        else if (action.equals(QUERY))
        {
          JSONObject params = object.getJSONObject(QueryFacade.PARAMETERS);

          String type = params.getString(QueryFacade.TYPE);

          String category = (String) parameters.get("category");
          String criteria = (String) parameters.get("criteria");

          ValueQueryDTO results = ReportItemDTO.getValuesForReporting(request, type, category, criteria);

          return new ComponentQueryResultSet(results);
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

  public IParameterMetaData getParameterMetaData(String query) throws OdaException
  {
    if (query != null)
    {
      try
      {
        JSONObject object = new JSONObject(query);
        String action = object.getString(ACTION);

        if (action.equals(TYPES))
        {
          return new ParameterMetaData();
        }
        else if (action.equals(QUERY))
        {
          LinkedHashMap<String, IParameter> map = new LinkedHashMap<String, IParameter>();
          map.put("category", new StringParameter("category", IParameterMetaData.parameterNullable));
          map.put("criteria", new StringParameter("criteria", IParameterMetaData.parameterNullable));

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
