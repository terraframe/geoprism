package com.runwaysdk.geodashboard.gis.sld;

public class SLDConstants
{
  public static final String MAX_PREFIX = "max_";
  
  public static final String MIN_PREFIX = "min_";
  
  public static String getMinProperty(String attribute)
  {
    return MIN_PREFIX + attribute;
  }
  
  public static String getMaxProperty(String attribute)
  {
    return MAX_PREFIX + attribute;
  }
}
