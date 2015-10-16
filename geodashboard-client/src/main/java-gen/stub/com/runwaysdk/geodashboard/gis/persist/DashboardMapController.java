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
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.ErrorUtility;
import com.runwaysdk.geodashboard.AccessConstants;
import com.runwaysdk.geodashboard.DashboardDTO;
import com.runwaysdk.geodashboard.DashboardQueryDTO;
import com.runwaysdk.geodashboard.FileDownloadUtil;
import com.runwaysdk.geodashboard.GeodashboardUserDTO;
import com.runwaysdk.geodashboard.JavascriptUtil;
import com.runwaysdk.geodashboard.MdAttributeViewDTO;
import com.runwaysdk.geodashboard.MetadataWrapperDTO;
import com.runwaysdk.geodashboard.gis.DashboardHasNoMapExceptionDTO;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverProperties;
import com.runwaysdk.system.RolesDTO;
import com.runwaysdk.system.gis.geo.GeoEntityDTO;
import com.runwaysdk.system.gis.geo.LocatedInDTO;
import com.runwaysdk.system.metadata.MdClassDTO;

public class DashboardMapController extends DashboardMapControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR = "/WEB-INF/com/runwaysdk/geodashboard/gis/persist/DashboardMap/";

  public static final String LAYOUT  = "WEB-INF/templates/layout.jsp";

  public DashboardMapController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }

  public void cancel(com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    dto.unlock();
    this.view(dto.getId());
  }

  public void failCancel(com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    this.edit(dto.getId());
  }

  public void create(com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      dto.apply();
      this.view(dto.getId());
    }
    catch (com.runwaysdk.ProblemExceptionDTO e)
    {
      this.failCreate(dto);
    }
  }

  public void failCreate(com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }

  public void delete(com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO dto) throws java.io.IOException, javax.servlet.ServletException
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

  public void failDelete(com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void edit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO dto = com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO.lock(super.getClientRequest(), id);
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void failEdit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.view(id);
  }

  public void newInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO dto = new com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO(clientRequest);
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }

  public void failNewInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }

  public void update(com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      dto.apply();
      this.view(dto.getId());
    }
    catch (com.runwaysdk.ProblemExceptionDTO e)
    {
      this.failUpdate(dto);
    }
  }

  public void failUpdate(com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void view(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    req.setAttribute("item", com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO.get(clientRequest, id));
    render("viewComponent.jsp");
  }

  public void failView(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }

  public void viewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.gis.persist.DashboardMapQueryDTO query = com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO.getAllInstances(clientRequest, null, true, 20, 1);
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
    com.runwaysdk.geodashboard.gis.persist.DashboardMapQueryDTO query = com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO.getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
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

    String dashboardId = req.getParameter("dashboard");

    // Get all dashboards
    DashboardQueryDTO dashboardQ = DashboardDTO.getSortedDashboards(clientRequest);
    List<? extends DashboardDTO> dashboards = dashboardQ.getResultSet();

    if (dashboards.size() == 0)
    {
      this.failCreateMapForSession();

      return;
    }

    // Figure out the active dashboard.
    DashboardDTO activeDashboard = dashboards.get(0);
    if (dashboardId != null && dashboardId.length() > 0)
    {
      activeDashboard = DashboardDTO.get(clientRequest, dashboardId);
    }

    if (!activeDashboard.hasAccess())
    {
      this.failCreateMapForSession();
    }
    else
    {
      req.setAttribute("activeDashboard", activeDashboard);
      req.setAttribute("dashboardId", activeDashboard.getId());
      req.setAttribute("workspace", GeoserverProperties.getWorkspace());

      // Dashboards does not include the active dashboard.
      dashboards.remove(activeDashboard);
      req.setAttribute("dashboards", dashboards);

      if (activeDashboard.getMapId() == null || activeDashboard.getMapId().equals(""))
      {
        throw new DashboardHasNoMapExceptionDTO(clientRequest);
      }

      req.setAttribute("mapId", activeDashboard.getMapId());

      // Add Dashboard's specified attributes (i.e. SalesTransaction) to the request.
      MdClassDTO[] types = activeDashboard.getSortedTypes();
      req.setAttribute("types", types);

      Map<String, List<MdAttributeViewDTO>> attrMap = new LinkedHashMap<String, List<MdAttributeViewDTO>>();

      for (MetadataWrapperDTO mdDTO : activeDashboard.getAllMetadata())
      {
        List<MdAttributeViewDTO> attrs = new LinkedList<MdAttributeViewDTO>();
        MdAttributeViewDTO[] views = mdDTO.getSortedAttributes();

        for (MdAttributeViewDTO mdAttrView : views)
        {
          attrs.add(mdAttrView);
        }

        attrMap.put(mdDTO.getWrappedMdClassId(), attrs);
      }

      req.setAttribute("attrMap", attrMap);

      GeoEntityDTO root = GeoEntityDTO.getRoot(this.getClientRequest());

      this.req.setAttribute("type", GeoEntityDTO.CLASS);
      this.req.setAttribute("relationshipType", LocatedInDTO.CLASS);
      this.req.setAttribute("rootId", root.getId());

      JavascriptUtil.loadDynamicMapBundle(this.getClientRequest(), req);

      /*
       * Load the conditions information
       */
      req.setAttribute("conditions", activeDashboard.getConditionsJSON());
      req.setAttribute("hasReport", activeDashboard.hasReport());
      req.setAttribute("editDashboard", GeodashboardUserDTO.hasAccess(this.getClientRequest(), AccessConstants.EDIT_DASHBOARD));
      req.setAttribute("editData", GeodashboardUserDTO.hasAccess(this.getClientRequest(), AccessConstants.EDIT_DATA));
      
      GeodashboardUserDTO currentUser = GeodashboardUserDTO.getCurrentUser(this.getClientRequest());
      boolean isAdmin = false;
      List<? extends RolesDTO> userRoles = currentUser.getAllAssignedRole();
      for(RolesDTO role : userRoles)
      {
        Pattern regex = Pattern.compile("\\.(\\S+)");
        Matcher match = regex.matcher(role.getRoleName());
        if (match.find())
        {
          if(match.group(1).equals("admin.Administrator"))
          {
            isAdmin = true;
          }
        }
      }
      req.setAttribute("isAdmin", isAdmin);

      req.setAttribute("aggregationMap", DashboardStyleDTO.getAggregationJSON(this.getClientRequest()));

      render("dashboardViewer.jsp");
    }
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
}
