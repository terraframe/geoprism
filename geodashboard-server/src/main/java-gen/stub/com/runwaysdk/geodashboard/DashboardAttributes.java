package com.runwaysdk.geodashboard;

public class DashboardAttributes extends DashboardAttributesBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1378379249;
  
  public DashboardAttributes(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public DashboardAttributes(com.runwaysdk.geodashboard.MetadataWrapper parent, com.runwaysdk.geodashboard.AttributeWrapper child)
  {
    this(parent.getId(), child.getId());
  }
  
}
