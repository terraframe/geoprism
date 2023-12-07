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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.commongeoregistry.adapter.Optional;
import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.Term;
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

  private Map<String, ServerGeoObjectType> geoGeoObjectTypeMap;

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
    
    this.geoGeoObjectTypeMap = new ConcurrentHashMap<String, ServerGeoObjectType>();
    this.hierarchyTypeMap = new ConcurrentHashMap<String, ServerHierarchyType>();

    getAdapter().getMetadataCache().rebuild();
  }

  public void addTerm(Term term)
  {
    getAdapter().getMetadataCache().addTerm(term);
  }

  public Optional<Term> getTerm(String code)
  {
    return getAdapter().getMetadataCache().getTerm(code);
  }

  /**
   * Updates the cache with the new GeoObjectType everywhere where it is cached
   * (including in HierarchyTypes).
   */
  public void refreshGeoObjectType(ServerGeoObjectType refreshGot)
  {
    this.addGeoObjectType(refreshGot);

    // List<ServerHierarchyType> hierarchyTypes =
    // ServiceFactory.getMetadataCache().getAllHierarchyTypes();
    // for (ServerHierarchyType ht : hierarchyTypes)
    // {
    // List<HierarchyNode> rootNodes = ht.getRootGeoObjectTypes();
    //
    // for (HierarchyNode rootNode : rootNodes)
    // {
    // if (refreshGot.getCode().equals(rootNode.getGeoObjectType().getCode()))
    // {
    // rootNode.setGeoObjectType(refreshGot.getType());
    // break;
    // }
    //
    // List<HierarchyNode> rootDescends = rootNode.getAllDescendants();
    //
    // Iterator<HierarchyNode> rootDescendIt = rootDescends.iterator();
    //
    // while (rootDescendIt.hasNext())
    // {
    // HierarchyNode childNode = rootDescendIt.next();
    //
    // if (childNode.getGeoObjectType().getCode().equals(refreshGot.getCode()))
    // {
    // childNode.setGeoObjectType(refreshGot.getType());
    // break;
    // }
    // }
    // }
    // }
  }

  public void addGeoObjectType(ServerGeoObjectType geoObjectType)
  {
    this.geoGeoObjectTypeMap.put(geoObjectType.getCode(), geoObjectType);

    getAdapter().getMetadataCache().addGeoObjectType(geoObjectType.getType());
  }

  public Optional<ServerGeoObjectType> getGeoObjectType(String code)
  {
    return Optional.of(this.geoGeoObjectTypeMap.get(code));
  }

  public void removeGeoObjectType(String code)
  {
    this.geoGeoObjectTypeMap.remove(code);

    getAdapter().getMetadataCache().removeGeoObjectType(code);
  }

  public void addHierarchyType(ServerHierarchyType hierarchyType, HierarchyType dto)
  {
    this.hierarchyTypeMap.put(hierarchyType.getCode(), hierarchyType);

    getAdapter().getMetadataCache().addHierarchyType(dto);
  }

  public Optional<ServerHierarchyType> getHierachyType(String code)
  {
    return Optional.of(this.hierarchyTypeMap.get(code));
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

    return new ArrayList<ServerGeoObjectType>(this.geoGeoObjectTypeMap.values());
  }

  public List<String> getAllGeoObjectTypeCodes()
  {
    List<ServerGeoObjectType> gots = this.getAllGeoObjectTypes();

    List<String> codes = new ArrayList<String>(gots.size());

    for (int i = 0; i < gots.size(); ++i)
    {
      codes.add(gots.get(i).getCode());
    }

    return codes;
  }

  public List<ServerHierarchyType> getAllHierarchyTypes()
  {
    return new ArrayList<ServerHierarchyType>(this.hierarchyTypeMap.values());
  }
}
