package com.runwaysdk.geodashboard;

import java.io.IOException;

import javax.servlet.ServletException;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.geodashboard.ontology.ClassifierDTO;
import com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO;
import com.runwaysdk.system.gis.geo.AllowedInDTO;
import com.runwaysdk.system.gis.geo.GeoEntityDTO;
import com.runwaysdk.system.gis.geo.IsARelationshipDTO;
import com.runwaysdk.system.gis.geo.LocatedInDTO;
import com.runwaysdk.system.gis.geo.UniversalDTO;
import com.runwaysdk.web.json.JSONController;

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
    JavascriptUtil.loadUserBundle(this.getClientRequest(), this.req);

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
    ClientRequestIF request = this.getClientRequest();

    GeoEntityDTO root = GeoEntityDTO.getRoot(request);

    this.req.setAttribute("type", GeoEntityDTO.CLASS);
    this.req.setAttribute("relationshipType", LocatedInDTO.CLASS);
    this.req.setAttribute("rootId", root.getId());

    JavascriptUtil.loadGeoEntityBundle(request, this.req);

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
    UniversalDTO root = UniversalDTO.getRoot(this.getClientRequest());

    this.req.setAttribute("type", UniversalDTO.CLASS);
    this.req.setAttribute("allowedInType", AllowedInDTO.CLASS);
    this.req.setAttribute("isARelationshipType", IsARelationshipDTO.CLASS);
    this.req.setAttribute("rootId", root.getId());

    JavascriptUtil.loadUniversalBundle(this.getClientRequest(), this.req);

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
    JavascriptUtil.loadSchedulerBundle(this.getClientRequest(), req);

    render("scheduler.jsp");
  }

  @Override
  public void failScheduler() throws IOException, ServletException
  {
    this.req.getRequestDispatcher(INDEX_JSP).forward(req, resp);
  }

  @Override
  public void databrowser() throws IOException, ServletException
  {
    String sessionId = this.getClientSession().getSessionId();
    String metadata = "{className:'com.runwaysdk.geodashboard.databrowser.DataBrowserUtil', methodName:'getDefaultTypes', declaredTypes: []}";
    String response = JSONController.invokeMethod(sessionId, metadata, null, "[]");

    this.req.setAttribute("response", response);

    JavascriptUtil.loadDatabrowserBundle(this.getClientRequest(), req);

    render("databrowser.jsp");
  }

  @Override
  public void failDatabrowser() throws IOException, ServletException
  {
    this.req.getRequestDispatcher(INDEX_JSP).forward(req, resp);
  }

  @Override
  public void account() throws IOException, ServletException
  {
    JavascriptUtil.loadUserBundle(this.getClientRequest(), this.req);

    render("account.jsp");
  }

  @Override
  public void failAccount() throws IOException, ServletException
  {
    this.req.getRequestDispatcher(INDEX_JSP).forward(req, resp);
  }

  @Override
  public void ontologies() throws java.io.IOException, javax.servlet.ServletException
  {
    ClassifierDTO root = ClassifierDTO.getRoot(getClientRequest());

    this.req.setAttribute("type", ClassifierDTO.CLASS);
    this.req.setAttribute("relationshipType", ClassifierIsARelationshipDTO.CLASS);
    this.req.setAttribute("rootId", root.getId());

    JavascriptUtil.loadOntologyBundle(this.getClientRequest(), this.req);

    render("ontologies.jsp");
  }

  @Override
  public void failOntologies() throws java.io.IOException, javax.servlet.ServletException
  {
    this.req.getRequestDispatcher(INDEX_JSP).forward(req, resp);
  }

}
