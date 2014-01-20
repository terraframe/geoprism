package com.runwaysdk.geodashboard.report;

@com.runwaysdk.business.ClassSignature(hash = -873404948)
public abstract class UnsupportedDrillThroughFormatExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.report.UnsupportedDrillThroughFormatException";
  private static final long serialVersionUID = -873404948;
  
  public UnsupportedDrillThroughFormatExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected UnsupportedDrillThroughFormatExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public UnsupportedDrillThroughFormatExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public UnsupportedDrillThroughFormatExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public UnsupportedDrillThroughFormatExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public UnsupportedDrillThroughFormatExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public UnsupportedDrillThroughFormatExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public UnsupportedDrillThroughFormatExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static java.lang.String OUTPUTFORMAT = "outputFormat";
  public String getOutputFormat()
  {
    return getValue(OUTPUTFORMAT);
  }
  
  public void setOutputFormat(String value)
  {
    if(value == null)
    {
      setValue(OUTPUTFORMAT, "");
    }
    else
    {
      setValue(OUTPUTFORMAT, value);
    }
  }
  
  public boolean isOutputFormatWritable()
  {
    return isWritable(OUTPUTFORMAT);
  }
  
  public boolean isOutputFormatReadable()
  {
    return isReadable(OUTPUTFORMAT);
  }
  
  public boolean isOutputFormatModified()
  {
    return isModified(OUTPUTFORMAT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getOutputFormatMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(OUTPUTFORMAT).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{id}", this.getId().toString());
    template = template.replace("{outputFormat}", this.getOutputFormat().toString());
    
    return template;
  }
  
}
