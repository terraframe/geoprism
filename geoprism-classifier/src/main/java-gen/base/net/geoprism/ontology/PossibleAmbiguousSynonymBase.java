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
package net.geoprism.ontology;

@com.runwaysdk.business.ClassSignature(hash = -1785237211)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to PossibleAmbiguousSynonym.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class PossibleAmbiguousSynonymBase extends com.runwaysdk.business.Warning 
{
  public final static String CLASS = "net.geoprism.ontology.PossibleAmbiguousSynonym";
  public static java.lang.String CLASSIFIERLABEL = "classifierLabel";
  public static java.lang.String OID = "oid";
  public static java.lang.String SYNONYMLABEL = "synonymLabel";
  private static final long serialVersionUID = -1785237211;
  
  public PossibleAmbiguousSynonymBase()
  {
    super();
  }
  
  public String getClassifierLabel()
  {
    return getValue(CLASSIFIERLABEL);
  }
  
  public void validateClassifierLabel()
  {
    this.validateAttribute(CLASSIFIERLABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getClassifierLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.ontology.PossibleAmbiguousSynonym.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(CLASSIFIERLABEL);
  }
  
  public void setClassifierLabel(String value)
  {
    if(value == null)
    {
      setValue(CLASSIFIERLABEL, "");
    }
    else
    {
      setValue(CLASSIFIERLABEL, value);
    }
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.ontology.PossibleAmbiguousSynonym.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public String getSynonymLabel()
  {
    return getValue(SYNONYMLABEL);
  }
  
  public void validateSynonymLabel()
  {
    this.validateAttribute(SYNONYMLABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getSynonymLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.ontology.PossibleAmbiguousSynonym.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(SYNONYMLABEL);
  }
  
  public void setSynonymLabel(String value)
  {
    if(value == null)
    {
      setValue(SYNONYMLABEL, "");
    }
    else
    {
      setValue(SYNONYMLABEL, value);
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public java.lang.String localize(java.util.Locale locale)
  {
    java.lang.String message = super.localize(locale);
    message = replace(message, "{classifierLabel}", this.getClassifierLabel());
    message = replace(message, "{oid}", this.getOid());
    message = replace(message, "{synonymLabel}", this.getSynonymLabel());
    return message;
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
