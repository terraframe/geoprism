/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism Registry(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service;

import java.util.Date;

import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.GeoObjectOverTime;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.geoprism.registry.conversion.ServerGeoObjectStrategyIF;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.query.ServerGeoObjectQuery;
import net.geoprism.registry.view.GeoObjectSplitView;

public interface ServerGeoObjectServiceIF
{
  public JsonObject getAll(String sessionId, String gotCode, String hierarchyCode, Date since, Boolean includeLevel, String format, String externalSystemId, Integer pageNumber, Integer pageSize);

  public ParentTreeNode addChild(String sessionId, String parentCode, String parentGeoObjectTypeCode, String childCode, String childGeoObjectTypeCode, String hierarchyCode, Date startDate, Date endDate);

  public void removeChild(String sessionId, String parentCode, String parentGeoObjectTypeCode, String childCode, String childGeoObjectTypeCode, String hierarchyCode, Date startDate, Date endDate);

  public ServerGeoObjectIF apply(GeoObject object, Date startDate, Date endDate, boolean isNew, boolean isImport);

  public ServerGeoObjectIF apply(GeoObject object, boolean isNew, boolean isImport);

  public ServerGeoObjectIF apply(GeoObjectOverTime goTime, boolean isNew, boolean isImport);

  public ServerGeoObjectIF split(GeoObjectSplitView view);

  public ServerGeoObjectIF newInstance(ServerGeoObjectType type);

  public ServerGeoObjectIF getGeoObject(GeoObject go);

  public ServerGeoObjectIF getGeoObject(GeoObjectOverTime timeGO);

  public ServerGeoObjectIF getGeoObjectByCode(String code, String typeCode);

  public ServerGeoObjectIF getGeoObjectByCode(String code, String typeCode, boolean throwException);

  public ServerGeoObjectIF getGeoObjectByCode(String code, ServerGeoObjectType type);

  public ServerGeoObjectIF getGeoObjectByCode(String code, ServerGeoObjectType type, boolean throwException);

  public ServerGeoObjectIF getGeoObject(String uid, String typeCode);

  public ServerGeoObjectStrategyIF getStrategy(ServerGeoObjectType type);

  public ServerGeoObjectIF build(ServerGeoObjectType type, String runwayId);

  public ServerGeoObjectIF build(ServerGeoObjectType type, Object dbObject);

  public ServerGeoObjectQuery createQuery(ServerGeoObjectType type, Date date);

  public boolean hasData(ServerHierarchyType serverHierarchyType, ServerGeoObjectType childType);

  public void removeAllEdges(ServerHierarchyType hierarchyType, ServerGeoObjectType childType);

  public JsonObject doesGeoObjectExistAtRange(String sessionId, Date startDate, Date endDate, String typeCode, String code);

  public JsonObject hasDuplicateLabel(String sessionId, Date date, String typeCode, String code, String label);
  
  public JsonArray getBusinessObjects(String sessionId, String typeCode, String code, String businessTypeCode);
}
