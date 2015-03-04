package com.runwaysdk.geodashboard.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.localization.LocalizationFacade;
import com.runwaysdk.geodashboard.parse.DateParseException;
import com.runwaysdk.query.GeneratedBusinessQuery;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class ReportProviderUtil implements Reloadable
{
  private static Log          log                 = LogFactory.getLog(ReportProviderUtil.class);

  /**
   * Condition type for restricting on global location
   */
  private static final String LOCATION_CONDITION  = "LOCATION_CONDITION";

  /**
   * Condition type for restricting on an attribute
   */
  private static final String ATTRIBUTE_CONDITION = "ATTRIBUTE_CONDITION";

  /**
   * Magic value for the json attribute name which specifies the id of the MdAttribute
   */
  private static final String MD_ATTRIBUTE        = "mdAttribute";

  /**
   * Magic value for the json attribute name which specifies the operation type
   */
  private static final String OPERATION           = "operation";

  /**
   * Magic value for the json attribute name which specifies the value
   */
  private static final String VALUE               = "value";

  /**
   * Magic value for the json attribute name which specifies the value
   */
  private static final String TYPE                = "type";

  public static GeoEntity getGeoEntity(String category, String defaultGeoId)
  {
    if (category != null && category.length() > 0)
    {
      try
      {
        return GeoEntity.getByKey(category);
      }
      catch (Exception e)
      {
        // Incoming data is bad, just use the default geo id
      }
    }

    return GeoEntity.getByKey(defaultGeoId);
  }

  private static void parseCondition(String _criteria, ReportConditionHandlerIF _handler)
  {
    try
    {
      if (_criteria != null)
      {
        JSONArray conditions = new JSONArray(_criteria);

        int length = conditions.length();

        for (int i = 0; i < length; i++)
        {
          JSONObject condition = conditions.getJSONObject(i);
          String operation = condition.getString(OPERATION);
          String value = condition.getString(VALUE);
          String conditionType = condition.getString(TYPE);

          if (conditionType.equals(ATTRIBUTE_CONDITION))
          {
            String mdAttributeId = condition.getString(MD_ATTRIBUTE);
            MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(mdAttributeId).getMdAttributeConcrete();

            _handler.handleAttributeCondition(mdAttribute, operation, value);
          }
          else if (conditionType.equals(LOCATION_CONDITION))
          {
            _handler.handleLocationCondition(value);
          }
        }
      }
    }
    catch (JSONException e)
    {
      log.error(e);
    }

  }

  public static void addConditions(String _criteria, String _type, GeneratedBusinessQuery _query, ValueQuery _vQuery)
  {
    ReportProviderUtil.parseCondition(_criteria, new ReportConditionHandler(_type, _vQuery, _query));
  }

  public static String getConditionInformation(String _criteria)
  {
    ConditionInformationHandler handler = new ConditionInformationHandler();

    ReportProviderUtil.parseCondition(_criteria, handler);

    return handler.getInformation();
  }

  public static Date parseDate(String source)
  {
    Locale locale = LocalizationFacade.getLocale();
    SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy", locale);

    try
    {
      Date date = format.parse(source);

      return date;
    }
    catch (ParseException cause)
    {
      DateParseException e = new DateParseException(cause);
      e.setInput(source);
      e.setPattern(format.toLocalizedPattern());
      throw e;
    }
  }
}
