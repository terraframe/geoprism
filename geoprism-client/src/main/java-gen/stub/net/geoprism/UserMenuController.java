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
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import net.geoprism.DashboardQueryDTO;
import net.geoprism.JavascriptUtil;
import net.geoprism.UserMenuControllerBase;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.system.RolesDTO;
import com.runwaysdk.util.FileIO;

public class UserMenuController extends UserMenuControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR    = "/WEB-INF/";

  public static final String LAYOUT     = "WEB-INF/templates/basicLayout.jsp";

  public static final String MENU       = "com/runwaysdk/geodashboard/userMenu/userMenu.jsp";

  public static final String DASHBOARDS = "com/runwaysdk/geodashboard/userMenu/userDashboards.jsp";

  public UserMenuController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }

  @Override
  public void dashboards() throws IOException, ServletException
  {

    ClientRequestIF clientRequest = super.getClientRequest();

    DashboardQueryDTO dashboardsQ = DashboardDTO.getSortedDashboards(clientRequest);
    List<? extends DashboardDTO> dashboards = dashboardsQ.getResultSet();

    JavascriptUtil.loadUserBundle(this.getClientRequest(), this.req);

    this.req.setAttribute("dashboards", dashboards);
    this.req.setAttribute("isAdmin", this.userIsAdmin());

    render(DASHBOARDS);
  }

  /**
   * Gets the dashboard thumbnail for display in the app.
   * 
   * @dashboardId
   */
  @Override
  public void getDashboardMapThumbnail(String dashboardId)
  {
    ClientRequestIF clientRequest = super.getClientRequest();

    DashboardDTO db = DashboardDTO.get(clientRequest, dashboardId);

    try
    {
      InputStream istream = db.getThumbnailStream();

      try
      {
        resp.setContentType("image/png");

        ServletOutputStream ostream = resp.getOutputStream();

        try
        {
          FileIO.write(ostream, istream);

          ostream.flush();
        }
        finally
        {
          ostream.close();
        }
      }
      finally
      {
        istream.close();
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void menu() throws IOException, ServletException
  {
    JavascriptUtil.loadUserBundle(this.getClientRequest(), this.req);

    this.req.setAttribute("isAdmin", this.userIsAdmin());

    render(MENU);
  }

  private boolean userIsAdmin()
  {
    GeodashboardUserDTO currentUser = GeodashboardUserDTO.getCurrentUser(this.getClientRequest());

    List<? extends RolesDTO> userRoles = currentUser.getAllAssignedRole();
    for (RolesDTO role : userRoles)
    {
      Pattern regex = Pattern.compile("\\.(\\S+)");
      Matcher match = regex.matcher(role.getRoleName());
      if (match.find())
      {
        if (match.group(1).equals("admin.Administrator"))
        {
          return true;
        }
      }
    }
    return false;
  }

}
