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
package net.geoprism;

import java.io.IOException;

import javax.servlet.ServletException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.system.metadata.MdAttributeIndicatorDTO;

public class DataSetController extends DataSetControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public DataSetController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous);
  }

  @Override
  public void getAll() throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      String datasets = MappableClassDTO.getAllAsJSON(request);

      JSONObject object = new JSONObject();
      object.put("datasets", new JSONArray(datasets));

      JSONControllerUtil.writeReponse(this.resp, object);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, this.getClientRequest());
    }
  }

  @Override
  public void applyDatasetUpdate(String dataset) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      JSONObject dsJSONObj = new JSONObject(dataset);
      String dsId = dsJSONObj.getString("id");

      MappableClassDTO ds = MappableClassDTO.lock(request, dsId);
      MappableClassDTO.applyDatasetUpdate(request, dataset);
      ds.unlock();

      JSONControllerUtil.writeReponse(this.resp, new JSONObject(ds.getAsJSON()));
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, this.getClientRequest());
    }
  }

  @Override
  public void remove(String id) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      MappableClassDTO.remove(request, id);

      JSONControllerUtil.writeReponse(this.resp, "");
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, this.getClientRequest());
    }
  }

  @Override
  public void edit(String id) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      MappableClassDTO mappableClass = MappableClassDTO.lock(request, id);

      JSONControllerUtil.writeReponse(this.resp, new JSONObject(mappableClass.getAsJSON()));
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, this.getClientRequest());
    }
  }

  @Override
  public void apply(String config) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      MappableClassDTO.applyDatasetUpdate(request, config);

      JSONControllerUtil.writeReponse(this.resp);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, this.getClientRequest());
    }
  }

  @Override
  public void cancel(String id) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      MappableClassDTO.unlock(request, id);

      JSONControllerUtil.writeReponse(this.resp);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, this.getClientRequest());
    }
  }

  @Override
  public void addIndicator(String datasetId, String indicator) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      String response = MappableClassDTO.addIndicator(request, datasetId, indicator);

      JSONControllerUtil.writeReponse(this.resp, new JSONObject(response));
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, this.getClientRequest());
    }
  }

  @Override
  public void editAttribute(String id) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      String response = MappableClassDTO.lockIndicator(request, id);

      JSONControllerUtil.writeReponse(this.resp, new JSONObject(response));
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, this.getClientRequest());
    }
  }

  @Override
  public void removeAttribute(String id) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      MappableClassDTO.removeIndicator(request, id);

      JSONControllerUtil.writeReponse(this.resp);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, this.getClientRequest());
    }
  }

  @Override
  public void unlockAttribute(String id) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      MdAttributeIndicatorDTO.unlock(request, id);

      JSONControllerUtil.writeReponse(this.resp);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, this.getClientRequest());
    }
  }
}
