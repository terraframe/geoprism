/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.gis.geoserver;

import net.geoprism.ontology.LayerPublisher.LayerType;

import org.json.JSONException;
import org.json.JSONObject;

public class GeoserverLayer implements GeoserverLayerIF
{
  private String    layerName;

  private String    styleName;

  private LayerType layerType;

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

  public void setLayerType(LayerType layerType)
  {
    this.layerType = layerType;
  }

  public LayerType getLayerType()
  {
    return layerType;
  }

  @Override
  public JSONObject toJSON() throws JSONException
  {
    JSONObject object = new JSONObject();
    object.put("layerName", this.layerName);
    object.put("layerType", this.layerType.name());
    object.put("workspace", GeoserverProperties.getWorkspace());
//    object.put("styleName", this.styleName);

    return object;
  }
}
