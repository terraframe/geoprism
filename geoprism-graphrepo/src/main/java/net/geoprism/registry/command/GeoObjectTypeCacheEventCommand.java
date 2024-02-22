package net.geoprism.registry.command;

import com.runwaysdk.dataaccess.Command;

import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.service.request.ServiceFactory;

public class GeoObjectTypeCacheEventCommand implements Command
{
  public static enum EventType {
    CREATE, UPDATE, DELETE
  }

  private ServerGeoObjectType type;

  private EventType           eventType;

  public GeoObjectTypeCacheEventCommand(ServerGeoObjectType type, EventType eventType)
  {
    this.type = type;
    this.eventType = eventType;
  }

  @Override
  public void doIt()
  {
    if (this.type != null && this.eventType.equals(EventType.CREATE))
    {
      ServiceFactory.getMetadataCache().addGeoObjectType(this.type);
    }
    else if (this.type != null && this.eventType.equals(EventType.UPDATE))
    {
      this.refreshCache(this.type);
    }
    else if (this.eventType.equals(EventType.DELETE))
    {
      // If we get here then it was successfully deleted
      // We have to do a full metadata cache
      // refresh because the GeoObjectType is
      // embedded in the HierarchyType
      ServiceFactory.getGraphRepoService().refreshMetadataCache();
    }
  }

  private void refreshCache(ServerGeoObjectType type)
  {
    type.getSubTypes().forEach(t -> t.markAsDirty());
    type.markAsDirty();
  }

  @Override
  public void undoIt()
  {
  }

  @Override
  public void doFinally()
  {
  }

  @Override
  public String doItString()
  {
    return "";
  }

  @Override
  public String undoItString()
  {
    return "";
  }

  @Override
  public boolean isUndoable()
  {
    return true;
  }

}
