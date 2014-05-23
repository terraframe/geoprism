package com.runwaysdk.geodashboard.report;

@com.runwaysdk.business.ClassSignature(hash = -1197382218)
public abstract class MultipleReportExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.report.MultipleReportException";
  private static final long serialVersionUID = -1197382218;
  
  public MultipleReportExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected MultipleReportExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public MultipleReportExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public MultipleReportExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public MultipleReportExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public MultipleReportExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public MultipleReportExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public MultipleReportExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String FORMAT = "format";
  public static java.lang.String ID = "id";
  public static java.lang.String REPORTNAME = "reportName";
  public String getFormat()
  {
    return getValue(FORMAT);
  }
  
  public void setFormat(String value)
  {
    if(value == null)
    {
      setValue(FORMAT, "");
    }
    else
    {
      setValue(FORMAT, value);
    }
  }
  
  public boolean isFormatWritable()
  {
    return isWritable(FORMAT);
  }
  
  public boolean isFormatReadable()
  {
    return isReadable(FORMAT);
  }
  
  public boolean isFormatModified()
  {
    return isModified(FORMAT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getFormatMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(FORMAT).getAttributeMdDTO();
  }
  
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
    
    template = template.replace("{format}", this.getFormat().toString());
    template = template.replace("{id}", this.getId().toString());
    template = template.replace("{reportName}", this.getReportName().toString());
    
    return template;
  }
  
}
