package net.geoprism.registry.command;

import com.runwaysdk.dataaccess.Command;

import net.geoprism.registry.cache.TransactionCacheFacade;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.service.request.ServiceFactory;

public class HierarchicalRelationshipTypeCacheEventCommand extends AbstractCacheCommand implements Command
{
  private ServerHierarchyType type;

  public HierarchicalRelationshipTypeCacheEventCommand(ServerHierarchyType type, CacheEventType eventType)
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
      ServiceFactory.getMetadataCache().addHierarchyType(this.type);
    }
    else if (this.type != null && this.getEventType().equals(CacheEventType.UPDATE))
    {
      this.type.markAsDirty();
    }
    else if (this.getEventType().equals(CacheEventType.DELETE))
    {
      // If we get here then it was successfully deleted
      // We have to do a full metadata cache
      // refresh because the GeoObjectType is
      // embedded in the HierarchyType
      ServiceFactory.getGraphRepoService().refreshMetadataCache();
    }
  }
}
