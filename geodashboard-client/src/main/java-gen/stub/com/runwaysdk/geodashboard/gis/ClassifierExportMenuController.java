package com.runwaysdk.geodashboard.gis;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;

import com.runwaysdk.business.ontology.TermDTO;
import com.runwaysdk.controller.ErrorUtility;
import com.runwaysdk.system.ontology.TermUtilDTO;
import com.runwaysdk.system.ontology.io.TermFileFormatDTO;
import com.runwaysdk.system.ontology.io.TermFileFormatMasterDTO;

public class ClassifierExportMenuController extends ClassifierExportMenuControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR = "/WEB-INF/com/runwaysdk/geodashboard/gis/ClassifierExportMenu/";
  public static final String LAYOUT = "WEB-INF/templates/layout.jsp";
  
  public ClassifierExportMenuController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }
  
  @Override
  public void export(ClassifierExportMenuDTO dto, String parentTerm, String downloadToken) throws java.io.IOException, javax.servlet.ServletException
  {
    ServletOutputStream out = resp.getOutputStream();
    
    try {
      // The reason we're including a cookie here is because the browser does not give us any indication of when our response from the server is successful and its downloading the file.
      // This "hack" sends a downloadToken to the client, which the client then checks for the existence of every so often. When the cookie exists, it knows its downloading it.
      // http://stackoverflow.com/questions/1106377/detect-when-browser-receives-file-download
      Cookie cookie = new Cookie("downloadToken", downloadToken);
      cookie.setMaxAge(10*60); // 10 minute cookie expiration
      resp.addCookie(cookie);
      
      String parentTermDisplay = ((TermDTO) getClientRequest().get(parentTerm)).getDisplayLabel().getValue();
      
      resp.addHeader("Content-Disposition", "attachment;filename=\"classifiers-" + parentTermDisplay + ".xml\"");
      resp.setContentType("application/xml");
      TermUtilDTO.exportTerm(getClientRequest(), out, parentTerm, true, dto.getFileFormat().get(0));
    }
    catch (RuntimeException e) {
      resp.reset();
      ErrorUtility.prepareThrowable(e, req, out, resp, true, true);
    }
  }
  
  @Override
  public void failExport(ClassifierExportMenuDTO dto, String parentTerm, String downloadToken) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }
  
  @Override
  public void viewExport() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    ClassifierExportMenuDTO dto = new ClassifierExportMenuDTO(clientRequest);
    
//    List<TermFileFormatMasterDTO> formats = TermFileFormatDTO.allItems(super.getClientSession().getRequest());
    List<TermFileFormatMasterDTO> formats = new ArrayList<TermFileFormatMasterDTO>();
    formats.add(TermFileFormatDTO.XML.item(getClientRequest()));
    
    req.setAttribute("_fileFormat", formats);
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }
  
  @Override
  public void failViewExport() throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }
}
