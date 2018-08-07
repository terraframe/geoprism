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
package net.geoprism.shapefile.ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.List;



public class ShapeFileBean 
{
  /**
   * Shape file
   */
  private File                  shapeFile;

  /**
   * Type of universal being imported
   */
  private String                universal;

  /**
   * Optional attribute with the column name which specifies the type attribute
   */
  private String                type;

  /**
   * Column of the name attribute
   */
  private String                name;

  /**
   * Column of the oid attribute. Option to auto-generate an oid.
   */
  private String                oid;

  /**
   * Optional column for the located in.
   */
  private String                parent;

  /**
   * Optional column for the located in sub-type.
   */
  private String                parentType;

  /**
   * List of attributes
   */
  private List<String>          attributes;

  /**
   * PropertyChangeSupport
   */
  private PropertyChangeSupport propertyChangeSupport;

  public ShapeFileBean()
  {
    propertyChangeSupport = new PropertyChangeSupport(this);
  }

  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }

  public File getShapeFile()
  {
    return shapeFile;
  }

  public void setShapeFile(File shapeFile)
  {
    this.shapeFile = shapeFile;
  }

  public String getUniversal()
  {
    return universal;
  }

  public void setUniversal(String universal)
  {
    propertyChangeSupport.firePropertyChange("universal", this.universal, this.universal = universal);
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    propertyChangeSupport.firePropertyChange("type", this.type, this.type = type);
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    propertyChangeSupport.firePropertyChange("name", this.name, this.name = name);
  }

  public String getOid()
  {
    return oid;
  }

  public void setId(String oid)
  {
    propertyChangeSupport.firePropertyChange("oid", this.oid, this.oid = oid);
  }

  public String getParent()
  {
    return parent;
  }

  public void setParent(String parentIn)
  {
    propertyChangeSupport.firePropertyChange("parent", this.parent, this.parent = parentIn);
  }

  public String getParentType()
  {
    return parentType;
  }

  public void setParentType(String parentType)
  {
    propertyChangeSupport.firePropertyChange("parentType", this.parentType, this.parentType = parentType);
  }

  public List<String> getAttributes()
  {
    return attributes;
  }

  public void setAttributes(List<String> attributes)
  {
    this.attributes = attributes;
  }

  public boolean hasRequiredAttributes()
  {
    return ( ( this.getUniversal() != null || this.getType() != null ) && this.getName() != null );
  }

  @Override
  public String toString()
  {
    return "Universal: " + universal + ", Type: " + type + " Name: " + name + ", Id: " + oid + ", Located In: " + parent + ", Located In Type:" + parentType;
  }
}
