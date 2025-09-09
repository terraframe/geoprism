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
package net.geoprism.registry.model;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.GraphTypeDTO;

import com.runwaysdk.dataaccess.MdEdgeDAOIF;

import net.geoprism.graph.GraphTypeSnapshot;
import net.geoprism.registry.DirectedAcyclicGraphType;
import net.geoprism.registry.UndirectedGraphType;
import net.geoprism.registry.model.graph.GraphStrategy;

public interface GraphType
{
  public static final String UNDIRECTED_GRAPH_TYPE       = GraphTypeSnapshot.UNDIRECTED_GRAPH_TYPE;

  public static final String DIRECTED_ACYCLIC_GRAPH_TYPE = GraphTypeSnapshot.DIRECTED_ACYCLIC_GRAPH_TYPE;

  public static final String HIERARCHY_TYPE              = GraphTypeSnapshot.HIERARCHY_TYPE;

  public MdEdgeDAOIF getMdEdgeDAO();

  public GraphStrategy getStrategy();

  public String getCode();

  public String getOrigin();

  public Long getSequence();

  public LocalizedValue getLabel();

  public LocalizedValue getDescriptionLV();

  public GraphTypeDTO toDTO();
  
  public static String getTypeCode(GraphType graphType)
  {
    if (graphType instanceof DirectedAcyclicGraphType)
    {
      return GraphTypeSnapshot.DIRECTED_ACYCLIC_GRAPH_TYPE;
    }
    else if (graphType instanceof UndirectedGraphType)
    {
      return GraphTypeSnapshot.UNDIRECTED_GRAPH_TYPE;
    }
    else if (graphType instanceof ServerHierarchyType)
    {
      return GraphTypeSnapshot.HIERARCHY_TYPE;
    }
    else
    {
      throw new UnsupportedOperationException();
    }
  }

}
