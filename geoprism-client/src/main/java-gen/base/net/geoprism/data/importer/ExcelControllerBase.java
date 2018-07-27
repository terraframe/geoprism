package net.geoprism.data.importer;

@com.runwaysdk.business.ClassSignature(hash = -31246299)
public class ExcelControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String CLASS = "net.geoprism.data.importer.ExcelController";
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
  public void clearHistory() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.clearHistory");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void failClearHistory() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.failClearHistory");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:historyId", post=false)
  public void downloadErrorSpreadsheet(java.lang.String historyId) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.downloadErrorSpreadsheet");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:historyId", post=false)
  public void failDownloadErrorSpreadsheet(java.lang.String historyId) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.failDownloadErrorSpreadsheet");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void excelImportForm() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.excelImportForm");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void failExcelImportForm() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.failExcelImportForm");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:vaultId, java.lang.String:config", post=false)
  public void excelImportFromVault(java.lang.String vaultId, java.lang.String config) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.excelImportFromVault");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:vaultId, java.lang.String:config", post=false)
  public void failExcelImportFromVault(java.lang.String vaultId, java.lang.String config) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.failExcelImportFromVault");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:type, java.lang.String:country, java.lang.String:downloadToken", post=false)
  public void exportExcelFile(java.lang.String type, java.lang.String country, java.lang.String downloadToken) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.exportExcelFile");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:type, java.lang.String:country, java.lang.String:downloadToken", post=false)
  public void failExportExcelFile(java.lang.String type, java.lang.String country, java.lang.String downloadToken) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.failExportExcelFile");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:type", post=false)
  public void exportExcelForm(java.lang.String type) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.exportExcelForm");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:type", post=false)
  public void failExportExcelForm(java.lang.String type) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.failExportExcelForm");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void getAllHistory() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.getAllHistory");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void failGetAllHistory() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.failGetAllHistory");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.controller.MultipartFileParameter:file, java.lang.String:country, java.lang.String:downloadToken", post=true)
  public void importExcelFile(com.runwaysdk.controller.MultipartFileParameter file, java.lang.String country, java.lang.String downloadToken) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.importExcelFile");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.controller.MultipartFileParameter:file, java.lang.String:country, java.lang.String:downloadToken", post=true)
  public void failImportExcelFile(com.runwaysdk.controller.MultipartFileParameter file, java.lang.String country, java.lang.String downloadToken) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.failImportExcelFile");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:historyId", post=false)
  public void resolveGeoSynonyms(java.lang.String historyId) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.resolveGeoSynonyms");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:historyId", post=false)
  public void failResolveGeoSynonyms(java.lang.String historyId) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.failResolveGeoSynonyms");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:historyId", post=false)
  public void resolveTermSynonyms(java.lang.String historyId) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.resolveTermSynonyms");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:historyId", post=false)
  public void failResolveTermSynonyms(java.lang.String historyId) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.data.importer.ExcelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "net.geoprism.data.importer.ExcelController.failResolveTermSynonyms");
  }
  
}
