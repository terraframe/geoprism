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

import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.springframework.stereotype.Component;

import net.geoprism.registry.permission.PermissionContext;

@Component
public interface GeoObjectTypeServiceIF
{
  public List<GeoObjectType> getAncestors(String sessionId, String code, String hierarchyCode, Boolean includeInheritedTypes, Boolean includeChild);

  public void deleteGeoObjectType(String sessionId, String code);

  public AttributeType createAttributeType(String sessionId, String geoObjectTypeCode, String attributeTypeJSON);

  public AttributeType updateAttributeType(String sessionId, String geoObjectTypeCode, String attributeTypeJSON);

  public void deleteAttributeType(String sessionId, String gtId, String attributeName);

  public GeoObjectType updateGeoObjectType(String sessionId, String gtJSON);

  public GeoObjectType createGeoObjectType(String sessionId, String gtJSON);

  public GeoObjectType[] getGeoObjectTypes(String sessionId, String[] codes, PermissionContext context);

}
