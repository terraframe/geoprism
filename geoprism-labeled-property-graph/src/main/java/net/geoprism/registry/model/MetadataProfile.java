package net.geoprism.registry.model;

public enum MetadataProfile {
  DCAT("DCAT", "General catalog metadata"),

  GEO_DCAT("GeoDCAT", "Geospatial catalog metadata"),

  ISO19115("ISO19115", "Traditional geospatial metadata"),

  STAC("STAC", "EO/raster catalog metadata"),

  SENSOR_ML("SensorML", "Sensor metadata"),

  FHIR("FHIR", "Healthcare resource metadata"),

  DATA_CITE("DataCite", "Research metadata"),

  CUSTOM("Custom", "Organization-specific structured metadata"),

  AD_HOC("AdHoc", "Manual metadata without formal schema"),

  NONE("None", "No metadata available");

  private String name;

  private String description;

  private MetadataProfile(String name, String description)
  {
    this.name = name;
    this.description = description;
  }

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    return description;
  }

}
