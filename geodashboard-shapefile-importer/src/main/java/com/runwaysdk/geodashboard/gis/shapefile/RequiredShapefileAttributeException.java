package com.runwaysdk.geodashboard.gis.shapefile;

import com.runwaysdk.geodashboard.gis.LocalizedException;

public class RequiredShapefileAttributeException extends LocalizedException
{

  /**
   * 
   */
  private static final long serialVersionUID = -4900063662064622215L;

  private String            attributeName;

  public RequiredShapefileAttributeException(String msg, String attributeName)
  {
    super(msg);

    this.attributeName = attributeName;
  }

  @Override
  public String getLocalizedMessage()
  {
    return super.getLocalizedMessage() + " [" + attributeName + "]";
  }

}
