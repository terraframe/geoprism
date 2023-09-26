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
package net.geoprism.graphrepo.permission;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import net.geoprism.graphrepo.permission.UserPermissionService.RepoPermissionAction;
import net.geoprism.registry.model.ServerGeoObjectType;

@Service
public class AllowAllGeoObjectTypePermissionService implements GeoObjectTypePermissionServiceIF
{

  @Override
  public boolean canRead(String orgCode, ServerGeoObjectType got, boolean isPrivate)
  {
    return true;
  }

  @Override
  public void enforceCanRead(String orgCode, ServerGeoObjectType got, boolean isPrivate)
  {
    
  }

  @Override
  public boolean canWrite(String orgCode, ServerGeoObjectType got, boolean isPrivate)
  {
    return true;
  }

  @Override
  public void enforceCanWrite(String orgCode, ServerGeoObjectType got, boolean isPrivate)
  {
    
  }

  @Override
  public boolean canCreate(String orgCode, ServerGeoObjectType got, boolean isPrivate)
  {
    return true;
  }

  @Override
  public void enforceCanCreate(String orgCode, boolean isPrivate)
  {
    
  }

  @Override
  public boolean canDelete(String orgCode, ServerGeoObjectType got, boolean isPrivate)
  {
    return true;
  }

  @Override
  public void enforceCanDelete(String orgCode, ServerGeoObjectType got, boolean isPrivate)
  {
    
  }

  @Override
  public void enforceActorHasPermission(String orgCode, ServerGeoObjectType got, boolean isPrivate, RepoPermissionActionIF action)
  {
    
  }

  @Override
  public Set<RepoPermissionActionIF> getPermissions(ServerGeoObjectType got)
  {
    return new HashSet<RepoPermissionActionIF>(Arrays.asList(RepoPermissionAction.values()));
  }

}
