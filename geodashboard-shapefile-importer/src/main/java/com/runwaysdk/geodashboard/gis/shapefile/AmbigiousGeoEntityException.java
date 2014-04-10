package com.runwaysdk.geodashboard.gis.shapefile;

import com.runwaysdk.geodashboard.gis.LocalizedException;

public class AmbigiousGeoEntityException extends LocalizedException
{
  /**
   * 
   */
  private static final long serialVersionUID = -5764187692708424880L;

  private String            entityName;

  private String            type;

  public AmbigiousGeoEntityException(String msg, String entityName, String type)
  {
    super(msg);

    this.entityName = entityName;
    this.type = type;
  }

  @Override
  public String getLocalizedMessage()
  {
    if (this.type != null)
    {
      return super.getLocalizedMessage() + " [" + this.entityName + ", " + this.type + "]";
    }

    return super.getLocalizedMessage() + " [" + this.entityName + "]";
  }
}
