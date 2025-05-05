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
package net.geoprism.registry.command;

import com.runwaysdk.dataaccess.Command;

import net.geoprism.registry.cache.TransactionCacheFacade;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.service.business.ServiceFactory;

public class GeoObjectTypeCacheEventCommand extends AbstractCacheCommand implements Command
{
  private ServerGeoObjectType type;

  public GeoObjectTypeCacheEventCommand(ServerGeoObjectType type, CacheEventType eventType)
  {
    super(eventType);

    this.type = type;

    if (this.getEventType().equals(CacheEventType.CREATE) || this.getEventType().equals(CacheEventType.UPDATE))
    {
      TransactionCacheFacade.put(this.type);
    }
  }

  @Override
  public void doIt()
  {
    if (this.type != null && this.getEventType().equals(CacheEventType.CREATE))
    {
      ServiceFactory.getMetadataCache().addGeoObjectType(this.type);
    }
    else if (this.type != null && this.getEventType().equals(CacheEventType.UPDATE))
    {
      this.refreshCache(this.type);
    }
    else if (this.getEventType().equals(CacheEventType.DELETE))
    {
      // If we get here then it was successfully deleted
      // We have to do a full metadata cache
      // refresh because the GeoObjectType is
      // embedded in the HierarchyType
      ServiceFactory.getGraphRepoService().refreshMetadataCache();
    }
    else if (this.getEventType().equals(CacheEventType.VIEW))
    {
      ServiceFactory.getMetadataCache().getAllGeoObjectTypes().stream().forEach(type -> type.refreshDTO());
    }
  }

  private void refreshCache(ServerGeoObjectType type)
  {
    type.getSubTypes().forEach(t -> t.markAsDirty());
    type.markAsDirty();
  }
}
