package com.runwaysdk.geodashboard;

public class DashboardMetadata extends DashboardMetadataBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -566171690;
  
  public DashboardMetadata(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public DashboardMetadata(com.runwaysdk.geodashboard.Dashboard parent, com.runwaysdk.geodashboard.MetadataWrapper child)
  {
    this(parent.getId(), child.getId());
  }
  
}
