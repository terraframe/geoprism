package com.runwaysdk.geodashboard.gis.persist;

public class HasStyle extends HasStyleBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 560741115;
  
  public HasStyle(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public HasStyle(com.runwaysdk.geodashboard.gis.persist.DashboardLayer parent, com.runwaysdk.geodashboard.gis.persist.DashboardStyle child)
  {
    this(parent.getId(), child.getId());
  }
  
}
