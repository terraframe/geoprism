/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.graph.LabeledPropertyGraphSynchronization;
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
}
