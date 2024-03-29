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

import org.springframework.stereotype.Service;

import net.geoprism.registry.model.ServerGeoObjectType;

@Service
public class AllowAllGeoObjectRelationshipPermissionService implements GeoObjectRelationshipPermissionServiceIF
{

  @Override
  public boolean canAddChild(String orgCode, ServerGeoObjectType parentType, ServerGeoObjectType childType)
  {
    return true;
  }

  @Override
  public void enforceCanAddChild(String orgCode, ServerGeoObjectType parentType, ServerGeoObjectType childType)
  {
    
  }

  @Override
  public boolean canViewChild(String orgCode, ServerGeoObjectType parentType, ServerGeoObjectType childType)
  {
    return true;
  }

  @Override
  public void enforceCanViewChild(String orgCode, ServerGeoObjectType parentType, ServerGeoObjectType childType)
  {
    
  }

  @Override
  public boolean canAddChildCR(String orgCode, ServerGeoObjectType parentType, ServerGeoObjectType childType)
  {
    return true;
  }

  @Override
  public void enforceCanAddChildCR(String orgCode, ServerGeoObjectType parentType, ServerGeoObjectType childType)
  {
  }

  @Override
  public boolean canRemoveChild(String orgCode, ServerGeoObjectType parentType, ServerGeoObjectType childType)
  {
    return true;
  }

  @Override
  public void enforceCanRemoveChild(String orgCode, ServerGeoObjectType parentType, ServerGeoObjectType childType)
  {
    
  }

  @Override
  public boolean canRemoveChildCR(String orgCode, ServerGeoObjectType parentType, ServerGeoObjectType childType)
  {
    return true;
  }

  @Override
  public void enforceCanRemoveChildCR(String orgCode, ServerGeoObjectType parentType, ServerGeoObjectType childType)
  {
    
  }

}
