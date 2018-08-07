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
package net.geoprism.dashboard;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.ErrorUtility;

import net.geoprism.AccessConstants;
import net.geoprism.FileDownloadUtil;
import net.geoprism.GeoprismUserDTO;
import net.geoprism.SystemLogoSingletonDTO;
import net.geoprism.dashboard.layer.DashboardLayerDTO;
import net.geoprism.gis.geoserver.GeoserverProperties;

public class DashboardMapController extends DashboardMapControllerBase 
{
  public static final String JSP_DIR = "/WEB-INF/net/geoprism/dashboard/DashboardMap/";

  public static final String LAYOUT  = "WEB-INF/templates/layout.jsp";

  public DashboardMapController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }

  public void cancel(net.geoprism.dashboard.DashboardMapDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    dto.unlock();
    this.view(dto.getOid());
  }

  public void failCancel(net.geoprism.dashboard.DashboardMapDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    this.edit(dto.getOid());
  }

  public void create(net.geoprism.dashboard.DashboardMapDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      dto.apply();
      this.view(dto.getOid());
    }
    catch (com.runwaysdk.ProblemExceptionDTO e)
    {
      this.failCreate(dto);
    }
  }

  public void failCreate(net.geoprism.dashboard.DashboardMapDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }

  public void delete(net.geoprism.dashboard.DashboardMapDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      dto.delete();
      this.viewAll();
    }
    catch (com.runwaysdk.ProblemExceptionDTO e)
    {
      this.failDelete(dto);
    }
  }

  public void failDelete(net.geoprism.dashboard.DashboardMapDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void edit(java.lang.String oid) throws java.io.IOException, javax.servlet.ServletException
  {
    net.geoprism.dashboard.DashboardMapDTO dto = net.geoprism.dashboard.DashboardMapDTO.lock(super.getClientRequest(), oid);
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void failEdit(java.lang.String oid) throws java.io.IOException, javax.servlet.ServletException
  {
    this.view(oid);
  }

  public void newInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    net.geoprism.dashboard.DashboardMapDTO dto = new net.geoprism.dashboard.DashboardMapDTO(clientRequest);
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }

  public void failNewInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }

  public void update(net.geoprism.dashboard.DashboardMapDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      dto.apply();
      this.view(dto.getOid());
    }
    catch (com.runwaysdk.ProblemExceptionDTO e)
    {
      this.failUpdate(dto);
    }
  }

  public void failUpdate(net.geoprism.dashboard.DashboardMapDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void view(java.lang.String oid) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    req.setAttribute("item", net.geoprism.dashboard.DashboardMapDTO.get(clientRequest, oid));
    render("viewComponent.jsp");
  }

  public void failView(java.lang.String oid) throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }

  public void viewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    net.geoprism.dashboard.DashboardMapQueryDTO query = net.geoprism.dashboard.DashboardMapDTO.getAllInstances(clientRequest, null, true, 20, 1);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }

  public void failViewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }

  public void viewPage(java.lang.String sortAttribute, java.lang.Boolean isAscending, java.lang.Integer pageSize, java.lang.Integer pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    net.geoprism.dashboard.DashboardMapQueryDTO query = net.geoprism.dashboard.DashboardMapDTO.getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }

  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }

  @Override
  public void createMapForSession() throws IOException, ServletException
  {
    ClientRequestIF clientRequest = this.getClientRequest();

    // Get all dashboards
    DashboardQueryDTO dashboardQ = DashboardDTO.getSortedDashboards(clientRequest);
    List<? extends DashboardDTO> dashboards = dashboardQ.getResultSet();

    if (dashboards.size() == 0)
    {
      this.failCreateMapForSession();

      return;
    }

    req.setAttribute("workspace", GeoserverProperties.getWorkspace());
    req.setAttribute("editDashboard", GeoprismUserDTO.hasAccess(this.getClientRequest(), AccessConstants.EDIT_DASHBOARD));
    req.setAttribute("editData", GeoprismUserDTO.hasAccess(this.getClientRequest(), AccessConstants.EDIT_DATA));

    String miniLogoFile = SystemLogoSingletonDTO.getMiniLogoFileFromCache(this.getClientRequest(), this.req);
    if (miniLogoFile != null)
    {
      this.req.setAttribute("miniLogoFilePath", miniLogoFile);
      this.req.setAttribute("miniLogoFileName", miniLogoFile.replaceFirst(SystemLogoSingletonDTO.getImagesTempDir(this.req), ""));
    }

    req.getRequestDispatcher("/WEB-INF/net/geoprism/dashboard/DashboardMap/dashboardViewer.jsp").forward(req, resp);
  }

  @Override
  public void failCreateMapForSession() throws IOException, ServletException
  {
    render("nodashboard.jsp");
  }

  @Override
  public void exportMap(String mapId, String outFileName, String outFileFormat, String mapBounds, String mapSize, String activeBaseMap) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();
    DashboardMapDTO map = DashboardMapDTO.get(request, mapId);

    if (outFileName == null || outFileName.length() == 0)
    {
      outFileName = "default";
    }

    try
    {
      InputStream mapImageInStream = map.generateMapImageExport(outFileFormat, mapBounds, mapSize, activeBaseMap);
      FileDownloadUtil.writeFile(resp, outFileName, outFileFormat, mapImageInStream, "application/" + outFileFormat);
    }
    catch (Exception e)
    {
      ErrorUtility.prepareThrowable(e, req, resp, false);
      this.failExportMap(mapId, outFileName, outFileFormat, mapBounds, mapSize, activeBaseMap);
    }
  }

  @Override
  public void exportLayerData(String mapId, String state, String layerId) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      DashboardMapDTO map = DashboardMapDTO.get(request, mapId);
      DashboardLayerDTO layer = DashboardLayerDTO.get(request, layerId);
      String layerName = layer.getNameLabel().getValue();

      InputStream istream = map.exportLayerData(state, layerId);

      try
      {
        FileDownloadUtil.writeFile(resp, layerName, "xlsx", istream, "application/xlsx");
      }
      finally
      {
        istream.close();
      }
    }
    catch (Exception e)
    {
      ErrorUtility.prepareThrowable(e, req, resp, false);
    }
  }
}
