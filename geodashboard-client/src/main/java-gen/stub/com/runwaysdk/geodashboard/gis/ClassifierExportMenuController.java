package com.runwaysdk.geodashboard.gis;

import java.util.ArrayList;
import java.util.List;

import com.runwaysdk.ontology.TermControllerUtil;
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
    TermControllerUtil.writeExport(req, resp, downloadToken, this.getClientRequest(), parentTerm, dto.getFileFormat().get(0));
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
