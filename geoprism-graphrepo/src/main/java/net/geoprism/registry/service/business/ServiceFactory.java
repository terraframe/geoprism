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

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.RegistryAdapterServer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import net.geoprism.registry.cache.ServerAdapterCache;
import net.geoprism.registry.cache.ServerMetadataCache;
import net.geoprism.registry.service.permission.GeoObjectPermissionServiceIF;
import net.geoprism.registry.service.permission.GeoObjectRelationshipPermissionServiceIF;
import net.geoprism.registry.service.permission.GeoObjectTypePermissionServiceIF;
import net.geoprism.registry.service.permission.GeoObjectTypeRelationshipPermissionServiceIF;
import net.geoprism.registry.service.permission.HierarchyTypePermissionServiceIF;
import net.geoprism.registry.service.permission.OrganizationPermissionServiceIF;
import net.geoprism.spring.core.ApplicationContextHolder;

@Service
@Primary
public class ServiceFactory implements CacheProviderIF
{
  private RegistryIdService   idService;

  private RegistryAdapter     adapter;

  private ServerMetadataCache metadataCache;

  private boolean             cacheInitialized = false;

  public ServiceFactory()
  {
    this.idService = new RegistryIdService();

    this.adapter = new RegistryAdapterServer(this.idService, new ServerAdapterCache());

    this.metadataCache = new ServerMetadataCache(this.adapter);
    this.metadataCache.rebuild();
  }

  @Override
  public synchronized ServerMetadataCache getServerCache()
  {
    if (!cacheInitialized)
    {
      cacheInitialized = true;
      getGraphRepoService().initialize();
    }

    return this.metadataCache;
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

  public static GeoObjectTypePermissionServiceIF getGeoObjectTypePermissionService()
  {
    return getBean(GeoObjectTypePermissionServiceIF.class);
  }

  public static ServerMetadataCache getMetadataCache()
  {
    ServiceFactory fac = ServiceFactory.getInstance();

    return fac.getServerCache();
  }
}
