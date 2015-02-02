package com.runwaysdk.geodashboard.report;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeMoment;
import com.runwaysdk.query.AttributeNumber;
import com.runwaysdk.query.GeneratedBusinessQuery;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class ReportProviderUtil
{
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

  public static void addMomentCondition(ValueQuery vQuery, String operation, String value, AttributeMoment attribute)
  {
    if (operation.equals("gt"))
    {
      vQuery.AND(attribute.GT(value));
    }
    else if (operation.equals("ge"))
    {
      vQuery.AND(attribute.GE(value));
    }
    else if (operation.equals("lt"))
    {
      vQuery.AND(attribute.LT(value));
    }
    else if (operation.equals("le"))
    {
      vQuery.AND(attribute.LE(value));
    }
    else
    {
      throw new RuntimeException("Unsupported moment comparision : [" + operation + "]");
    }
  }

  public static void addNumberCondition(ValueQuery vQuery, String operation, String value, AttributeNumber attribute)
  {
    if (operation.equals("gt"))
    {
      vQuery.AND(attribute.GT(value));
    }
    else if (operation.equals("ge"))
    {
      vQuery.AND(attribute.GE(value));
    }
    else if (operation.equals("lt"))
    {
      vQuery.AND(attribute.LT(value));
    }
    else if (operation.equals("le"))
    {
      vQuery.AND(attribute.LE(value));
    }
    else
    {
      throw new RuntimeException("Unsupported number comparision : [" + operation + "]");
    }
  }

}
