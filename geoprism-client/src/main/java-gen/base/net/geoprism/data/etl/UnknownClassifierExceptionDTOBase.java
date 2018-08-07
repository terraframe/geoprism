package net.geoprism.data.etl;

@com.runwaysdk.business.ClassSignature(hash = 598459646)
public abstract class UnknownClassifierExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "net.geoprism.data.etl.UnknownClassifierException";
  private static final long serialVersionUID = 598459646;
  
  public UnknownClassifierExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected UnknownClassifierExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public UnknownClassifierExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public UnknownClassifierExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public UnknownClassifierExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public UnknownClassifierExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public UnknownClassifierExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public UnknownClassifierExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CLASSIFIERLABEL = "classifierLabel";
  public static java.lang.String ID = "id";
  public String getClassifierLabel()
  {
    return getValue(CLASSIFIERLABEL);
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
  
  public boolean isClassifierLabelWritable()
  {
    return isWritable(CLASSIFIERLABEL);
  }
  
  public boolean isClassifierLabelReadable()
  {
    return isReadable(CLASSIFIERLABEL);
  }
  
  public boolean isClassifierLabelModified()
  {
    return isModified(CLASSIFIERLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getClassifierLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(CLASSIFIERLABEL).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{classifierLabel}", this.getClassifierLabel().toString());
    template = template.replace("{id}", this.getId().toString());
    
    return template;
  }
  
}
