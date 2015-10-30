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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;

import com.runwaysdk.ProblemExceptionDTO;
import com.runwaysdk.geodashboard.GDBErrorUtility;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionDTO;
import com.runwaysdk.system.gis.geo.UniversalDTO;
import com.runwaysdk.transport.conversion.json.JSONReturnObject;

public class DashboardReferenceLayerController extends DashboardReferenceLayerControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR = "/WEB-INF/com/runwaysdk/geodashboard/gis/persist/DashboardReferenceLayer/";
  
  public static final String LAYOUT = "WEB-INF/templates/layout.jsp";
  
  private static final Log   log     = LogFactory.getLog(DashboardReferenceLayerController.class);
  
  public DashboardReferenceLayerController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }
  
  public void cancel(com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    if (!dto.isNewInstance())
    {
      dto.unlock();
    }
  }
  public void failCancel(com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    this.edit(dto.getId());
  }
  public void create(com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
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
  
  public void failCreate(DashboardReferenceLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }
  
  public void delete(com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
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
  
  public void failDelete(DashboardReferenceLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  
  @Override
  public void edit(String id) throws java.io.IOException, javax.servlet.ServletException
  {
    DashboardReferenceLayerDTO layer = DashboardReferenceLayerDTO.lock(super.getClientRequest(), id);

    DashboardMapDTO map = layer.getAllContainingMap().get(0);

    // There will be one style only for this layer
    DashboardStyleDTO style = layer.getAllHasStyle().get(0);
    
    String universalId = layer.getUniversalId();

    this.loadLayerData(layer, style, map.getId(), universalId);

    render("editComponent.jsp");
  }
  
  public void failEdit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.view(id);
  }
  
  @Override
  public void newReferenceInstance(String universalId, String mapId) throws IOException, ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    DashboardReferenceLayerDTO layer = new DashboardReferenceLayerDTO(clientRequest);
    DashboardStyleDTO style = new DashboardStyleDTO(clientRequest);

    this.loadLayerData(layer, style, mapId, universalId);

    render("createComponent.jsp");
  }
  
  public void failNewInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }
  public void update(com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
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
  public void failUpdate(com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void view(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    req.setAttribute("_dashboardMap", com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("_geoEntity", com.runwaysdk.system.metadata.MdAttributeReferenceDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("_layerType", com.runwaysdk.geodashboard.gis.persist.AllLayerTypeDTO.allItems(super.getClientSession().getRequest()));
    req.setAttribute("_universal", com.runwaysdk.system.gis.geo.UniversalDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("item", com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO.get(clientRequest, id));
    render("viewComponent.jsp");
  }
  public void failView(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }
  public void viewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerQueryDTO query = com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO.getAllInstances(clientRequest, null, true, 20, 1);
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
    com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerQueryDTO query = com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO.getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }
  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }
  
  @Override
  public void applyWithStyle(DashboardReferenceLayerDTO layer, DashboardStyleDTO style, String mapId) throws IOException, ServletException
  {
    try
    {
      String layerJSON = layer.applyWithStyle(style, mapId, "");

      JSONReturnObject jsonReturn = new JSONReturnObject(layerJSON);
      jsonReturn.setInformation(this.getClientRequest().getInformation());
      jsonReturn.setWarnings(this.getClientRequest().getWarnings());

      this.getResponse().setStatus(200);
      this.getResponse().setContentType("application/json");

      this.getResponse().getWriter().print(jsonReturn.toString());
    }
    catch (Throwable t)
    {
      DashboardReferenceLayerDTO tLayer = (DashboardReferenceLayerDTO) layer;
      
      String universalId = tLayer.getUniversalId();
      
      this.loadLayerData(layer, style, mapId, universalId);

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
  
  
  /**
   * Loads artifacts for layer/style CRUD.
   * @param style
   * @param mapId
   *          TODO
   * @param universalId TODO
   * @param tLayer
   * @param mdAttributeId
   * @param mdAttribute
   * 
   * @throws JSONException
   */
  private void loadLayerData(DashboardLayerDTO layer, DashboardStyleDTO style, String mapId, String universalId)
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();

    if (layer instanceof DashboardReferenceLayerDTO)
    {

      DashboardReferenceLayerDTO rLayer = (DashboardReferenceLayerDTO) layer;
      req.setAttribute("layer", rLayer);
      
      req.setAttribute("style", style);

      String[] fonts = DashboardThematicStyleDTO.getSortedFonts(clientRequest);
      req.setAttribute("fonts", fonts);

      // layer types
      Map<String, String> labels = rLayer.getLayerTypeMd().getEnumItems();
      
      List<String> pointTypes = new ArrayList<String>();
      pointTypes.add("CIRCLE");
      pointTypes.add("STAR");
      pointTypes.add("SQUARE");
      pointTypes.add("TRIANGLE");
      pointTypes.add("CROSS");
      pointTypes.add("X");
      req.setAttribute("pointTypes", pointTypes);
      req.setAttribute("activeBasicPointType", style.getPointWellKnownName());

      Map<String, String> layerTypes = new LinkedHashMap<String, String>();
      layerTypes.put(AllLayerTypeDTO.BASICPOLYGON.getName(), labels.get(AllLayerTypeDTO.BASICPOLYGON.getName()));
      layerTypes.put(AllLayerTypeDTO.BASICPOINT.getName(), labels.get(AllLayerTypeDTO.BASICPOINT.getName()));

      req.setAttribute("layerTypeNames", layerTypes.keySet().toArray());
      req.setAttribute("layerTypeLabels", layerTypes.values().toArray());

      List<String> activeLayerType = rLayer.getLayerTypeEnumNames();
      if (activeLayerType.size() > 0)
      { // Set the selected layer type to what its currently set to in the database (this will exist for edits, but not
        // new instances)
        req.setAttribute("activeLayerTypeName", activeLayerType.get(0));
      }
      else
      {
        req.setAttribute("activeLayerTypeName", AllLayerTypeDTO.BASICPOLYGON.getName());
      }
      
      req.setAttribute("universalId", universalId);
      
      UniversalDTO universal = UniversalDTO.get(clientRequest, universalId);
      String uniDispLabel = universal.getDisplayLabel().toString();
      req.setAttribute("layerName", uniDispLabel);
    }
  }
}
