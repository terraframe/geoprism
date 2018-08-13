/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.dashboard.layer;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.mvc.Controller;
import com.runwaysdk.mvc.Endpoint;
import com.runwaysdk.mvc.ErrorSerialization;
import com.runwaysdk.mvc.RequestParamter;
import com.runwaysdk.mvc.ResponseIF;
import com.runwaysdk.mvc.RestBodyResponse;
import com.runwaysdk.transport.conversion.json.BusinessDTOToJSON;

import net.geoprism.dashboard.DashboardMapDTO;
import net.geoprism.dashboard.DashboardStyleDTO;

@Controller(url = "reference-layer")
public class DashboardReferenceLayerController
{
  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF edit(ClientRequestIF request, @RequestParamter(name = "oid") String oid) throws JSONException
  {
    DashboardReferenceLayerDTO layer = DashboardReferenceLayerDTO.lock(request, oid);
    DashboardMapDTO map = layer.getDashboardMap();
    DashboardStyleDTO style = layer.getAllHasStyle().get(0);

    String options = DashboardReferenceLayerDTO.getOptionsJSON(request, map.getDashboardId());

    JSONObject response = new JSONObject();
    response.put("layerDTO", BusinessDTOToJSON.getConverter(layer).populate());
    response.put("styleDTO", BusinessDTOToJSON.getConverter(style).populate());
    response.put("layer", new JSONObject(layer.getJSON()));
    response.put("options", new JSONObject(options));

    return new RestBodyResponse(response);
  }

  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF newReferenceInstance(ClientRequestIF request, @RequestParamter(name = "universalId") String universalId, @RequestParamter(name = "mapId") String mapId) throws JSONException
  {
    DashboardMapDTO map = DashboardMapDTO.get(request, mapId);

    DashboardReferenceLayerDTO layer = new DashboardReferenceLayerDTO(request);
    layer.setDashboardMap(map);

    DashboardStyleDTO style = new DashboardStyleDTO(request);

    String options = DashboardReferenceLayerDTO.getOptionsJSON(request, map.getDashboardId());

    JSONObject response = new JSONObject();
    response.put("layerDTO", BusinessDTOToJSON.getConverter(layer).populate());
    response.put("styleDTO", BusinessDTOToJSON.getConverter(style).populate());
    response.put("layer", new JSONObject(layer.getJSON()));
    response.put("options", new JSONObject(options));

    return new RestBodyResponse(response);
  }
}
