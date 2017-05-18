package net.geoprism.dhis2.response;

public class DHIS2EmptyDatasetException extends DHIS2EmptyDatasetExceptionBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -801687972;
  
  public DHIS2EmptyDatasetException()
  {
    super();
  }
  
  public DHIS2EmptyDatasetException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public DHIS2EmptyDatasetException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public DHIS2EmptyDatasetException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
