package net.geoprism.ontology;

@com.runwaysdk.business.ClassSignature(hash = 1669551561)
public class ClassifierControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String CLASS = "net.geoprism.ontology.ClassifierController";
  protected javax.servlet.http.HttpServletRequest req;
  protected javax.servlet.http.HttpServletResponse resp;
  protected java.lang.Boolean isAsynchronous;
  protected java.lang.String dir;
  protected java.lang.String layout;
  
  public ClassifierControllerBase(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    this(req, resp, isAsynchronous, "","");
  }
  
  public ClassifierControllerBase(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous, java.lang.String dir, java.lang.String layout)
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
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:config", post=true)
  public void applyOption(java.lang.String config) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.applyOption");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:config", post=true)
  public void failApplyOption(java.lang.String config) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.failApplyOption");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="net.geoprism.ontology.ClassifierDTO:dto", post=true)
  public void cancel(net.geoprism.ontology.ClassifierDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.cancel");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="net.geoprism.ontology.ClassifierDTO:dto", post=true)
  public void failCancel(net.geoprism.ontology.ClassifierDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.failCancel");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="net.geoprism.ontology.ClassifierDTO:dto, java.lang.String:parentId", post=true)
  public void create(net.geoprism.ontology.ClassifierDTO dto, java.lang.String parentId) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.create");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="net.geoprism.ontology.ClassifierDTO:dto, java.lang.String:parentId", post=true)
  public void failCreate(net.geoprism.ontology.ClassifierDTO dto, java.lang.String parentId) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.failCreate");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:option", post=true)
  public void createOption(java.lang.String option) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.createOption");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:option", post=true)
  public void failCreateOption(java.lang.String option) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.failCreateOption");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="net.geoprism.ontology.ClassifierDTO:dto", post=true)
  public void delete(net.geoprism.ontology.ClassifierDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.delete");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="net.geoprism.ontology.ClassifierDTO:dto", post=true)
  public void failDelete(net.geoprism.ontology.ClassifierDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.failDelete");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=false)
  public void edit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.edit");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=false)
  public void failEdit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.failEdit");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=true)
  public void editCategory(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.editCategory");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=true)
  public void failEditCategory(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.failEditCategory");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void getAllCategories() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.getAllCategories");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void failGetAllCategories() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.failGetAllCategories");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=true)
  public void getCategory(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.getCategory");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=true)
  public void failGetCategory(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.failGetCategory");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:parentId, java.lang.Integer:pageNum, java.lang.Integer:pageSize", post=true)
  public void getDirectDescendants(java.lang.String parentId, java.lang.Integer pageNum, java.lang.Integer pageSize) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.getDirectDescendants");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:parentId, java.lang.String:pageNum, java.lang.String:pageSize", post=true)
  public void failGetDirectDescendants(java.lang.String parentId, java.lang.String pageNum, java.lang.String pageSize) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.failGetDirectDescendants");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void newInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.newInstance");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void failNewInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.failNewInstance");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=true)
  public void unlockCategory(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.unlockCategory");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=true)
  public void failUnlockCategory(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.failUnlockCategory");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="net.geoprism.ontology.ClassifierDTO:dto", post=true)
  public void update(net.geoprism.ontology.ClassifierDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.update");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="net.geoprism.ontology.ClassifierDTO:dto", post=true)
  public void failUpdate(net.geoprism.ontology.ClassifierDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.failUpdate");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=false)
  public void view(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.view");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=false)
  public void failView(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.failView");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void viewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.viewAll");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void failViewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.failViewAll");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=true)
  public void viewCreate() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.viewCreate");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=true)
  public void failViewCreate() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.failViewCreate");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:sortAttribute, java.lang.Boolean:isAscending, java.lang.Integer:pageSize, java.lang.Integer:pageNumber", post=false)
  public void viewPage(java.lang.String sortAttribute, java.lang.Boolean isAscending, java.lang.Integer pageSize, java.lang.Integer pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.viewPage");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:sortAttribute, java.lang.String:isAscending, java.lang.String:pageSize, java.lang.String:pageNumber", post=false)
  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.failViewPage");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=true)
  public void viewUpdate(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.viewUpdate");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=true)
  public void failViewUpdate(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.ontology.ClassifierController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.ontology.ClassifierController.failViewUpdate");
  }
  
}
