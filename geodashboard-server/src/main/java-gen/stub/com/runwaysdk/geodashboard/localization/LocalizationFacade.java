package com.runwaysdk.geodashboard.localization;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;

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
}
