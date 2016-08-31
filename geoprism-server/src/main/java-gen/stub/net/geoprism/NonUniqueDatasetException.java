package net.geoprism;

public class NonUniqueDatasetException extends NonUniqueDatasetExceptionBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 64818962;
  
  public NonUniqueDatasetException()
  {
    super();
  }
  
  public NonUniqueDatasetException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public NonUniqueDatasetException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public NonUniqueDatasetException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
