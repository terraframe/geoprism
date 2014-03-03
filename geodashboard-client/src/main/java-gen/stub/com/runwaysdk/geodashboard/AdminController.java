package com.runwaysdk.geodashboard;

import java.io.IOException;

import javax.servlet.ServletException;

public class AdminController extends AdminControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR   = "/WEB-INF/com/runwaysdk/geodashboard/admin/";

  public static final String LAYOUT    = "/WEB-INF/templates/layout.jsp";

  public static final String INDEX_JSP = "/com/runwaysdk/geodashboard/jsp/index.jsp";

  public AdminController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }

  @Override
  public void users() throws IOException, ServletException
  {
    render("useraccounts.jsp");
  }

  @Override
  public void failUsers() throws IOException, ServletException
  {
    this.req.getRequestDispatcher(INDEX_JSP).forward(req, resp);
  }

  @Override
  public void geoentity() throws IOException, ServletException
  {
    render("geoentity.jsp");
  }

  @Override
  public void failGeoentity() throws IOException, ServletException
  {
    this.req.getRequestDispatcher(INDEX_JSP).forward(req, resp);
  }

  @Override
  public void roles() throws IOException, ServletException
  {
    render("roles.jsp");
  }

  @Override
  public void failRoles() throws IOException, ServletException
  {
    this.req.getRequestDispatcher(INDEX_JSP).forward(req, resp);
  }

  @Override
  public void universal() throws IOException, ServletException
  {
    render("universal.jsp");
  }

  @Override
  public void failUniversal() throws IOException, ServletException
  {
    this.req.getRequestDispatcher(INDEX_JSP).forward(req, resp);
  }

  @Override
  public void scheduler() throws IOException, ServletException
  {
    render("scheduler.jsp");
  }

  @Override
  public void failScheduler() throws IOException, ServletException
  {
    this.req.getRequestDispatcher(INDEX_JSP).forward(req, resp);
  }

}
