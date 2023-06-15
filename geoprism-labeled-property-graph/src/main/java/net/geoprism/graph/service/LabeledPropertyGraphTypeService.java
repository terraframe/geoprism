/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism Registry(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.graph.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.Session;

import net.geoprism.graph.LabeledPropertyGraphJsonExporter;
import net.geoprism.graph.LabeledPropertyGraphType;
import net.geoprism.graph.LabeledPropertyGraphTypeEntry;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.registry.GeoRegistryUtil;

@Component
public class LabeledPropertyGraphTypeService implements LabeledPropertyGraphTypeServiceIF
{
  @Request(RequestType.SESSION)
  public JsonArray getAll(String sessionId)
  {
    return LabeledPropertyGraphType.list();
  }

  @Request(RequestType.SESSION)
  public JsonObject apply(String sessionId, JsonObject list)
  {
    LabeledPropertyGraphType mList = LabeledPropertyGraphType.apply(list);

    // Refresh the users session
    ( (Session) Session.getCurrentSession() ).reloadPermissions();

    // Auto publish the working versions of the lists
    List<LabeledPropertyGraphTypeVersion> versions = mList.getVersions();
    for (LabeledPropertyGraphTypeVersion version : versions)
    {
      LabeledPropertyGraphServiceIF.getInstance().createPublishJob(version);
    }

    return mList.toJSON();
  }

  @Request(RequestType.SESSION)
  public JsonObject createEntries(String sessionId, String oid)
  {
    LabeledPropertyGraphType mList = LabeledPropertyGraphType.get(oid);
    mList.createEntries();

    // Refresh the users session
    ( (Session) Session.getCurrentSession() ).reloadPermissions();

    return mList.toJSON(true);
  }

  @Request(RequestType.SESSION)
  public void remove(String sessionId, String oid)
  {
    try
    {
      LabeledPropertyGraphType listType = LabeledPropertyGraphType.get(oid);

      listType.delete();
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
    LabeledPropertyGraphTypeEntry entry = LabeledPropertyGraphTypeEntry.get(oid);

    // if (!listType.isValid())
    // {
    // throw new InvalidMasterListException();
    // }

    String version = entry.publish();

    // Refresh the users session
    ( (Session) Session.getCurrentSession() ).reloadPermissions();

    return JsonParser.parseString(version).getAsJsonObject();
  }

  @Request(RequestType.SESSION)
  public JsonObject get(String sessionId, String oid)
  {
    return LabeledPropertyGraphType.get(oid).toJSON();
  }

  @Request(RequestType.SESSION)
  public JsonObject getEntries(String sessionId, String oid)
  {
    LabeledPropertyGraphType listType = LabeledPropertyGraphType.get(oid);

    return listType.toJSON(true);
  }

  @Request(RequestType.SESSION)
  public JsonArray getVersions(String sessionId, String oid)
  {
    LabeledPropertyGraphTypeEntry listType = LabeledPropertyGraphTypeEntry.get(oid);

    return listType.getVersionJson();
  }

  @Request(RequestType.SESSION)
  public JsonObject getEntry(String sessionId, String oid)
  {
    return LabeledPropertyGraphTypeEntry.get(oid).toJSON();
  }

  @Request(RequestType.SESSION)
  public JsonObject getVersion(String sessionId, String oid, Boolean includeTableDefinitions)
  {
    return LabeledPropertyGraphTypeVersion.get(oid).toJSON(includeTableDefinitions);
  }

  @Request(RequestType.SESSION)
  public void removeVersion(String sessionId, String oid)
  {
    try
    {
      LabeledPropertyGraphTypeVersion version = LabeledPropertyGraphTypeVersion.get(oid);

      version.remove();

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
    LabeledPropertyGraphTypeVersion version = LabeledPropertyGraphTypeVersion.get(oid);

    LabeledPropertyGraphJsonExporter exporter = new LabeledPropertyGraphJsonExporter(version);

    return exporter.export();
  }
}
