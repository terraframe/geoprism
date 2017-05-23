package net.geoprism.dhis2.response;

@com.runwaysdk.business.ClassSignature(hash = 1600047307)
public abstract class DHIS2DuplicateDataExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "net.geoprism.dhis2.response.DHIS2DuplicateDataException";
  private static final long serialVersionUID = 1600047307;
  
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
  
  public static java.lang.String DATATYPE = "dataType";
  public static java.lang.String ID = "id";
  public static java.lang.String PROPERTYNAME = "propertyName";
  public static java.lang.String PROPERTYVALUE = "propertyValue";
  public String getDataType()
  {
    return getValue(DATATYPE);
  }
  
  public void setDataType(String value)
  {
    if(value == null)
    {
      setValue(DATATYPE, "");
    }
    else
    {
      setValue(DATATYPE, value);
    }
  }
  
  public boolean isDataTypeWritable()
  {
    return isWritable(DATATYPE);
  }
  
  public boolean isDataTypeReadable()
  {
    return isReadable(DATATYPE);
  }
  
  public boolean isDataTypeModified()
  {
    return isModified(DATATYPE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getDataTypeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(DATATYPE).getAttributeMdDTO();
  }
  
  public String getPropertyName()
  {
    return getValue(PROPERTYNAME);
  }
  
  public void setPropertyName(String value)
  {
    if(value == null)
    {
      setValue(PROPERTYNAME, "");
    }
    else
    {
      setValue(PROPERTYNAME, value);
    }
  }
  
  public boolean isPropertyNameWritable()
  {
    return isWritable(PROPERTYNAME);
  }
  
  public boolean isPropertyNameReadable()
  {
    return isReadable(PROPERTYNAME);
  }
  
  public boolean isPropertyNameModified()
  {
    return isModified(PROPERTYNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getPropertyNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(PROPERTYNAME).getAttributeMdDTO();
  }
  
  public String getPropertyValue()
  {
    return getValue(PROPERTYVALUE);
  }
  
  public void setPropertyValue(String value)
  {
    if(value == null)
    {
      setValue(PROPERTYVALUE, "");
    }
    else
    {
      setValue(PROPERTYVALUE, value);
    }
  }
  
  public boolean isPropertyValueWritable()
  {
    return isWritable(PROPERTYVALUE);
  }
  
  public boolean isPropertyValueReadable()
  {
    return isReadable(PROPERTYVALUE);
  }
  
  public boolean isPropertyValueModified()
  {
    return isModified(PROPERTYVALUE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getPropertyValueMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(PROPERTYVALUE).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{dataType}", this.getDataType().toString());
    template = template.replace("{id}", this.getId().toString());
    template = template.replace("{propertyName}", this.getPropertyName().toString());
    template = template.replace("{propertyValue}", this.getPropertyValue().toString());
    
    return template;
  }
  
}
