/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.report;

@com.runwaysdk.business.ClassSignature(hash = -1114884298)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MultipleReportException.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MultipleReportExceptionBase extends com.runwaysdk.business.SmartException 
{
  public final static String CLASS = "net.geoprism.report.MultipleReportException";
  public static java.lang.String FORMAT = "format";
  public static java.lang.String OID = "oid";
  public static java.lang.String REPORTNAME = "reportName";
  private static final long serialVersionUID = -1114884298;
  
  public MultipleReportExceptionBase()
  {
    super();
  }
  
  public MultipleReportExceptionBase(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public MultipleReportExceptionBase(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public MultipleReportExceptionBase(java.lang.Throwable cause)
  {
    super(cause);
  }
  
  public String getFormat()
  {
    return getValue(FORMAT);
  }
  
  public void validateFormat()
  {
    this.validateAttribute(FORMAT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getFormatMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.report.MultipleReportException.CLASS);
    return mdClassIF.definesAttribute(FORMAT);
  }
  
  public void setFormat(String value)
  {
    if(value == null)
    {
      setValue(FORMAT, "");
    }
    else
    {
      setValue(FORMAT, value);
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
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getOidMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.report.MultipleReportException.CLASS);
    return mdClassIF.definesAttribute(OID);
  }
  
  public String getReportName()
  {
    return getValue(REPORTNAME);
  }
  
  public void validateReportName()
  {
    this.validateAttribute(REPORTNAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getReportNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.report.MultipleReportException.CLASS);
    return mdClassIF.definesAttribute(REPORTNAME);
  }
  
  public void setReportName(String value)
  {
    if(value == null)
    {
      setValue(REPORTNAME, "");
    }
    else
    {
      setValue(REPORTNAME, value);
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public java.lang.String localize(java.util.Locale locale)
  {
    java.lang.String message = super.localize(locale);
    message = replace(message, "{format}", this.getFormat());
    message = replace(message, "{oid}", this.getOid());
    message = replace(message, "{reportName}", this.getReportName());
    return message;
  }
  
}
