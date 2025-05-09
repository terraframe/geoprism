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
import java.util.Set;

import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.springframework.stereotype.Component;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdClass;

import net.geoprism.registry.graph.HierarchicalRelationshipType;
import net.geoprism.registry.graph.InheritedHierarchyAnnotation;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.permission.PermissionContext;

@Component
public interface GeoObjectTypeBusinessServiceIF
{
  public List<ServerGeoObjectType> getSubtypes(ServerGeoObjectType sgot);

  public Set<ServerHierarchyType> getHierarchiesOfSubTypes(ServerGeoObjectType sgot);

  /**
   * @param sType
   *          Hierarchy Type
   * 
   * @return If this geo object type is the direct (non-inherited) root of the
   *         given hierarchy
   */
  public boolean isRoot(ServerGeoObjectType sgot, ServerHierarchyType sType);

  public InheritedHierarchyAnnotation setInheritedHierarchy(ServerGeoObjectType sgot, ServerHierarchyType forHierarchy, ServerHierarchyType inheritedHierarchy);

  public void removeInheritedHierarchy(ServerHierarchyType forHierarchy);

  public List<ServerHierarchyType> getHierarchies(ServerGeoObjectType sgot);

  public List<ServerHierarchyType> getHierarchies(ServerGeoObjectType sgot, boolean includeFromSuperType);

  public ServerHierarchyType getInheritedHierarchy(ServerGeoObjectType sgot, ServerHierarchyType hierarchy);

  public ServerHierarchyType getInheritedHierarchy(ServerGeoObjectType sgot, HierarchicalRelationshipType hierarchicalRelationship);

  /**
   * Returns all ancestors of a GeoObjectType
   * 
   * @param hierarchyType
   *          The Hierarchy code
   * @param includeInheritedTypes
   *          TODO
   * @param GeoObjectType
   *          child
   * 
   * @return
   */
  public List<ServerGeoObjectType> getTypeAncestors(ServerGeoObjectType sgot, ServerHierarchyType hierarchyType, Boolean includeInheritedTypes);

  /**
   * Finds the actual hierarchy used for the parent type if the parent type is
   * inherited from a different hierarchy
   * 
   * @param hierarchyType
   * @param parent
   * @return
   */
  public ServerHierarchyType findHierarchy(ServerGeoObjectType sgot, ServerHierarchyType hierarchyType, ServerGeoObjectType parent);

  public List<ServerGeoObjectType> getChildren(ServerGeoObjectType sgot, ServerHierarchyType hierarchy);

  public ServerGeoObjectType create(String json);

  public ServerGeoObjectType create(GeoObjectType geoObjectType);

  public List<GeoObjectType> getAncestors(String code, String hierarchyCode, Boolean includeInheritedTypes, Boolean includeChild);

  public void deleteGeoObjectType(String code);

  public <T extends AttributeType> T createAttributeType(String geoObjectTypeCode, String attributeTypeJSON);

  public <T extends AttributeType> T createAttributeType(ServerGeoObjectType type, String attributeTypeJSON);

  public <T extends AttributeType> T createAttributeType(ServerGeoObjectType type, AttributeType attributeType);

  public <T extends AttributeType> T updateAttributeType(String geoObjectTypeCode, String attributeTypeJSON);

  public void deleteAttributeType(String gtId, String attributeName);

  public void deleteAttributeType(ServerGeoObjectType type, String attributeName);

  public GeoObjectType updateGeoObjectType(String gtJSON);

  public List<GeoObjectType> getGeoObjectTypes(String[] codes, PermissionContext context);

  <T extends AttributeType> T updateAttributeType(ServerGeoObjectType serverType, String attributeTypeJSON);

  <T extends AttributeType> T updateAttributeType(ServerGeoObjectType serverType, AttributeType dto);

  /**
   * Returns the {link MdAttributeConcreteDAOIF} for the given
   * {@link AttributeType} defined on the given {@link MdBusiness} or null no
   * such attribute is defined.
   * 
   * @param attributeName
   * 
   * @return
   */
  MdAttributeConcreteDAOIF getMdAttribute(MdClass mdClass, String attributeName);

}
