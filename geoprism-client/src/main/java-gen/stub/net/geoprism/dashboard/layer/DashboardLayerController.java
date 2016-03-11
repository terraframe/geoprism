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



public class DashboardLayerController extends DashboardLayerControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR = "/WEB-INF/net/geoprism/dashboard/layer/DashboardLayer/";

  public static final String LAYOUT  = "WEB-INF/templates/layout.jsp";

  public DashboardLayerController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }

  public void cancel(DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    if (!dto.isNewInstance())
    {
      dto.unlock();
    }
  }

  public void failCancel(DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    this.edit(dto.getId());
  }

  public void create(DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
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

  public void failCreate(DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }

  public void delete(DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
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

  public void failDelete(DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void failEdit(String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.view(id);
  }

  public void failNewInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }

  public void update(DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      dto.apply();
    }
    catch (com.runwaysdk.ProblemExceptionDTO e)
    {
      this.failUpdate(dto);
    }
  }

  public void failUpdate(DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void view(String id) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    req.setAttribute("layer", DashboardLayerDTO.get(clientRequest, id));
    render("viewComponent.jsp");
  }

  public void failView(String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }

  public void viewAll() throws java.io.IOException, javax.servlet.ServletException
  {
//    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
//    DashboardLayerQueryDTO query = DashboardLayerDTO.getAllInstances(clientRequest, null, true, 20, 1);
//    req.setAttribute("query", query);
//    render("viewAllComponent.jsp");
  }

  public void failViewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }

  public void viewPage(String sortAttribute, Boolean isAscending, Integer pageSize, Integer pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
//    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
//    DashboardLayerQueryDTO query = DashboardLayerDTO.getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
//    req.setAttribute("query", query);
//    render("viewAllComponent.jsp");
  }

  public void failViewPage(String sortAttribute, String isAscending, String pageSize, String pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }

}
