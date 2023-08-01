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
package net.geoprism.graph.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface LabeledPropertyGraphTypeServiceIF
{
  public JsonArray getAll(String sessionId);

  public JsonObject apply(String sessionId, JsonObject list);

  public JsonObject createEntries(String sessionId, String oid);

  public void remove(String sessionId, String oid);

  public JsonObject createVersion(String sessionId, String oid);

  public JsonObject get(String sessionId, String oid);

  public JsonObject getEntries(String sessionId, String oid);

  public JsonArray getVersions(String sessionId, String oid);

  public JsonObject getEntry(String sessionId, String oid);

  public JsonObject getVersion(String sessionId, String oid, Boolean includeTableDefinitions);

  public void removeVersion(String sessionId, String oid);

  public JsonObject getData(String sessionId, String oid);

  public JsonArray getGeoObjects(String sessionId, String oid, Long skip, Integer blockSize);

  public JsonArray getEdges(String sessionId, String oid, Long skip, Integer blockSize);

}