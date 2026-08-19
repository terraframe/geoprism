/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
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
