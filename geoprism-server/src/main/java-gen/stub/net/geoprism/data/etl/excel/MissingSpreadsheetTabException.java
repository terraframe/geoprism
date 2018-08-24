package net.geoprism.data.etl.excel;

public class MissingSpreadsheetTabException extends MissingSpreadsheetTabExceptionBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1203297944;
  
  public MissingSpreadsheetTabException()
  {
    super();
  }
  
  public MissingSpreadsheetTabException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public MissingSpreadsheetTabException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public MissingSpreadsheetTabException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
