package net.geoprism.registry.command;

import com.runwaysdk.dataaccess.Command;

import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.service.request.ServiceFactory;

public class GeoObjectTypeCacheEventCommand extends AbstractCacheCommand implements Command
{
  private ServerGeoObjectType type;

  public GeoObjectTypeCacheEventCommand(ServerGeoObjectType type, CacheEventType eventType)
  {
    super(eventType);

    this.type = type;
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
