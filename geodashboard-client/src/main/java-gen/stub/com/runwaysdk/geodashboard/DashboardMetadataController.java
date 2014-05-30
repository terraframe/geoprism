package com.runwaysdk.geodashboard;

public class DashboardMetadataController extends DashboardMetadataControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR = "/WEB-INF/com/runwaysdk/geodashboard/DashboardMetadata/";
  public static final String LAYOUT = "WEB-INF/templates/layout.jsp";
  
  public DashboardMetadataController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }
  
  public void cancel(com.runwaysdk.geodashboard.DashboardMetadataDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    dto.unlock();
    this.view(dto.getId());
  }
  public void failCancel(com.runwaysdk.geodashboard.DashboardMetadataDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    this.edit(dto.getId());
  }
  public void childQuery(java.lang.String childId) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.DashboardMetadataQueryDTO query = com.runwaysdk.geodashboard.DashboardMetadataDTO.childQuery(clientRequest, childId);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }
  public void failChildQuery(java.lang.String childId) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }
  public void create(com.runwaysdk.geodashboard.DashboardMetadataDTO dto) throws java.io.IOException, javax.servlet.ServletException
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
  public void failCreate(com.runwaysdk.geodashboard.DashboardMetadataDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }
  public void delete(com.runwaysdk.geodashboard.DashboardMetadataDTO dto) throws java.io.IOException, javax.servlet.ServletException
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
  public void failDelete(com.runwaysdk.geodashboard.DashboardMetadataDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void edit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.geodashboard.DashboardMetadataDTO dto = com.runwaysdk.geodashboard.DashboardMetadataDTO.lock(super.getClientRequest(), id);
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void failEdit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.view(id);
  }
  public void newInstance(java.lang.String parentId, java.lang.String childId) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.DashboardMetadataDTO dto = new com.runwaysdk.geodashboard.DashboardMetadataDTO(clientRequest, parentId, childId);
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }
  public void failNewInstance(java.lang.String parentId, java.lang.String childId) throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }
  public void newRelationship() throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("parentList", com.runwaysdk.geodashboard.DashboardDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("childList", com.runwaysdk.geodashboard.MetadataWrapperDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    render("newRelationshipComponent.jsp");
  }
  public void failNewRelationship() throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }
  public void parentQuery(java.lang.String parentId) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.DashboardMetadataQueryDTO query = com.runwaysdk.geodashboard.DashboardMetadataDTO.parentQuery(clientRequest, parentId);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }
  public void failParentQuery(java.lang.String parentId) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }
  public void update(com.runwaysdk.geodashboard.DashboardMetadataDTO dto) throws java.io.IOException, javax.servlet.ServletException
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
  public void failUpdate(com.runwaysdk.geodashboard.DashboardMetadataDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void view(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    req.setAttribute("item", com.runwaysdk.geodashboard.DashboardMetadataDTO.get(clientRequest, id));
    render("viewComponent.jsp");
  }
  public void failView(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }
  public void viewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.DashboardMetadataQueryDTO query = com.runwaysdk.geodashboard.DashboardMetadataDTO.getAllInstances(clientRequest, null, true, 20, 1);
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
    com.runwaysdk.geodashboard.DashboardMetadataQueryDTO query = com.runwaysdk.geodashboard.DashboardMetadataDTO.getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }
  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }
}
