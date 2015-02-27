package com.runwaysdk.geodashboard.gis.persist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.ProblemExceptionDTO;
import com.runwaysdk.business.ontology.TermDTO;
import com.runwaysdk.geodashboard.DashboardDTO;
import com.runwaysdk.geodashboard.GDBErrorUtility;
import com.runwaysdk.geodashboard.gis.ClassifierExportMenuDTO;
import com.runwaysdk.geodashboard.gis.GeoEntityExportMenuDTO;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionDTO;
import com.runwaysdk.geodashboard.ontology.ClassifierAttributeRootDTO;
import com.runwaysdk.geodashboard.ontology.ClassifierController;
import com.runwaysdk.geodashboard.ontology.ClassifierDTO;
import com.runwaysdk.geodashboard.ontology.ClassifierDisplayLabelDTO;
import com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO;
import com.runwaysdk.geodashboard.util.Iterables;
import com.runwaysdk.system.gis.geo.AllowedInDTO;
import com.runwaysdk.system.gis.geo.GeoEntityController;
import com.runwaysdk.system.gis.geo.GeoEntityDTO;
import com.runwaysdk.system.gis.geo.GeoEntityDisplayLabelDTO;
import com.runwaysdk.system.gis.geo.GeoEntityViewDTO;
import com.runwaysdk.system.gis.geo.LocatedInDTO;
import com.runwaysdk.system.gis.geo.SynonymDTO;
import com.runwaysdk.system.gis.geo.SynonymDisplayLabelDTO;
import com.runwaysdk.system.gis.geo.UniversalDTO;
import com.runwaysdk.system.gis.geo.UniversalDisplayLabelDTO;
import com.runwaysdk.system.metadata.MdAttributeConcreteDTO;
import com.runwaysdk.system.metadata.MdAttributeDTO;
import com.runwaysdk.system.metadata.MdAttributeDateDTO;
import com.runwaysdk.system.metadata.MdAttributeTermDTO;
import com.runwaysdk.system.metadata.MdAttributeNumberDTO;
import com.runwaysdk.system.metadata.MdAttributeVirtualDTO;
import com.runwaysdk.system.ontology.TermUtilDTO;
import com.runwaysdk.transport.conversion.json.JSONReturnObject;
import com.runwaysdk.web.json.JSONController;

public class DashboardLayerController extends DashboardLayerControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR = "/WEB-INF/com/runwaysdk/geodashboard/gis/persist/DashboardLayer/";

  public static final String LAYOUT  = "WEB-INF/templates/layout.jsp";

  private static final Log   log     = LogFactory.getLog(DashboardLayerController.class);

  private static String      rootUniId;

  public DashboardLayerController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);

    rootUniId = UniversalDTO.getRoot(this.getClientRequest()).getId();
  }

  public void cancel(DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    if (!dto.isNewInstance())
    {
      dto.unlock();
    }
  }

  public void failCancel(DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    this.edit(dto.getId());
  }

  public void create(DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
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

  public void failCreate(DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }

  public void delete(DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
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

  public void failDelete(DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void edit(String id) throws java.io.IOException, javax.servlet.ServletException
  {
    DashboardLayerDTO layer = DashboardLayerDTO.lock(super.getClientRequest(), id);

    // There will be one style only for this layer (for IDE)
    DashboardThematicStyleDTO style = (DashboardThematicStyleDTO) layer.getAllHasStyle().get(0);

    this.loadLayerData(layer, style, null);

    render("editComponent.jsp");
  }

  public void failEdit(String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.view(id);
  }

  /**
   * Loads artifacts for layer/style CRUD.
   * 
   * @param layer
   */
  @SuppressWarnings("unchecked")
  private void loadLayerData(DashboardLayerDTO layer, DashboardThematicStyleDTO style, String mdAttribute)
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();

    req.setAttribute("layer", layer);

    req.setAttribute("style", style);

    String[] fonts = DashboardThematicStyleDTO.getSortedFonts(clientRequest);
    req.setAttribute("fonts", fonts);

    // Get the universals, sorted by their ordering in the universal tree.
    List<TermDTO> universals = Arrays.asList(TermUtilDTO.getAllDescendants(this.getClientRequest(), rootUniId, new String[] { AllowedInDTO.CLASS }));

    // Getting the lowest level universal in the tree (the leaf)
    TermDTO leaf = null;
    for (TermDTO universal : universals)
    {
      TermDTO[] descendants = universal.getAllDescendants(new String[] { AllowedInDTO.CLASS });
      if (descendants.length == 0)
      {
        leaf = universal;
      }
    }

    req.setAttribute("universals", universals);
    req.setAttribute("universalLeafId", leaf.getId());


    // selected attribute
    MdAttributeDTO mdAttr;
    if (mdAttribute != null)
    { // new instance
      mdAttr = MdAttributeDTO.get(clientRequest, mdAttribute);
    }
    else
    { // edit
      mdAttr = ( (MdAttributeDTO) style.getMdAttribute() );
    }

    req.setAttribute("mdAttributeId", mdAttr.getId());
    req.setAttribute("activeMdAttributeLabel", this.getDisplayLabel(mdAttr));

    // aggregations
    List<AggregationTypeDTO> aggregations = (List<AggregationTypeDTO>) DashboardStyleDTO.getSortedAggregations(clientRequest).getResultSet();
    Collections.reverse(aggregations); // Simple solution for making SUM the default aggregation type

    // Filter out the invalid aggregation types based upon the
    new Iterables<AggregationTypeDTO>().remove(aggregations, new AggregationPredicate(mdAttr));

    req.setAttribute("aggregations", aggregations);
    req.setAttribute("activeAggregation", style.getActiveAggregationLabel(aggregations));

    // layer types
    Map<String, String> labels = layer.getLayerTypeMd().getEnumItems();

    Map<String, String> layerTypes = new LinkedHashMap<String, String>();
    layerTypes.put(AllLayerTypeDTO.BASIC.getName(), labels.get(AllLayerTypeDTO.BASIC.getName()));
    layerTypes.put(AllLayerTypeDTO.BUBBLE.getName(), labels.get(AllLayerTypeDTO.BUBBLE.getName()));

    // filter out invalid layer types depending on attribute type
    // this is primarily to prevent creating gradients on date fields
    MdAttributeConcreteDTO mdAttributeConcrete = this.getMdAttributeConcrete(mdAttr);

    if (! ( mdAttributeConcrete instanceof MdAttributeDateDTO ))
    {
      layerTypes.put(AllLayerTypeDTO.GRADIENT.getName(), labels.get(AllLayerTypeDTO.GRADIENT.getName()));
      layerTypes.put(AllLayerTypeDTO.CATEGORY.getName(), labels.get(AllLayerTypeDTO.CATEGORY.getName()));
    }

    req.setAttribute("layerTypeNames", layerTypes.keySet().toArray());
    req.setAttribute("layerTypeLabels", layerTypes.values().toArray());

    List<String> activeLayerType = layer.getLayerTypeEnumNames();
    if (activeLayerType.size() > 0)
    { // Set the selected layer type to what its currently set to in the database (this will exist for edits, but not
      // new instances)
      req.setAttribute("activeLayerTypeName", activeLayerType.get(0));
    }
    else
    {
      req.setAttribute("activeLayerTypeName", AllLayerTypeDTO.BASIC.getName());
    }
    
    // Determine if the attribute is an ontology attribute
    MdAttributeConcreteDTO mtAttrConcrete = ((MdAttributeVirtualDTO) mdAttr).getMdAttributeConcrete();
    if (mtAttrConcrete instanceof MdAttributeTermDTO)
    {
      req.setAttribute("isOntologyAttribute", true);
      
      String js = JSONController.importTypes(clientRequest.getSessionId(), new String[] { 
      	GeoEntityDTO.CLASS, 
      	LocatedInDTO.CLASS, 
      	GeoEntityDisplayLabelDTO.CLASS, 
      	GeoEntityController.CLASS, 
      	UniversalDTO.CLASS, 
      	UniversalDisplayLabelDTO.CLASS, 
      	TermUtilDTO.CLASS, 
      	GeoEntityViewDTO.CLASS, 
      	SynonymDTO.CLASS, 
      	SynonymDisplayLabelDTO.CLASS, 
      	GeoEntityExportMenuDTO.CLASS, 
      	
        ClassifierDTO.CLASS, 
        ClassifierIsARelationshipDTO.CLASS, 
        ClassifierDisplayLabelDTO.CLASS, 
        ClassifierController.CLASS, 
        ClassifierExportMenuDTO.CLASS
          
      }, true);
      
      req.setAttribute("js", js);
      
     
      ClassifierDTO[] roots = DashboardDTO.getClassifierRoots(clientRequest, mdAttr.getId());  
      JSONObject rootsIds = new JSONObject();
      JSONArray ids = new JSONArray();
      
      Map<String, Boolean> selectableMap = new HashMap<String, Boolean>();
      
      for(ClassifierDTO root : roots)
      {
    	  ids.put(root.getId());
    	  List<? extends ClassifierAttributeRootDTO> relationships = root.getAllClassifierAttributeRootsRelationships();   			  
    	  for(ClassifierAttributeRootDTO relationship : relationships)
    	  {
    		  if(relationship.getParentId().equals(mtAttrConcrete.getId()))
    		  {
    			  selectableMap.put(root.getId(), relationship.getSelectable());			  
    		  }
    	  }
      }
      
	    try {
			rootsIds.put("rootsIds", ids);
		} 
	    catch (JSONException e) 
	    {
	    	throw new RuntimeException(e);
		}
      
      // Passing ontology root to layer form categories 
      req.setAttribute("roots", rootsIds);
      req.setAttribute("selectableMap", selectableMap);     
    }
    else
    {
    	req.setAttribute("isOntologyAttribute", false);
    }
    
    req.setAttribute("categoryType", this.getCategoryType(mdAttributeConcrete));
    
  }

  private String getDisplayLabel(MdAttributeDTO mdAttr)
  {
    if (mdAttr instanceof MdAttributeVirtualDTO)
    {
      MdAttributeVirtualDTO mdAttributeVirtual = (MdAttributeVirtualDTO) mdAttr;

      String label = mdAttributeVirtual.getDisplayLabel().getValue();

      if (label == null || label.length() > 0)
      {
        return label;
      }

      return mdAttributeVirtual.getMdAttributeConcrete().getDisplayLabel().getValue();
    }

    return ( (MdAttributeConcreteDTO) mdAttr ).getDisplayLabel().getValue();
  }

  private String getCategoryType(MdAttributeDTO mdAttr)
  {
    MdAttributeConcreteDTO concrete = this.getMdAttributeConcrete(mdAttr);

    if (concrete instanceof MdAttributeDateDTO)
    {
      return "date";
    }
    else if (concrete instanceof MdAttributeNumberDTO)
    {
      return "number";
    }

    return "text";
  }

  private MdAttributeConcreteDTO getMdAttributeConcrete(MdAttributeDTO mdAttr)
  {
    if (mdAttr instanceof MdAttributeVirtualDTO)
    {
      MdAttributeVirtualDTO mdAttributeVirtual = (MdAttributeVirtualDTO) mdAttr;

      return mdAttributeVirtual.getMdAttributeConcrete();
    }

    return ( (MdAttributeConcreteDTO) mdAttr );
  }

  /**
   * @deprecated
   * 
   *             Call newThematicInstance instead.
   */
  @Deprecated
  public void newInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    // com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    // DashboardLayerDTO layer = new DashboardLayerDTO(
    // clientRequest);
    // DashboardThematicStyleDTO style = new DashboardThematicStyleDTO(clientRequest);
    //
    // this.loadLayerData(layer, style);
    //
    // render("createComponent.jsp");

    throw new UnsupportedOperationException();
  }

  public void failNewInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }

  public void update(DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
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

  public void failUpdate(DashboardLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void view(String id) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    req.setAttribute("layer", DashboardLayerDTO.get(clientRequest, id));
    render("viewComponent.jsp");
  }

  public void failView(String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }

  public void viewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    DashboardLayerQueryDTO query = DashboardLayerDTO.getAllInstances(clientRequest, null, true, 20, 1);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }

  public void failViewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }

  public void viewPage(String sortAttribute, Boolean isAscending, Integer pageSize, Integer pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    DashboardLayerQueryDTO query = DashboardLayerDTO.getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }

  public void failViewPage(String sortAttribute, String isAscending, String pageSize, String pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }

  public void newThematicInstance(String mdAttribute) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    DashboardLayerDTO layer = new DashboardLayerDTO(clientRequest);
    DashboardThematicStyleDTO style = new DashboardThematicStyleDTO(clientRequest);

    this.loadLayerData(layer, style, mdAttribute);

    render("createComponent.jsp");
  }

  @Override
  public void applyWithStyle(DashboardLayerDTO layer, DashboardStyleDTO style, String mapId, DashboardConditionDTO[] conditions) throws IOException, ServletException
  {
    try
    {
      String layerJSON = layer.applyWithStyle(style, mapId, conditions);

      JSONReturnObject jsonReturn = new JSONReturnObject(layerJSON);
      jsonReturn.setInformation(this.getClientRequest().getInformation());
      jsonReturn.setWarnings(this.getClientRequest().getWarnings());

      this.getResponse().setStatus(200);
      this.getResponse().setContentType("application/json");

      this.getResponse().getWriter().print(jsonReturn.toString());
    }
    catch (Throwable t)
    {
      this.loadLayerData(layer, (DashboardThematicStyleDTO) style, ( (DashboardThematicStyleDTO) style ).getMdAttributeId());

      if (t instanceof ProblemExceptionDTO)
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
