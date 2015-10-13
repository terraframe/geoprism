package com.runwaysdk.geodashboard;

public class ClassUniversal extends ClassUniversalBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 538122561;
  
  public ClassUniversal(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public ClassUniversal(com.runwaysdk.geodashboard.MappableClass parent, com.runwaysdk.system.gis.geo.Universal child)
  {
    this(parent.getId(), child.getId());
  }
  
}
