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
package net.geoprism.registry.service.business;

import java.util.Set;

import org.springframework.stereotype.Component;

import net.geoprism.GeoprismUser;

@Component
public interface AccountBusinessServiceIF
{
  /**
   * Applys the user, and then (if roleIds is not null) assigns all provided roles to the user.
   * All roles that the user has which are not part of the roleIds set will be removed.
   * 
   * @param user
   * @param roleIds
   */
  public void applyUserWithRoles(GeoprismUser user, Set<String> roleIds);
}
