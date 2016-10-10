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
package net.geoprism.data;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import net.geoprism.ontology.GeoEntityUtilDTO;

import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.mvc.Controller;
import com.runwaysdk.mvc.Endpoint;
import com.runwaysdk.mvc.ErrorSerialization;
import com.runwaysdk.mvc.RequestParamter;
import com.runwaysdk.mvc.ResponseIF;
import com.runwaysdk.mvc.RestResponse;
import com.runwaysdk.system.gis.geo.GeoEntityDTO;
import com.runwaysdk.system.gis.geo.UniversalDTO;

@Controller(url = "location")
public class LocationController implements Reloadable
{
  @Endpoint(error = ErrorSerialization.JSON)
  public ResponseIF select(ClientRequestIF request, @RequestParamter(name = "id") String id, @RequestParamter(name = "universalId") String universalId, @RequestParamter(name = "existingLayers") String existingLayers) throws JSONException
  {
    GeoEntityDTO entity = GeoEntityUtilDTO.getEntity(request, id);
    List<? extends UniversalDTO> universals = entity.getUniversal().getAllContains();

    UniversalDTO universal = null;

    if (universalId != null && universalId.length() > 0)
    {
      universal = UniversalDTO.get(request, universalId);
    }
    else if (universals.size() > 0)
    {
      universal = universals.get(0);
      universalId = universal.getId();
    }

    String layers = GeoEntityUtilDTO.publishLayers(request, entity.getId(), universalId, existingLayers);

    ValueQueryDTO children = GeoEntityUtilDTO.getChildren(request, entity.getId(), universalId, 200);

    RestResponse response = new RestResponse();
    response.set("children", children);
    response.set("layers", new JSONArray(layers));
    response.set("universals", new ListSerializable(universals));
    response.set("universal", universal);
    response.set("entity", entity, new GeoEntityJsonConfiguration());

    return response;
  }
}
