/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.geodashboard.gis.persist;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.runwaysdk.geodashboard.DashboardDTO;
import com.runwaysdk.geodashboard.GDBErrorUtility;
import com.runwaysdk.geodashboard.JavascriptUtil;
import com.runwaysdk.geodashboard.ontology.ClassifierDTO;
import com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO;
import com.runwaysdk.geodashboard.ontology.ClassifierTermAttributeRootDTO;
import com.runwaysdk.geodashboard.util.EscapeUtil;
import com.runwaysdk.system.gis.geo.GeoNodeDTO;
import com.runwaysdk.system.metadata.MdAttributeCharacterDTO;
import com.runwaysdk.system.metadata.MdAttributeConcreteDTO;
import com.runwaysdk.system.metadata.MdAttributeDTO;
import com.runwaysdk.system.metadata.MdAttributeDateDTO;
import com.runwaysdk.system.metadata.MdAttributeNumberDTO;
import com.runwaysdk.system.metadata.MdAttributeTermDTO;
import com.runwaysdk.system.metadata.MdAttributeTextDTO;
import com.runwaysdk.system.metadata.MdAttributeVirtualDTO;
import com.runwaysdk.transport.conversion.json.JSONReturnObject;

public class DashboardThematicLayerController extends DashboardThematicLayerControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR = "/WEB-INF/com/runwaysdk/geodashboard/gis/persist/DashboardThematicLayer/";

  public static final String LAYOUT  = "WEB-INF/templates/layout.jsp";

  private static final Log   log     = LogFactory.getLog(DashboardThematicLayerController.class);

  public DashboardThematicLayerController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }

  public void create(DashboardThematicLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
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

  public void failCreate(DashboardThematicLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }

  public void delete(DashboardThematicLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
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

  public void failDelete(DashboardThematicLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void failEdit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.view(id);
  }

  public void update(DashboardThematicLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      dto.apply();
      this.view(dto.getId());
    }
    catch (com.runwaysdk.ProblemExceptionDTO e)
    {
      this.failUpdate(dto);
    }
  }

  public void failUpdate(DashboardThematicLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void view(String id) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    req.setAttribute("layer", DashboardThematicLayerDTO.get(clientRequest, id));
    render("viewComponent.jsp");
  }

  public void failView(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }

  public void viewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerQueryDTO query = com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerDTO.getAllInstances(clientRequest, null, true, 20, 1);
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
    com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerQueryDTO query = com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerDTO.getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }

  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }
  
  private String encodeString(String val)
  {
    
    String value = null;
    try
    {
      value = URLEncoder.encode(val, "UTF-8");
    }
    catch (UnsupportedEncodingException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return value;
  }
  
  private JSONArray formatAggregationMethods(List<AggregationTypeDTO> aggMethods)
  {
    JSONArray formattedAggMethods = new JSONArray();
    for(AggregationTypeDTO aggMethod : aggMethods)
    {
      try
      {
        JSONObject aggMethodObj = new JSONObject();
        String formattedAggMethod = aggMethod.toString().replaceAll(".*\\.", "");
        aggMethodObj.put("method", formattedAggMethod);
        aggMethodObj.put("label", aggMethod.getDisplayLabel());
        aggMethodObj.put("id", aggMethod.getId());
        formattedAggMethods.put(aggMethodObj);
      }
      catch (JSONException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    
    return formattedAggMethods;
  }

  
  /**
   * Loads artifacts for layer/style CRUD.
   * 
   * @param tLayer
   * @param style
   * @param mapId
   *          TODO
   * @param mdAttributeId
   * @param mdAttribute
   * @throws JSONException
   */
  @SuppressWarnings("unchecked")
  private void loadLayerData(DashboardLayerDTO layer, DashboardThematicStyleDTO style, String mapId, String mdAttributeId)
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();

    if (layer instanceof DashboardThematicLayerDTO)
    {

      DashboardThematicLayerDTO tLayer = (DashboardThematicLayerDTO) layer;
      req.setAttribute("layer", tLayer);

      req.setAttribute("style", style);

      String[] fonts = DashboardThematicStyleDTO.getSortedFonts(clientRequest);
      req.setAttribute("fonts", this.encodeString(new JSONArray(Arrays.asList(fonts)).toString()) );

      // selected attribute
      MdAttributeDTO mdAttr;

      if (mdAttributeId != null)
      { // new instance
        mdAttr = MdAttributeDTO.get(clientRequest, mdAttributeId);
      }
      else
      { // edit
        mdAttr = ( (MdAttributeDTO) tLayer.getMdAttribute() );
      }

      DashboardMapDTO map = DashboardMapDTO.get(clientRequest, mapId);
      DashboardDTO dashboard = map.getDashboard();

      String geoNodesJSON = dashboard.getGeoNodesJSON(mdAttr);
      req.setAttribute("geoNodes", this.encodeString(geoNodesJSON));
      
      // TODO: REMOVE 'nodes' - geoNodes replaces it
//      GeoNodeDTO[] nodes = dashboard.getGeoNodes(mdAttr);
//      req.setAttribute("nodes", nodes);

      req.setAttribute("mdAttributeId", mdAttr.getId());
      req.setAttribute("activeMdAttributeLabel", this.getDisplayLabel(mdAttr));

      // aggregations
      List<AggregationTypeDTO> aggregations = (List<AggregationTypeDTO>) DashboardStyleDTO.getSortedAggregations(clientRequest, mdAttr.getId()).getResultSet();

      req.setAttribute("aggregations", this.encodeString(formatAggregationMethods(aggregations).toString()) );
      req.setAttribute("activeAggregation", tLayer.getActiveAggregationLabel(aggregations));

      List<String> pointTypes = new ArrayList<String>();
      pointTypes.add("CIRCLE");
      pointTypes.add("STAR");
      pointTypes.add("SQUARE");
      pointTypes.add("TRIANGLE");
      pointTypes.add("CROSS");
      pointTypes.add("X");
      req.setAttribute("pointTypes", pointTypes);
      req.setAttribute("activeBasicPointType", style.getPointWellKnownName());
      req.setAttribute("activeGradientPointType", style.getGradientPointWellKnownName());
      req.setAttribute("activeCategoryPointType", style.getCategoryPointWellKnownName());

      // layer types
      Map<String, String> labels = tLayer.getLayerTypeMd().getEnumItems();

      Map<String, String> layerTypes = new LinkedHashMap<String, String>();

      // Set possible layer types based on attribute type
      MdAttributeConcreteDTO mdAttributeConcrete = this.getMdAttributeConcrete(mdAttr);
      if (mdAttributeConcrete instanceof MdAttributeDateDTO)
      {
        layerTypes.put(AllLayerTypeDTO.BASICPOINT.getName(), labels.get(AllLayerTypeDTO.BASICPOINT.getName()));
        layerTypes.put(AllLayerTypeDTO.BASICPOLYGON.getName(), labels.get(AllLayerTypeDTO.BASICPOLYGON.getName()));
      }
      else if (mdAttributeConcrete instanceof MdAttributeTermDTO || mdAttributeConcrete instanceof MdAttributeTextDTO || mdAttributeConcrete instanceof MdAttributeCharacterDTO)
      {
        layerTypes.put(AllLayerTypeDTO.BASICPOINT.getName(), labels.get(AllLayerTypeDTO.BASICPOINT.getName()));
        layerTypes.put(AllLayerTypeDTO.CATEGORYPOINT.getName(), labels.get(AllLayerTypeDTO.CATEGORYPOINT.getName()));
        layerTypes.put(AllLayerTypeDTO.BASICPOLYGON.getName(), labels.get(AllLayerTypeDTO.BASICPOLYGON.getName()));
        layerTypes.put(AllLayerTypeDTO.CATEGORYPOLYGON.getName(), labels.get(AllLayerTypeDTO.CATEGORYPOLYGON.getName()));
      }
      else
      {
        layerTypes.put(AllLayerTypeDTO.BASICPOINT.getName(), labels.get(AllLayerTypeDTO.BASICPOINT.getName()));
        layerTypes.put(AllLayerTypeDTO.GRADIENTPOINT.getName(), labels.get(AllLayerTypeDTO.GRADIENTPOINT.getName()));
        layerTypes.put(AllLayerTypeDTO.CATEGORYPOINT.getName(), labels.get(AllLayerTypeDTO.CATEGORYPOINT.getName()));
        layerTypes.put(AllLayerTypeDTO.BUBBLE.getName(), labels.get(AllLayerTypeDTO.BUBBLE.getName()));
        layerTypes.put(AllLayerTypeDTO.BASICPOLYGON.getName(), labels.get(AllLayerTypeDTO.BASICPOLYGON.getName()));
        layerTypes.put(AllLayerTypeDTO.GRADIENTPOLYGON.getName(), labels.get(AllLayerTypeDTO.GRADIENTPOLYGON.getName()));
        layerTypes.put(AllLayerTypeDTO.CATEGORYPOLYGON.getName(), labels.get(AllLayerTypeDTO.CATEGORYPOLYGON.getName()));
      }

      req.setAttribute("layerTypeNames", layerTypes.keySet().toArray());
      req.setAttribute("layerTypeLabels", layerTypes.values().toArray());
      req.setAttribute("layerTypeNamesJSON", EscapeUtil.escapeHTMLAttribut(new JSONArray(layerTypes.keySet()).toString()));

      List<String> activeLayerType = tLayer.getLayerTypeEnumNames();
      if (activeLayerType.size() > 0)
      { // Set the selected layer type to what its currently set to in the database (this will exist for edits, but not
        // new instances)
        req.setAttribute("activeLayerTypeName", activeLayerType.get(0));
      }
      else
      {
        req.setAttribute("activeLayerTypeName", AllLayerTypeDTO.BASICPOINT.getName());
      }

      // Determine if the attribute is an ontology attribute
      if (mdAttributeConcrete instanceof MdAttributeTermDTO)
      {
        req.setAttribute("isOntologyAttribute", true);
        req.setAttribute("isTextAttribute", false);
        req.setAttribute("relationshipType", ClassifierIsARelationshipDTO.CLASS);
        req.setAttribute("termType", ClassifierDTO.CLASS);

        ClassifierDTO[] roots = DashboardDTO.getClassifierRoots(clientRequest, mdAttr.getId());
        JSONObject rootsIds = new JSONObject();
        JSONArray ids = new JSONArray();

        Map<String, Boolean> selectableMap = new HashMap<String, Boolean>();
        for (ClassifierDTO root : roots)
        {
          JSONObject newJSON = new JSONObject();
          try
          {
            newJSON.put("termId", root.getId());
          }
          catch (JSONException e)
          {
            throw new RuntimeException(e);
          }

          List<? extends ClassifierTermAttributeRootDTO> relationships = root.getAllClassifierTermAttributeRootsRelationships();
          for (ClassifierTermAttributeRootDTO relationship : relationships)
          {
            if (relationship.getParentId().equals(mdAttributeConcrete.getId()))
            {
              try
              {
                newJSON.put("selectable", relationship.getSelectable());
              }
              catch (JSONException e)
              {
                throw new RuntimeException(e);
              }
              selectableMap.put(root.getId(), relationship.getSelectable());
            }
          }

          ids.put(newJSON);
        }

        try
        {
          rootsIds.put("roots", ids);
        }
        catch (JSONException e)
        {
          throw new RuntimeException(e);
        }

        // Passing ontology root to layer form categories
        req.setAttribute("roots", rootsIds);
        req.setAttribute("selectableMap", selectableMap);

        JavascriptUtil.loadOntologyBundle(this.getClientRequest(), req);
      }
      else if (mdAttributeConcrete instanceof MdAttributeCharacterDTO || mdAttributeConcrete instanceof MdAttributeTextDTO)
      {
        req.setAttribute("isTextAttribute", true);
        req.setAttribute("isOntologyAttribute", false);
      }
      else
      {
        req.setAttribute("isOntologyAttribute", false);
        req.setAttribute("isTextAttribute", false);
      }

      req.setAttribute("categoryType", this.getCategoryType(mdAttributeConcrete));
      req.setAttribute("polygoncategories", EscapeUtil.escapeHTMLAttribut(style.getCategoryPolygonStyles()));
      req.setAttribute("pointcategories", EscapeUtil.escapeHTMLAttribut(style.getCategoryPointStyles()));
      req.setAttribute("secondaryCategories", EscapeUtil.escapeHTMLAttribut(style.getSecondaryCategories()));

      /*
       * Secondary attribute objects
       */
      req.setAttribute("secondaryAttributes", DashboardMapDTO.getSecondaryAttributes(this.getClientRequest(), mapId, mdAttributeId));
      req.setAttribute("secondaryAggregation", style.getSecondaryAggregationType().size() > 0 ? style.getSecondaryAggregationType().get(0).getName() : "");
    }
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
  
  @Override
  public void applyWithStyle(DashboardThematicLayerDTO layer, DashboardStyleDTO style, String mapId, AggregationStrategyDTO strategy, String state) throws IOException, ServletException
  {
    try
    {
      String layerJSON = layer.applyWithStyleAndStrategy(style, mapId, strategy, state);

      JSONReturnObject jsonReturn = new JSONReturnObject(layerJSON);
      jsonReturn.setInformation(this.getClientRequest().getInformation());
      jsonReturn.setWarnings(this.getClientRequest().getWarnings());

      this.getResponse().setStatus(200);
      this.getResponse().setContentType("application/json");

      this.getResponse().getWriter().print(jsonReturn.toString());
    }
    catch (Throwable t)
    {
      DashboardThematicLayerDTO tLayer = (DashboardThematicLayerDTO) layer;
      this.loadLayerData(layer, (DashboardThematicStyleDTO) style, mapId, tLayer.getMdAttributeId());

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

  @Override
  public void newThematicInstance(String mdAttribute, String mapId) throws IOException, ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    DashboardThematicLayerDTO layer = new DashboardThematicLayerDTO(clientRequest);
    DashboardThematicStyleDTO style = new DashboardThematicStyleDTO(clientRequest);

    this.loadLayerData(layer, style, mapId, mdAttribute);

    render("createComponent.jsp");
  }

  @Override
  public void edit(String id) throws java.io.IOException, javax.servlet.ServletException
  {
    DashboardThematicLayerDTO layer = DashboardThematicLayerDTO.lock(super.getClientRequest(), id);

    DashboardMapDTO map = layer.getAllContainingMap().get(0);

    // There will be one style only for this layer
    DashboardThematicLayerDTO tLayer = (DashboardThematicLayerDTO) layer;
    DashboardThematicStyleDTO style = (DashboardThematicStyleDTO) layer.getAllHasStyle().get(0);

    this.loadLayerData(layer, style, map.getId(), tLayer.getMdAttributeId());

    render("editComponent.jsp");
  }

  public void cancel(DashboardThematicLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    if (!dto.isNewInstance())
    {
      dto.unlock();
    }
  }

  public void failCancel(DashboardThematicLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    this.edit(dto.getId());
  }

}
