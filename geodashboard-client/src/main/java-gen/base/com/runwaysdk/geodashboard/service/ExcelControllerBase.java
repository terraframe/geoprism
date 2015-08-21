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
package com.runwaysdk.geodashboard.service;

@com.runwaysdk.business.ClassSignature(hash = -1075517900)
public class ExcelControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String CLASS = "com.runwaysdk.geodashboard.service.ExcelController";
  protected javax.servlet.http.HttpServletRequest req;
  protected javax.servlet.http.HttpServletResponse resp;
  protected java.lang.Boolean isAsynchronous;
  protected java.lang.String dir;
  protected java.lang.String layout;
  
  public ExcelControllerBase(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    this(req, resp, isAsynchronous, "","");
  }
  
  public ExcelControllerBase(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous, java.lang.String dir, java.lang.String layout)
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
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void excelImportForm() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.geodashboard.service.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.geodashboard.service.ExcelController.excelImportForm");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void failExcelImportForm() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.geodashboard.service.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.geodashboard.service.ExcelController.failExcelImportForm");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:type, java.lang.String:country, java.lang.String:downloadToken", post=false)
  public void exportExcelFile(java.lang.String type, java.lang.String country, java.lang.String downloadToken) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.geodashboard.service.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.geodashboard.service.ExcelController.exportExcelFile");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:type, java.lang.String:country, java.lang.String:downloadToken", post=false)
  public void failExportExcelFile(java.lang.String type, java.lang.String country, java.lang.String downloadToken) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.geodashboard.service.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.geodashboard.service.ExcelController.failExportExcelFile");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:type", post=false)
  public void exportExcelForm(java.lang.String type) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.geodashboard.service.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.geodashboard.service.ExcelController.exportExcelForm");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:type", post=false)
  public void failExportExcelForm(java.lang.String type) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.geodashboard.service.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.geodashboard.service.ExcelController.failExportExcelForm");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.controller.MultipartFileParameter:file, java.lang.String:country, java.lang.String:downloadToken", post=true)
  public void importExcelFile(com.runwaysdk.controller.MultipartFileParameter file, java.lang.String country, java.lang.String downloadToken) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.geodashboard.service.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.geodashboard.service.ExcelController.importExcelFile");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.controller.MultipartFileParameter:file, java.lang.String:country, java.lang.String:downloadToken", post=true)
  public void failImportExcelFile(com.runwaysdk.controller.MultipartFileParameter file, java.lang.String country, java.lang.String downloadToken) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.geodashboard.service.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.geodashboard.service.ExcelController.failImportExcelFile");
  }
  
}
