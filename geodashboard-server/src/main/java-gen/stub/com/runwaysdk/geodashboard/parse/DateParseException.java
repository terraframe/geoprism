package com.runwaysdk.geodashboard.parse;

public class DateParseException extends DateParseExceptionBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1053974576;
  
  public DateParseException()
  {
    super();
  }
  
  public DateParseException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public DateParseException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public DateParseException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
