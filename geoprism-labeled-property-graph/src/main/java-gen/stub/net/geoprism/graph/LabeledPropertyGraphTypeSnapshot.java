package net.geoprism.graph;

public class LabeledPropertyGraphTypeSnapshot extends LabeledPropertyGraphTypeSnapshotBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1471053930;
  
  public LabeledPropertyGraphTypeSnapshot(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public LabeledPropertyGraphTypeSnapshot(net.geoprism.graph.LabeledPropertyGraphTypeVersion parent, net.geoprism.graph.MetadataSnapshot child)
  {
    this(parent.getOid(), child.getOid());
  }
  
}
