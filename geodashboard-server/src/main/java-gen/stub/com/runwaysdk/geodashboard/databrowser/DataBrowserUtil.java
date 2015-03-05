package com.runwaysdk.geodashboard.databrowser;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.geodashboard.dashboard.ConfigurationIF;
import com.runwaysdk.geodashboard.dashboard.ConfigurationService;
import com.runwaysdk.query.QueryFactory;

public class DataBrowserUtil extends DataBrowserUtilBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 884335891;

  public DataBrowserUtil()
  {
    super();
  }

  public static MetadataTypeQuery getDefaultTypes()
  {
    List<String> packages = new LinkedList<String>();
    List<String> types = new LinkedList<String>();

    List<ConfigurationIF> configurations = ConfigurationService.getConfigurations();

    for (ConfigurationIF configuration : configurations)
    {
      packages.addAll(configuration.getDatabrowserPackages());
      types.addAll(configuration.getDatabrowserTypes());
    }

    QueryFactory f = new QueryFactory();

    MetadataTypeQuery query = new MetadataTypeQuery(f, packages.toArray(new String[packages.size()]), types.toArray(new String[types.size()]));

    return query;
  }

  public static MetadataTypeQuery getTypes(String[] packages, String[] types)
  {
    QueryFactory f = new QueryFactory();

    MetadataTypeQuery query = new MetadataTypeQuery(f, packages, types);

    return query;
  }
}
