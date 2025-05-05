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
package net.geoprism.registry.service.business;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.commongeoregistry.adapter.metadata.GraphTypeDTO;
import org.springframework.stereotype.Service;

import com.runwaysdk.query.QueryFactory;

import net.geoprism.registry.DirectedAcyclicGraphTypeQuery;
import net.geoprism.registry.UndirectedGraphTypeQuery;
import net.geoprism.registry.model.GraphType;
import net.geoprism.registry.model.ServerHierarchyType;

@Service
public class GraphTypeBusinessService implements GraphTypeBusinessServiceIF
{
  public GraphTypeDTO toDTO(GraphType graphType)
  {
    return graphType.toDTO();
  }
  
  public List<GraphType> getTypes(String... typeCodes)
  {
    List<GraphType> result = new ArrayList<GraphType>();
    
    result.addAll(ServerHierarchyType.getAll().stream().filter(t -> typeCodes == null || ArrayUtils.contains(typeCodes, t.getCode())).collect(Collectors.toList()));
    
    DirectedAcyclicGraphTypeQuery query = new DirectedAcyclicGraphTypeQuery(new QueryFactory());
    if (typeCodes != null)
    {
      query.AND(query.getCode().IN(typeCodes));
    }
    result.addAll(query.getIterator().getAll());
    
    UndirectedGraphTypeQuery query2 = new UndirectedGraphTypeQuery(new QueryFactory());
    if (typeCodes != null)
    {
      query2.AND(query2.getCode().IN(typeCodes));
    }
    result.addAll(query2.getIterator().getAll());
    
    return result;
  }
}
