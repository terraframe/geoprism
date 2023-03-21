package net.geoprism.session;

public class UserNotFoundException extends UserNotFoundExceptionBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -1866985341;
  
  public UserNotFoundException()
  {
    super();
  }
  
  public UserNotFoundException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public UserNotFoundException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public UserNotFoundException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
