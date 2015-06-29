package com.runwaysdk.geodashboard.oda.driver.ui.provider;

public class ConnectionException extends RuntimeException
{

  /**
   * 
   */
  private static final long serialVersionUID = -2762406745243630459L;

  public ConnectionException(String message)
  {
    super(message);
  }

  public ConnectionException(Throwable cause)
  {
    super(cause);
  }

  public ConnectionException(String message, Throwable cause)
  {
    super(message, cause);
  }

}
