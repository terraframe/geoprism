package net.geoprism.dhis2.response;

@com.runwaysdk.business.ClassSignature(hash = 143517797)
public abstract class DHIS2DuplicateAttributeExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "net.geoprism.dhis2.response.DHIS2DuplicateAttributeException";
  private static final long serialVersionUID = 143517797;
  
  public DHIS2DuplicateAttributeExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected DHIS2DuplicateAttributeExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public DHIS2DuplicateAttributeExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public DHIS2DuplicateAttributeExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public DHIS2DuplicateAttributeExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public DHIS2DuplicateAttributeExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public DHIS2DuplicateAttributeExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public DHIS2DuplicateAttributeExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DHIS2ATTRS = "dhis2Attrs";
  public static java.lang.String ID = "id";
  public String getDhis2Attrs()
  {
    return getValue(DHIS2ATTRS);
  }
  
  public void setDhis2Attrs(String value)
  {
    if(value == null)
    {
      setValue(DHIS2ATTRS, "");
    }
    else
    {
      setValue(DHIS2ATTRS, value);
    }
  }
  
  public boolean isDhis2AttrsWritable()
  {
    return isWritable(DHIS2ATTRS);
  }
  
  public boolean isDhis2AttrsReadable()
  {
    return isReadable(DHIS2ATTRS);
  }
  
  public boolean isDhis2AttrsModified()
  {
    return isModified(DHIS2ATTRS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getDhis2AttrsMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(DHIS2ATTRS).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{dhis2Attrs}", this.getDhis2Attrs().toString());
    template = template.replace("{id}", this.getId().toString());
    
    return template;
  }
  
}
