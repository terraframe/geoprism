package com.runwaysdk.geodashboard.gis.geoserver;

public class NoLayerDataException extends NoLayerDataExceptionBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -2036004314;
  
  public NoLayerDataException()
  {
    super();
  }
  
  public NoLayerDataException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public NoLayerDataException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public NoLayerDataException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
