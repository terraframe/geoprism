package com.runwaysdk.geodashboard.report;

public class UnknownReportException extends UnknownReportExceptionBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1092458469;
  
  public UnknownReportException()
  {
    super();
  }
  
  public UnknownReportException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public UnknownReportException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public UnknownReportException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
