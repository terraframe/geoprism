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
package net.geoprism.dashboard.layer;

import java.io.IOException;

import javax.servlet.ServletException;

import net.geoprism.JSONControllerUtil;
import net.geoprism.dashboard.DashboardMapDTO;
import net.geoprism.dashboard.DashboardThematicStyleDTO;

import org.json.JSONObject;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.system.metadata.MdAttributeDTO;
import com.runwaysdk.transport.conversion.json.BusinessDTOToJSON;

public class DashboardThematicLayerController extends DashboardThematicLayerControllerBase 
{
  public static final String JSP_DIR = "/WEB-INF/net/geoprism/dashboard/layer/DashboardThematicLayer/";

  public static final String LAYOUT  = "WEB-INF/templates/layout.jsp";

  public DashboardThematicLayerController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }

  @Override
  public void edit(String oid) throws java.io.IOException, javax.servlet.ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      DashboardThematicLayerDTO layer = DashboardThematicLayerDTO.lock(request, oid);
      DashboardThematicStyleDTO style = (DashboardThematicStyleDTO) layer.getAllHasStyle().get(0);

      DashboardMapDTO map = layer.getDashboardMap();

      String options = DashboardThematicLayerDTO.getOptionsJSON(request, layer.getMdAttributeId(), map.getDashboardId());

      JSONObject response = new JSONObject();
      response.put("layerDTO", BusinessDTOToJSON.getConverter(layer).populate());
      response.put("styleDTO", BusinessDTOToJSON.getConverter(style).populate());
      response.put("layer", new JSONObject(layer.getJSON()));
      response.put("options", new JSONObject(options));

      JSONControllerUtil.writeReponse(this.resp, response);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, this.getClientRequest());
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

      JSONControllerUtil.writeReponse(this.resp, response);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, this.getClientRequest());
    }
  }

}
