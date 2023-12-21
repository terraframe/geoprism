package net.geoprism.account;

public class InactiveUserException extends InactiveUserExceptionBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -5828522;
  
  public InactiveUserException()
  {
    super();
  }
  
  public InactiveUserException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public InactiveUserException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public InactiveUserException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
