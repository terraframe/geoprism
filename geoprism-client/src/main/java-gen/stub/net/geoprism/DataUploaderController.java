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
package net.geoprism;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.geoprism.ontology.ClassifierDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import com.runwaysdk.business.ValueObjectDTO;
import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.MultipartFileParameter;
import com.runwaysdk.generation.loader.Reloadable;

public class DataUploaderController extends DataUploaderControllerBase implements Reloadable
{
  public DataUploaderController(HttpServletRequest req, HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous);
  }

  @Override
  public void getAttributeInformation(MultipartFileParameter file) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      String fileName = file.getFilename();
      InputStream stream = file.getInputStream();

      try
      {
        JSONObject object = new JSONObject();
        object.put("information", new JSONObject(DataUploaderDTO.getAttributeInformation(request, fileName, stream)));
        object.put("options", new JSONObject(DataUploaderDTO.getOptionsJSON(request)));
        object.put("classifiers", new JSONArray(ClassifierDTO.getManagedClassifiersAsJSON(request)));

        JSONControllerUtil.writeReponse(this.resp, object);
      }
      finally
      {
        /*
         * Just in case the stream isn't closed by the server method
         */
        stream.close();
      }
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, this.getClientRequest());
    }
  }

  @Override
  public void importData(String configuration) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      String result = DataUploaderDTO.importData(request, configuration);

      JSONControllerUtil.writeReponse(this.resp, new JSONObject(result));
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, request);
    }
  }

  @Override
  public void cancelImport(String configuration) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      DataUploaderDTO.cancelImport(request, configuration);

      JSONControllerUtil.writeReponse(this.resp, "");
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, request);
    }
  }

  @Override
  public void getSavedConfiguration(String id, String sheetName) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      String configuration = DataUploaderDTO.getSavedConfiguration(request, id, sheetName);

      JSONObject object = new JSONObject();
      object.put("datasets", new JSONObject(configuration));

      JSONControllerUtil.writeReponse(this.resp, object);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, request);
    }
  }

  @Override
  public void createGeoEntity(String parentId, String universalId, String label) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      String entityId = DataUploaderDTO.createGeoEntity(request, parentId, universalId, label);

      JSONObject object = new JSONObject();
      object.put("entityId", entityId);

      JSONControllerUtil.writeReponse(this.resp, object);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, request);
    }
  }

  @Override
  public void createGeoEntitySynonym(String entityId, String label) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      String response = DataUploaderDTO.createGeoEntitySynonym(request, entityId, label);

      JSONObject object = new JSONObject(response);

      JSONControllerUtil.writeReponse(this.resp, object);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, request);
    }
  }

  @Override
  public void deleteGeoEntity(String entityId) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      DataUploaderDTO.deleteGeoEntity(request, entityId);

      JSONControllerUtil.writeReponse(this.resp);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, request);
    }
  }

  @Override
  public void deleteGeoEntitySynonym(String synonymId) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      DataUploaderDTO.deleteGeoEntitySynonym(request, synonymId);

      JSONControllerUtil.writeReponse(this.resp);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, request);
    }
  }

  @Override
  public void createClassifierSynonym(String classifierId, String label) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      String response = DataUploaderDTO.createClassifierSynonym(request, classifierId, label);

      JSONObject object = new JSONObject(response);

      JSONControllerUtil.writeReponse(this.resp, object);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, request);
    }
  }

  @Override
  public void deleteClassifierSynonym(String synonymId) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      DataUploaderDTO.deleteClassifierSynonym(request, synonymId);

      JSONControllerUtil.writeReponse(this.resp);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, request);
    }
  }

  @Override
  public void getClassifierSuggestions(String mdAttributeId, String text, Integer limit) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      JSONArray response = new JSONArray();

      ValueQueryDTO query = ClassifierDTO.getClassifierSuggestions(request, mdAttributeId, text, limit);
      List<ValueObjectDTO> results = query.getResultSet();

      for (ValueObjectDTO result : results)
      {
        JSONObject object = new JSONObject();
        object.put("label", result.getValue(ClassifierDTO.DISPLAYLABEL));
        object.put("value", result.getValue(ClassifierDTO.ID));

        response.put(object);
      }

      JSONControllerUtil.writeReponse(this.resp, response);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, request);
    }
  }
}
