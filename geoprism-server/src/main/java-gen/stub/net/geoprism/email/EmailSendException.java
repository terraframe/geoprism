package net.geoprism.email;

public class EmailSendException extends EmailSendExceptionBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 682713854;
  
  public EmailSendException()
  {
    super();
  }
  
  public EmailSendException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public EmailSendException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public EmailSendException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
