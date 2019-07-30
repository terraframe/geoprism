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
package net.geoprism.dashboard.layer;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.mvc.Controller;
import com.runwaysdk.mvc.Endpoint;
import com.runwaysdk.mvc.ErrorSerialization;
import com.runwaysdk.mvc.RequestParamter;
import com.runwaysdk.mvc.ResponseIF;
import com.runwaysdk.mvc.RestResponse;

@Controller(url = "dashboard-layer")
public class DashboardLayerController
{
  @Endpoint(url = "update-legend", method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF updateLegend(ClientRequestIF request, @RequestParamter(name = "layerId") String layerId, @RequestParamter(name = "legendXPosition") Integer legendXPosition, @RequestParamter(name = "legendYPosition") Integer legendYPosition, @RequestParamter(name = "groupedInLegend") Boolean groupedInLegend)
  {
    DashboardLayerDTO.updateLegend(request, layerId, legendXPosition, legendYPosition, groupedInLegend);

    return new RestResponse();
  }

  @Endpoint(url = "remove", method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF remove(ClientRequestIF request, @RequestParamter(name = "layerId") String layerId)
  {
    request.delete(layerId);

    return new RestResponse();
  }

  @Endpoint(url = "unlock", method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF unlock(ClientRequestIF request, @RequestParamter(name = "oid") String oid)
  {
    DashboardLayerDTO.unlock(request, oid);

    return new RestResponse();
  }
}
