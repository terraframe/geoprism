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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;

import net.geoprism.registry.graph.ConceptClass;
import net.geoprism.registry.graph.ConceptEdgeType;
import net.geoprism.registry.graph.ConceptVertex;
import net.geoprism.registry.model.ConceptObject;
import net.geoprism.registry.model.graph.VertexServerGeoObject;
import net.geoprism.registry.view.ConceptClassDTO;

@Service
public class ConceptObjectBusinessService extends ObjectEdgeBusinessService<ConceptObject, ConceptClass, ConceptClassDTO, ConceptEdgeType, ConceptObject> implements ConceptObjectBusinessServiceIF
{
  public ConceptObjectBusinessService(ConceptClassBusinessServiceIF typeService)
  {
    super(typeService, ConceptVertex.CLASS);
  }

  @Override
  public ConceptObject newInstance(ConceptClass type)
  {
    VertexObject vertex = VertexObject.instantiate(VertexObjectDAO.newInstance(type.getMdVertexDAO()));

    return new ConceptObject(type, vertex, new TreeMap<>());
  }

  @Override
  protected ConceptObject build(ConceptClass type, VertexObject current, Map<String, List<VertexObject>> nodeMap, Date date)
  {
    return new ConceptObject(type, current, nodeMap, date);
  }

  @Override
  protected boolean isValidEdge(ConceptObject child, ConceptEdgeType type, ConceptObject parent, Date startDate, Date endDate)
  {
    boolean isValid = super.isValidEdge(child, type, parent, startDate, endDate);

    if (isValid)
    {
      // Concept edges must be a DAG
      return !this.isCycle(child, type, parent, startDate, endDate);
    }

    return true;
  }

}
