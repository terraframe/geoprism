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
package net.geoprism.registry.graph;

@com.runwaysdk.business.ClassSignature(hash = 1354775090)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to AttributeShapeValue.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class AttributeShapeValueBase extends net.geoprism.registry.graph.AttributeGeometryValue
{
  public final static String CLASS = "net.geoprism.registry.graph.AttributeShapeValue";
  public final static java.lang.String VALUE = "value";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1354775090;
  
  public AttributeShapeValueBase()
  {
    super();
  }
  
  public org.locationtech.jts.geom.Geometry getValue()
  {
    return (org.locationtech.jts.geom.Geometry) this.getObjectValue(VALUE);
  }
  
  public static com.runwaysdk.gis.dataaccess.MdAttributeShapeDAOIF getValueMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.AttributeShapeValue.CLASS);
    return (com.runwaysdk.gis.dataaccess.MdAttributeShapeDAOIF)mdClassIF.definesAttribute(VALUE);
  }
  
  public void setValue(org.locationtech.jts.geom.Geometry value)
  {
    this.setValue(VALUE, value);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static AttributeShapeValue get(String oid)
  {
    return (AttributeShapeValue) com.runwaysdk.business.graph.VertexObject.get(CLASS, oid);
  }
  
  public String toString()
  {
    if (this.isNew())
    {
      return "New: "+ this.getClassDisplayLabel();
    }
    else
    {
      return super.toString();
    }
  }
}