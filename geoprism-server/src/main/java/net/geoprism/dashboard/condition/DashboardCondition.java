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
package net.geoprism.dashboard.condition;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.geoprism.localization.LocalizationFacade;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.ValueQuery;

public abstract class DashboardCondition implements Reloadable
{
  /**
   * Magic value for the json attribute name which specifies the operation type
   */
  public static final String OPERATION_KEY = "operation";

  /**
   * Magic value for the json attribute name which specifies the value
   */
  public static final String VALUE_KEY     = "value";

  /**
   * Magic value for the json attribute name which specifies the value
   */
  public static final String TYPE_KEY      = "type";

  public abstract JSONObject getJSON();

  public abstract String getJSONKey();

  public abstract List<String> getConditionInformation();

  public abstract void restrictQuery(ValueQuery vQuery, Attribute attribute);

  public abstract void restrictQuery(String _type, ValueQuery _vQuery, GeneratedComponentQuery _query);

  public DashboardCondition()
  {
    super();
  }

  protected String handleCondition(String label, String operation, String value)
  {
    String message = LocalizationFacade.getFromBundles("attribute.condition");
    message = message.replace("{0}", label);
    message = message.replace("{1}", operation);
    message = message.replace("{2}", value);

    return message;
  }

  public static String serialize(DashboardCondition[] conditions)
  {
    JSONArray array = new JSONArray();

    for (DashboardCondition condition : conditions)
    {
      array.put(condition.getJSON());
    }

    return array.toString();
  }

  public static DashboardCondition[] deserialize(String json)
  {
    try
    {
      JSONArray criteria = new JSONArray(json);

      List<DashboardCondition> conditions = new LinkedList<DashboardCondition>();

      for (int i = 0; i < criteria.length(); i++)
      {
        DashboardCondition condition = null;

        JSONObject object = criteria.getJSONObject(i);
        String type = object.getString(TYPE_KEY);

        if (type.equals(DateCondition.CONDITION_TYPE))
        {
          String mdAttributeId = object.getString(DateCondition.MD_ATTRIBUTE_KEY);
          String startDate = ( object.has(DateCondition.START_DATE) ? object.getString(DateCondition.START_DATE) : null );
          String endDate = ( object.has(DateCondition.END_DATE) ? object.getString(DateCondition.END_DATE) : null );

          condition = new DateCondition(mdAttributeId, startDate, endDate);
        }
        else if (type.equals(ClassifierCondition.CONDITION_TYPE))
        {
          String value = object.getString(VALUE_KEY);
          String mdAttributeId = object.getString(ClassifierCondition.MD_ATTRIBUTE_KEY);

          if (value != null && !value.equals("[]"))
          {
            condition = new ClassifierCondition(mdAttributeId, value);
          }
        }
        else if (type.equals(LocationCondition.CONDITION_TYPE))
        {
          if (object.has(LocationCondition.VALUE_KEY))
          {
            String value = object.getString(LocationCondition.VALUE_KEY);

            condition = new LocationCondition(value);
          }
          else if (object.has(LocationCondition.LOCATIONS_KEY))
          {
            String value = object.getString(LocationCondition.LOCATIONS_KEY);

            if (value != null && !value.equals("[]"))
            {
              condition = new LocationCondition(value);
            }
          }
        }
        else if (object.has(OPERATION_KEY) && object.has(VALUE_KEY))
        {
          String operation = object.getString(OPERATION_KEY);
          String value = object.getString(VALUE_KEY);

          if (type.equals(DashboardAttributeCondition.CONDITION_TYPE))
          {
            String mdAttributeId = object.getString(DashboardAttributeCondition.MD_ATTRIBUTE_KEY);

            if (operation.equals(DashboardGreaterThanCondition.OPERATION))
            {
              condition = new DashboardGreaterThanCondition(mdAttributeId, value);
            }
            else if (operation.equals(DashboardGreaterThanOrEqualCondition.OPERATION))
            {
              condition = new DashboardGreaterThanOrEqualCondition(mdAttributeId, value);
            }
            else if (operation.equals(DashboardLessThanCondition.OPERATION))
            {
              condition = new DashboardLessThanCondition(mdAttributeId, value);
            }
            else if (operation.equals(DashboardLessThanOrEqualCondition.OPERATION))
            {
              condition = new DashboardLessThanOrEqualCondition(mdAttributeId, value);
            }
            else if (operation.equals(DashboardNotEqualCondition.OPERATION))
            {
              condition = new DashboardNotEqualCondition(mdAttributeId, value);
            }
            else
            {
              condition = new DashboardEqualCondition(mdAttributeId, value);
            }
          }
        }

        if (condition != null)
        {
          conditions.add(condition);
        }
      }

      return conditions.toArray(new DashboardCondition[conditions.size()]);
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private static JSONArray parseConditions(String state) throws JSONException
  {
    JSONArray conditions = new JSONArray();

    JSONObject object = new JSONObject(state);

    if (object.has("location"))
    {
      JSONObject condition = object.getJSONObject("location");

      if (!condition.has(TYPE_KEY))
      {
        condition.put(TYPE_KEY, LocationCondition.CONDITION_TYPE);
      }

      if (!condition.has(OPERATION_KEY))
      {
        condition.put(OPERATION_KEY, "eq");
      }

      if (!isEmpty(condition))
      {
        conditions.put(condition);
      }
    }

    if (object.has("types"))
    {
      JSONArray types = object.getJSONArray("types");

      for (int i = 0; i < types.length(); i++)
      {
        JSONObject type = types.getJSONObject(i);
        JSONArray attributes = type.getJSONArray("attributes");

        for (int j = 0; j < attributes.length(); j++)
        {
          JSONObject attribute = attributes.getJSONObject(j);

          JSONObject condition = attribute.getJSONObject("filter");

          if (!isEmpty(condition))
          {
            if (!condition.has(TYPE_KEY))
            {
              condition.put(TYPE_KEY, DashboardAttributeCondition.CONDITION_TYPE);
            }

            if (!condition.has(DashboardPrimitiveCondition.MD_ATTRIBUTE_KEY))
            {
              String mdAttributeId = attribute.getString("mdAttributeId");

              condition.put(DashboardPrimitiveCondition.MD_ATTRIBUTE_KEY, mdAttributeId);
            }

            conditions.put(condition);
          }
        }
      }
    }

    return conditions;
  }

  @SuppressWarnings("unchecked")
  private static boolean isEmpty(JSONObject condition) throws JSONException
  {
    Iterator<String> it = condition.keys();

    while (it.hasNext())
    {
      String key = it.next();

      if (! ( key.equals(TYPE_KEY) || key.equals(OPERATION_KEY) || key.equals(DashboardAttributeCondition.MD_ATTRIBUTE_KEY) ))
      {
        Object value = condition.get(key);

        if (value != null)
        {
          if (value instanceof JSONArray)
          {
            if ( ( (JSONArray) value ).length() > 0)
            {
              return false;
            }
          }
          else if (value instanceof String)
          {
            if ( ( (String) value ).length() > 0)
            {
              return false;
            }
          }
          else
          {
            return false;
          }
        }
      }
    }

    return true;
  }

  public static DashboardCondition[] getConditionsFromState(String state)
  {
    if (state != null && state.length() > 0)
    {
      try
      {
        JSONArray conditions = DashboardCondition.parseConditions(state);

        return DashboardCondition.deserialize(conditions.toString());
      }
      catch (JSONException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }

    return new DashboardCondition[] {};
  }
}
