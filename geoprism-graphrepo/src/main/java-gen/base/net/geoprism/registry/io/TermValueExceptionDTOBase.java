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
package net.geoprism.registry.io;

@com.runwaysdk.business.ClassSignature(hash = -345226289)
public abstract class TermValueExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO
{
  public final static String CLASS = "net.geoprism.registry.io.TermValueException";
  private static final long serialVersionUID = -345226289;
  
  public TermValueExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected TermValueExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public TermValueExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public TermValueExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public TermValueExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public TermValueExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public TermValueExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public TermValueExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static final java.lang.String ATTRIBUTELABEL = "attributeLabel";
  public static final java.lang.String CODE = "code";
  public static final java.lang.String OID = "oid";
  public String getAttributeLabel()
  {
    return getValue(ATTRIBUTELABEL);
  }
  
  public void setAttributeLabel(String value)
  {
    if(value == null)
    {
      setValue(ATTRIBUTELABEL, "");
    }
    else
    {
      setValue(ATTRIBUTELABEL, value);
    }
  }
  
  public boolean isAttributeLabelWritable()
  {
    return isWritable(ATTRIBUTELABEL);
  }
  
  public boolean isAttributeLabelReadable()
  {
    return isReadable(ATTRIBUTELABEL);
  }
  
  public boolean isAttributeLabelModified()
  {
    return isModified(ATTRIBUTELABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getAttributeLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(ATTRIBUTELABEL).getAttributeMdDTO();
  }
  
  public String getCode()
  {
    return getValue(CODE);
  }
  
  public void setCode(String value)
  {
    if(value == null)
    {
      setValue(CODE, "");
    }
    else
    {
      setValue(CODE, value);
    }
  }
  
  public boolean isCodeWritable()
  {
    return isWritable(CODE);
  }
  
  public boolean isCodeReadable()
  {
    return isReadable(CODE);
  }
  
  public boolean isCodeModified()
  {
    return isModified(CODE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getCodeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(CODE).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{attributeLabel}", this.getAttributeLabel().toString());
    template = template.replace("{code}", this.getCode().toString());
    template = template.replace("{oid}", this.getOid().toString());
    
    return template;
  }
  
}
