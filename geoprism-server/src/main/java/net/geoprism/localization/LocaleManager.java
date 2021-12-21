/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.LocaleUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;


public class LocaleManager 
{
  private static LocaleManager    instance = new LocaleManager();

  /**
   * Cache of the known CLDR locales
   */
  private Collection<Locale>      cldrs;

  /**
   * Cache of the known datepicker locales
   */
  private Collection<Locale>      datepickers;

  /**
   * Cache of the CLDR configurations per locale
   */
  private Map<String, JSONObject> configurations;

  private LocaleManager()
  {
    this.cldrs = null;
    this.datepickers = null;
    this.configurations = new HashMap<String, JSONObject>();
  }

  private Collection<Locale> loadCLDRs()
  {
    try
    {

      // Get the list of known CLDR locale
      Set<Locale> locales = new HashSet<Locale>();

      Set<String> paths = new HashSet<String>();

      URL resource = this.getClass().getResource("/cldr/main");
      URI uri = resource.toURI();

      if (uri.getScheme().equals("jar"))
      {
        FileSystem fileSystem = FileSystems.newFileSystem(uri, new HashMap<String, Object>());
        Path path = fileSystem.getPath("/cldr/main");

        Stream<Path> walk = Files.walk(path, 1);

        try
        {
          for (Iterator<Path> it = walk.iterator(); it.hasNext();)
          {
            Path location = it.next();
            
            paths.add(location.toAbsolutePath().toString());            
          }
        }
        finally
        {
          walk.close();
        }
      }
      else
      {
        String url = resource.getPath();
        File root = new File(url);

        File[] files = root.listFiles(new DirectoryFilter());

        if (files != null)
        {
          for (File file : files)
          {
            paths.add(file.getAbsolutePath());
          }
        }
      }

      for (String path : paths)
      {
        File file = new File(path);

        String filename = file.getName();

        locales.add(LocaleManager.getLocaleForName(filename));
      }

      return locales;
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private Collection<Locale> loadDatepickers()
  {
    try
    {
      // Get the list of known CLDR locale
      Set<Locale> locales = new HashSet<Locale>();

      BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/datepickerLocales.txt")));

      try
      {
        while (reader.ready())
        {
          String locale = reader.readLine();

          locales.add(LocaleManager.getLocaleForName(locale));
        }

        return locales;

      }
      finally
      {
        reader.close();
      }
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private synchronized Collection<Locale> getCLDRs()
  {
    if (this.cldrs == null)
    {
      this.cldrs = this.loadCLDRs();
    }

    return cldrs;
  }

  private synchronized Collection<Locale> getDatepickers()
  {
    if (this.datepickers == null)
    {
      this.datepickers = this.loadDatepickers();
    }

    return this.datepickers;
  }

  private synchronized JSONObject getConfiguration(String name)
  {
    if (!this.configurations.containsKey(name))
    {
      List<String> paths = new LinkedList<String>();
      paths.add("/cldr/main/" + name + "/numbers.json");
      paths.add("/cldr/main/" + name + "/currencies.json");
      paths.add("/cldr/main/" + name + "/ca-gregorian.json");
      paths.add("/cldr/main/" + name + "/timeZoneNames.json");
      paths.add("/cldr/supplemental/likelySubtags.json");
      paths.add("/cldr/supplemental/numberingSystems.json");
      paths.add("/cldr/supplemental/timeData.json");
      paths.add("/cldr/supplemental/weekData.json");

      try
      {
        JSONObject merged = new JSONObject();

        for (String path : paths)
        {
          InputStream istream = LocalizationFacade.class.getResourceAsStream(path);

          try
          {
            String text = IOUtils.toString(istream, "UTF-8");

            JSONObject object = new JSONObject(text);

            this.merge(merged, object);
          }
          finally
          {
            istream.close();
          }
        }

        this.configurations.put(name, merged);
      }
      catch (IOException e)
      {
        throw new ProgrammingErrorException(e);
      }
      catch (JSONException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }

    return this.configurations.get(name);
  }

  @SuppressWarnings("unchecked")
  private void merge(JSONObject dest, JSONObject src) throws JSONException
  {
    Iterator<String> iterator = (Iterator<String>) src.keys();

    while (iterator.hasNext())
    {
      String key = iterator.next();

      Object value = src.get(key);

      if (value instanceof JSONObject && dest.has(key))
      {
        // Recursive merge
        JSONObject existing = dest.getJSONObject(key);

        this.merge(existing, (JSONObject) value);

        dest.put(key, existing);
      }
      else
      {
        dest.put(key, value);
      }
    }
  }

  public static Collection<Locale> getAllCLDRs()
  {
    return instance.getCLDRs();
  }

  public static Collection<Locale> getAllDatepickers()
  {
    return instance.getDatepickers();
  }

  public static Locale getLocaleForName(String name)
  {
    // Convert the filename to a locale
    String[] components = name.split("-");

    if (components.length == 2)
    {
      return new Locale(components[0], components[1]);
    }
    else if (components.length >= 3)
    {
      return new Locale(components[0], components[1], components[2]);
    }

    return new Locale(components[0]);
  }

  public static String getLocaleName(Locale _locale)
  {
    StringBuffer buffer = new StringBuffer();

    if (_locale.getLanguage() != null && _locale.getLanguage().length() > 0)
    {
      buffer.append(_locale.getLanguage());
    }

    if (_locale.getCountry() != null && _locale.getCountry().length() > 0)
    {
      buffer.append("-" + _locale.getCountry());
    }

    if (_locale.getVariant() != null && _locale.getVariant().length() > 0)
    {
      buffer.append("-" + _locale.getVariant());
    }

    return buffer.toString();
  }

  public static Locale getBestFitCLDR(Locale _locale)
  {
    Collection<Locale> locales = getAllCLDRs();

    return getBestFitLocale(_locale, locales);
  }

  public static Locale getBestFitDatepicker(Locale _locale)
  {
    Collection<Locale> locales = getAllDatepickers();

    return getBestFitLocale(_locale, locales);
  }

  @SuppressWarnings("unchecked")
  private static Locale getBestFitLocale(Locale _locale, Collection<Locale> _locales)
  {
    List<Locale> lookups = (List<Locale>) LocaleUtils.localeLookupList(_locale);

    for (Locale lookup : lookups)
    {
      if (_locales.contains(lookup))
      {
        return lookup;
      }
    }

    // There is no best fit, return English as the default locale
    return Locale.ENGLISH;
  }

  public static JSONObject getConfiguration(Locale _locale)
  {
    Locale cldr = getBestFitCLDR(_locale);
    String name = getLocaleName(cldr);

    return instance.getConfiguration(name);
  }

  public static String getDatepickerLocaleName(Locale _locale)
  {
    Locale datepicker = getBestFitDatepicker(_locale);
    String name = getLocaleName(datepicker);

    return name;
  }

}
