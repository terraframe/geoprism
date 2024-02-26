package net.geoprism.registry.command;

import com.runwaysdk.dataaccess.Command;

public abstract class AbstractCacheCommand implements Command
{
  private CacheEventType eventType;

  public AbstractCacheCommand(CacheEventType eventType)
  {
    this.eventType = eventType;
  }

  public CacheEventType getEventType()
  {
    return eventType;
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
    return false;
  }

}
