package com.runwaysdk.geodashboard.report;

@com.runwaysdk.business.ClassSignature(hash = 1798211627)
public abstract class UnknownReportExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.report.UnknownReportException";
  private static final long serialVersionUID = 1798211627;
  
  public UnknownReportExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected UnknownReportExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public UnknownReportExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public UnknownReportExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public UnknownReportExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public UnknownReportExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public UnknownReportExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public UnknownReportExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static java.lang.String REPORTNAME = "reportName";
  public String getReportName()
  {
    return getValue(REPORTNAME);
  }
  
  public void setReportName(String value)
  {
    if(value == null)
    {
      setValue(REPORTNAME, "");
    }
    else
    {
      setValue(REPORTNAME, value);
    }
  }
  
  public boolean isReportNameWritable()
  {
    return isWritable(REPORTNAME);
  }
  
  public boolean isReportNameReadable()
  {
    return isReadable(REPORTNAME);
  }
  
  public boolean isReportNameModified()
  {
    return isModified(REPORTNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getReportNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(REPORTNAME).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{id}", this.getId().toString());
    template = template.replace("{reportName}", this.getReportName().toString());
    
    return template;
  }
  
}
