package net.geoprism.graph;

public class ConceptSetSnapshotHasEdge extends ConceptSetSnapshotHasEdgeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1251849924;
  
  public ConceptSetSnapshotHasEdge(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public ConceptSetSnapshotHasEdge(net.geoprism.graph.ConceptSetSnapshot parent, net.geoprism.graph.ConceptEdgeTypeSnapshot child)
  {
    this(parent.getOid(), child.getOid());
  }
  
}
