package net.geoprism.dhis2.response;

public class DHIS2DuplicateDataException extends DHIS2DuplicateDataExceptionBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 183792011;
  
  public DHIS2DuplicateDataException()
  {
    super();
  }
  
  public DHIS2DuplicateDataException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public DHIS2DuplicateDataException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public DHIS2DuplicateDataException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
