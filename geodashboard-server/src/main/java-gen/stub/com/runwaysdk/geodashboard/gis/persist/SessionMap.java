package com.runwaysdk.geodashboard.gis.persist;

public class SessionMap extends SessionMapBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1714203169;
  
  public SessionMap(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public SessionMap(com.runwaysdk.geodashboard.SessionEntry parent, com.runwaysdk.geodashboard.gis.persist.DashboardMap child)
  {
    this(parent.getId(), child.getId());
  }

  
}
