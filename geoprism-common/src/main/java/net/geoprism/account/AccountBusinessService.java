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
package net.geoprism.account;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.GeoprismUser;
import net.geoprism.rbac.RoleBusinessService;
import net.geoprism.rbac.RoleBusinessServiceIF;

@Component
public class AccountBusinessService implements AccountBusinessServiceIF
{
  // TODO : Do not autowire components here. This service is directly instantiated in IDM
  
  public RoleBusinessServiceIF roleService = new RoleBusinessService();
  
  @Override
  @Transaction
  public void applyUserWithRoles(GeoprismUser user, Set<String> roleIds)
  {
    user.apply();

    roleService.assignRoles(user, roleIds);
  }
}
