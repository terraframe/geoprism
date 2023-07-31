package net.geoprism.session;

public class LoginBlockedException extends LoginBlockedExceptionBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -967611854;
  
  public LoginBlockedException()
  {
    super();
  }
  
  public LoginBlockedException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public LoginBlockedException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public LoginBlockedException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
