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
package net.geoprism.registry.cache;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.metadata.HierarchyType;

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
    this.hierarchyTypeMap = new ConcurrentHashMap<String, ServerHierarchyType>();

    getAdapter().getMetadataCache().rebuild();
  }


  public void addGeoObjectType(ServerGeoObjectType geoObjectType)
  {
    this.geoGeoObjectTypeCodeMap.put(geoObjectType.getCode(), geoObjectType);
    this.geoGeoObjectTypeOidMap.put(geoObjectType.getOid(), geoObjectType);

    getAdapter().getMetadataCache().addGeoObjectType(geoObjectType.toDTO());
  }

  public Optional<ServerGeoObjectType> getGeoObjectType(String code)
  {
    return Optional.ofNullable(this.geoGeoObjectTypeCodeMap.get(code));
  }

  public Optional<ServerGeoObjectType> getGeoObjectTypeByOid(String oid)
  {
    return Optional.ofNullable(this.geoGeoObjectTypeOidMap.get(oid));
  }
  
  public void removeGeoObjectType(ServerGeoObjectType type)
  {
    removeGeoObjectType(type.getCode(), type.getOid());
  }

  private void removeGeoObjectType(String code, String oid)
  {
    this.geoGeoObjectTypeCodeMap.remove(code);
    this.geoGeoObjectTypeOidMap.remove(oid);

    getAdapter().getMetadataCache().removeGeoObjectType(code);
  }

  public void addHierarchyType(ServerHierarchyType hierarchyType, HierarchyType dto)
  {
    this.hierarchyTypeMap.put(hierarchyType.getCode(), hierarchyType);

    getAdapter().getMetadataCache().addHierarchyType(dto);
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
    // return this.geoGeoObjectTypeMap.values().toArray(new
    // GeoObjectType[this.geoGeoObjectTypeMap.values().size()]);

    return new ArrayList<ServerGeoObjectType>(this.geoGeoObjectTypeOidMap.values());
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
