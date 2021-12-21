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
package net.geoprism.report;

@com.runwaysdk.business.ClassSignature(hash = -1825863938)
public abstract class ReportRenderExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO 
{
  public final static String CLASS = "net.geoprism.report.ReportRenderException";
  private static final long serialVersionUID = -1825863938;
  
  public ReportRenderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected ReportRenderExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public ReportRenderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public ReportRenderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public ReportRenderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public ReportRenderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public ReportRenderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public ReportRenderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ERRORMESSAGE = "errorMessage";
  public static java.lang.String OID = "oid";
  public String getErrorMessage()
  {
    return getValue(ERRORMESSAGE);
  }
  
  public void setErrorMessage(String value)
  {
    if(value == null)
    {
      setValue(ERRORMESSAGE, "");
    }
    else
    {
      setValue(ERRORMESSAGE, value);
    }
  }
  
  public boolean isErrorMessageWritable()
  {
    return isWritable(ERRORMESSAGE);
  }
  
  public boolean isErrorMessageReadable()
  {
    return isReadable(ERRORMESSAGE);
  }
  
  public boolean isErrorMessageModified()
  {
    return isModified(ERRORMESSAGE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getErrorMessageMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(ERRORMESSAGE).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{errorMessage}", this.getErrorMessage().toString());
    template = template.replace("{oid}", this.getOid().toString());
    
    return template;
  }
  
}
