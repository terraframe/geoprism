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

import java.util.List;

import org.commongeoregistry.adapter.metadata.HierarchyNode;
import org.commongeoregistry.adapter.metadata.HierarchyType;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.rbac.RoleDAO;

import net.geoprism.registry.graph.HierarchicalRelationshipType;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.view.ServerParentTreeNodeOverTime;

@Component
public interface HierarchyTypeBusinessServiceIF
{
  public ServerHierarchyType createHierarchyType(HierarchyType hierarchyType);

  /*
   * Gets a ServerHierarchyType from the cache or returns null if doesn't exist
   */
  public ServerHierarchyType get(String code);

  /*
   * Creates a new non cached ServerHierarchyType from the database object
   */
  public ServerHierarchyType get(HierarchicalRelationshipType hierarchicalRelationship);

  /*
   * Creates a new non cached ServerHierarchyType from the database object
   */
  public ServerHierarchyType get(HierarchicalRelationshipType hierarchicalRelationship, boolean buildHierarchyNodes);

  public void refresh(ServerHierarchyType sht);

  public void update(ServerHierarchyType sht, HierarchyType hierarchyType);

  public void delete(ServerHierarchyType sht);

  public void addToHierarchy(ServerHierarchyType sht, ServerGeoObjectType parentType, ServerGeoObjectType childType);

  public void addToHierarchy(ServerHierarchyType sht, ServerGeoObjectType parentType, ServerGeoObjectType childType, boolean validate);

  public void removeChild(ServerHierarchyType sht, ServerGeoObjectType parentType, ServerGeoObjectType childType, boolean migrateChildren);

  public List<ServerGeoObjectType> getAllTypes(ServerHierarchyType sht);

  public List<ServerGeoObjectType> getAllTypes(ServerHierarchyType sht, boolean includeInherited);

  public HierarchyType toHierarchyType(ServerHierarchyType sht);

  public HierarchyType toHierarchyType(ServerHierarchyType sht, boolean includePrivateTypes);

  public List<ServerGeoObjectType> getChildren(ServerHierarchyType sht, ServerGeoObjectType parent);

  public boolean hasVisibleRoot(ServerHierarchyType sht);

  public List<HierarchyNode> getRootGeoObjectTypes(ServerHierarchyType sht);

  public List<HierarchyNode> getRootGeoObjectTypes(ServerHierarchyType sht, boolean includePrivateTypes);

  public void insertBetween(ServerHierarchyType sht, ServerGeoObjectType parentType, ServerGeoObjectType middleType, List<ServerGeoObjectType> youngestTypes);

  public List<ServerGeoObjectType> getDirectRootNodes(ServerHierarchyType sht);

  public void validateUniversalRelationship(ServerHierarchyType sht, ServerGeoObjectType childType, ServerGeoObjectType parentType);

  void filterHierarchiesFromPermissions(ServerGeoObjectType type, ServerParentTreeNodeOverTime pot);

  JsonArray getHierarchiesForGeoObjectOverTime(String code, String typeCode);

  JsonArray getHierarchiesForType(String code, Boolean includeTypes);

  void grantWritePermissionsOnMdTermRel(ComponentIF mdTermRelationship);

  void grantWritePermissionsOnMdTermRel(RoleDAO role, ComponentIF mdTermRelationship);

  void grantReadPermissionsOnMdTermRel(RoleDAO role, ComponentIF mdTermRelationship);

}
