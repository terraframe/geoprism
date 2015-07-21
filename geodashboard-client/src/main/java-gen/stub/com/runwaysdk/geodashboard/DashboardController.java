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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.dataaccess.ProgrammingErrorExceptionDTO;
import com.runwaysdk.transport.conversion.json.JSONReturnObject;

public class DashboardController extends DashboardControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR = "/WEB-INF/com/runwaysdk/geodashboard/Dashboard/";

  public static final String LAYOUT  = "WEB-INF/templates/layout.jsp";

  public DashboardController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }

  public void cancel(com.runwaysdk.geodashboard.DashboardDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    if (!dto.isNewInstance())
    {
      dto.unlock();
    }
  }

  public void failCancel(com.runwaysdk.geodashboard.DashboardDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    this.edit(dto.getId());
  }
  

  public void create(com.runwaysdk.geodashboard.DashboardDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      DashboardDTO applied = DashboardDTO.create(getClientRequest(), dto);

      JSONReturnObject jsonReturn = new JSONReturnObject(applied);
      jsonReturn.setInformation(this.getClientRequest().getInformation());
      jsonReturn.setWarnings(this.getClientRequest().getWarnings());

      this.getResponse().setStatus(200);
      this.getResponse().setContentType("application/json");

      this.getResponse().getWriter().print(jsonReturn.toString());
    }
    catch (com.runwaysdk.ProblemExceptionDTO e)
    {
      this.failCreate(dto);
    }
  }

  public void failCreate(com.runwaysdk.geodashboard.DashboardDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("dashboard", dto);
    render("createComponent.jsp");
  }

  public void delete(com.runwaysdk.geodashboard.DashboardDTO dto) throws java.io.IOException, javax.servlet.ServletException
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

  public void failDelete(com.runwaysdk.geodashboard.DashboardDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void edit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.geodashboard.DashboardDTO dto = com.runwaysdk.geodashboard.DashboardDTO.lock(super.getClientRequest(), id);
    req.setAttribute("item", dto);
    
    DashboardDTO dashboard = DashboardDTO.get(this.getClientRequest(), id);
    req.setAttribute("dashboard", dashboard);
    
    String dashboardUsersJSON = dashboard.getAllDashboardUsersJSON();
    req.setAttribute("dashboardUsersJSON", encode(dashboardUsersJSON));
    
    render("editComponent.jsp");
  }

  public void failEdit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.view(id);
  }

  public void newInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.DashboardDTO dto = new com.runwaysdk.geodashboard.DashboardDTO(clientRequest);
    req.setAttribute("dashboard", dto);
      
    render("createComponent.jsp");
  }

  public void failNewInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }

  public void update(com.runwaysdk.geodashboard.DashboardDTO dto) throws java.io.IOException, javax.servlet.ServletException
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

  public void failUpdate(com.runwaysdk.geodashboard.DashboardDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void view(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    req.setAttribute("item", com.runwaysdk.geodashboard.DashboardDTO.get(clientRequest, id));
    render("viewComponent.jsp");
  }

  public void failView(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }

  public void viewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.DashboardQueryDTO query = com.runwaysdk.geodashboard.DashboardDTO.getAllInstances(clientRequest, null, true, 20, 1);
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
    com.runwaysdk.geodashboard.DashboardQueryDTO query = com.runwaysdk.geodashboard.DashboardDTO.getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }

  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }

  @Override
  public void newClone(String dashboardId) throws IOException, ServletException
  {
    DashboardDTO dashboard = DashboardDTO.get(this.getClientRequest(), dashboardId);

    req.setAttribute("dashboard", dashboard);
    
    render("newClone.jsp");
  }
  
  private String encode(String value)
  {
    if (value != null)
    {
      try
      {
        String encoded = URLEncoder.encode(value, "UTF-8");
        encoded = encoded.replaceAll("\\+", "%20");
        encoded = encoded.replaceAll("\\%21", "!");
        encoded = encoded.replaceAll("\\%27", "'");
        encoded = encoded.replaceAll("\\%28", "(");
        encoded = encoded.replaceAll("\\%29", ")");
        encoded = encoded.replaceAll("\\%7E", "~");

        return encoded;
      }
      catch (UnsupportedEncodingException e)
      {
        throw new ProgrammingErrorExceptionDTO(e.getClass().getName(), e.getLocalizedMessage(), e.getMessage());
      }
    }

    return "";
  }
}
