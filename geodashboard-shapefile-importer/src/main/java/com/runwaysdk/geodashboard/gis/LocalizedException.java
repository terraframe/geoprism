package com.runwaysdk.geodashboard.gis;

import com.runwaysdk.RunwayException;

public abstract class LocalizedException extends RunwayException
{
  /**
   * 
   */
  private static final long serialVersionUID = 1737755267802818919L;

  public LocalizedException(String msg)
  {
    super(msg);
  }

  @Override
  public String getLocalizedMessage()
  {
    return Localizer.getMessage(this.getClass().getName());
  }

}
