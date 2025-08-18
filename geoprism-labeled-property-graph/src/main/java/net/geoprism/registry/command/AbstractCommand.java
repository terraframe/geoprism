package net.geoprism.registry.command;

import com.runwaysdk.dataaccess.Command;

public abstract class AbstractCommand implements Command
{

  @Override
  public void doFinally()
  {

  }

  @Override
  public void doIt()
  {

  }

  @Override
  public String doItString()
  {
    return null;
  }

  @Override
  public boolean isUndoable()
  {
    return false;
  }

  @Override
  public void undoIt()
  {

  }

  @Override
  public String undoItString()
  {
    return null;
  }

}
