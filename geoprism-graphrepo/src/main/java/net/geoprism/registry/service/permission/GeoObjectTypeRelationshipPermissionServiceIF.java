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
package net.geoprism.registry.service.permission;

import java.util.Collection;

import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;

public interface GeoObjectTypeRelationshipPermissionServiceIF
{
  public boolean canAddChild(ServerHierarchyType ht, ServerGeoObjectType parentType, ServerGeoObjectType childType);

  public void enforceCanAddChild(ServerHierarchyType ht, ServerGeoObjectType parentType, ServerGeoObjectType childType);

  public boolean canRemoveChild(ServerHierarchyType ht, ServerGeoObjectType parentType, ServerGeoObjectType childType);

  public void enforceCanRemoveChild(ServerHierarchyType ht, ServerGeoObjectType parentType, ServerGeoObjectType childType);

  public Collection<? extends RepoPermissionActionIF> getPermissions(ServerGeoObjectType serverGeoObjectType);

}
