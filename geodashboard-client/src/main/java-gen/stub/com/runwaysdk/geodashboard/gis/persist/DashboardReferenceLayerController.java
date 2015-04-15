package com.runwaysdk.geodashboard.gis.persist;

public class DashboardReferenceLayerController extends DashboardReferenceLayerControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR = "/WEB-INF/com/runwaysdk/geodashboard/gis/persist/DashboardReferenceLayer/";
  public static final String LAYOUT = "WEB-INF/templates/layout.jsp";
  
  public DashboardReferenceLayerController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }
  
  public void cancel(com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    dto.unlock();
    this.view(dto.getId());
  }
  public void failCancel(com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    this.edit(dto.getId());
  }
  public void create(com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      dto.apply();
      this.view(dto.getId());
    }
    catch(com.runwaysdk.ProblemExceptionDTO e)
    {
      this.failCreate(dto);
    }
  }
  public void failCreate(com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("_dashboardMap", com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("_geoEntity", com.runwaysdk.system.metadata.MdAttributeReferenceDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("_layerType", com.runwaysdk.geodashboard.gis.persist.AllLayerTypeDTO.allItems(super.getClientSession().getRequest()));
    req.setAttribute("_universal", com.runwaysdk.system.gis.geo.UniversalDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }
  public void delete(com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      dto.delete();
      this.viewAll();
    }
    catch(com.runwaysdk.ProblemExceptionDTO e)
    {
      this.failDelete(dto);
    }
  }
  public void failDelete(com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("_dashboardMap", com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("_geoEntity", com.runwaysdk.system.metadata.MdAttributeReferenceDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("_layerType", com.runwaysdk.geodashboard.gis.persist.AllLayerTypeDTO.allItems(super.getClientSession().getRequest()));
    req.setAttribute("_universal", com.runwaysdk.system.gis.geo.UniversalDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void edit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO dto = com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO.lock(super.getClientRequest(), id);
    req.setAttribute("_dashboardMap", com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("_geoEntity", com.runwaysdk.system.metadata.MdAttributeReferenceDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("_layerType", com.runwaysdk.geodashboard.gis.persist.AllLayerTypeDTO.allItems(super.getClientSession().getRequest()));
    req.setAttribute("_universal", com.runwaysdk.system.gis.geo.UniversalDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void failEdit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.view(id);
  }
  public void newInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO dto = new com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO(clientRequest);
    req.setAttribute("_dashboardMap", com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("_geoEntity", com.runwaysdk.system.metadata.MdAttributeReferenceDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("_layerType", com.runwaysdk.geodashboard.gis.persist.AllLayerTypeDTO.allItems(super.getClientSession().getRequest()));
    req.setAttribute("_universal", com.runwaysdk.system.gis.geo.UniversalDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }
  public void failNewInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }
  public void update(com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      dto.apply();
      this.view(dto.getId());
    }
    catch(com.runwaysdk.ProblemExceptionDTO e)
    {
      this.failUpdate(dto);
    }
  }
  public void failUpdate(com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("_dashboardMap", com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("_geoEntity", com.runwaysdk.system.metadata.MdAttributeReferenceDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("_layerType", com.runwaysdk.geodashboard.gis.persist.AllLayerTypeDTO.allItems(super.getClientSession().getRequest()));
    req.setAttribute("_universal", com.runwaysdk.system.gis.geo.UniversalDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void view(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    req.setAttribute("_dashboardMap", com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("_geoEntity", com.runwaysdk.system.metadata.MdAttributeReferenceDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("_layerType", com.runwaysdk.geodashboard.gis.persist.AllLayerTypeDTO.allItems(super.getClientSession().getRequest()));
    req.setAttribute("_universal", com.runwaysdk.system.gis.geo.UniversalDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("item", com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO.get(clientRequest, id));
    render("viewComponent.jsp");
  }
  public void failView(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }
  public void viewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerQueryDTO query = com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO.getAllInstances(clientRequest, null, true, 20, 1);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }
  public void failViewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }
  public void viewPage(java.lang.String sortAttribute, java.lang.Boolean isAscending, java.lang.Integer pageSize, java.lang.Integer pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerQueryDTO query = com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO.getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }
  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }
}
