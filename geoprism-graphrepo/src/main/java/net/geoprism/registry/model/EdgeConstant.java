package net.geoprism.registry.model;

import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;

public enum EdgeConstant {
  HAS_VALUE("net.geoprism.registry.graph.HasValue"), HAS_GEOMETRY("net.geoprism.registry.graph.HasGeometry");

  private String      edgeType;

  private MdEdgeDAOIF mdEdge;

  private EdgeConstant(String edgeType)
  {
    this.edgeType = edgeType;
  }

  public MdEdgeDAOIF getMdEdge()
  {
    synchronized (this)
    {
      if (this.mdEdge == null)
      {
        this.mdEdge = MdEdgeDAO.getMdEdgeDAO(edgeType);
      }
    }

    return mdEdge;
  }

  public String getDBClassName()
  {
    return this.getMdEdge().getDBClassName();
  }

}
