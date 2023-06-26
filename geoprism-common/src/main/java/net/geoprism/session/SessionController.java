/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.session;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.runwaysdk.ClientSession;
import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.request.ServletRequestIF;
import com.runwaysdk.web.WebClientSession;

import net.geoprism.rbac.RoleServiceIF;
import net.geoprism.rbac.RoleView;
import net.geoprism.registry.controller.RunwaySpringController;

@Controller
@Validated
public class SessionController extends RunwaySpringController
{
  public static final long serialVersionUID = 1234283350799L;
  
  public static class LoginBody
  {
    @NotEmpty
    String username;

    @NotEmpty
    String password;

    public String getUsername()
    {
      return username;
    }

    public void setUsername(String username)
    {
      this.username = username;
    }

    public String getPassword()
    {
      return password;
    }

    public void setPassword(String password)
    {
      this.password = password;
    }
  }
  
  public static final String API_PATH = "session";
  
  @Autowired
  protected SessionServiceIF service;
  
  @Autowired
  protected RoleServiceIF roleService;

  @PostMapping(API_PATH + "/login")
  public ResponseEntity<String> login(@Valid @RequestBody LoginBody body) throws UnsupportedEncodingException
  {
    String username = body.username;
    String password = body.password;
    
    if (username != null)
    {
      username = username.trim();
    }

    Locale[] locales = this.getLocales();

    ClientRequestIF clientRequest = loginWithLocales(username, password, locales);

    Set<RoleView> roles = this.roleService.getCurrentRoles(clientRequest.getSessionId(), true);
    
    JsonElement cookieValue = this.service.getCookieInformation(clientRequest.getSessionId(), roles);
 
    this.addCookie(cookieValue);

    JsonElement response = this.service.getLoginResponse(clientRequest.getSessionId(), roles);

    return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
  }

  public ClientRequestIF loginWithLocales(String username, String password, Locale[] locales)
  {
    try
    {
      WebClientSession clientSession = WebClientSession.createUserSession(username, password, locales);
      ClientRequestIF clientRequest = clientSession.getRequest();

      this.getRequest().getSession().setMaxInactiveInterval(CommonProperties.getSessionTime());
      this.getRequest().getSession().setAttribute(ClientConstants.CLIENTSESSION, clientSession);
      this.getRequest().setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);

      this.service.onLoginSuccess(username, clientRequest.getSessionId());

      return clientRequest;
    }
    catch (RuntimeException e)
    {
      this.service.onLoginFailure(username);

      throw e;
    }
  }

  @GetMapping(API_PATH + "/logout")
  public RedirectView logout() throws IOException
  {
    ClientSession session = (ClientSession) this.getRequest().getSession().getAttribute(ClientConstants.CLIENTSESSION);
    
    // process which logs the user out.
    if (session != null)
    {
      session.logout();
    }

    this.getRequest().getSession().removeAttribute(ClientConstants.CLIENTSESSION);
    this.getRequest().getSession().invalidate();

    Cookie cookie = new Cookie("user", "");
    cookie.setMaxAge(0);
    cookie.setPath(this.getRequest().getContextPath());

    this.getResponse().addCookie(cookie);
    
    final String cp = this.getRequest().getContextPath();
    return new RedirectView(cp.equals("") ? "/" : cp);
  }

  public void createSession(ServletRequestIF req, String sessionId, Locale[] locales)
  {
    WebClientSession clientSession = WebClientSession.getExistingSession(sessionId, locales);
    ClientRequestIF clientRequest = clientSession.getRequest();

    req.getSession().setMaxInactiveInterval(CommonProperties.getSessionTime());
    req.getSession().setAttribute(ClientConstants.CLIENTSESSION, clientSession);
    req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);
  }

  public Locale[] getLocales()
  {
    Enumeration<Locale> enumeration = this.getRequest().getLocales();
    List<Locale> locales = new LinkedList<Locale>();

    while (enumeration.hasMoreElements())
    {
      locales.add(enumeration.nextElement());
    }

    return locales.toArray(new Locale[locales.size()]);
  }
  
  public void addCookie(JsonElement cookieValue) throws UnsupportedEncodingException
  {
    String path = this.getRequest().getContextPath();

    if (path.equals("") || path.length() == 0)
    {
      path = "/";
    }

    final String value = URLEncoder.encode(cookieValue.toString(), "UTF-8");
    
    Cookie cookie = new Cookie("user", value);
    cookie.setMaxAge(-1);
    cookie.setPath(path);    
    
    this.getResponse().addCookie(cookie);
  }
}
