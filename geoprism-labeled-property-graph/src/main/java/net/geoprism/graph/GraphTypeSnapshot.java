package net.geoprism.graph;

import com.google.gson.JsonObject;
import com.runwaysdk.system.metadata.MdEdge;

public interface GraphTypeSnapshot
{
  public final static java.lang.String TYPE_CODE = "typeCode";
  
  public static final String DIRECTED_ACYCLIC_GRAPH_TYPE = "DirectedAcyclicGraphType";
  public static final String UNDIRECTED_GRAPH_TYPE = "UndirectedGraphType";
  public static final String HIERARCHY_TYPE = "HierarchyType";
  
  public String getGraphMdEdgeOid();
  
  public MdEdge getGraphMdEdge();
  
  public String getType();
  
  public String getTypeCode();
  
  public void delete();
  
  public static String getTypeCode(GraphTypeSnapshot graphType)
  {
    if (graphType instanceof DirectedAcyclicGraphTypeSnapshot)
    {
      return GraphTypeSnapshot.DIRECTED_ACYCLIC_GRAPH_TYPE;
    }
    else if (graphType instanceof DirectedAcyclicGraphTypeSnapshot)
    {
      return GraphTypeSnapshot.UNDIRECTED_GRAPH_TYPE;
    }
    else if (graphType instanceof HierarchyTypeSnapshot)
    {
      return GraphTypeSnapshot.HIERARCHY_TYPE;
    }
    else
    {
      throw new UnsupportedOperationException();
    }
  }

  public JsonObject toJSON(GeoObjectTypeSnapshot root);

  public String getCode();
}
