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
package net.geoprism.registry.service;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.RegistryAdapterServer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import net.geoprism.graphrepo.permission.GeoObjectPermissionServiceIF;
import net.geoprism.graphrepo.permission.GeoObjectRelationshipPermissionServiceIF;
import net.geoprism.graphrepo.permission.GeoObjectTypePermissionServiceIF;
import net.geoprism.graphrepo.permission.GeoObjectTypeRelationshipPermissionServiceIF;
import net.geoprism.graphrepo.permission.HierarchyTypePermissionServiceIF;
import net.geoprism.graphrepo.permission.OrganizationPermissionServiceIF;
import net.geoprism.registry.business.GeoObjectBusinessServiceIF;
import net.geoprism.registry.cache.ServerMetadataCache;
import net.geoprism.registry.hierarchy.HierarchyService;

@Component
public class ServiceFactory implements ApplicationContextAware
{
  private static ServiceFactory                        instance;
  
  private static ApplicationContext context;
  
  private RegistryIdService                            idService;

  private RegistryAdapter                              adapter;

  private HierarchyService                             hierarchyService;

  private ServerMetadataCache                          metadataCache;

  private void initialize()
  {
    GraphRepoServiceIF repoService = getGraphRepoService();
    
    this.idService = new RegistryIdService();
    
    this.adapter = new RegistryAdapterServer(this.idService);

    this.metadataCache = new ServerMetadataCache(this.adapter);
    this.metadataCache.rebuild();

    repoService.initialize();
  }

  public static synchronized ServiceFactory getInstance()
  {
    return instance;
  }
  
  public ServiceFactory() {
      instance = this;
      initialize();
  }

  public static <T> T getBean(Class<T> clazz) {
      return context.getBean(clazz);
  }

  public static RegistryAdapter getAdapter()
  {
    return ServiceFactory.getInstance().adapter;
  }

  public static GraphRepoServiceIF getGraphRepoService()
  {
    return getBean(GraphRepoServiceIF.class);
  }

  public static HierarchyService getHierarchyService()
  {
    return ServiceFactory.getInstance().hierarchyService;
  }

  public static GeoObjectBusinessServiceIF getGeoObjectService()
  {
    return getBean(GeoObjectBusinessServiceIF.class);
  }
  
  public static RegistryIdService getIdService()
  {
    return instance.idService;
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
  
  public static OrganizationServiceIF getOrganizationService()
  {
    return getBean(OrganizationServiceIF.class);
  }

  public static ServerMetadataCache getMetadataCache()
  {
    return ServiceFactory.getInstance().metadataCache;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
  {
    context = applicationContext;
  }
}
