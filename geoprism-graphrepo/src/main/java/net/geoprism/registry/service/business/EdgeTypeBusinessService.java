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
package net.geoprism.registry.service.business;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.commongeoregistry.adapter.metadata.GraphTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.runwaysdk.dataaccess.ProgrammingErrorException;

import net.geoprism.graph.GraphTypeReference;
import net.geoprism.registry.DirectedAcyclicGraphType;
import net.geoprism.registry.UndirectedGraphType;
import net.geoprism.registry.model.EdgeType;
import net.geoprism.registry.model.GraphType;
import net.geoprism.registry.model.ServerHierarchyType;

@Service
public class EdgeTypeBusinessService implements EdgeTypeBusinessServiceIF
{
  @Autowired
  private DirectedAcyclicGraphTypeBusinessServiceIF dagService;

  @Autowired
  private UndirectedGraphTypeBusinessServiceIF      undirectedService;

  @Autowired
  private BusinessEdgeTypeBusinessServiceIF         bEdgeService;

  @Override
  public GraphTypeDTO toDTO(EdgeType graphType)
  {
    return graphType.toDTO();
  }

  @Override
  public List<EdgeType> getGraphTypes(String... typeCodes)
  {
    List<EdgeType> result = new ArrayList<EdgeType>();

    result.addAll(ServerHierarchyType.getAll().stream().filter(t -> typeCodes == null || ArrayUtils.contains(typeCodes, t.getCode())).collect(Collectors.toList()));

    result.addAll(this.dagService.getAll(typeCodes));

    result.addAll(this.undirectedService.getAll(typeCodes));
    result.addAll(this.bEdgeService.getAll());

    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends EdgeType> T getByCode(String relationshipType, String code)
  {
    if (relationshipType != null)
    {
      if (relationshipType.equals(GraphType.UNDIRECTED_GRAPH_TYPE) || relationshipType.equals(UndirectedGraphType.CLASS))
      {
        return (T) this.undirectedService.getByCode(code).orElseThrow(() -> {
          throw new ProgrammingErrorException("Unable to find undirected graph with the code [" + code + "]");
        });
      }
      else if (relationshipType.equals(GraphType.DIRECTED_ACYCLIC_GRAPH_TYPE) || relationshipType.equals(DirectedAcyclicGraphType.CLASS))
      {
        return (T) this.dagService.getByCode(code).orElseThrow(() -> {
          throw new ProgrammingErrorException("Unable to find undirected graph with the code [" + code + "]");
        });
      }
      else if (relationshipType.equals(GraphType.HIERARCHY_TYPE))
      {
        return (T) ServerHierarchyType.get(code);
      }
      else if (relationshipType.equals(GraphType.BUSINESS_EDGE_TYPE))
      {
        return (T) this.bEdgeService.getByCodeOrThrow(code);
      }
      else
      {
        return (T) com.runwaysdk.business.Business.get(relationshipType, code);
      }
    }

    return (T) ServerHierarchyType.get(code);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends EdgeType> T resolve(GraphTypeReference ref)
  {
    return (T) getByCode(ref.typeCode, ref.code);
  }

}
