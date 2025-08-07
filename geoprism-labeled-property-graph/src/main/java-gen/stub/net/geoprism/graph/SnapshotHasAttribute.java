package net.geoprism.graph;

public class SnapshotHasAttribute extends SnapshotHasAttributeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -2105388673;
  
  public SnapshotHasAttribute(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public SnapshotHasAttribute(net.geoprism.graph.ObjectTypeSnapshot parent, net.geoprism.graph.AttributeTypeSnapshot child)
  {
    this(parent.getOid(), child.getOid());
  }
  
}
