package net.geoprism.session;

public class UserNotOuathEnabledException extends UserNotOuathEnabledExceptionBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1485218211;
  
  public UserNotOuathEnabledException()
  {
    super();
  }
  
  public UserNotOuathEnabledException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public UserNotOuathEnabledException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public UserNotOuathEnabledException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
