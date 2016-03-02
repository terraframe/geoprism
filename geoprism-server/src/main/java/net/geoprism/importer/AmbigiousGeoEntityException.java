/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.importer;

import com.runwaysdk.geodashboard.localization.LocalizationFacade;

public class AmbigiousGeoEntityException extends RuntimeException
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
      String message = LocalizationFacade.getFromBundles("error.ambigiousEntity");
      message = message.replace("{0}", this.entityName);
      message = message.replace("{1}", this.type);
      return message;
    }

    String message = LocalizationFacade.getFromBundles("error.ambigiousEntityName");
    message = message.replace("{0}", this.entityName);
    return message;
  }
}
