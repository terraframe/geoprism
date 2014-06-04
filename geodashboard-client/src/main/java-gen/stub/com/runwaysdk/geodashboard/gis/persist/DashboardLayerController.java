package com.runwaysdk.geodashboard.gis.persist;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;

import org.json.JSONException;

import com.runwaysdk.ClientException;
import com.runwaysdk.system.gis.geo.UniversalQueryDTO;
import com.runwaysdk.transport.conversion.json.BusinessDTOToJSON;

public class DashboardLayerController extends DashboardLayerControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR = "/WEB-INF/com/runwaysdk/geodashboard/gis/persist/DashboardLayer/";
  public static final String LAYOUT = "WEB-INF/templates/layout.jsp";
  
  public DashboardLayerController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }
  
  public void cancel(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    if(!dto.isNewInstance())
    {
      dto.unlock();
    }
  }
  public void failCancel(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    this.edit(dto.getId());
  }
  public void create(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
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
  public void failCreate(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }
  public void delete(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
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
  public void failDelete(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void edit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO dto = com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO.lock(super.getClientRequest(), id);
    req.setAttribute("layer", dto);
    render("editComponent.jsp");
  }
  public void failEdit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.view(id);
  }
  public void newInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO layer = new com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO(clientRequest);
    req.setAttribute("layer", layer);

    DashboardThematicStyleDTO style = new DashboardThematicStyleDTO(clientRequest);
    req.setAttribute("style", style);
    
    String[] fonts = DashboardThematicStyleDTO.getSortedFonts(clientRequest);
    req.setAttribute("fonts", fonts);
    
    // get the universals
    UniversalQueryDTO universals = DashboardLayerDTO.getSortedUniversals(clientRequest);
    req.setAttribute("universals", universals.getResultSet());

    // aggregations
    AggregationTypeQueryDTO aggQuery = DashboardStyleDTO.getSortedAggregations(clientRequest);
    req.setAttribute("aggregations", aggQuery.getResultSet());
    Map<String, String> aggregations = style.getAggregationTypeMd().getEnumItems();
    req.setAttribute("aggregationLabels", aggregations);

    // feature types
    Map<String, String> features = layer.getLayerTypeMd().getEnumItems();
    req.setAttribute("features", features);
    
    render("createComponent.jsp");
  }
  public void failNewInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }
  public void update(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
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
  public void failUpdate(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void view(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    req.setAttribute("layer", com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO.get(clientRequest, id));
    render("viewComponent.jsp");
  }
  public void failView(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }
  public void viewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.gis.persist.DashboardLayerQueryDTO query = com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO.getAllInstances(clientRequest, null, true, 20, 1);
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
    com.runwaysdk.geodashboard.gis.persist.DashboardLayerQueryDTO query = com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO.getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }
  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }
  
  @Override
  public void applyWithStyle(DashboardLayerDTO layer, DashboardStyleDTO style, String mapId) throws IOException,
      ServletException
  {
    layer.applyWithStyle(style, mapId);

    try
    {
      String layerJSON = BusinessDTOToJSON.getConverter(layer).populate().toString();
      resp.setStatus(200);
      resp.getWriter().write(layerJSON);
      resp.flushBuffer();
    }
    catch(JSONException ex)
    {
      throw new ClientException("Error applying map ["+mapId+"] layer ["+layer+"] with style ["+style+"]", ex);
    }
  }
}
