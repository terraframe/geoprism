package com.runwaysdk.geodashboard.util;

import java.util.Map;

import com.runwaysdk.generation.loader.Reloadable;

public class CollectionUtil implements Reloadable
{
  /**
   * Populate the key-value pair of map with the value specified. If the value is null or empty then the default value is used instead.
   * 
   * @param _map
   * @param _key
   * @param _value
   * @param _defaultValue
   */
  public static void populateMap(Map<String, Double> _map, String _key, String _value, Double _defaultValue)
  {
    if (_value != null && _value.length() > 0)
    {
      _map.put(_key, Double.parseDouble(_value));
    }
    else
    {
      _map.put(_key, _defaultValue);
    }
  }

}
