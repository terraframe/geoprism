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
package net.geoprism.localization;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.Session;

@Component
public class LocalizationService implements LocalizationServiceIF
{
  // TODO : This service is directly instantiated by both CGR and IDM. Do not add autowired services here until fixed
  
  public String getAllView()
  {
    Map<String, String> properties = com.runwaysdk.localization.LocalizationFacade.getAll();

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
  
  @Override
  @Request(RequestType.SESSION)
  public JsonObject getNewLocaleInformation(String sessionId)
  {
    JsonObject json = new JsonObject();

    JsonArray languages = new JsonArray();
    JsonArray countries = new JsonArray();

    Locale sessionLocale = Session.getCurrentLocale();

    for (Locale locale : this.getAvailableLanguagesSorted())
    {
      JsonObject jobj = new JsonObject();
      jobj.addProperty("key", locale.getLanguage());
      jobj.addProperty("label", locale.getDisplayLanguage(sessionLocale));
      languages.add(jobj);
    }

    for (Locale locale : this.getAvailableCountriesSorted())
    {
      JsonObject jobj = new JsonObject();
      jobj.addProperty("key", locale.getCountry());
      jobj.addProperty("label", locale.getDisplayCountry(sessionLocale));
      countries.add(jobj);
    }

    json.add("languages", languages);
    json.add("countries", countries);

    return json;
  }
  
  public List<Locale> getAvailableLanguagesSorted()
  {
    List<Locale> languages = new LinkedList<Locale>();
    
    for (String s : Locale.getISOLanguages())
    {
      languages.add(new Locale(s));
    }

    Collections.sort(languages, new LocaleLanguageComparator());
    
    return languages;
  }
  
  public List<Locale> getAvailableCountriesSorted()
  {
    List<Locale> countries = new LinkedList<Locale>();

    for (String s : Locale.getISOCountries())
    {
      countries.add(new Locale("en", s));
    }

    Collections.sort(countries, new LocaleCountryComparator());
    
    return countries;
  }
  
  public class LocaleLanguageComparator implements Comparator<Locale>
  {
    public int compare(Locale o1, Locale o2)
    {
      return o1.getDisplayLanguage().compareTo(o2.getDisplayLanguage());
    }
  }
  
  public class LocaleCountryComparator implements Comparator<Locale>
  {
    public int compare(Locale o1, Locale o2)
    {
      return o1.getDisplayCountry().compareTo(o2.getDisplayCountry());
    }
  }
}
