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
package net.geoprism.registry.service.request;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.RegistryAdapterServer;
import org.commongeoregistry.adapter.metadata.MetadataCache;
import org.springframework.stereotype.Service;

import net.geoprism.registry.cache.ServerOrganizationCache;

@Service
public class CacheProvider implements CacheProviderIF
{
  private RegistryIdService       idService;

  private RegistryAdapter         adapter;

  private ServerOrganizationCache metadataCache;

  public CacheProvider()
  {
    this.idService = new RegistryIdService();

    this.adapter = new RegistryAdapterServer(this.idService);

    this.metadataCache = new ServerOrganizationCache(this.adapter);
    this.metadataCache.rebuild();
  }

  @Override
  public ServerOrganizationCache getServerCache()
  {
    return this.metadataCache;
  }

}
