package net.geoprism;

@com.runwaysdk.business.ClassSignature(hash = -1841787306)
public abstract class GenericExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO
{
  public final static String CLASS = "net.geoprism.GenericException";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -1841787306;
  
  public GenericExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected GenericExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public GenericExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public GenericExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public GenericExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public GenericExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public GenericExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public GenericExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String OID = "oid";
  public static java.lang.String USERMESSAGE = "userMessage";
  public String getUserMessage()
  {
    return getValue(USERMESSAGE);
  }
  
  public void setUserMessage(String value)
  {
    if(value == null)
    {
      setValue(USERMESSAGE, "");
    }
    else
    {
      setValue(USERMESSAGE, value);
    }
  }
  
  public boolean isUserMessageWritable()
  {
    return isWritable(USERMESSAGE);
  }
  
  public boolean isUserMessageReadable()
  {
    return isReadable(USERMESSAGE);
  }
  
  public boolean isUserMessageModified()
  {
    return isModified(USERMESSAGE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getUserMessageMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(USERMESSAGE).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{oid}", this.getOid().toString());
    template = template.replace("{userMessage}", this.getUserMessage().toString());
    
    return template;
  }
  
}
