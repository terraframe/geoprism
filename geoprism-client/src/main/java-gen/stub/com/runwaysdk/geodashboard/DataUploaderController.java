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
package com.runwaysdk.geodashboard;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.MultipartFileParameter;
import com.runwaysdk.generation.loader.Reloadable;

public class DataUploaderController extends DataUploaderControllerBase implements Reloadable
{
  public DataUploaderController(HttpServletRequest req, HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous);
  }

  @Override
  public void getAttributeInformation(MultipartFileParameter file) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      String fileName = file.getFilename();
      InputStream stream = file.getInputStream();

      try
      {
        JSONObject object = new JSONObject();
        object.put("information", new JSONObject(DataUploaderDTO.getAttributeInformation(request, fileName, stream)));
        object.put("options", new JSONObject(DataUploaderDTO.getOptionsJSON(request)));

        JSONControllerUtil.writeReponse(this.resp, object.toString());
      }
      finally
      {
        /*
         * Just in case the stream isn't closed by the server method
         */
        stream.close();
      }
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, this.getClientRequest());
    }
  }

  @Override
  public void importData(String configuration) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      String datasets = DataUploaderDTO.importData(request, configuration);

      JSONObject object = new JSONObject();
      object.put("datasets", new JSONArray(datasets));

      JSONControllerUtil.writeReponse(this.resp, object.toString());
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, request);
    }

  }

}
