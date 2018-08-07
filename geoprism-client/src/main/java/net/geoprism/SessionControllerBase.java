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

@com.runwaysdk.business.ClassSignature(hash = -1602277397)
public class SessionControllerBase 
{
  public static final String                       CLASS            = "com.runwaysdk.defaults.SessionController";

  protected javax.servlet.http.HttpServletRequest  req;

  protected javax.servlet.http.HttpServletResponse resp;

  protected java.lang.Boolean                      isAsynchronous;

  protected java.lang.String                       dir;

  protected java.lang.String                       layout;

  public static final long                         serialVersionUID = -1602277397;

  public SessionControllerBase(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    this(req, resp, isAsynchronous, "", "");
  }

  public SessionControllerBase(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous, java.lang.String dir, java.lang.String layout)
  {
    this.req = req;
    this.resp = resp;
    this.isAsynchronous = isAsynchronous;
    this.dir = dir;
    this.layout = layout;
  }

  protected void render(String jsp) throws java.io.IOException, javax.servlet.ServletException
  {
    if (!resp.isCommitted())
    {
      if (this.isAsynchronous())
      {
        req.getRequestDispatcher(dir + jsp).forward(req, resp);
      }
      else
      {
        req.setAttribute(com.runwaysdk.controller.JSPFetcher.INNER_JSP, dir + jsp);
        req.getRequestDispatcher(layout).forward(req, resp);
      }
    }
  }

  public javax.servlet.http.HttpServletRequest getRequest()
  {
    return this.req;
  }

  public javax.servlet.http.HttpServletResponse getResponse()
  {
    return this.resp;
  }

  public java.lang.Boolean isAsynchronous()
  {
    return this.isAsynchronous;
  }

  public com.runwaysdk.constants.ClientRequestIF getClientRequest()
  {
    return (com.runwaysdk.constants.ClientRequestIF) req.getAttribute(com.runwaysdk.constants.ClientConstants.CLIENTREQUEST);
  }

  public com.runwaysdk.ClientSession getClientSession()
  {
    return (com.runwaysdk.ClientSession) req.getSession().getAttribute(com.runwaysdk.constants.ClientConstants.CLIENTSESSION);
  }

  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void form() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.defaults.SessionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.defaults.SessionController.form");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void failForm(java.lang.String username, java.lang.String password) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.defaults.SessionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.defaults.SessionController.failForm");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters = "java.lang.String:username, java.lang.String:password", post = true)
  public void login(java.lang.String username, java.lang.String password) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.defaults.SessionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.defaults.SessionController.login");
  }

  @com.runwaysdk.controller.ActionParameters(parameters = "java.lang.String:code, java.lang.String:state", post = false)
  public void ologin(java.lang.String code, java.lang.String state) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.defaults.SessionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.defaults.SessionController.login");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters = "java.lang.String:username, java.lang.String:password", post = true)
  public void failLogin(java.lang.String username, java.lang.String password) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.defaults.SessionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.defaults.SessionController.failLogin");
  }

  @com.runwaysdk.controller.ActionParameters(parameters = "", post = false)
  public void logout() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.defaults.SessionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.defaults.SessionController.logout");
  }

  @com.runwaysdk.controller.ActionParameters(parameters = "", post = false)
  public void failLogout() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.defaults.SessionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.defaults.SessionController.failLogout");
  }

}
