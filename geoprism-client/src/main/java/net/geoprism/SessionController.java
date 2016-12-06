/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Runway SDK(tm). If not, see
 * <http://www.gnu.org/licenses/>.
 */
package net.geoprism;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.geoprism.account.OauthBridge;

import com.runwaysdk.ClientSession;
import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.web.ServletUtility;
import com.runwaysdk.web.WebClientSession;

public class SessionController extends SessionControllerBase implements Reloadable
{
  public static final long   serialVersionUID = 1234283350799L;

  public static final String FORM_ACTION      = SessionController.class.getName() + ".form" + MdActionInfo.ACTION_SUFFIX;

  public static final String LOGIN_ACTION     = SessionController.class.getName() + ".login" + MdActionInfo.ACTION_SUFFIX;

  public static final String LOGOUT_ACTION    = SessionController.class.getName() + ".logout" + MdActionInfo.ACTION_SUFFIX;

  public SessionController(HttpServletRequest req, HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous);
  }

  @Override
  public void form() throws IOException, ServletException
  {
    String errorMessage = this.req.getParameter("errorMessage");

    this.form(errorMessage);
  }

  private void form(String errorMessage) throws IOException, ServletException
  {
    CachedImageUtil.setBannerPath(this.req, this.resp);

    if (errorMessage != null)
    {
      req.setAttribute("errorMessage", errorMessage);
    }
    
    req.setAttribute("servers", ClientConfigurationService.getOauthServers());

    req.getRequestDispatcher("/login.jsp").forward(req, resp);
  }

  @Override
  public void login(String username, String password) throws IOException, ServletException
  {
    if (username != null)
    {
      username = username.trim();
    }

    try
    {
      // Ensure the server context has been initialized

      Locale[] locales = ServletUtility.getLocales(req);

      WebClientSession clientSession = WebClientSession.createUserSession(username, password, locales);
      ClientRequestIF clientRequest = clientSession.getRequest();

      req.getSession().setMaxInactiveInterval(CommonProperties.getSessionTime());
      req.getSession().setAttribute(ClientConstants.CLIENTSESSION, clientSession);
      req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);

      String path = req.getContextPath() + "/menu";
      resp.sendRedirect(path);
    }
    catch (Throwable t)
    {
      String message = t.getLocalizedMessage() == null ? t.getMessage() : t.getLocalizedMessage();
      resp.sendRedirect(req.getContextPath() + "/session/form?errorMessage=" + URLEncoder.encode(message, EncodingConstants.ENCODING));

      // this.form(message);
    }
  }

  @Override
  public void failLogin(String username, String password) throws IOException, ServletException
  {
    // this.form("Unable to login");

    resp.sendRedirect(req.getContextPath() + "/session/form?errorMessage=" + URLEncoder.encode("Unable to login", EncodingConstants.ENCODING));
  }

  @Override
  public void logout() throws IOException, ServletException
  {
    // process which logs the user out.
    ClientSession clientSession = super.getClientSession();
    if (clientSession != null)
    {
      clientSession.logout();
    }

    req.getSession().removeAttribute(ClientConstants.CLIENTSESSION);
    req.getSession().invalidate();

    resp.sendRedirect(req.getContextPath() + "/session/form");
  }

  @Override
  public void ologin(String code) throws IOException, ServletException
  {
    try
    {
      Locale[] locales = ServletUtility.getLocales(req);

      String sessionId = OauthBridge.createSession("DHIS2", code, locales);

      WebClientSession clientSession = WebClientSession.getExistingSession(sessionId, locales);
      ClientRequestIF clientRequest = clientSession.getRequest();

      HttpSession session = req.getSession();
      
      session.setMaxInactiveInterval(CommonProperties.getSessionTime());
      session.setAttribute(ClientConstants.CLIENTSESSION, clientSession);
      req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);

      resp.sendRedirect(req.getContextPath() + "/menu");
//      req.getRequestDispatcher("/geoprism/menu").forward(req, resp);
    }
    catch (Throwable t)
    {
      String message = t.getLocalizedMessage() == null ? t.getMessage() : t.getLocalizedMessage();
      resp.sendRedirect(req.getContextPath() + "/session/form?errorMessage=" + URLEncoder.encode(message, EncodingConstants.ENCODING));
    }
  }
}
