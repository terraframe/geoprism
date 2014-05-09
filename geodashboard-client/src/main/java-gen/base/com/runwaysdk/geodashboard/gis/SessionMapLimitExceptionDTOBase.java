package com.runwaysdk.geodashboard.gis;

@com.runwaysdk.business.ClassSignature(hash = 1277186906)
public abstract class SessionMapLimitExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.gis.SessionMapLimitException";
  private static final long serialVersionUID = 1277186906;
  
  public SessionMapLimitExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected SessionMapLimitExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public SessionMapLimitExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public SessionMapLimitExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public SessionMapLimitExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public SessionMapLimitExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public SessionMapLimitExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public SessionMapLimitExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static java.lang.String MAPLIMIT = "mapLimit";
  public Integer getMapLimit()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(MAPLIMIT));
  }
  
  public void setMapLimit(Integer value)
  {
    if(value == null)
    {
      setValue(MAPLIMIT, "");
    }
    else
    {
      setValue(MAPLIMIT, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isMapLimitWritable()
  {
    return isWritable(MAPLIMIT);
  }
  
  public boolean isMapLimitReadable()
  {
    return isReadable(MAPLIMIT);
  }
  
  public boolean isMapLimitModified()
  {
    return isModified(MAPLIMIT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getMapLimitMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(MAPLIMIT).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{id}", this.getId().toString());
    template = template.replace("{mapLimit}", this.getMapLimit().toString());
    
    return template;
  }
  
}
