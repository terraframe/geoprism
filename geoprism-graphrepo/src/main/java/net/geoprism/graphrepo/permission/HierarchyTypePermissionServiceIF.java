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

import java.util.Set;

public interface HierarchyTypePermissionServiceIF
{

  public Set<RepoPermissionActionIF> getPermissions(String orgCode);
  
  public boolean canDelete(String orgCode);

  public void enforceCanDelete(String orgCode);

  public boolean canRead(String orgCode);

  public void enforceCanRead(String orgCode);

  public boolean canWrite(String orgCode);

  public void enforceCanWrite(String orgCode);

  public boolean canCreate(String orgCode);

  public void enforceCanCreate(String orgCode);
}
