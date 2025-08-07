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

import java.util.Date;

import org.commongeoregistry.adapter.dataaccess.ChildTreeNode;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.GeoObjectOverTime;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
public interface GeoObjectServiceIF
{
  public GeoObject getGeoObject(String sessionId, String uid, String geoObjectTypeCode, Date date);

  public GeoObject getGeoObjectByCode(String sessionId, String code, String typeCode, Date date);

  public GeoObject createGeoObject(String sessionId, String jGeoObj, Date startDate, Date endDate);

  public GeoObject updateGeoObject(String sessionId, String jGeoObj, Date startDate, Date endDate);

  public String getGeoObjectBounds(String sessionId, GeoObject geoObject);

  public String getGeoObjectBoundsAtDate(String sessionId, GeoObject geoObject, Date date);

  public GeoObjectOverTime getGeoObjectOverTimeByCode(String sessionId, String code, String typeCode);

  public GeoObjectOverTime updateGeoObjectOverTime(String sessionId, String jGeoObj);

  public GeoObjectOverTime createGeoObjectOverTime(String sessionId, String jGeoObj);

  public GeoObjectOverTime getGeoObjectOverTime(String sessionId, String id, String typeCode);

  public ChildTreeNode getChildGeoObjects(String sessionId, String parentCode, String parentGeoObjectTypeCode, String hierarchyCode, String[] childrenTypes, Boolean recursive, Date date);

  public ParentTreeNode getParentGeoObjects(String sessionId, String childCode, String childGeoObjectTypeCode, String hierarchyCode, String[] parentTypes, boolean recursive, boolean includeInherited, Date date);

  public GeoObject newGeoObjectInstance(String sessionId, String geoObjectTypeCode);

  public String newGeoObjectInstance2(String sessionId, String geoObjectTypeCode);

  public String newGeoObjectInstanceOverTime(String sessionId, String typeCode);

  public JsonObject getAll(String sessionId, String typeCode, String hierarchyCode, Date dUpdatedSince, Boolean includeLevel, String format, String externalSystemId, Integer pageNumber, Integer pageSize);

  public JsonObject doesGeoObjectExistAtRange(String sessionId, Date startDate, Date endDate, String typeCode, String code);

  public JsonObject hasDuplicateLabel(String sessionId, Date date, String typeCode, String code, String label);

  public ParentTreeNode addChild(String sessionId, String parentCode, String parentTypeCode, String childCode, String childTypeCode, String hierarchyCode, Date startDate, Date endDate);

  public void removeChild(String sessionId, String parentCode, String parentTypeCode, String childCode, String childTypeCode, String hierarchyCode, Date startDate, Date endDate);

  public JsonArray getBusinessObjects(String sessionId, String typeCode, String code, String edgeTypeCode, String direction);

}
