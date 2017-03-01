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
package net.geoprism.prism;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.mvc.Controller;
import com.runwaysdk.mvc.Endpoint;
import com.runwaysdk.mvc.ErrorSerialization;
import com.runwaysdk.mvc.RequestParamter;
import com.runwaysdk.mvc.ResponseIF;
import com.runwaysdk.mvc.RestBodyResponse;
import com.runwaysdk.mvc.RestResponse;
import com.runwaysdk.mvc.ViewResponse;

import net.geoprism.MappableClassDTO;

@Controller(url = "prism")
public class PrismController implements Reloadable
{
  @Endpoint(method = ServletMethod.GET)
  public ResponseIF management()
  {
    return new ViewResponse("/WEB-INF/net/geoprism/prism/prism.jsp");
  }

  @Endpoint(method = ServletMethod.GET, error = ErrorSerialization.JSON)
  public ResponseIF datasets(ClientRequestIF request) throws JSONException
  {
    String datasets = MappableClassDTO.getAllAsJSON(request);

    return new RestBodyResponse(new JSONObject(datasets));
  }

  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF remove(ClientRequestIF request, @RequestParamter(name = "id") String id)
  {
    MappableClassDTO.remove(request, id);

    return new RestResponse();
  }

  @Endpoint(url = "edit-dataset", method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF edit(ClientRequestIF request, @RequestParamter(name = "id") String id) throws JSONException
  {
    MappableClassDTO mappableClass = MappableClassDTO.lock(request, id);

    return new RestBodyResponse(new JSONObject(mappableClass.getAsJSON()));
  }
  
  @Endpoint(url = "xport-dataset", method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF xport(ClientRequestIF request, @RequestParamter(name = "id") String id) throws JSONException
  {
    MappableClassDTO.xport(request, id);

    return new RestResponse();
  }

  @Endpoint(url = "unlock-dataset", method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF cancel(ClientRequestIF request, @RequestParamter(name = "id") String id)
  {
    MappableClassDTO.unlock(request, id);

    return new RestResponse();
  }

  @Endpoint(url = "apply-dataset", method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF applyDatasetUpdate(ClientRequestIF request, @RequestParamter(name = "dataset") String dataset) throws JSONException
  {
    JSONObject dsJSONObj = new JSONObject(dataset);
    String dsId = dsJSONObj.getString("id");

    MappableClassDTO ds = MappableClassDTO.lock(request, dsId);
    MappableClassDTO.applyDatasetUpdate(request, dataset);
    ds.unlock();

    return new RestBodyResponse(new JSONObject(ds.getAsJSON()));
  }

}
