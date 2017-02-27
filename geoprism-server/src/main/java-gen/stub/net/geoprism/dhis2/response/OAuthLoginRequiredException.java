package net.geoprism.dhis2.response;

public class OAuthLoginRequiredException extends OAuthLoginRequiredExceptionBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1285981276;
  
  public OAuthLoginRequiredException()
  {
    super();
  }
  
  public OAuthLoginRequiredException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public OAuthLoginRequiredException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public OAuthLoginRequiredException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
