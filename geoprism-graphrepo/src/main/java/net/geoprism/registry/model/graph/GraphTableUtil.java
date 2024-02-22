package net.geoprism.registry.model.graph;

import com.runwaysdk.dataaccess.cache.ObjectCache;

public class GraphTableUtil
{
  public static String generateTableName(String prefix, String suffix)
  {
    int count = 0;

    String name = prefix + count + suffix;

    if (name.length() > 25)
    {
      name = name.substring(0, 25);
    }

    while (ObjectCache.hasClassByTableName(name))
    {
      count++;

      name = prefix + count + "suffix";

      if (name.length() > 25)
      {
        name = name.substring(0, 25);
      }
    }

    return name;
  }

}
