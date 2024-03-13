/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service.request;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.HierarchyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.registry.Organization;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.permission.PermissionContext;
import net.geoprism.registry.service.business.GeoObjectBusinessServiceIF;
import net.geoprism.registry.service.business.GeoObjectTypeBusinessServiceIF;
import net.geoprism.registry.service.business.HierarchyTypeBusinessServiceIF;
import net.geoprism.registry.service.permission.GeoObjectRelationshipPermissionServiceIF;
import net.geoprism.registry.service.permission.HierarchyTypePermissionServiceIF;
import net.geoprism.registry.view.ServerParentTreeNodeOverTime;

@Service
public class HierarchyTypeService implements HierarchyTypeServiceIF
{
  @Autowired
  protected HierarchyTypeBusinessServiceIF service;

  @Autowired
  protected GeoObjectTypeBusinessServiceIF gotServ;

  @Autowired
  protected GeoObjectBusinessServiceIF     goServ;

  @Request(RequestType.SESSION)
  public JsonArray getHierarchiesForType(String sessionId, String code, Boolean includeTypes)
  {
    return this.service.getHierarchiesForType(code, includeTypes);
  }

  @Request(RequestType.SESSION)
  public JsonArray getHierarchiesForSubtypes(String sessionId, String code)
  {
    ServerGeoObjectType geoObjectType = ServerGeoObjectType.get(code);
    Set<ServerHierarchyType> hierarchyTypes = gotServ.getHierarchiesOfSubTypes(geoObjectType);

    JsonArray hierarchies = new JsonArray();

    HierarchyTypePermissionServiceIF pService = ServiceFactory.getHierarchyPermissionService();

    for (ServerHierarchyType sHT : hierarchyTypes)
    {
      if (pService.canWrite(sHT.getOrganizationCode()))
      {
        JsonObject object = new JsonObject();
        object.addProperty("code", sHT.getCode());
        object.addProperty("label", sHT.getLabel().getValue());

        hierarchies.add(object);
      }
    }

    return hierarchies;
  }

  @Request(RequestType.SESSION)
  public JsonArray getHierarchiesForGeoObjectOverTime(String sessionId, String code, String typeCode)
  {
    return this.service.getHierarchiesForGeoObjectOverTime(code, typeCode);
  }

  /**
   * Returns the {@link HierarchyType}s with the given codes or all
   * {@link HierarchyType}s if no codes are provided.
   * 
   * @param sessionId
   * @param codes
   *          codes of the {@link HierarchyType}s.
   * @param context
   * @return the {@link HierarchyType}s with the given codes or all
   *         {@link HierarchyType}s if no codes are provided.
   */
  @Request(RequestType.SESSION)
  public HierarchyType[] getHierarchyTypes(String sessionId, String[] codes, PermissionContext context)
  {
    final HierarchyTypePermissionServiceIF hierPermServ = ServiceFactory.getHierarchyPermissionService();

    List<ServerHierarchyType> types = ServerHierarchyType.getAll();

    if (codes != null && codes.length > 0)
    {
      final List<String> list = Arrays.asList(codes);

      types = types.stream().filter(type -> list.contains(type.getCode())).collect(Collectors.toList());
    }

    // Filter out what they're not allowed to see
    List<HierarchyType> hierarchies = types.stream().filter(type -> {
      Organization org = Organization.getByCode(type.getOrganizationCode());

      return ! ( ( context.equals(PermissionContext.READ) && !hierPermServ.canRead(org.getCode()) ) || ( context.equals(PermissionContext.WRITE) && !hierPermServ.canWrite(org.getCode()) ) );
    }).filter(type -> {
      return service.hasVisibleRoot(type);
    }).map(type -> {
      return service.toHierarchyType(type, false);
    }).collect(Collectors.toList());

    return hierarchies.toArray(new HierarchyType[hierarchies.size()]);
  }

  /**
   * Create the {@link HierarchyType} from the given JSON.
   * 
   * @param sessionId
   * @param htJSON
   *          JSON of the {@link HierarchyType} to be created.
   */
  @Request(RequestType.SESSION)
  public HierarchyType createHierarchyType(String sessionId, String htJSON)
  {
    RegistryAdapter adapter = ServiceFactory.getAdapter();

    HierarchyType hierarchyType = HierarchyType.fromJSON(htJSON, adapter);

    ServerHierarchyType sType = service.createHierarchyType(hierarchyType);


    return service.toHierarchyType(sType);
  }

  /**
   * Updates the given {@link HierarchyType} represented as JSON.
   * 
   * @param sessionId
   * @param gtJSON
   *          JSON of the {@link HierarchyType} to be updated.
   */
  @Request(RequestType.SESSION)
  public HierarchyType updateHierarchyType(String sessionId, String htJSON)
  {
    HierarchyType dto = HierarchyType.fromJSON(htJSON, ServiceFactory.getAdapter());
    ServerHierarchyType type = ServerHierarchyType.get(dto);

    ServiceFactory.getHierarchyPermissionService().enforceCanWrite(type.getOrganization().getCode());

    service.update(type, dto);

    return service.toHierarchyType(type);
  }

  /**
   * Deletes the {@link HierarchyType} with the given code.
   * 
   * @param sessionId
   * @param code
   *          code of the {@link HierarchyType} to delete.
   */
  @Request(RequestType.SESSION)
  public void deleteHierarchyType(String sessionId, String code)
  {
    ServerHierarchyType type = ServerHierarchyType.get(code);

    ServiceFactory.getHierarchyPermissionService().enforceCanDelete(type.getOrganization().getCode());

    service.delete(type);
  }

  /**
   * Adds the {@link GeoObjectType} with the given child code to the parent
   * {@link GeoObjectType} with the given code for the given
   * {@link HierarchyType} code.
   * 
   * @param sessionId
   * @param hierarchyTypeCode
   *          code of the {@link HierarchyType} the child is being added to.
   * @param parentGeoObjectTypeCode
   *          parent {@link GeoObjectType}.
   * @param childGeoObjectTypeCode
   *          child {@link GeoObjectType}.
   */
  @Request(RequestType.SESSION)
  public HierarchyType addToHierarchy(String sessionId, String hierarchyTypeCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode)
  {
    ServerHierarchyType type = ServerHierarchyType.get(hierarchyTypeCode);
    ServerGeoObjectType parentType = ServerGeoObjectType.get(parentGeoObjectTypeCode);
    ServerGeoObjectType childType = ServerGeoObjectType.get(childGeoObjectTypeCode);

    ServiceFactory.getGeoObjectTypeRelationshipPermissionService().enforceCanAddChild(type, parentType, childType);

    service.addToHierarchy(type, parentType, childType);

    return service.toHierarchyType(type);
  }

  /**
   * Inserts the {@link GeoObjectType} 'middleGeoObjectTypeCode' into the
   * hierarchy as the child of 'parentGeoObjectTypeCode' and the new parent for
   * 'youngestGeoObjectTypeCode'. If an existing parent/child relationship
   * already exists between 'youngestGeoObjectTypeCode' and
   * 'parentgeoObjectTypeCode', it will first be removed.
   * youngestGeoObjectTypeCode can also be an array (comma separated list).
   * 
   * @param sessionId
   * @param hierarchyTypeCode
   *          code of the {@link HierarchyType}
   * @param parentGeoObjectTypeCode
   *          parent {@link GeoObjectType}.
   * @param middleGeoObjectTypeCode
   *          middle child {@link GeoObjectType} after this method returns
   * @param youngestGeoObjectTypeCode
   *          youngest child {@link GeoObjectType} after this method returns
   */
  @Request(RequestType.SESSION)
  public HierarchyType insertBetweenTypes(String sessionId, String hierarchyTypeCode, String parentGeoObjectTypeCode, String middleGeoObjectTypeCode, String youngestGeoObjectTypeCode)
  {
    ServerHierarchyType type = ServerHierarchyType.get(hierarchyTypeCode);
    ServerGeoObjectType parentType = ServerGeoObjectType.get(parentGeoObjectTypeCode);
    ServerGeoObjectType middleType = ServerGeoObjectType.get(middleGeoObjectTypeCode);

    List<ServerGeoObjectType> youngestTypes = Arrays.asList(youngestGeoObjectTypeCode.split(",")).stream().map(code -> ServerGeoObjectType.get(code.trim())).collect(Collectors.toList());

    ServiceFactory.getGeoObjectTypeRelationshipPermissionService().enforceCanAddChild(type, parentType, middleType);

    service.insertBetween(type, parentType, middleType, youngestTypes);

    return service.toHierarchyType(type);
  }

  /**
   * Modifies a hierarchy to inherit from another hierarchy at the given
   * GeoObjectType
   * 
   * @param sessionId
   * @param hierarchyTypeCode
   *          code of the {@link HierarchyType} being modified.
   * @param inheritedHierarchyTypeCode
   *          code of the {@link HierarchyType} being inherited.
   * @param geoObjectTypeCode
   *          code of the root {@link GeoObjectType}.
   */
  @Request(RequestType.SESSION)
  public HierarchyType setInheritedHierarchy(String sessionId, String hierarchyTypeCode, String inheritedHierarchyTypeCode, String geoObjectTypeCode)
  {
    ServerHierarchyType forHierarchy = ServerHierarchyType.get(hierarchyTypeCode);
    ServerHierarchyType inheritedHierarchy = ServerHierarchyType.get(inheritedHierarchyTypeCode);
    ServerGeoObjectType childType = ServerGeoObjectType.get(geoObjectTypeCode);

    ServiceFactory.getGeoObjectTypeRelationshipPermissionService().enforceCanAddChild(forHierarchy, null, childType);

    ServerGeoObjectType type = ServerGeoObjectType.get(geoObjectTypeCode);

    gotServ.setInheritedHierarchy(type, forHierarchy, inheritedHierarchy);
    service.refresh(forHierarchy);

    return service.toHierarchyType(forHierarchy);
  }

  /**
   * Modifies a hierarchy to remove inheritance from another hierarchy for the
   * given root
   * 
   * @param sessionId
   * @param hierarchyTypeCode
   *          code of the {@link HierarchyType} being modified.
   * @param geoObjectTypeCode
   *          code of the root {@link GeoObjectType}.
   */
  @Request(RequestType.SESSION)
  public HierarchyType removeInheritedHierarchy(String sessionId, String hierarchyTypeCode, String geoObjectTypeCode)
  {
    ServerHierarchyType forHierarchy = ServerHierarchyType.get(hierarchyTypeCode);
    ServerGeoObjectType childType = ServerGeoObjectType.get(geoObjectTypeCode);

    ServiceFactory.getGeoObjectTypeRelationshipPermissionService().enforceCanAddChild(forHierarchy, null, childType);

    gotServ.removeInheritedHierarchy(forHierarchy);

    return service.toHierarchyType(forHierarchy);
  }

  /**
   * Removes the {@link GeoObjectType} with the given child code from the parent
   * {@link GeoObjectType} with the given code for the given
   * {@link HierarchyType} code.
   * 
   * @param sessionId
   * @param hierarchyCode
   *          code of the {@link HierarchyType} the child is being added to.
   * @param parentGeoObjectTypeCode
   *          parent {@link GeoObjectType}.
   * @param childGeoObjectTypeCode
   *          child {@link GeoObjectType}.
   */
  @Request(RequestType.SESSION)
  public HierarchyType removeFromHierarchy(String sessionId, String hierarchyTypeCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode, boolean migrateChildren)
  {
    ServerHierarchyType type = ServerHierarchyType.get(hierarchyTypeCode);
    ServerGeoObjectType parentType = ServerGeoObjectType.get(parentGeoObjectTypeCode);
    ServerGeoObjectType childType = ServerGeoObjectType.get(childGeoObjectTypeCode);

    ServiceFactory.getGeoObjectTypeRelationshipPermissionService().enforceCanRemoveChild(type, parentType, childType);

    service.removeChild(type, parentType, childType, migrateChildren);

    return service.toHierarchyType(type);
  }
}
