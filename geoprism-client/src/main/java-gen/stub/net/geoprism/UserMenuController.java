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
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import net.geoprism.dashboard.DashboardDTO;
import net.geoprism.dashboard.DashboardQueryDTO;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.system.RolesDTO;
import com.runwaysdk.util.FileIO;

public class UserMenuController extends UserMenuControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR         = "/WEB-INF/";

  public static final String LAYOUT          = "WEB-INF/templates/basicLayout.jsp";

  public static final String MENU            = "net/geoprism/userMenu/userMenu.jsp";

  public static final String DATA_MANAGEMENT = "net/geoprism/userMenu/data-management.jsp";

  public static final String DASHBOARDS      = "net/geoprism/userMenu/userDashboards.jsp";

  public UserMenuController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }

  @Override
  public void dataManagement() throws IOException, ServletException
  {
    JavascriptUtil.loadDataManagementBundle(this.getClientRequest(), this.req);

    req.getRequestDispatcher(JSP_DIR + DATA_MANAGEMENT).forward(req, resp);
  }

  @Override
  public void dashboards() throws IOException, ServletException
  {

    ClientRequestIF clientRequest = super.getClientRequest();

    DashboardQueryDTO dashboardsQ = DashboardDTO.getSortedDashboards(clientRequest);
    List<? extends DashboardDTO> dashboards = dashboardsQ.getResultSet();

    JavascriptUtil.loadUserBundle(this.getClientRequest(), this.req);

    String bannerFile = SystemLogoSingletonDTO.getBannerFileFromCache(this.getClientRequest(), this.req);
    if (bannerFile != null)
    {
      this.req.setAttribute("bannerFilePath", bannerFile);
      this.req.setAttribute("bannerFileName", bannerFile.replaceFirst(SystemLogoSingletonDTO.getImagesTempDir(this.req), ""));
    }
    String miniLogoFile = SystemLogoSingletonDTO.getMiniLogoFileFromCache(this.getClientRequest(), this.req);
    if (miniLogoFile != null)
    {
      this.req.setAttribute("miniLogoFilePath", miniLogoFile);
      this.req.setAttribute("miniLogoFileName", miniLogoFile.replaceFirst(SystemLogoSingletonDTO.getImagesTempDir(this.req), ""));
    }

    Set<String> roleNames = this.getAssignedRoleNames();

    this.req.setAttribute("dashboards", dashboards);
    this.req.setAttribute("isAdmin", roleNames.contains(RoleConstants.ADIM_ROLE));

    setLogoReqAttrs();

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

    String bannerFile = SystemLogoSingletonDTO.getBannerFileFromCache(this.getClientRequest(), this.req);
    if (bannerFile != null)
    {
      this.req.setAttribute("bannerFilePath", bannerFile);
      this.req.setAttribute("bannerFileName", bannerFile.replaceFirst(SystemLogoSingletonDTO.getImagesTempDir(this.req), ""));
    }
    String miniLogoFile = SystemLogoSingletonDTO.getMiniLogoFileFromCache(this.getClientRequest(), this.req);
    if (miniLogoFile != null)
    {
      this.req.setAttribute("miniLogoFilePath", miniLogoFile);
      this.req.setAttribute("miniLogoFileName", miniLogoFile.replaceFirst(SystemLogoSingletonDTO.getImagesTempDir(this.req), ""));
    }

    Set<String> roleNames = this.getAssignedRoleNames();

    List<GeoprismApplication> allApplications = ClientConfigurationService.getApplications(this.getClientRequest());
    List<GeoprismApplication> authorizedApplications = allApplications.stream().filter(p -> p.isValid(roleNames)).collect(Collectors.toList());

    this.req.setAttribute("applications", authorizedApplications);
    this.req.setAttribute("isAdmin", roleNames.contains(RoleConstants.ADIM_ROLE));
    this.req.setAttribute("isBuilder", roleNames.contains(RoleConstants.BUILDER_ROLE));

    setLogoReqAttrs();

    render(MENU);
  }

  private void setLogoReqAttrs() throws IOException, ServletException
  {
    CachedImageUtil.setBannerPath(this.req, this.resp);

    String miniLogoFile = SystemLogoSingletonDTO.getMiniLogoFileFromCache(this.getClientRequest(), this.req);

    if (miniLogoFile != null)
    {
      this.req.setAttribute("miniLogoFilePath", miniLogoFile);
    }
    else
    {
      // For now, let's just not display the mini logo if it doesn't exist.
      // this.req.setAttribute("miniLogoFilePath", "net/geoprism/images/splash_logo_icon.png");
    }
  }

  private Set<String> getAssignedRoleNames()
  {
    Set<String> roleNames = new TreeSet<String>();

    GeoprismUserDTO currentUser = GeoprismUserDTO.getCurrentUser(this.getClientRequest());

    List<? extends RolesDTO> userRoles = currentUser.getAllAssignedRole();
    for (RolesDTO role : userRoles)
    {
      roleNames.add(role.getRoleName());
    }

    return roleNames;
  }

}
