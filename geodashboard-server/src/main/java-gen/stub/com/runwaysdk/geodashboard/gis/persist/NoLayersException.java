package com.runwaysdk.geodashboard.gis.persist;

public class NoLayersException extends NoLayersExceptionBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1807518585;
  
  public NoLayersException()
  {
    super();
  }
  
  public NoLayersException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public NoLayersException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public NoLayersException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
