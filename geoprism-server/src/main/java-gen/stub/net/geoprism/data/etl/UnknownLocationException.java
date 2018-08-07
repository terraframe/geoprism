package net.geoprism.data.etl;

public class UnknownLocationException extends UnknownLocationExceptionBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1913258841;
  
  public UnknownLocationException()
  {
    super();
  }
  
  public UnknownLocationException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public UnknownLocationException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public UnknownLocationException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
