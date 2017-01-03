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
package net.geoprism.prism;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ontology.TermDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.mvc.Controller;
import com.runwaysdk.mvc.Endpoint;
import com.runwaysdk.mvc.ErrorSerialization;
import com.runwaysdk.mvc.RequestParamter;
import com.runwaysdk.mvc.ResponseIF;
import com.runwaysdk.mvc.RestBodyResponse;
import com.runwaysdk.mvc.RestResponse;

import net.geoprism.ontology.ClassifierDTO;
import net.geoprism.ontology.ClassifierIsARelationshipDTO;
import net.geoprism.ontology.ClassifierSynonymDTO;

@Controller(url = "category")
public class CategoryController
{
  @Endpoint(url = "all", method = ServletMethod.GET, error = ErrorSerialization.JSON)
  public ResponseIF getAllCategories(ClientRequestIF request) throws JSONException
  {
    String classifiers = ClassifierDTO.getCategoryClassifiersAsJSON(request);

    return new RestBodyResponse(new JSONArray(classifiers));
  }

  @Endpoint(url = "get", method = ServletMethod.GET, error = ErrorSerialization.JSON)
  public ResponseIF getCategory(ClientRequestIF request, @RequestParamter(name = "id") String id) throws JSONException
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

    return new RestBodyResponse(response);
  }

  @Endpoint(url = "edit", method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF editOption(ClientRequestIF request, @RequestParamter(name = "id") String id) throws JSONException
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

    return new RestBodyResponse(response);
  }

  @Endpoint(url = "apply", method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF applyOption(ClientRequestIF request, @RequestParamter(name = "config") String config) throws JSONException
  {
    JSONObject object = new JSONObject(config);
    String categoryId = object.getString("categoryId");

    ClassifierDTO.applyOption(request, config);

    return this.getCategory(request, categoryId);
  }

  @Endpoint(url = "unlock", method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF unlockCategory(ClientRequestIF request, @RequestParamter(name = "id") String id)
  {
    ClassifierDTO.unlockCategory(request, id);

    return new RestResponse();
  }

  @Endpoint(url = "create", method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF createOption(ClientRequestIF request, @RequestParamter(name = "option") String option) throws JSONException
  {
    ClassifierDTO classifier = ClassifierDTO.createOption(request, option);

    JSONObject object = new JSONObject();
    object.put("label", classifier.getDisplayLabel().getValue());
    object.put("id", classifier.getId());

    return new RestBodyResponse(object);
  }

  @Endpoint(url = "remove", method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF deleteOption(ClientRequestIF request, @RequestParamter(name = "id") String id)
  {
    ClassifierDTO.deleteOption(request, id);

    return new RestResponse();
  }

  @Endpoint(url = "validate", method = ServletMethod.GET, error = ErrorSerialization.JSON)
  public ResponseIF validateCategoryName(ClientRequestIF request, @RequestParamter(name = "name") String name, @RequestParamter(name = "id") String id)
  {
    ClassifierDTO.validateCategoryName(request, name, id);

    return new RestResponse();
  }

  @Endpoint(url = "update", method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF updateCategory(ClientRequestIF request, @RequestParamter(name = "category") String category) throws JSONException
  {
    JSONObject config = new JSONObject();
    config.put("option", new JSONObject(category));
    config.put("restore", new JSONArray());
    config.put("synonym", "");

    ClassifierDTO.applyOption(request, config.toString());

    return new RestResponse();
  }

}
