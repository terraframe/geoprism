/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.graph;

import com.runwaysdk.system.metadata.MdEdge;

import net.geoprism.registry.view.TypeClass;

public interface GraphTypeSnapshot
{
  public final static java.lang.String TYPE_CODE                   = "typeCode";

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
      return TypeClass.DAG.getCode();
    }
    else if (graphType instanceof DirectedAcyclicGraphTypeSnapshot)
    {
      return TypeClass.UNDIRECTED_GRAPH.getCode();
    }
    else if (graphType instanceof HierarchyTypeSnapshot)
    {
      return TypeClass.HIERARCHY.getCode();
    }
    else
    {
      throw new UnsupportedOperationException();
    }
  }

  public String getCode();
}
