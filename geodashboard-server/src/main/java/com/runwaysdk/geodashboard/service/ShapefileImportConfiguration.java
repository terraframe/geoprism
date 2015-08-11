package com.runwaysdk.geodashboard.service;

import java.util.HashMap;
import java.util.Map;

import org.opengis.feature.type.AttributeDescriptor;

import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.KeyGeneratorIF;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class ShapefileImportConfiguration implements Reloadable
{
  /**
   * Type being imported
   */
  private String                                 type;

  /**
   * Mapping between shapefile-columsn and attributes
   */
  private Map<String, String>                    attributeMapping;

  /**
   * Mapping between Classifier columns and classifier packages
   */
  private Map<String, String>                    classifierMapping;

  /**
   * Map between shapefile column name and the location column information
   */
  private Map<String, LocationColumn>            locationMapping;

  /**
   * Map between attribute name and data handler functions
   */
  private Map<String, ShapefileAttributeHandler> handlers;

  /**
   * Custom attribute handler for the shapefile ID column
   */
  private ShapefileAttributeHandler              idHandler;

  /**
   * Root geo entity for the data being imported
   */
  private GeoEntity                              root;

  /**
   * Generator used for generating geo ids of unmatched entities
   */
  private KeyGeneratorIF                         generator;

  public ShapefileImportConfiguration(String type)
  {
    this(type, new SeedKeyGenerator());
  }

  public ShapefileImportConfiguration(String type, KeyGeneratorIF generator)
  {
    this.type = type;
    this.generator = generator;
    this.attributeMapping = new HashMap<String, String>();
    this.classifierMapping = new HashMap<String, String>();
    this.locationMapping = new HashMap<String, LocationColumn>();
    this.handlers = new HashMap<String, ShapefileAttributeHandler>();
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

    return null;
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

  public void addHandler(String attributeName, ShapefileAttributeHandler handler)
  {
    this.handlers.put(attributeName, handler);
  }

  public ShapefileAttributeHandler getHandler(String attributeName)
  {
    return this.handlers.get(attributeName);
  }

  public ShapefileAttributeHandler getIdHandler()
  {
    return this.idHandler;
  }

  public void setIdHandler(ShapefileAttributeHandler idHandler)
  {
    this.idHandler = idHandler;
  }

  public KeyGeneratorIF getGenerator()
  {
    return generator;
  }

}
