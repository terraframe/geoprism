/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism Registry(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.RegistryAdapterServer;
import org.springframework.stereotype.Service;

import net.geoprism.graphrepo.permission.GeoObjectPermissionServiceIF;
import net.geoprism.graphrepo.permission.GeoObjectRelationshipPermissionServiceIF;
import net.geoprism.graphrepo.permission.GeoObjectTypePermissionServiceIF;
import net.geoprism.graphrepo.permission.GeoObjectTypeRelationshipPermissionServiceIF;
import net.geoprism.graphrepo.permission.HierarchyTypePermissionServiceIF;
import net.geoprism.graphrepo.permission.OrganizationPermissionServiceIF;
import net.geoprism.registry.business.GeoObjectBusinessServiceIF;
import net.geoprism.registry.business.HierarchyTypeBusinessServiceIF;
import net.geoprism.registry.cache.ServerMetadataCache;
import net.geoprism.spring.ApplicationContextHolder;

@Service
public class ServiceFactory
{
  private RegistryIdService   idService;

  private RegistryAdapter     adapter;

  private ServerMetadataCache metadataCache;

  public ServiceFactory()
  {
    this.idService = new RegistryIdService();

    this.adapter = new RegistryAdapterServer(this.idService);

    this.metadataCache = new ServerMetadataCache(this.adapter);
    this.metadataCache.rebuild();
  }

  public static <T> T getBean(Class<T> clazz)
  {
    return ApplicationContextHolder.getBean(clazz);
  }

  private static ServiceFactory getInstance()
  {
    return getBean(ServiceFactory.class);
  }

  public static RegistryAdapter getAdapter()
  {
    return ServiceFactory.getInstance().adapter;
  }

  public static RegistryIdService getIdService()
  {
    return getInstance().idService;
  }

  public static GraphRepoServiceIF getGraphRepoService()
  {
    return getBean(GraphRepoServiceIF.class);
  }

  public static GeoObjectPermissionServiceIF getGeoObjectPermissionService()
  {
    return getBean(GeoObjectPermissionServiceIF.class);
  }

  public static GeoObjectRelationshipPermissionServiceIF getGeoObjectRelationshipPermissionService()
  {
    return getBean(GeoObjectRelationshipPermissionServiceIF.class);
  }

  public static GeoObjectTypeRelationshipPermissionServiceIF getGeoObjectTypeRelationshipPermissionService()
  {
    return getBean(GeoObjectTypeRelationshipPermissionServiceIF.class);
  }

  public static OrganizationPermissionServiceIF getOrganizationPermissionService()
  {
    return getBean(OrganizationPermissionServiceIF.class);
  }

  public static HierarchyTypePermissionServiceIF getHierarchyPermissionService()
  {
    return getBean(HierarchyTypePermissionServiceIF.class);
  }
  
  public static HierarchyTypeBusinessServiceIF getHierarchyService()
  {
    return getBean(HierarchyTypeBusinessServiceIF.class);
  }
  
  public static GeoObjectBusinessServiceIF getGeoObjectService()
  {
    return getBean(GeoObjectBusinessServiceIF.class);
  }

  public static GeoObjectTypePermissionServiceIF getGeoObjectTypePermissionService()
  {
    return getBean(GeoObjectTypePermissionServiceIF.class);
  }

  public static OrganizationServiceIF getOrganizationService()
  {
    return getBean(OrganizationServiceIF.class);
  }

  public static ServerMetadataCache getMetadataCache()
  {
    return ServiceFactory.getInstance().metadataCache;
  }
}
