package net.geoprism.registry.command;

import java.util.List;

import com.runwaysdk.dataaccess.Command;

import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.service.business.GeoObjectTypeBusinessServiceIF;
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
    GeoObjectTypeBusinessServiceIF service = ServiceFactory.getBean(GeoObjectTypeBusinessServiceIF.class);

    type.setType(net.geoprism.registry.graph.GeoObjectType.getByCode(type.getCode()));

    ServiceFactory.getMetadataCache().addGeoObjectType(type);

    // Refresh all of the subtypes
    List<ServerGeoObjectType> subtypes = service.getSubtypes(type);

    for (ServerGeoObjectType subtype : subtypes)
    {
      this.refreshCache(subtype);
    }
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
