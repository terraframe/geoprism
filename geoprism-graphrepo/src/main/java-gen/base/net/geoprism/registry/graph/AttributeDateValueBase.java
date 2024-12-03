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

@com.runwaysdk.business.ClassSignature(hash = -2071555910)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to AttributeDateValue.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class AttributeDateValueBase extends net.geoprism.registry.graph.AttributeBasicValue
{
  public final static String CLASS = "net.geoprism.registry.graph.AttributeDateValue";
  public final static java.lang.String VALUE = "value";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -2071555910;
  
  public AttributeDateValueBase()
  {
    super();
  }
  
  public java.util.Date getValue()
  {
    return (java.util.Date) this.getObjectValue(VALUE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF getValueMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.AttributeDateValue.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF)mdClassIF.definesAttribute(VALUE);
  }
  
  public void setValue(java.util.Date value)
  {
    this.setValue(VALUE, value);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static AttributeDateValue get(String oid)
  {
    return (AttributeDateValue) com.runwaysdk.business.graph.VertexObject.get(CLASS, oid);
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