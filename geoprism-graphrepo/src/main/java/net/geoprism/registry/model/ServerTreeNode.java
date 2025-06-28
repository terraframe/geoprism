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
package net.geoprism.registry.model;

import java.util.Date;

import org.commongeoregistry.adapter.dataaccess.TreeNode;

public abstract class ServerTreeNode extends ServerGraphNode
{
  public ServerTreeNode(ServerGeoObjectIF geoObject, ServerHierarchyType hierarchyType, Date startDate, Date endDate, String oid, String uid)
  {
    super(geoObject, hierarchyType, startDate, endDate, oid, uid);
  }

  public ServerHierarchyType getHierarchyType()
  {
    return (ServerHierarchyType) super.getHierarchyType();
  }


  public abstract TreeNode toNode(boolean enforcePermissions);

}
