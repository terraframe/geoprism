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

import com.runwaysdk.business.rbac.SingleActorDAOIF;

@Service
public class AllowAllOrganizationPermissionService implements OrganizationPermissionServiceIF
{

  @Override
  public void enforceActorCanCreate()
  {
    
  }

  @Override
  public void enforceActorCanUpdate()
  {
    
  }

  @Override
  public boolean canActorCreate()
  {
    return true;
  }

  @Override
  public boolean canActorUpdate()
  {
    return true;
  }

  @Override
  public void enforceActorCanDelete()
  {
    
  }

  @Override
  public boolean canActorDelete()
  {
    return true;
  }

  @Override
  public boolean canActorRead(String orgCode)
  {
    return true;
  }

  @Override
  public void enforceActorCanRead(SingleActorDAOIF actor, String orgCode)
  {
    
  }
}
