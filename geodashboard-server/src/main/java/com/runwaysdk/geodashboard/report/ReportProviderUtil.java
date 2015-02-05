package com.runwaysdk.geodashboard.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.localization.LocalizationFacade;
import com.runwaysdk.geodashboard.parse.DateParseException;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeMoment;
import com.runwaysdk.query.AttributeNumber;
import com.runwaysdk.query.GeneratedBusinessQuery;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class ReportProviderUtil implements Reloadable
{
  /**
   * Greater than comparison
   */
  public static final String GT = "gt";

  /**
   * Greater than or equal comparison
   */
  public static final String GE = "ge";

  /**
   * Less than comparison
   */
  public static final String LT = "lt";

  /**
   * Less than or equal comparison
   */
  public static final String LE = "le";

  /**
   * Equal comparison
   */
  public static final String EQ = "eq";

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

  public static void addConditions(String criteria, String type, GeneratedBusinessQuery query, ValueQuery vQuery)
  {
    try
    {
      if (criteria != null)
      {
        JSONArray conditions = new JSONArray(criteria);

        int length = conditions.length();

        for (int i = 0; i < length; i++)
        {
          JSONObject condition = conditions.getJSONObject(i);
          String mdAttributeId = condition.getString("mdAttribute");
          String operation = condition.getString("operation");
          String value = condition.getString("value");

          MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(mdAttributeId);

          String attributeName = mdAttribute.definesAttribute();
          String key = mdAttribute.getKey();

          if (key.startsWith(type))
          {
            Attribute attribute = query.get(attributeName);

            if (attribute instanceof AttributeNumber)
            {
              addNumberCondition(vQuery, operation, value, (AttributeNumber) attribute);
            }
            else if (attribute instanceof AttributeMoment)
            {
              addMomentCondition(vQuery, operation, value, (AttributeMoment) attribute);
            }
          }
        }
      }
    }
    catch (JSONException e)
    {
      // Invalid JSON: do nothing
    }
  }

  public static Date parseDate(String source)
  {
    Locale locale = LocalizationFacade.getLocale();
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", locale);

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

  public static void addMomentCondition(ValueQuery vQuery, String operation, String value, AttributeMoment attribute)
  {
    Date date = ReportProviderUtil.parseDate(value);

    if (operation.equals(GT))
    {
      vQuery.AND(attribute.GT(date));
    }
    else if (operation.equals(GE))
    {
      vQuery.AND(attribute.GE(date));
    }
    else if (operation.equals(LT))
    {
      vQuery.AND(attribute.LT(date));
    }
    else if (operation.equals(LE))
    {
      vQuery.AND(attribute.LE(date));
    }
    else if (operation.equals(EQ))
    {
      vQuery.AND(attribute.EQ(date));
    }
    else
    {
      UnsupportedComparisonException e = new UnsupportedComparisonException();
      e.setComparison(operation);

      throw e;
    }
  }

  public static void addNumberCondition(ValueQuery vQuery, String operation, String value, AttributeNumber attribute)
  {
    if (operation.equals(GT))
    {
      vQuery.AND(attribute.GT(value));
    }
    else if (operation.equals(GE))
    {
      vQuery.AND(attribute.GE(value));
    }
    else if (operation.equals(LT))
    {
      vQuery.AND(attribute.LT(value));
    }
    else if (operation.equals(LE))
    {
      vQuery.AND(attribute.LE(value));
    }
    else if (operation.equals(EQ))
    {
      vQuery.AND(attribute.EQ(value));
    }
    else
    {
      UnsupportedComparisonException e = new UnsupportedComparisonException();
      e.setComparison(operation);

      throw e;
    }
  }

}
