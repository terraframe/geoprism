/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.geodashboard.service;

import java.util.HashMap;
import java.util.Map;

import org.opengis.feature.type.AttributeDescriptor;

import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class ShapefileImportConfiguration implements Reloadable
{
  /**
   * Type being imported
   */
  private String                          type;

  /**
   * Mapping between shapefile-columsn and attributes
   */
  private Map<String, String>             attributeMapping;

  /**
   * Mapping between Classifier columns and classifier packages
   */
  private Map<String, String>             classifierMapping;

  /**
   * Map between shapefile column name and the location column information
   */
  private Map<String, LocationColumn>     locationMapping;

  /**
   * Map between attribute name and data transform function
   */
  private Map<String, ShapefileTransform> transforms;

  /**
   * Root geo entity for the data being imported
   */
  private GeoEntity                       root;

  public ShapefileImportConfiguration(String type)
  {
    this.type = type;
    this.attributeMapping = new HashMap<String, String>();
    this.classifierMapping = new HashMap<String, String>();
    this.locationMapping = new HashMap<String, LocationColumn>();
    this.transforms = new HashMap<String, ShapefileTransform>();
  }

  public String getType()
  {
    return type;
  }

  /**
   * Returns the attribute name of the database attribute which corresponds to the shapefile attribute
   * 
   * @param descriptor
   * @return
   */
  public String getAttributeName(AttributeDescriptor descriptor)
  {
    String key = descriptor.getLocalName();

    if (this.attributeMapping.containsKey(key))
    {
      return this.attributeMapping.get(key);
    }
    else
    {
      throw new ProgrammingErrorException("Unknown shapefile-type mapping for the shapefile attribute [" + key + "]");
    }
  }

  public void addAttributeMapping(String key, String attributeName)
  {
    this.attributeMapping.put(key, attributeName);
  }

  public void addClassifierPackage(String attributeName, String packageName)
  {
    this.classifierMapping.put(attributeName, packageName);
  }

  public String getClassifierPackage(MdAttributeTermDAOIF mdAttributeTerm)
  {
    String key = mdAttributeTerm.definesAttribute();

    if (this.classifierMapping.containsKey(key))
    {
      return this.classifierMapping.get(key);
    }
    else
    {
      throw new ProgrammingErrorException("Unknown package mapping for the ontology attribute [" + key + "]");
    }
  }

  public void addLocationColumn(LocationColumn location)
  {
    this.locationMapping.put(location.getName(), location);
  }

  public boolean isLocationColumn(AttributeDescriptor descriptor)
  {
    String key = descriptor.getLocalName();

    return this.locationMapping.containsKey(key);
  }

  public LocationColumn getLocationColumn(AttributeDescriptor descriptor)
  {
    String key = descriptor.getLocalName();

    return this.locationMapping.get(key);
  }

  public GeoEntity getRootEntity()
  {
    return this.root;
  }

  public void setRoot(GeoEntity root)
  {
    this.root = root;
  }

  public void addTransform(String attributeName, ShapefileTransform transform)
  {
    this.transforms.put(attributeName, transform);
  }

  public ShapefileTransform getTransform(String attributeName)
  {
    return this.transforms.get(attributeName);
  }

}
