package net.geoprism.registry.model.graph;

import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdGraphClassQuery;

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

    while (isAlreadyUsed(name))
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

  protected static boolean isAlreadyUsed(String dbClassName)
  {
    MdGraphClassQuery query = new MdGraphClassQuery(new QueryFactory());
    query.WHERE(query.getDbClassName().EQ(dbClassName));

    return query.getCount() > 0;
  }

}
