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
package net.geoprism.ontology;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import net.geoprism.JSONControllerUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import com.runwaysdk.business.ontology.TermAndRelDTO;
import com.runwaysdk.business.ontology.TermDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.ErrorUtility;
import com.runwaysdk.system.ontology.TermUtilDTO;
import com.runwaysdk.transport.conversion.json.BusinessDTOToJSON;
import com.runwaysdk.transport.conversion.json.JSONReturnObject;

public class ClassifierController extends ClassifierControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR = "/WEB-INF/net/geoprism/ontology/Classifier/";

  public static final String LAYOUT  = "WEB-INF/templates/layout.jsp";

  public ClassifierController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }

  public void create(ClassifierDTO dto, java.lang.String parentId) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      ClientRequestIF request = super.getClientRequest();

      TermAndRelDTO tnr = ClassifierDTO.create(request, dto, parentId);

      this.resp.getWriter().print(new JSONReturnObject(tnr.toJSON().toString()).toString());
    }
    catch (Throwable t)
    {
      boolean needsRedirect = ErrorUtility.handleFormError(t, req, resp);

      if (needsRedirect)
      {
        this.failCreate(dto, parentId);
      }
    }
  }

  public void delete(ClassifierDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      dto.delete();
    }
    catch (com.runwaysdk.ProblemExceptionDTO e)
    {
      this.failDelete(dto);
    }
  }

  public void getDirectDescendants(java.lang.String parentId, java.lang.Integer pageNum, java.lang.Integer pageSize) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      JSONArray array = new JSONArray();

      TermAndRelDTO[] tnrs = TermUtilDTO.getDirectDescendants(getClientRequest(), parentId, new String[] { ClassifierIsARelationshipDTO.CLASS });

      JSONObject page = new JSONObject();

      if (pageNum != null && pageSize != null && pageNum > 0 && pageSize > 0)
      {
        int startIndex = Math.max(0, ( ( pageNum - 1 ) * pageSize ));
        int endIndex = Math.min( ( pageNum * pageSize ), tnrs.length);
        int maxPages = ( (int) tnrs.length / pageSize ) + 1;

        for (int i = startIndex; i < endIndex; i++)
        {
          TermAndRelDTO tnr = tnrs[i];

          array.put(tnr.toJSON());
        }

        page.put("pageNumber", pageNum);
        page.put("maxPages", maxPages);
      }
      else
      {
        for (TermAndRelDTO tnr : tnrs)
        {
          array.put(tnr.toJSON());
        }

        page.put("pageNumber", pageNum);
        page.put("maxPages", 0);
      }

      page.put("values", array);

      resp.getWriter().print(new JSONReturnObject(page).toString());
    }
    catch (Throwable t)
    {
      ErrorUtility.prepareAjaxThrowable(t, resp);
    }
  }

  public void update(ClassifierDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      dto.apply();

      String ret = BusinessDTOToJSON.getConverter(dto).populate().toString();
      this.resp.getWriter().print(new JSONReturnObject(ret).toString());
    }
    catch (Throwable t)
    {
      boolean needsRedirect = ErrorUtility.handleFormError(t, req, resp);

      if (needsRedirect)
      {
        this.viewUpdate(dto.getId());
      }
    }
  }

  public void failCreate(ClassifierDTO dto, java.lang.String parentId) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }

  public void cancel(ClassifierDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    dto.unlock();
    this.view(dto.getId());
  }

  public void failCancel(ClassifierDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    this.edit(dto.getId());
  }

  public void failDelete(ClassifierDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void edit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    ClassifierDTO dto = ClassifierDTO.lock(super.getClientRequest(), id);
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void failEdit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.view(id);
  }

  public void failGetDirectDescendants(java.lang.String parentId, java.lang.String pageNum, java.lang.String pageSize) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }

  public void newInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    ClassifierDTO dto = new ClassifierDTO(clientRequest);
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }

  public void failNewInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }

  public void failUpdate(ClassifierDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void view(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    req.setAttribute("item", ClassifierDTO.get(clientRequest, id));
    render("viewComponent.jsp");
  }

  public void failView(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }

  public void viewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    ClassifierQueryDTO query = ClassifierDTO.getAllInstances(clientRequest, null, true, 20, 1);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }

  public void failViewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }

  public void viewCreate() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    ClassifierDTO dto = new ClassifierDTO(clientRequest);
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }

  public void failViewCreate() throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }

  public void viewPage(java.lang.String sortAttribute, java.lang.Boolean isAscending, java.lang.Integer pageSize, java.lang.Integer pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    ClassifierQueryDTO query = ClassifierDTO.getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }

  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }

  @Override
  public void viewUpdate(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    ClassifierDTO dto = ClassifierDTO.lock(super.getClientRequest(), id);
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void failViewUpdate(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }

  @Override
  public void getAllCategories() throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {

      String classifiers = ClassifierDTO.getCategoryClassifiersAsJSON(request);

      JSONControllerUtil.writeReponse(this.resp, new JSONArray(classifiers));
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, request);
    }
  }

  @Override
  public void getCategory(String id) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      ClassifierDTO dto = ClassifierDTO.get(request, id);

      JSONArray dArray = new JSONArray();

      TermDTO[] descendants = dto.getAllDescendants(new String[] { ClassifierIsARelationshipDTO.CLASS });

      for (TermDTO descendant : descendants)
      {
        JSONObject object = new JSONObject();
        object.put("label", descendant.getDisplayLabel().getValue());
        object.put("id", descendant.getId());

        dArray.put(object);
      }

      JSONObject response = new JSONObject();
      response.put("label", dto.getDisplayLabel().getValue());
      response.put("id", dto.getId());
      response.put("descendants", dArray);

      JSONControllerUtil.writeReponse(this.resp, response);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, request);
    }
  }
  
  @Override
  public void editOption(String id) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      ClassifierDTO dto = ClassifierDTO.editOption(request, id);

      JSONArray sArray = new JSONArray();

      List<? extends ClassifierSynonymDTO> synonyms = dto.getAllHasSynonym();

      for (ClassifierSynonymDTO synonym : synonyms)
      {
        JSONObject object = new JSONObject();
        object.put("label", synonym.getDisplayLabel().getValue());
        object.put("id", synonym.getId());

        sArray.put(object);
      }

      JSONObject response = new JSONObject();
      response.put("label", dto.getDisplayLabel().getValue());
      response.put("id", dto.getId());
      response.put("synonyms", sArray);

      JSONControllerUtil.writeReponse(this.resp, response);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, request);
    }
  }

  @Override
  public void applyOption(String config) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      JSONObject object = new JSONObject(config);
      String categoryId = object.getString("categoryId");

      ClassifierDTO.applyOption(request, config);

      this.getCategory(categoryId);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, request);
    }
  }

  @Override
  public void unlockCategory(String id) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      ClassifierDTO.unlockCategory(request, id);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, request);
    }
  }

  @Override
  public void createOption(String option) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      ClassifierDTO classifier = ClassifierDTO.createOption(request, option);

      JSONObject object = new JSONObject();
      object.put("label", classifier.getDisplayLabel().getValue());
      object.put("id", classifier.getId());

      JSONControllerUtil.writeReponse(this.resp, object);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, request);
    }
  }

  @Override
  public void deleteOption(String id) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      ClassifierDTO.deleteOption(request, id);

      JSONControllerUtil.writeReponse(this.resp);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, request);
    }
  }
  
  @Override
  public void validateCategoryName(String name, String id) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      ClassifierDTO.validateCategoryName(request, name, id);

      JSONControllerUtil.writeReponse(this.resp);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, request);
    }
  }
}
