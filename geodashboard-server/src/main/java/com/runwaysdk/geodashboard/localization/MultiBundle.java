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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.session.Session;

public class MultiBundle implements Reloadable
{
  private Map<String, Bundle> bundles;

  public static String        BUNDLE_NAME = "messages";

  private MultiBundle()
  {
    this.bundles = new HashMap<String, Bundle>();
  }

  /**
   * Singleton is loaded on the first execution of MultiBundle.getInstance() or
   * the first access to Singleton.INSTANCE, not before.
   */
  private static class Singleton implements Reloadable
  {
    public static final MultiBundle INSTANCE = new MultiBundle();
  }

  public static String get(String key)
  {
    return get(key, Session.getCurrentLocale());
  }

  public static String get(String key, Locale locale)
  {
    synchronized (Singleton.INSTANCE)
    {
      String localeName = locale.toString();
      MdDimensionDAOIF dimension = Session.getCurrentDimension();
      LocaleDimension ld = new LocaleDimension(localeName, dimension);

      return smartGet(key, localeName, dimension, ld);
    }
  }

  /**
   * The heart of the MultiBundle, smartGet will traverse up the bundle
   * hierarchy until it finds a value
   * 
   * @param key
   * @param locale
   * @param dimension
   * @param ld
   * @return
   */
  private static String smartGet(String key, String locale, MdDimensionDAOIF dimension, LocaleDimension ld)
  {
    while (ld != null)
    {
      // Amazingly, containsKey doesn't appear to be working
      String value = getExactly(key, ld);
      if (value != null)
      {
        return value;
      }

      // This moves us up the bundle chain
      ld = ld.getParent();
      if (ld == null && dimension != null)
      {
        // If dimension-specific bundles fail, start the loop over with the
        // generic bundles
        ld = new LocaleDimension(locale);
        dimension = null;
      }
    }
    return "???_" + key + "_???";
  }

  public static Map<String, String> getAll()
  {
    synchronized (Singleton.INSTANCE)
    {
      Set<String> keySet = MultiBundle.getKeySet();

      String locale = Session.getCurrentLocale().toString();
      MdDimensionDAOIF dimension = Session.getCurrentDimension();
      LocaleDimension ld = new LocaleDimension(locale, dimension);

      Map<String, String> map = new TreeMap<String, String>();

      for (String key : keySet)
      {
        String value = smartGet(key, locale, dimension, ld);

        map.put(key, value);
      }

      return map;
    }
  }

  private static Set<String> getKeySet()
  {
    synchronized (Singleton.INSTANCE)
    {
      Set<String> keySet = new TreeSet<String>();

      String locale = Session.getCurrentLocale().toString();
      MdDimensionDAOIF dimension = Session.getCurrentDimension();
      LocaleDimension ld = new LocaleDimension(locale, dimension);

      while (ld != null)
      {
        Bundle bundle = getBundle(ld);

        keySet.addAll(bundle.getKeySet());

        // This moves us up the bundle chain
        ld = ld.getParent();

        if (ld == null && dimension != null)
        {
          // If dimension-specific bundles fail, start the loop over with the
          // generic bundles
          ld = new LocaleDimension(locale);
          dimension = null;
        }
      }

      return keySet;
    }
  }

  /**
   * Returns the value for this key-LocaleDimension pair exactly, without
   * traversing up the hierarchy of other bundles
   * 
   * @param key
   * @param ld
   * @return
   */
  private static String getExactly(String key, LocaleDimension ld)
  {
    synchronized (Singleton.INSTANCE)
    {
      Map<String, Bundle> cache = Singleton.INSTANCE.bundles;
      String lds = ld.toString();
      if (!cache.containsKey(lds))
      {
        Bundle newBundle = getBundle(ld);
        cache.put(lds, newBundle);
      }

      Bundle bundle = cache.get(lds);
      String value = bundle.getValue(key);
      return value;
    }
  }

  private static Bundle getBundle(LocaleDimension ld)
  {
    synchronized (Singleton.INSTANCE)
    {
      return new Bundle(BUNDLE_NAME, ld);
    }
  }

  public static void setBundle(String bundleName)
  {
    synchronized (Singleton.INSTANCE)
    {
      MultiBundle.BUNDLE_NAME = bundleName;
    }
  }

  public static void reload()
  {
    synchronized (Singleton.INSTANCE)
    {
      Singleton.INSTANCE.bundles.clear();
    }
  }
}
