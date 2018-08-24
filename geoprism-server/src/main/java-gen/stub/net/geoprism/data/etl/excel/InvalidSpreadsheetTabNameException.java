package net.geoprism.data.etl.excel;

public class InvalidSpreadsheetTabNameException extends InvalidSpreadsheetTabNameExceptionBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 212252396;
  
  public InvalidSpreadsheetTabNameException()
  {
    super();
  }
  
  public InvalidSpreadsheetTabNameException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public InvalidSpreadsheetTabNameException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public InvalidSpreadsheetTabNameException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
