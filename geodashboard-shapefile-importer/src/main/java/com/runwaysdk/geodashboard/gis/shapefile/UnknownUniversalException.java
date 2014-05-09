package com.runwaysdk.geodashboard.gis.shapefile;

import com.runwaysdk.geodashboard.gis.LocalizedException;

public class UnknownUniversalException extends LocalizedException
{

  /**
   * 
   */
  private static final long serialVersionUID = -4900063662064622215L;

  private String            universalName;

  public UnknownUniversalException(String msg, String universalName)
  {
    super(msg);

    this.universalName = universalName;
  }

  @Override
  public String getLocalizedMessage()
  {
    return super.getLocalizedMessage() + " [" + universalName + "]";
  }

}
