/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.graph;

import com.runwaysdk.system.metadata.MdEdge;

public interface GraphTypeSnapshot
{
  public final static java.lang.String TYPE_CODE                   = "typeCode";

  public static final String           DIRECTED_ACYCLIC_GRAPH_TYPE = "DirectedAcyclicGraphType";

  public static final String           UNDIRECTED_GRAPH_TYPE       = "UndirectedGraphType";

  public static final String           HIERARCHY_TYPE              = "HierarchyType";

  public String getGraphMdEdgeOid();

  public MdEdge getGraphMdEdge();

  public String getType();

  public String getTypeCode();
  
  public Long getSequence();

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

  public String getCode();
}
