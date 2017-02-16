package net.geoprism.dhis2.response;

public class DHIS2UnexpectedResponseException extends RuntimeException
{
  private static final long serialVersionUID = -3944686827403531446L;

  public DHIS2UnexpectedResponseException(String msg)
  {
    super(msg);
  }
  
  public DHIS2UnexpectedResponseException(String msg, Throwable cause)
  {
    super(msg, cause);
  }
}
