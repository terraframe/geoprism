package net.geoprism;

public class GenericException extends GenericExceptionBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -625800333;

  public GenericException()
  {
    super();
  }

  public GenericException(java.lang.String developerMessage)
  {
    super(developerMessage);

    this.setUserMessage(developerMessage);
  }

  public GenericException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);

    this.setUserMessage(developerMessage);
  }

  public GenericException(java.lang.Throwable cause)
  {
    super(cause);
  }

}
