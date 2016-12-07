package net.geoprism.account;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LocaleSerializer
{
  public static String serialize(Locale... locales)
  {
    try
    {
      JSONArray array = new JSONArray();

      for (Locale locale : locales)
      {
        JSONObject object = new JSONObject();
        object.put("language", locale.getLanguage());
        object.put("country", locale.getCountry());
        object.put("variant", locale.getVariant());

        array.put(object);
      }

      return array.toString();
    }
    catch (JSONException e)
    {
      throw new RuntimeException(e);
    }
  }

  public static Locale[] deserialize(String serialized)
  {
    try
    {
      JSONArray array = new JSONArray(serialized);

      Locale[] locales = new Locale[array.length()];

      for (int i = 0; i < array.length(); i++)
      {
        JSONObject object = array.getJSONObject(i);

        String language = object.getString("language");
        String country = object.getString("country");
        String variant = object.getString("variant");

        locales[i] = new Locale(language, country, variant);
      }

      return locales;
    }
    catch (JSONException e)
    {
      throw new RuntimeException(e);
    }
  }
}
