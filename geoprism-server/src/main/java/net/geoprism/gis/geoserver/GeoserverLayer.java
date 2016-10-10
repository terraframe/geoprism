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
package net.geoprism.gis.geoserver;

import org.json.JSONException;
import org.json.JSONObject;

public class GeoserverLayer implements GeoserverLayerIF
{
  private String layerName;

  private String styleName;

  public void setLayerName(String layerName)
  {
    this.layerName = layerName;
  }

  @Override
  public String getLayerName()
  {
    return this.layerName;
  }

  public void setStyleName(String styleName)
  {
    this.styleName = styleName;
  }

  @Override
  public String getStyleName()
  {
    return this.styleName;
  }

  @Override
  public JSONObject toJSON() throws JSONException
  {
    JSONObject object = new JSONObject();
    object.put("layerName", layerName);
    object.put("styleName", styleName);

    return object;
  }
}
