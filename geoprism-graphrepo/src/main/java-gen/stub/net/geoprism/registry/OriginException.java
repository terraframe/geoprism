package net.geoprism.registry;

public class OriginException extends OriginExceptionBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -2066457300;
  
  public OriginException()
  {
    super();
  }
  
  public OriginException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public OriginException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public OriginException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
