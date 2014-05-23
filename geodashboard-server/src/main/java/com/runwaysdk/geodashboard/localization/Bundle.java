package com.runwaysdk.geodashboard.localization;

import java.util.Properties;
import java.util.Set;

import com.runwaysdk.generation.loader.Reloadable;

public class Bundle implements Reloadable
{
  private Properties properties;

  public Bundle(String bundleName, LocaleDimension localeDimension)
  {
    properties = localeDimension.getPropertiesFromFile(bundleName);
  }

  public String getValue(String key)
  {
    return properties.getProperty(key);
  }

  public Set<String> getKeySet()
  {
    return properties.stringPropertyNames();
  }
}