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

@com.runwaysdk.business.ClassSignature(hash = -1582559010)
public class DashboardLayerControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String CLASS = "net.geoprism.dashboard.layer.DashboardLayerController";
  protected javax.servlet.http.HttpServletRequest req;
  protected javax.servlet.http.HttpServletResponse resp;
  protected java.lang.Boolean isAsynchronous;
  protected java.lang.String dir;
  protected java.lang.String layout;
  
  public DashboardLayerControllerBase(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    this(req, resp, isAsynchronous, "","");
  }
  
  public DashboardLayerControllerBase(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous, java.lang.String dir, java.lang.String layout)
  {
    this.req = req;
    this.resp = resp;
    this.isAsynchronous = isAsynchronous;
    this.dir = dir;
    this.layout = layout;
  }
  
  protected void render(String jsp) throws java.io.IOException, javax.servlet.ServletException
  {
    if(!resp.isCommitted())
    {
      if(this.isAsynchronous())
      {
        req.getRequestDispatcher(dir+jsp).forward(req, resp);
      }
      else
      {
        req.setAttribute(com.runwaysdk.controller.JSPFetcher.INNER_JSP, dir+jsp);
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
  
  @com.runwaysdk.controller.ActionParameters(parameters="net.geoprism.dashboard.layer.DashboardLayerDTO:dto", post=true)
  public void cancel(net.geoprism.dashboard.layer.DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardLayerController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.DashboardLayerController.cancel");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="net.geoprism.dashboard.layer.DashboardLayerDTO:dto", post=true)
  public void failCancel(net.geoprism.dashboard.layer.DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardLayerController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.DashboardLayerController.failCancel");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="net.geoprism.dashboard.layer.DashboardLayerDTO:dto", post=true)
  public void create(net.geoprism.dashboard.layer.DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardLayerController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.DashboardLayerController.create");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="net.geoprism.dashboard.layer.DashboardLayerDTO:dto", post=true)
  public void failCreate(net.geoprism.dashboard.layer.DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardLayerController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.DashboardLayerController.failCreate");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="net.geoprism.dashboard.layer.DashboardLayerDTO:dto", post=true)
  public void delete(net.geoprism.dashboard.layer.DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardLayerController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.DashboardLayerController.delete");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="net.geoprism.dashboard.layer.DashboardLayerDTO:dto", post=true)
  public void failDelete(net.geoprism.dashboard.layer.DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardLayerController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.DashboardLayerController.failDelete");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=false)
  public void edit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardLayerController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.DashboardLayerController.edit");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=false)
  public void failEdit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardLayerController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.DashboardLayerController.failEdit");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="net.geoprism.dashboard.layer.DashboardLayerDTO:dto", post=true)
  public void update(net.geoprism.dashboard.layer.DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardLayerController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.DashboardLayerController.update");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="net.geoprism.dashboard.layer.DashboardLayerDTO:dto", post=true)
  public void failUpdate(net.geoprism.dashboard.layer.DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardLayerController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.DashboardLayerController.failUpdate");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=false)
  public void view(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardLayerController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.DashboardLayerController.view");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=false)
  public void failView(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardLayerController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.DashboardLayerController.failView");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void viewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardLayerController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.DashboardLayerController.viewAll");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void failViewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardLayerController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.DashboardLayerController.failViewAll");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:sortAttribute, java.lang.Boolean:isAscending, java.lang.Integer:pageSize, java.lang.Integer:pageNumber", post=false)
  public void viewPage(java.lang.String sortAttribute, java.lang.Boolean isAscending, java.lang.Integer pageSize, java.lang.Integer pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardLayerController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.DashboardLayerController.viewPage");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:sortAttribute, java.lang.String:isAscending, java.lang.String:pageSize, java.lang.String:pageNumber", post=false)
  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardLayerController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.DashboardLayerController.failViewPage");
  }
  
}
