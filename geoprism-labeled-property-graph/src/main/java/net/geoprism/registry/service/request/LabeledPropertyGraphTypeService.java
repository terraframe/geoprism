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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.Session;

import net.geoprism.graph.LabeledPropertyGraphType;
import net.geoprism.graph.LabeledPropertyGraphTypeEntry;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.graph.LabeledPropertyGraphUtil;
import net.geoprism.registry.service.business.LabeledPropertyGraphJsonExporterService;
import net.geoprism.registry.service.business.LabeledPropertyGraphTypeBusinessServiceIF;
import net.geoprism.registry.service.business.LabeledPropertyGraphTypeEntryBusinessServiceIF;
import net.geoprism.registry.service.business.LabeledPropertyGraphTypeVersionBusinessServiceIF;

@Service
public class LabeledPropertyGraphTypeService implements LabeledPropertyGraphTypeServiceIF
{
  @Autowired
  private LabeledPropertyGraphTypeBusinessServiceIF        typeService;

  @Autowired
  private LabeledPropertyGraphTypeEntryBusinessServiceIF   entryService;

  @Autowired
  private LabeledPropertyGraphTypeVersionBusinessServiceIF versionService;

  @Autowired
  private LabeledPropertyGraphJsonExporterService          exportService;

  @Request(RequestType.SESSION)
  public JsonArray getAll(String sessionId)
  {
    return typeService.list();
  }

  @Request(RequestType.SESSION)
  public JsonObject apply(String sessionId, JsonObject list)
  {
    LabeledPropertyGraphType type = typeService.apply(list);

    // Refresh the users session
    ( (Session) Session.getCurrentSession() ).reloadPermissions();

    // Auto publish the working versions of the lists
    List<LabeledPropertyGraphTypeVersion> versions = typeService.getVersions(type);
    for (LabeledPropertyGraphTypeVersion version : versions)
    {
      this.versionService.createPublishJob(version);
    }

    return type.toJSON();
  }

  @Request(RequestType.SESSION)
  public JsonObject createEntries(String sessionId, String oid)
  {
    LabeledPropertyGraphType type = typeService.get(oid);
    typeService.createEntries(type);

    // Refresh the users session
    ( (Session) Session.getCurrentSession() ).reloadPermissions();

    return typeService.toJSON(type, true);
  }

  @Request(RequestType.SESSION)
  public void remove(String sessionId, String oid)
  {
    try
    {
      LabeledPropertyGraphType type = typeService.get(oid);

      typeService.delete(type);
      // Refresh the users session
      ( (Session) Session.getCurrentSession() ).reloadPermissions();
    }
    catch (DataNotFoundException e)
    {
      e.printStackTrace();
      // Do nothing
    }
  }

  @Request(RequestType.SESSION)
  public JsonObject createVersion(String sessionId, String oid)
  {
    LabeledPropertyGraphTypeEntry entry = this.entryService.get(oid);

    // if (!type.isValid())
    // {
    // throw new InvalidMasterListException();
    // }

    LabeledPropertyGraphTypeVersion version = this.entryService.publish(entry);

    // Refresh the users session
    ( (Session) Session.getCurrentSession() ).reloadPermissions();

    return this.versionService.toJSON(version, false);
  }

  @Request(RequestType.SESSION)
  public JsonObject get(String sessionId, String oid)
  {
    return this.typeService.get(oid).toJSON();
  }

  @Request(RequestType.SESSION)
  public JsonObject getEntries(String sessionId, String oid)
  {
    LabeledPropertyGraphType type = this.typeService.get(oid);

    return typeService.toJSON(type, true);
  }

  @Request(RequestType.SESSION)
  public JsonArray getVersions(String sessionId, String oid)
  {
    LabeledPropertyGraphTypeEntry entry = this.entryService.get(oid);

    return this.entryService.getVersionJson(entry);
  }

  @Request(RequestType.SESSION)
  public JsonObject getEntry(String sessionId, String oid)
  {
    LabeledPropertyGraphTypeEntry entry = this.entryService.get(oid);

    return this.entryService.toJSON(entry);
  }

  @Request(RequestType.SESSION)
  public JsonObject getVersion(String sessionId, String oid, Boolean includeTableDefinitions)
  {
    LabeledPropertyGraphTypeVersion version = this.versionService.get(oid);
    return this.versionService.toJSON(version, includeTableDefinitions);
  }

  @Request(RequestType.SESSION)
  public void removeVersion(String sessionId, String oid)
  {
    try
    {
      LabeledPropertyGraphTypeVersion version = this.versionService.get(oid);

      this.versionService.remove(version);

      // Refresh the users session
      ( (Session) Session.getCurrentSession() ).reloadPermissions();
    }
    catch (DataNotFoundException e)
    {
      // Do nothing
    }
  }

  @Request(RequestType.SESSION)
  public JsonObject getData(String sessionId, String oid)
  {
    LabeledPropertyGraphTypeVersion version = this.versionService.get(oid);

    return this.exportService.export(version);
  }

  @Override
  @Request(RequestType.SESSION)
  public JsonArray getGeoObjects(String sessionId, String oid, Long skip, Integer blockSize)
  {
    LabeledPropertyGraphTypeVersion version = this.versionService.get(oid);

    return this.exportService.getGeoObjects(version, skip, blockSize);
  }

  @Override
  @Request(RequestType.SESSION)
  public JsonArray getEdges(String sessionId, String oid, Long skip, Integer blockSize)
  {
    LabeledPropertyGraphTypeVersion version = this.versionService.get(oid);

    return this.exportService.getEdges(version, skip, blockSize);
  }
}
