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
package net.geoprism.data.etl.excel;

@com.runwaysdk.business.ClassSignature(hash = 752203526)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to ExcelValueException.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class ExcelValueExceptionBase extends com.runwaysdk.business.SmartException 
{
  public final static String CLASS = "net.geoprism.data.etl.excel.ExcelValueException";
  public static java.lang.String CELL = "cell";
  public static java.lang.String OID = "oid";
  public static java.lang.String MSG = "msg";
  private static final long serialVersionUID = 752203526;
  
  public ExcelValueExceptionBase()
  {
    super();
  }
  
  public ExcelValueExceptionBase(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public ExcelValueExceptionBase(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public ExcelValueExceptionBase(java.lang.Throwable cause)
  {
    super(cause);
  }
  
  public String getCell()
  {
    return getValue(CELL);
  }
  
  public void validateCell()
  {
    this.validateAttribute(CELL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getCellMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.data.etl.excel.ExcelValueException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(CELL);
  }
  
  public void setCell(String value)
  {
    if(value == null)
    {
      setValue(CELL, "");
    }
    else
    {
      setValue(CELL, value);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.data.etl.excel.ExcelValueException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public String getMsg()
  {
    return getValue(MSG);
  }
  
  public void validateMsg()
  {
    this.validateAttribute(MSG);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getMsgMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.data.etl.excel.ExcelValueException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(MSG);
  }
  
  public void setMsg(String value)
  {
    if(value == null)
    {
      setValue(MSG, "");
    }
    else
    {
      setValue(MSG, value);
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public java.lang.String localize(java.util.Locale locale)
  {
    java.lang.String message = super.localize(locale);
    message = replace(message, "{cell}", this.getCell());
    message = replace(message, "{oid}", this.getOid());
    message = replace(message, "{msg}", this.getMsg());
    return message;
  }
  
}
