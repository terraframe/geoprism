package net.geoprism.configuration;

import com.runwaysdk.configuration.ConfigurationManager.ConfigGroupIF;

/**
 * Defines the bundle locations for configuration groups. Intended for use with Runway's ConfigurationManager API.
 * 
 * @author Richard Rowlands
 */
public enum GeoprismConfigGroup implements ConfigGroupIF {
  CLIENT("geoprism/", "client"),
  COMMON("geoprism/", "common"),
  SERVER("geoprism/", "server"),
  ROOT("", "root");

  private String path;

  private String identifier;

  GeoprismConfigGroup(String path, String identifier) {
    this.path = path;
    this.identifier = identifier;
  }

  public String getPath() {
    return this.path;
  }

  public String getIdentifier() {
    return identifier;
  }
}