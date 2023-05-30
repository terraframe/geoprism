package net.geoprism.graph;

public class SnapshotHierarchy extends SnapshotHierarchyBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1196292856;
  
  public SnapshotHierarchy(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public SnapshotHierarchy(net.geoprism.graph.GeoObjectTypeSnapshot parent, net.geoprism.graph.GeoObjectTypeSnapshot child)
  {
    this(parent.getOid(), child.getOid());
  }
  
}
