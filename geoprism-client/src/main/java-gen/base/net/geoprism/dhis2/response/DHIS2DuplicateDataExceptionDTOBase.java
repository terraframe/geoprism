package net.geoprism.dhis2.response;

@com.runwaysdk.business.ClassSignature(hash = -1312130296)
public abstract class DHIS2DuplicateDataExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "net.geoprism.dhis2.response.DHIS2DuplicateDataException";
  private static final long serialVersionUID = -1312130296;
  
  public DHIS2DuplicateDataExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected DHIS2DuplicateDataExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public DHIS2DuplicateDataExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public DHIS2DuplicateDataExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public DHIS2DuplicateDataExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public DHIS2DuplicateDataExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public DHIS2DuplicateDataExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public DHIS2DuplicateDataExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DHIS2DATATYPE = "dhis2Datatype";
  public static java.lang.String DHIS2VALUE = "dhis2Value";
  public static java.lang.String ID = "id";
  public String getDhis2Datatype()
  {
    return getValue(DHIS2DATATYPE);
  }
  
  public void setDhis2Datatype(String value)
  {
    if(value == null)
    {
      setValue(DHIS2DATATYPE, "");
    }
    else
    {
      setValue(DHIS2DATATYPE, value);
    }
  }
  
  public boolean isDhis2DatatypeWritable()
  {
    return isWritable(DHIS2DATATYPE);
  }
  
  public boolean isDhis2DatatypeReadable()
  {
    return isReadable(DHIS2DATATYPE);
  }
  
  public boolean isDhis2DatatypeModified()
  {
    return isModified(DHIS2DATATYPE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getDhis2DatatypeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(DHIS2DATATYPE).getAttributeMdDTO();
  }
  
  public String getDhis2Value()
  {
    return getValue(DHIS2VALUE);
  }
  
  public void setDhis2Value(String value)
  {
    if(value == null)
    {
      setValue(DHIS2VALUE, "");
    }
    else
    {
      setValue(DHIS2VALUE, value);
    }
  }
  
  public boolean isDhis2ValueWritable()
  {
    return isWritable(DHIS2VALUE);
  }
  
  public boolean isDhis2ValueReadable()
  {
    return isReadable(DHIS2VALUE);
  }
  
  public boolean isDhis2ValueModified()
  {
    return isModified(DHIS2VALUE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getDhis2ValueMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(DHIS2VALUE).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{dhis2Datatype}", this.getDhis2Datatype().toString());
    template = template.replace("{dhis2Value}", this.getDhis2Value().toString());
    template = template.replace("{id}", this.getId().toString());
    
    return template;
  }
  
}
