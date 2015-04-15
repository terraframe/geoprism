package com.runwaysdk.geodashboard.gis.persist;

public class HasThematicLayer extends HasThematicLayerBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1449327966;
  
  public HasThematicLayer(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public HasThematicLayer(com.runwaysdk.geodashboard.gis.persist.DashboardMap parent, com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer child)
  {
    this(parent.getId(), child.getId());
  }
  
}
