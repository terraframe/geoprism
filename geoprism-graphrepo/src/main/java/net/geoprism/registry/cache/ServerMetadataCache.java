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
package net.geoprism.registry.cache;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.dataaccess.GeoObject;

import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;

/**
 * This is a singleton instance that caches {@link ServerGeoObjectType} objects
 * for creating {@link GeoObject}s and that caches {@link ServerHierarchyType}.
 * 
 * @author nathan
 *
 */
public class ServerMetadataCache extends ServerOrganizationCache
{
  /**
   * 
   */
  private static final long                serialVersionUID = -8829469298178067536L;

  private Map<String, ServerGeoObjectType> geoGeoObjectTypeCodeMap;

  private Map<String, ServerGeoObjectType> geoGeoObjectTypeOidMap;
  
  private Map<String, ServerGeoObjectType> geoGeoObjectTypeMdVertexOidMap;

  private Map<String, ServerHierarchyType> hierarchyTypeMap;

  public ServerMetadataCache(RegistryAdapter adapter)
  {
    super(adapter);
  }

  /**
   * Clears the metadata cache.
   */
  public void rebuild()
  {
    super.rebuild();

    this.geoGeoObjectTypeCodeMap = new ConcurrentHashMap<String, ServerGeoObjectType>();
    this.geoGeoObjectTypeOidMap = new ConcurrentHashMap<String, ServerGeoObjectType>();
    this.geoGeoObjectTypeMdVertexOidMap = new ConcurrentHashMap<String, ServerGeoObjectType>();
    this.hierarchyTypeMap = new ConcurrentHashMap<String, ServerHierarchyType>();

    getAdapter().getMetadataCache().rebuild();
  }

  public void addGeoObjectType(ServerGeoObjectType geoObjectType)
  {
    String code = geoObjectType.getCode();
    String oid = geoObjectType.getOid();
    String mdVertexOid = geoObjectType.getMdVertex().getOid();
    
    this.geoGeoObjectTypeCodeMap.put(code, geoObjectType);
    this.geoGeoObjectTypeOidMap.put(oid, geoObjectType);
    this.geoGeoObjectTypeMdVertexOidMap.put(mdVertexOid, geoObjectType);

    getAdapter().getMetadataCache().addGeoObjectType(geoObjectType.toDTO());
  }

  public Optional<ServerGeoObjectType> getGeoObjectType(String code)
  {
    return Optional.ofNullable(geoGeoObjectTypeCodeMap.get(code));
  }

  public Optional<ServerGeoObjectType> getGeoObjectTypeByOid(String oid)
  {
    return Optional.ofNullable(geoGeoObjectTypeOidMap.get(oid));
  }
  
  public Optional<ServerGeoObjectType> getGeoObjectTypeByMdVertexOid(String mdVertexOid)
  {
    return Optional.ofNullable(geoGeoObjectTypeMdVertexOidMap.get(mdVertexOid));
  }

  public void removeGeoObjectType(ServerGeoObjectType type)
  {
    removeGeoObjectType(type.getCode(), type.getOid());
  }

  private void removeGeoObjectType(String code, String oid)
  {
    this.geoGeoObjectTypeCodeMap.remove(code);
    this.geoGeoObjectTypeOidMap.remove(oid);
    this.geoGeoObjectTypeMdVertexOidMap.remove(oid);

    getAdapter().getMetadataCache().removeGeoObjectType(code);
  }

  public void addHierarchyType(ServerHierarchyType hierarchyType)
  {
    this.hierarchyTypeMap.put(hierarchyType.getCode(), hierarchyType);

    getAdapter().getMetadataCache().addHierarchyType(hierarchyType.toDTO());
  }

  public Optional<ServerHierarchyType> getHierachyType(String code)
  {
    return Optional.ofNullable(this.hierarchyTypeMap.get(code));
  }

  public void removeHierarchyType(String code)
  {
    this.hierarchyTypeMap.remove(code);

    getAdapter().getMetadataCache().removeHierarchyType(code);
  }

  public List<ServerGeoObjectType> getAllGeoObjectTypes()
  {
    return this.getAllGeoObjectTypeCodes().stream().map(code -> this.getGeoObjectType(code).get()).collect(Collectors.toList());
  }

  public List<String> getAllGeoObjectTypeCodes()
  {
    return new LinkedList<String>(this.geoGeoObjectTypeCodeMap.keySet());
  }

  public List<ServerHierarchyType> getAllHierarchyTypes()
  {
    return new ArrayList<ServerHierarchyType>(this.hierarchyTypeMap.values());
  }
}
