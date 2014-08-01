package com.runwaysdk.geodashboard.gis;

public class DuplicateMapDataException extends DuplicateMapDataExceptionBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 971317097;
  
  public DuplicateMapDataException()
  {
    super();
  }
  
  public DuplicateMapDataException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public DuplicateMapDataException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public DuplicateMapDataException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
