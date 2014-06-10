package com.runwaysdk.geodashboard;


import com.runwaysdk.configuration.ConfigurationManager.ConfigGroupIF;

public class GDBConfigurationManager
{
  public static enum GDBConfigGroup implements ConfigGroupIF {
    CLIENT("geodashboard/", "client"), COMMON("geodashboard/", "common"), SERVER("geodashboard/", "server"), ROOT("", "root");

    private String path;

    private String identifier;

    GDBConfigGroup(String path, String identifier)
    {
      this.path = path;
      this.identifier = identifier;
    }

    public String getPath()
    {
      return this.path;
    }

    public String getIdentifier()
    {
      return identifier;
    }
  }
}
