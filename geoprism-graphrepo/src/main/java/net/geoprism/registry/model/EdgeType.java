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
package net.geoprism.registry.model;

import org.commongeoregistry.adapter.metadata.GraphTypeDTO;

import com.runwaysdk.dataaccess.MdEdgeDAOIF;

import net.geoprism.registry.graph.BusinessEdgeType;
import net.geoprism.registry.graph.ConceptEdgeType;
import net.geoprism.registry.graph.DirectedAcyclicGraphType;
import net.geoprism.registry.graph.UndirectedGraphType;
import net.geoprism.registry.view.TypeClass;
import net.geoprism.registry.view.TypeInfo;

public interface EdgeType
{
  public static final String START_DATE = "startDate";

  public static final String END_DATE   = "endDate";

  public MdEdgeDAOIF getMdEdgeDAO();

  public GraphTypeDTO toDTO();

  public String getCode();

  public TypeInfo getTypeInfo();

  public String getOrigin();

  public Long getSequence();

  public TypeClass getSourceType();

  public TypeClass getTargetType();

  public static String getTypeCode(EdgeType graphType)
  {
    if (graphType instanceof DirectedAcyclicGraphType)
    {
      return TypeClass.DAG.getCode();
    }
    else if (graphType instanceof UndirectedGraphType)
    {
      return TypeClass.UNDIRECTED_GRAPH.getCode();
    }
    else if (graphType instanceof ServerHierarchyType)
    {
      return TypeClass.HIERARCHY.getCode();
    }
    else if (graphType instanceof BusinessEdgeType)
    {
      return TypeClass.BUSINESS_EDGE.getCode();
    }
    else if (graphType instanceof ConceptEdgeType)
    {
      return TypeClass.CONCEPT_EDGE.getCode();
    }
    else
    {
      throw new UnsupportedOperationException();
    }
  }

}
