package com.runwaysdk.geodashboard.gis.persist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.ProblemExceptionDTO;
import com.runwaysdk.business.ontology.TermDTO;
import com.runwaysdk.geodashboard.GDBErrorUtility;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionDTO;
import com.runwaysdk.system.gis.geo.AllowedInDTO;
import com.runwaysdk.system.gis.geo.UniversalDTO;
import com.runwaysdk.system.metadata.MdAttributeConcreteDTO;
import com.runwaysdk.system.metadata.MdAttributeMomentDTO;
import com.runwaysdk.system.ontology.TermUtilDTO;
import com.runwaysdk.transport.conversion.json.JSONReturnObject;

public class DashboardLayerController extends DashboardLayerControllerBase implements
    com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR = "/WEB-INF/com/runwaysdk/geodashboard/gis/persist/DashboardLayer/";

  public static final String LAYOUT  = "WEB-INF/templates/layout.jsp";

  private static final Log log = LogFactory.getLog(DashboardLayerController.class);
  
  private static String rootUniId;
  
  public DashboardLayerController(javax.servlet.http.HttpServletRequest req,
      javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
    
    rootUniId = UniversalDTO.getRoot(this.getClientRequest()).getId();
  }

  public void cancel(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO dto)
      throws java.io.IOException, javax.servlet.ServletException
  {
    if (!dto.isNewInstance())
    {
      dto.unlock();
    }
  }

  public void failCancel(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO dto)
      throws java.io.IOException, javax.servlet.ServletException
  {
    this.edit(dto.getId());
  }

  public void create(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO dto)
      throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      dto.apply();
      this.view(dto.getId());
    }
    catch (com.runwaysdk.ProblemExceptionDTO e)
    {
      this.failCreate(dto);
    }
  }

  public void failCreate(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO dto)
      throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }

  public void delete(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO dto)
      throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      dto.delete();
      this.viewAll();
    }
    catch (com.runwaysdk.ProblemExceptionDTO e)
    {
      this.failDelete(dto);
    }
  }

  public void failDelete(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO dto)
      throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void edit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO layer = com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO
        .lock(super.getClientRequest(), id);

    // There will be one style only for this layer (for IDE)
    DashboardThematicStyleDTO style = (DashboardThematicStyleDTO) layer.getAllHasStyle().get(0);

    this.loadLayerData(layer, style, null);

    render("editComponent.jsp");
  }

  public void failEdit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.view(id);
  }

  /**
   * Loads artifacts for layer/style CRUD.
   * 
   * @param layer
   */
  private void loadLayerData(DashboardLayerDTO layer, DashboardThematicStyleDTO style, String mdAttribute)
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    
    req.setAttribute("layer", layer);
    
    req.setAttribute("style", style);
    
    String[] fonts = DashboardThematicStyleDTO.getSortedFonts(clientRequest);
    req.setAttribute("fonts", fonts);
    
    
    // get the universals
//    UniversalQueryDTO universals = DashboardLayerDTO.getSortedUniversals(clientRequest);
//    req.setAttribute("universals", universals.getResultSet());
    
    // Get the universals, sorted by their ordering in the universal tree.
    List<TermDTO> universals = Arrays.asList(TermUtilDTO.getAllDescendants(this.getClientRequest(), rootUniId, new String[]{AllowedInDTO.CLASS}));
    req.setAttribute("universals", universals);
    
    
    // selected attribute
    MdAttributeConcreteDTO mdAttr;
    if (mdAttribute != null) 
    { // new instance
      mdAttr = MdAttributeConcreteDTO.get(clientRequest, mdAttribute);
    }
    else 
    { // edit
      mdAttr = ((MdAttributeConcreteDTO) style.getMdAttribute());
    }
    req.setAttribute("activeMdAttributeLabel", mdAttr.getDisplayLabel().getValue());
    
    
    
    // aggregations
    List<? extends AggregationTypeDTO> aggregations = DashboardStyleDTO.getSortedAggregations(clientRequest).getResultSet();
    
    // filter out invalid aggregations depending on attribute type
    List<AllAggregationTypeDTO> filters = new ArrayList<AllAggregationTypeDTO>();
    if (mdAttr instanceof MdAttributeMomentDTO) 
    {
      filters.add(AllAggregationTypeDTO.AVG);
      filters.add(AllAggregationTypeDTO.SUM);
    }
    
    for (AllAggregationTypeDTO filter : filters) 
    {
      for (int i = 0; i < aggregations.size(); ++i) 
      {
        if (aggregations.get(i).getEnumName().equals(filter.getName())) 
        {
          aggregations.remove(i);
        }
      }
    }
    
    req.setAttribute("aggregations", aggregations);
    
    // Pick an active aggregation.
    if (aggregations.size() > 0) 
    {
      req.setAttribute("activeAggregation", aggregations.get(0).getDisplayLabel().getValue());
    }
    else 
    {
      req.setAttribute("activeAggregation", "");
    }
    
    
    
    // layer types
    Map<String, String> labels = layer.getLayerTypeMd().getEnumItems();
    
    Map<String, String> layerTypes = new LinkedHashMap<String, String>();
    layerTypes.put(AllLayerTypeDTO.BASIC.getName(), labels.get(AllLayerTypeDTO.BASIC.getName()));
    layerTypes.put(AllLayerTypeDTO.BUBBLE.getName(), labels.get(AllLayerTypeDTO.BUBBLE.getName()));
    
    // filter out invalid layer types depending on attribute type
    // this is primarily to prevent creating gradients on date fields
    if ( !(mdAttr instanceof MdAttributeMomentDTO) ) 
    {
      layerTypes.put(AllLayerTypeDTO.GRADIENT.getName(), labels.get(AllLayerTypeDTO.GRADIENT.getName()));
    }
    
    layerTypes.put(AllLayerTypeDTO.CATEGORY.getName(), labels.get(AllLayerTypeDTO.CATEGORY.getName()));
    
    req.setAttribute("layerTypeNames", layerTypes.keySet().toArray());
    req.setAttribute("layerTypeLabels", layerTypes.values().toArray());
    
    List<String> activeLayerType = layer.getLayerTypeEnumNames();
    if (activeLayerType.size() > 0) 
    { // Set the selected layer type to what its currently set to in the database (this will exist for edits, but not new instances)
      req.setAttribute("activeLayerTypeName", activeLayerType.get(0));
    }
    else 
    {
      req.setAttribute("activeLayerTypeName", AllLayerTypeDTO.BASIC.getName());
    }
  }

  /**
   * @deprecated
   * 
   * Call newThematicInstance instead.
   */
  @Deprecated
  public void newInstance() throws java.io.IOException, javax.servlet.ServletException
  {
//    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
//    com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO layer = new com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO(
//        clientRequest);
//    DashboardThematicStyleDTO style = new DashboardThematicStyleDTO(clientRequest);
//    
//    this.loadLayerData(layer, style);
//    
//    render("createComponent.jsp");
    
    throw new UnsupportedOperationException();
  }

  public void failNewInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }

  public void update(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO dto)
      throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      dto.apply();
    }
    catch (com.runwaysdk.ProblemExceptionDTO e)
    {
      this.failUpdate(dto);
    }
  }

  public void failUpdate(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO dto)
      throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void view(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    req.setAttribute("layer",
        com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO.get(clientRequest, id));
    render("viewComponent.jsp");
  }

  public void failView(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }

  public void viewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.gis.persist.DashboardLayerQueryDTO query = com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO
        .getAllInstances(clientRequest, null, true, 20, 1);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }

  public void failViewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }

  public void viewPage(java.lang.String sortAttribute, java.lang.Boolean isAscending,
      java.lang.Integer pageSize, java.lang.Integer pageNumber) throws java.io.IOException,
      javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.gis.persist.DashboardLayerQueryDTO query = com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO
        .getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }

  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending,
      java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException,
      javax.servlet.ServletException
  {
    resp.sendError(500);
  }
  
  public void newThematicInstance(java.lang.String mdAttribute) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO layer = new com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO(
        clientRequest);
    DashboardThematicStyleDTO style = new DashboardThematicStyleDTO(clientRequest);
    
    this.loadLayerData(layer, style, mdAttribute);
    
    render("createComponent.jsp");
  }

  @Override
  public void applyWithStyle(DashboardLayerDTO layer, DashboardStyleDTO style, String mapId, DashboardConditionDTO[] conditions)
      throws IOException, ServletException
  {
    try
    {
      String layerJSON = layer.applyWithStyle(style, mapId, conditions);
      
      JSONReturnObject jsonReturn = new JSONReturnObject(layerJSON);
      jsonReturn.setInformation( this.getClientRequest().getInformation() );
      jsonReturn.setWarnings(this.getClientRequest().getWarnings());
      
      this.getResponse().setStatus(200);
      this.getResponse().setContentType("application/json");
      
      this.getResponse().getWriter().print(jsonReturn.toString());
    }
    catch (Throwable t)
    {
      this.loadLayerData(layer, (DashboardThematicStyleDTO) style, ((DashboardThematicStyleDTO) style).getMdAttributeId());
      
      if(t instanceof ProblemExceptionDTO)
      {
        ProblemExceptionDTO ex = (ProblemExceptionDTO) t;
        GDBErrorUtility.prepareProblems(ex, req, true);
      }
      else
      {
        log.error(t);
        GDBErrorUtility.prepareThrowable(t, req);
      }
      
      // TODO: this needs to be pushed into the render method.
      this.getResponse().setContentType("text/html");
 
      if (layer.isNewInstance())
      {
        render("createComponent.jsp");
      }
      else
      {
        render("editComponent.jsp");
      }
    }
  }
}
