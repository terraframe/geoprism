package com.runwaysdk.geodashboard;

public class MappableClassGeoNode extends MappableClassGeoNodeBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -737027213;
  
  public MappableClassGeoNode(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public MappableClassGeoNode(com.runwaysdk.geodashboard.MappableClass parent, com.runwaysdk.system.gis.geo.GeoNode child)
  {
    this(parent.getId(), child.getId());
  }
  
}
