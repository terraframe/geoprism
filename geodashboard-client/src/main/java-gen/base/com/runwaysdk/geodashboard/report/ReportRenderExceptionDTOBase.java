package com.runwaysdk.geodashboard.report;

@com.runwaysdk.business.ClassSignature(hash = -1896693577)
public abstract class ReportRenderExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.report.ReportRenderException";
  private static final long serialVersionUID = -1896693577;
  
  public ReportRenderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected ReportRenderExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public ReportRenderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public ReportRenderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public ReportRenderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public ReportRenderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public ReportRenderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public ReportRenderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ERRORMESSAGE = "errorMessage";
  public static java.lang.String ID = "id";
  public String getErrorMessage()
  {
    return getValue(ERRORMESSAGE);
  }
  
  public void setErrorMessage(String value)
  {
    if(value == null)
    {
      setValue(ERRORMESSAGE, "");
    }
    else
    {
      setValue(ERRORMESSAGE, value);
    }
  }
  
  public boolean isErrorMessageWritable()
  {
    return isWritable(ERRORMESSAGE);
  }
  
  public boolean isErrorMessageReadable()
  {
    return isReadable(ERRORMESSAGE);
  }
  
  public boolean isErrorMessageModified()
  {
    return isModified(ERRORMESSAGE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getErrorMessageMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(ERRORMESSAGE).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{errorMessage}", this.getErrorMessage().toString());
    template = template.replace("{id}", this.getId().toString());
    
    return template;
  }
  
}
