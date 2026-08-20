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
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;

import net.geoprism.registry.graph.BusinessEdgeType;
import net.geoprism.registry.graph.BusinessType;
import net.geoprism.registry.graph.BusinessVertex;
import net.geoprism.registry.model.BusinessObject;
import net.geoprism.registry.model.EdgeConstant;
import net.geoprism.registry.model.graph.VertexComponent;
import net.geoprism.registry.model.graph.VertexServerGeoObject;
import net.geoprism.registry.view.BusinessTypeDTO;
import net.geoprism.registry.view.ObjectAtTimeDTO;
import net.geoprism.registry.view.ObjectOverTimeDTO;

@Service
public class BusinessObjectBusinessService extends ObjectEdgeBusinessService<BusinessObject, BusinessType, BusinessTypeDTO, BusinessEdgeType, VertexComponent> implements BusinessObjectBusinessServiceIF
{

  public BusinessObjectBusinessService(BusinessTypeBusinessServiceIF typeService)
  {
    super(typeService, BusinessVertex.CLASS);
  }

  @Override
  public BusinessObject newInstance(BusinessType type)
  {
    VertexObject vertex = VertexObject.instantiate(VertexObjectDAO.newInstance(type.getMdVertexDAO()));

    return new BusinessObject(type, vertex, new TreeMap<>());
  }

  @Override
  protected BusinessObject build(BusinessType type, VertexObject current, Map<String, List<VertexObject>> nodeMap, Date date)
  {
    return new BusinessObject(type, current, nodeMap, date);
  }

  @Override
  public ObjectAtTimeDTO toDTO(BusinessObject object, Date date)
  {
    ObjectAtTimeDTO dto = super.toDTO(object, date);
    dto.setLabel(object.getLabel());

    return dto;
  }

  @Override
  public BusinessObject newInstance(BusinessType type, ObjectOverTimeDTO dto)
  {
    BusinessObject object = this.newInstance(type);

    populate(object, dto);

    return object;
  }

  @Override
  public List<VertexComponent> getParents(BusinessObject object, BusinessEdgeType type, Date date)
  {
    StringBuilder statement = new StringBuilder();

    statement.append("TRAVERSE out('");
    statement.append(EdgeConstant.HAS_VALUE.getDBClassName());
    statement.append("', '");
    statement.append(EdgeConstant.HAS_GEOMETRY.getDBClassName());
    statement.append("') FROM (");

    if (date != null)
    {
      statement.append("SELECT EXPAND(inE('");
      statement.append(type.getMdEdge().getDbClassName());
      statement.append("')[:date BETWEEN startDate AND endDate].outV()) ");
    }
    else
    {
      statement.append("SELECT EXPAND(in('");
      statement.append(type.getMdEdge().getDbClassName());
      statement.append("')) ");
    }

    statement.append("FROM :rid");
    statement.append(")");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("rid", object.getVertex().getRID());
    query.setParameter("date", date);

    if (type.getIsParentGeoObject())
    {
      return VertexServerGeoObject.processTraverseResults(query.getResults(), date).stream().map(s -> (VertexComponent) s).toList();
    }

    return this.processTraverseResults(query.getResults(), date).stream().sorted((a, b) -> {
      return a.getLabel().compareTo(b.getLabel());
    }).collect(Collectors.toList());
  }

  @Override
  public List<VertexComponent> getChildren(BusinessObject object, BusinessEdgeType type, Date date)
  {
    StringBuilder statement = new StringBuilder();

    statement.append("TRAVERSE out('");
    statement.append(EdgeConstant.HAS_VALUE.getDBClassName());
    statement.append("', '");
    statement.append(EdgeConstant.HAS_GEOMETRY.getDBClassName());
    statement.append("') FROM (");

    if (date != null)
    {
      statement.append("SELECT EXPAND(outE('");
      statement.append(type.getMdEdge().getDbClassName());
      statement.append("')[:date BETWEEN startDate AND endDate].inV()) ");
    }
    else
    {
      statement.append("SELECT EXPAND(out('");
      statement.append(type.getMdEdge().getDbClassName());
      statement.append("')) ");
    }

    statement.append("FROM :rid");
    statement.append(")");


    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("rid", object.getVertex().getRID());
    query.setParameter("date", date);

    if (type.getIsChildGeoObject())
    {
      return VertexServerGeoObject.processTraverseResults(query.getResults(), date).stream().map(s -> (VertexComponent) s).toList();
    }

    return this.processTraverseResults(query.getResults(), date).stream().sorted((a, b) -> {
      return a.getLabel().compareTo(b.getLabel());
    }).collect(Collectors.toList());
  }

}
