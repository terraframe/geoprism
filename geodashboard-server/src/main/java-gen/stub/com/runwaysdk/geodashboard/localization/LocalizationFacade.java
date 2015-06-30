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
package com.runwaysdk.geodashboard.localization;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.session.Session;

public class LocalizationFacade extends LocalizationFacadeBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -43207120;

  public LocalizationFacade()
  {
    super();
  }

  public static String getFromBundles(String key)
  {
    return MultiBundle.get(key);
  }

  public static String getJSON()
  {
    Map<String, String> properties = MultiBundle.getAll();

    try
    {
      JSONObject object = new JSONObject();

      Set<Entry<String, String>> entries = properties.entrySet();

      Iterator<Entry<String, String>> it = entries.iterator();

      while (it.hasNext())
      {
        Entry<String, String> entry = it.next();

        String key = entry.getKey();
        String value = entry.getValue();

        if (key.contains("."))
        {
          int index = key.lastIndexOf(".");

          if (index != -1)
          {
            String language = key.substring(0, index);
            String subKey = key.substring(index + 1);

            if (!object.has(language))
            {
              object.put(language, new JSONObject());
            }

            JSONObject subMap = object.getJSONObject(language);
            subMap.put(subKey, value);
          }
        }
        else
        {
          object.put(key, value);
        }
      }

      return object.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException("Error occured creating the localized JSON map", e);
    }
  }

  public static Locale getLocale()
  {
    return Session.getCurrentSession() != null ? Session.getCurrentLocale() : Locale.US;
  }

  public static String getConfigurationJSON()
  {
    Locale locale = getLocale();

    JSONObject configuration = LocaleManager.getConfiguration(locale);

    String json = configuration.toString();

    return json;
  }

  public static String getCLDRLocaleName()
  {
    Locale locale = getLocale();
    Locale cldr = LocaleManager.getBestFitCLDR(locale);

    return LocaleManager.getLocaleName(cldr);
  }

  public static String getCalendarLocale()
  {
    Locale locale = getLocale();
    Locale datepicker = LocaleManager.getBestFitDatepicker(locale);

    if (datepicker.equals(Locale.ENGLISH))
    {
      return "";
    }

    return LocaleManager.getLocaleName(datepicker);
  }
}
