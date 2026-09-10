package net.geoprism.graph;

public class ConceptSetSnapshotHasClass extends ConceptSetSnapshotHasClassBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -621700978;
  
  public ConceptSetSnapshotHasClass(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public ConceptSetSnapshotHasClass(net.geoprism.graph.ConceptSetSnapshot parent, net.geoprism.graph.ConceptClassSnapshot child)
  {
    this(parent.getOid(), child.getOid());
  }
  
}
