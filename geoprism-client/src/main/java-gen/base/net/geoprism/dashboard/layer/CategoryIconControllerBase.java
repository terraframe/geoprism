package net.geoprism.dashboard.layer;

@com.runwaysdk.business.ClassSignature(hash = -1808034317)
public class CategoryIconControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String CLASS = "net.geoprism.dashboard.layer.CategoryIconController";
  protected javax.servlet.http.HttpServletRequest req;
  protected javax.servlet.http.HttpServletResponse resp;
  protected java.lang.Boolean isAsynchronous;
  protected java.lang.String dir;
  protected java.lang.String layout;
  
  public CategoryIconControllerBase(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    this(req, resp, isAsynchronous, "","");
  }
  
  public CategoryIconControllerBase(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous, java.lang.String dir, java.lang.String layout)
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
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id, com.runwaysdk.controller.MultipartFileParameter:file, java.lang.String:label", post=true)
  public void apply(java.lang.String id, com.runwaysdk.controller.MultipartFileParameter file, java.lang.String label) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.CategoryIconController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.CategoryIconController.apply");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id, com.runwaysdk.controller.MultipartFileParameter:file, java.lang.String:label", post=true)
  public void failApply(java.lang.String id, com.runwaysdk.controller.MultipartFileParameter file, java.lang.String label) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.CategoryIconController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.CategoryIconController.failApply");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.controller.MultipartFileParameter:file, java.lang.String:label", post=true)
  public void create(com.runwaysdk.controller.MultipartFileParameter file, java.lang.String label) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.CategoryIconController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.CategoryIconController.create");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.controller.MultipartFileParameter:file, java.lang.String:label", post=true)
  public void failCreate(com.runwaysdk.controller.MultipartFileParameter file, java.lang.String label) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.CategoryIconController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.CategoryIconController.failCreate");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:iconId", post=true)
  public void edit(java.lang.String iconId) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.CategoryIconController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.CategoryIconController.edit");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:iconId", post=true)
  public void failEdit(java.lang.String iconId) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.CategoryIconController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.CategoryIconController.failEdit");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=true)
  public void getAll() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.CategoryIconController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.CategoryIconController.getAll");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=true)
  public void failGetAll() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.CategoryIconController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.CategoryIconController.failGetAll");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:iconId", post=false)
  public void getCategoryIconImage(java.lang.String iconId) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.CategoryIconController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.CategoryIconController.getCategoryIconImage");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:iconId", post=false)
  public void failGetCategoryIconImage(java.lang.String iconId) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.CategoryIconController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.CategoryIconController.failGetCategoryIconImage");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=true)
  public void remove(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.CategoryIconController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.CategoryIconController.remove");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=true)
  public void failRemove(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.CategoryIconController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.dashboard.layer.CategoryIconController.failRemove");
  }
  
}
