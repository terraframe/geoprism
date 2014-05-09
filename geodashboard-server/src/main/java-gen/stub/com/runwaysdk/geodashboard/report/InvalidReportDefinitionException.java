package com.runwaysdk.geodashboard.report;

public class InvalidReportDefinitionException extends InvalidReportDefinitionExceptionBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 737376829;
  
  public InvalidReportDefinitionException()
  {
    super();
  }
  
  public InvalidReportDefinitionException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public InvalidReportDefinitionException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public InvalidReportDefinitionException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
