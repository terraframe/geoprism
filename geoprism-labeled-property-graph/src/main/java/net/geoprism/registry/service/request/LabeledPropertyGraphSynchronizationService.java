/**
 * Copyright 2020 The Department of Interior
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package net.geoprism.registry.service.request;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.graph.LabeledPropertyGraphSynchronization;
import net.geoprism.registry.LPGTileCache;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.service.business.LabeledPropertyGraphSynchronizationBusinessService;

@Service
public class LabeledPropertyGraphSynchronizationService implements LabeledPropertyGraphSynchronizationServiceIF
{
  @Autowired
  private LabeledPropertyGraphSynchronizationBusinessService synchronizationService;

  @Override
  @Request(RequestType.SESSION)
  public JsonArray getAll(String sessionId)
  {
    return this.synchronizationService.getAll();
  }

  @Override
  @Request(RequestType.SESSION)
  public JsonArray getForOrganization(String sessionId, String orginzationCode)
  {
    ServerOrganization organization = ServerOrganization.getByCode(orginzationCode);

    return this.synchronizationService.getForOrganization(organization);
  }

  @Override
  @Request(RequestType.SESSION)
  public JsonObject apply(String sessionId, JsonObject json)
  {
    LabeledPropertyGraphSynchronization synchronization = this.synchronizationService.apply(json);

    return synchronization.toJSON();
  }

  @Override
  @Request(RequestType.SESSION)
  public void remove(String sessionId, String oid)
  {
    LabeledPropertyGraphSynchronization synchronization = this.synchronizationService.get(oid);

    if (synchronization != null)
    {
      this.synchronizationService.delete(synchronization);
    }
  }


  @Override
  @Request(RequestType.SESSION)
  public JsonObject get(String sessionId, String oid)
  {
    return this.synchronizationService.get(oid).toJSON();
  }

  @Override
  @Request(RequestType.SESSION)
  public JsonObject newInstance(String sessionId)
  {
    return new LabeledPropertyGraphSynchronization().toJSON();
  }

  @Override
  @Request(RequestType.SESSION)
  public JsonObject page(String sessionId, JsonObject criteria)
  {
    return this.synchronizationService.page(criteria).toJSON();
  }

  @Override
  @Request(RequestType.SESSION)
  public JsonObject updateRemoteVersion(String sessionId, String oid, String versionId, Integer versionNumber)
  {
    LabeledPropertyGraphSynchronization synchronization = this.synchronizationService.get(oid);

    this.synchronizationService.updateRemoteVersion(synchronization, versionId, versionNumber);

    return synchronization.toJSON();
  }
  
  @Override
  @Request(RequestType.SESSION)
  public void createTiles(String sessionId, String oid)
  {
    LabeledPropertyGraphSynchronization synchronization = this.synchronizationService.get(oid);

    this.synchronizationService.createTiles(synchronization);
  }

  @Override
  @Request(RequestType.SESSION)
  public InputStream getTile(String sessionId, JSONObject object)
  {
    try
    {
      byte[] bytes = LPGTileCache.getTile(object);

      if (bytes != null)
      {
        return new ByteArrayInputStream(bytes);
      }

      return new ByteArrayInputStream(new byte[] {});
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }



}
