package com.runwaysdk.geodashboard;

public class MetadataGeoNode extends MetadataGeoNodeBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1451281126;
  
  public MetadataGeoNode(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public MetadataGeoNode(com.runwaysdk.geodashboard.MetadataWrapper parent, com.runwaysdk.system.gis.geo.GeoNode child)
  {
    this(parent.getId(), child.getId());
  }
  
}
