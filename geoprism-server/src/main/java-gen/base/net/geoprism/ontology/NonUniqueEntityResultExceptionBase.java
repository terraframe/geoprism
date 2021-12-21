/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.ontology;

@com.runwaysdk.business.ClassSignature(hash = 154888595)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to NonUniqueEntityResultException.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class NonUniqueEntityResultExceptionBase extends com.runwaysdk.business.SmartException 
{
  public final static String CLASS = "net.geoprism.ontology.NonUniqueEntityResultException";
  public static java.lang.String OID = "oid";
  public static java.lang.String LABEL = "label";
  public static java.lang.String PARENT = "parent";
  public static java.lang.String UNIVERSAL = "universal";
  private static final long serialVersionUID = 154888595;
  
  public NonUniqueEntityResultExceptionBase()
  {
    super();
  }
  
  public NonUniqueEntityResultExceptionBase(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public NonUniqueEntityResultExceptionBase(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public NonUniqueEntityResultExceptionBase(java.lang.Throwable cause)
  {
    super(cause);
  }
  
  public String getOid()
  {
    return getValue(OID);
  }
  
  public void validateId()
  {
    this.validateAttribute(OID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getOidMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.ontology.NonUniqueEntityResultException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public String getLabel()
  {
    return getValue(LABEL);
  }
  
  public void validateLabel()
  {
    this.validateAttribute(LABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.ontology.NonUniqueEntityResultException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(LABEL);
  }
  
  public void setLabel(String value)
  {
    if(value == null)
    {
      setValue(LABEL, "");
    }
    else
    {
      setValue(LABEL, value);
    }
  }
  
  public String getParent()
  {
    return getValue(PARENT);
  }
  
  public void validateParent()
  {
    this.validateAttribute(PARENT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getParentMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.ontology.NonUniqueEntityResultException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(PARENT);
  }
  
  public void setParent(String value)
  {
    if(value == null)
    {
      setValue(PARENT, "");
    }
    else
    {
      setValue(PARENT, value);
    }
  }
  
  public String getUniversal()
  {
    return getValue(UNIVERSAL);
  }
  
  public void validateUniversal()
  {
    this.validateAttribute(UNIVERSAL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getUniversalMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.ontology.NonUniqueEntityResultException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(UNIVERSAL);
  }
  
  public void setUniversal(String value)
  {
    if(value == null)
    {
      setValue(UNIVERSAL, "");
    }
    else
    {
      setValue(UNIVERSAL, value);
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public java.lang.String localize(java.util.Locale locale)
  {
    java.lang.String message = super.localize(locale);
    message = replace(message, "{oid}", this.getOid());
    message = replace(message, "{label}", this.getLabel());
    message = replace(message, "{parent}", this.getParent());
    message = replace(message, "{universal}", this.getUniversal());
    return message;
  }
  
}
