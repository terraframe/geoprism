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
package net.geoprism.session;

@com.runwaysdk.business.ClassSignature(hash = 35066400)
public abstract class UserNotOuathEnabledExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO
{
  public final static String CLASS = "net.geoprism.session.UserNotOuathEnabledException";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 35066400;
  
  public UserNotOuathEnabledExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected UserNotOuathEnabledExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public UserNotOuathEnabledExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public UserNotOuathEnabledExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public UserNotOuathEnabledExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public UserNotOuathEnabledExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public UserNotOuathEnabledExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public UserNotOuathEnabledExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String OAUTHSERVER = "oauthServer";
  public static java.lang.String OID = "oid";
  public static java.lang.String USERNAME = "username";
  public String getOauthServer()
  {
    return getValue(OAUTHSERVER);
  }
  
  public void setOauthServer(String value)
  {
    if(value == null)
    {
      setValue(OAUTHSERVER, "");
    }
    else
    {
      setValue(OAUTHSERVER, value);
    }
  }
  
  public boolean isOauthServerWritable()
  {
    return isWritable(OAUTHSERVER);
  }
  
  public boolean isOauthServerReadable()
  {
    return isReadable(OAUTHSERVER);
  }
  
  public boolean isOauthServerModified()
  {
    return isModified(OAUTHSERVER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getOauthServerMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(OAUTHSERVER).getAttributeMdDTO();
  }
  
  public String getUsername()
  {
    return getValue(USERNAME);
  }
  
  public void setUsername(String value)
  {
    if(value == null)
    {
      setValue(USERNAME, "");
    }
    else
    {
      setValue(USERNAME, value);
    }
  }
  
  public boolean isUsernameWritable()
  {
    return isWritable(USERNAME);
  }
  
  public boolean isUsernameReadable()
  {
    return isReadable(USERNAME);
  }
  
  public boolean isUsernameModified()
  {
    return isModified(USERNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getUsernameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(USERNAME).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{oauthServer}", this.getOauthServer().toString());
    template = template.replace("{oid}", this.getOid().toString());
    template = template.replace("{username}", this.getUsername().toString());
    
    return template;
  }
  
}
