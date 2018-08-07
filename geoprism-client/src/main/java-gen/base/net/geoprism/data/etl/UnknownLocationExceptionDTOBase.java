package net.geoprism.data.etl;

@com.runwaysdk.business.ClassSignature(hash = 1773031652)
public abstract class UnknownLocationExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "net.geoprism.data.etl.UnknownLocationException";
  private static final long serialVersionUID = 1773031652;
  
  public UnknownLocationExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected UnknownLocationExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public UnknownLocationExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public UnknownLocationExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public UnknownLocationExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public UnknownLocationExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public UnknownLocationExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public UnknownLocationExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static java.lang.String LOCATIONLABEL = "locationLabel";
  public String getLocationLabel()
  {
    return getValue(LOCATIONLABEL);
  }
  
  public void setLocationLabel(String value)
  {
    if(value == null)
    {
      setValue(LOCATIONLABEL, "");
    }
    else
    {
      setValue(LOCATIONLABEL, value);
    }
  }
  
  public boolean isLocationLabelWritable()
  {
    return isWritable(LOCATIONLABEL);
  }
  
  public boolean isLocationLabelReadable()
  {
    return isReadable(LOCATIONLABEL);
  }
  
  public boolean isLocationLabelModified()
  {
    return isModified(LOCATIONLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getLocationLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(LOCATIONLABEL).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{id}", this.getId().toString());
    template = template.replace("{locationLabel}", this.getLocationLabel().toString());
    
    return template;
  }
  
}
