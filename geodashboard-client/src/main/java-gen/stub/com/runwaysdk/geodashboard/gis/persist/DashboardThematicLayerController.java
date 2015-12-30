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
package com.runwaysdk.geodashboard.gis.persist;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.system.metadata.MdAttributeDTO;
import com.runwaysdk.transport.conversion.ClientConversionFacade;
import com.runwaysdk.transport.conversion.json.BusinessDTOToJSON;
import com.runwaysdk.transport.conversion.json.JSONReturnObject;
import com.runwaysdk.web.json.JSONRunwayExceptionDTO;

public class DashboardThematicLayerController extends DashboardThematicLayerControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR = "/WEB-INF/com/runwaysdk/geodashboard/gis/persist/DashboardThematicLayer/";

  public static final String LAYOUT  = "WEB-INF/templates/layout.jsp";

  private static final Log   log     = LogFactory.getLog(DashboardThematicLayerController.class);

  public DashboardThematicLayerController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }

  @Override
  public void edit(String id) throws java.io.IOException, javax.servlet.ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      DashboardThematicLayerDTO layer = DashboardThematicLayerDTO.lock(request, id);
      DashboardThematicStyleDTO style = (DashboardThematicStyleDTO) layer.getAllHasStyle().get(0);

      DashboardMapDTO map = layer.getDashboardMap();

      String options = DashboardThematicLayerDTO.getOptionsJSON(request, layer.getMdAttributeId(), map.getDashboardId());

      JSONObject response = new JSONObject();
      response.put("layerDTO", BusinessDTOToJSON.getConverter(layer).populate());
      response.put("styleDTO", BusinessDTOToJSON.getConverter(style).populate());
      response.put("layer", new JSONObject(layer.getJSON()));
      response.put("options", new JSONObject(options));

      JSONReturnObject ret = new JSONReturnObject();
      ret.setReturnValue(response);

      String content = ret.getJSON().toString();
      byte[] bytes = content.getBytes("UTF-8");

      this.resp.setStatus(200);
      this.resp.setContentType("application/json");

      ServletOutputStream ostream = this.resp.getOutputStream();
      ostream.write(bytes);
      ostream.flush();
      ostream.close();
    }
    catch (Throwable t)
    {
      RuntimeException throwable = ClientConversionFacade.buildJSONThrowable(t, request.getSessionId(), false);
      JSONRunwayExceptionDTO ex = new JSONRunwayExceptionDTO(throwable);

      String content = ex.getJSON();

      this.resp.setStatus(500);
      this.resp.setContentType("application/json");

      ServletOutputStream ostream = this.resp.getOutputStream();
      ostream.write(content.getBytes("UTF-8"));
      ostream.flush();
      ostream.close();
    }
  }

  @Override
  public void newThematicInstance(String mdAttribute, String mapId) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      MdAttributeDTO mdAttributeDTO = MdAttributeDTO.get(request, mdAttribute);
      DashboardMapDTO map = DashboardMapDTO.get(request, mapId);

      DashboardThematicLayerDTO layer = new DashboardThematicLayerDTO(request);
      layer.setMdAttribute(mdAttributeDTO);
      layer.setDashboardMap(map);

      DashboardThematicStyleDTO style = new DashboardThematicStyleDTO(request);

      String options = DashboardThematicLayerDTO.getOptionsJSON(request, mdAttribute, map.getDashboardId());

      JSONObject response = new JSONObject();
      response.put("layerDTO", BusinessDTOToJSON.getConverter(layer).populate());
      response.put("styleDTO", BusinessDTOToJSON.getConverter(style).populate());
      response.put("layer", new JSONObject(layer.getJSON()));
      response.put("options", new JSONObject(options));

      JSONReturnObject ret = new JSONReturnObject();
      ret.setReturnValue(response);

      String content = ret.getJSON().toString();
      byte[] bytes = content.getBytes("UTF-8");

      this.resp.setStatus(200);
      this.resp.setContentType("application/json");

      ServletOutputStream ostream = this.resp.getOutputStream();
      ostream.write(bytes);
      ostream.flush();
      ostream.close();
    }
    catch (Throwable t)
    {
      RuntimeException throwable = ClientConversionFacade.buildJSONThrowable(t, request.getSessionId(), false);
      JSONRunwayExceptionDTO ex = new JSONRunwayExceptionDTO(throwable);

      String content = ex.getJSON();

      this.resp.setStatus(500);
      this.resp.setContentType("application/json");

      ServletOutputStream ostream = this.resp.getOutputStream();
      ostream.write(content.getBytes("UTF-8"));
      ostream.flush();
      ostream.close();
    }
  }

}
