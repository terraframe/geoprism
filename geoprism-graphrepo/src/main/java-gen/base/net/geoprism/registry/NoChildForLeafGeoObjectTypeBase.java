/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism Registry(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry;

@com.runwaysdk.business.ClassSignature(hash = -57825893)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to NoChildForLeafGeoObjectType.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class NoChildForLeafGeoObjectTypeBase extends com.runwaysdk.business.SmartException
{
  public final static String CLASS = "net.geoprism.registry.NoChildForLeafGeoObjectType";
  public static final java.lang.String CHILDGEOOBJECTTYPELABEL = "childGeoObjectTypeLabel";
  public static final java.lang.String HIERARCHYTYPELABEL = "hierarchyTypeLabel";
  public static final java.lang.String OID = "oid";
  public static final java.lang.String PARENTGEOOBJECTTYPELABEL = "parentGeoObjectTypeLabel";
  private static final long serialVersionUID = -57825893;
  
  public NoChildForLeafGeoObjectTypeBase()
  {
    super();
  }
  
  public NoChildForLeafGeoObjectTypeBase(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public NoChildForLeafGeoObjectTypeBase(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public NoChildForLeafGeoObjectTypeBase(java.lang.Throwable cause)
  {
    super(cause);
  }
  
  public String getChildGeoObjectTypeLabel()
  {
    return getValue(CHILDGEOOBJECTTYPELABEL);
  }
  
  public void validateChildGeoObjectTypeLabel()
  {
    this.validateAttribute(CHILDGEOOBJECTTYPELABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getChildGeoObjectTypeLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.NoChildForLeafGeoObjectType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(CHILDGEOOBJECTTYPELABEL);
  }
  
  public void setChildGeoObjectTypeLabel(String value)
  {
    if(value == null)
    {
      setValue(CHILDGEOOBJECTTYPELABEL, "");
    }
    else
    {
      setValue(CHILDGEOOBJECTTYPELABEL, value);
    }
  }
  
  public String getHierarchyTypeLabel()
  {
    return getValue(HIERARCHYTYPELABEL);
  }
  
  public void validateHierarchyTypeLabel()
  {
    this.validateAttribute(HIERARCHYTYPELABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getHierarchyTypeLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.NoChildForLeafGeoObjectType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(HIERARCHYTYPELABEL);
  }
  
  public void setHierarchyTypeLabel(String value)
  {
    if(value == null)
    {
      setValue(HIERARCHYTYPELABEL, "");
    }
    else
    {
      setValue(HIERARCHYTYPELABEL, value);
    }
  }
  
  public String getOid()
  {
    return getValue(OID);
  }
  
  public void validateOid()
  {
    this.validateAttribute(OID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF getOidMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.NoChildForLeafGeoObjectType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public String getParentGeoObjectTypeLabel()
  {
    return getValue(PARENTGEOOBJECTTYPELABEL);
  }
  
  public void validateParentGeoObjectTypeLabel()
  {
    this.validateAttribute(PARENTGEOOBJECTTYPELABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getParentGeoObjectTypeLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.NoChildForLeafGeoObjectType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(PARENTGEOOBJECTTYPELABEL);
  }
  
  public void setParentGeoObjectTypeLabel(String value)
  {
    if(value == null)
    {
      setValue(PARENTGEOOBJECTTYPELABEL, "");
    }
    else
    {
      setValue(PARENTGEOOBJECTTYPELABEL, value);
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public java.lang.String localize(java.util.Locale locale)
  {
    java.lang.String message = super.localize(locale);
    message = replace(message, "{childGeoObjectTypeLabel}", this.getChildGeoObjectTypeLabel());
    message = replace(message, "{hierarchyTypeLabel}", this.getHierarchyTypeLabel());
    message = replace(message, "{oid}", this.getOid());
    message = replace(message, "{parentGeoObjectTypeLabel}", this.getParentGeoObjectTypeLabel());
    return message;
  }
  
}
