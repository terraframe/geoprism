package com.runwaysdk.geodashboard.gis;

public class DashboardHasNoMapException extends DashboardHasNoMapExceptionBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -655333327;
  
  public DashboardHasNoMapException()
  {
    super();
  }
  
  public DashboardHasNoMapException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public DashboardHasNoMapException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public DashboardHasNoMapException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
