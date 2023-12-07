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

import org.commongeoregistry.adapter.metadata.HierarchyType;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;

import net.geoprism.registry.permission.PermissionContext;

@Component
public interface HierarchyTypeServiceIF
{
  public HierarchyType addToHierarchy(String sessionId, String hierarchyCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode);

  public HierarchyType removeFromHierarchy(String sessionId, String hierarchyCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode, boolean b);

  public HierarchyType insertBetweenTypes(String sessionId, String hierarchyCode, String parentGeoObjectTypeCode, String middleGeoObjectTypeCode, String youngestGeoObjectTypeCode);

  public HierarchyType setInheritedHierarchy(String sessionId, String hierarchyTypeCode, String inheritedHierarchyTypeCode, String geoObjectTypeCode);

  public HierarchyType removeInheritedHierarchy(String sessionId, String hierarchyTypeCode, String geoObjectTypeCode);

  public HierarchyType[] getHierarchyTypes(String sessionId, String[] aTypes, PermissionContext pContext);

  public HierarchyType createHierarchyType(String sessionId, String string);

  public HierarchyType updateHierarchyType(String sessionId, String string);

  public void deleteHierarchyType(String sessionId, String code);
  
  public JsonArray getHierarchiesForType(String sessionId, String code, Boolean includeTypes);
  
  public JsonArray getHierarchiesForSubtypes(String sessionId, String code);
  
  public JsonArray getHierarchiesForGeoObjectOverTime(String sessionId, String code, String typeCode);
}
