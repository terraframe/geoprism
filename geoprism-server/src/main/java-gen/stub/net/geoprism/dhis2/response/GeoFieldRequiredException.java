package net.geoprism.dhis2.response;

public class GeoFieldRequiredException extends GeoFieldRequiredExceptionBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -873792059;
  
  public GeoFieldRequiredException()
  {
    super();
  }
  
  public GeoFieldRequiredException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public GeoFieldRequiredException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public GeoFieldRequiredException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
