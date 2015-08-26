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
import java.util.List;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.geodashboard.DashboardDTO;
import com.runwaysdk.system.gis.geo.GeoEntityDTO;

import javax.servlet.ServletException;

public class UserMenuController extends UserMenuControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR   = "/WEB-INF/";
  
  public static final String JSP = "/userDashboards.jsp";
  
  public UserMenuController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, JSP);
  }
  
  @Override
  public void getSortedDashboards() throws IOException, ServletException
  {
    
    ClientRequestIF clientRequest = super.getClientRequest();
    
    DashboardQueryDTO dashboardsQ = DashboardDTO.getSortedDashboards(clientRequest);
    List<? extends DashboardDTO> dashboards = dashboardsQ.getResultSet(); 
    
    this.req.setAttribute("dashboards", dashboards);
    
    render("userDashboards.jsp");
  }
  
}
