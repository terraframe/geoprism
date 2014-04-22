package com.runwaysdk.geodashboard.gis.persist;

public class HasLayer extends HasLayerBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1044882992;
  
  public HasLayer(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public HasLayer(com.runwaysdk.geodashboard.gis.persist.DashboardMap parent, com.runwaysdk.geodashboard.gis.persist.DashboardLayer child)
  {
    this(parent.getId(), child.getId());
  }
  
}
