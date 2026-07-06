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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.springframework.stereotype.Service;

import com.runwaysdk.business.graph.EdgeObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.OriginException;
import net.geoprism.registry.graph.BusinessEdgeType;
import net.geoprism.registry.graph.BusinessType;
import net.geoprism.registry.graph.BusinessVertex;
import net.geoprism.registry.graph.DataSource;
import net.geoprism.registry.model.BusinessObject;
import net.geoprism.registry.model.EdgeConstant;
import net.geoprism.registry.model.EdgeType;
import net.geoprism.registry.model.graph.VertexComponent;
import net.geoprism.registry.model.graph.VertexServerGeoObject;
import net.geoprism.registry.view.BusinessTypeDTO;
import net.geoprism.registry.view.ObjectAtTimeDTO;
import net.geoprism.registry.view.ObjectOverTimeDTO;

@Service
public class BusinessObjectBusinessService extends ObjectBusinessService<BusinessObject, BusinessType, BusinessTypeDTO> implements BusinessObjectBusinessServiceIF
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
  public boolean exists(VertexComponent object, BusinessEdgeType edgeType, VertexComponent parent)
  {
    return getEdge(object, edgeType, parent) != null;
  }

  protected EdgeObject getEdge(VertexComponent object, BusinessEdgeType edgeType, VertexComponent parent)
  {
    String statement = "SELECT FROM " + edgeType.getMdEdgeDAO().getDBClassName();
    statement += " WHERE out = :parent";
    statement += " AND in = :child";

    GraphQuery<EdgeObject> query = new GraphQuery<EdgeObject>(statement);
    query.setParameter("parent", parent.getVertex().getRID());
    query.setParameter("child", object.getVertex().getRID());

    return query.getSingleResult();
  }

  @Override
  public boolean exists(BusinessEdgeType type, VertexComponent parent, VertexComponent child, Date startDate, Date endDate)
  {
    return getEdge(type, parent, child, startDate, endDate) != null;
  }

  protected EdgeObject getEdge(BusinessEdgeType type, VertexComponent parent, VertexComponent child, Date startDate, Date endDate)
  {
    String statement = "SELECT FROM " + type.getMdEdgeDAO().getDBClassName();
    statement += " WHERE out = :parent";
    statement += " AND in = :child";
    statement += " AND startDate = :startDate";
    statement += " AND endDate = :endDate";

    GraphQuery<EdgeObject> query = new GraphQuery<EdgeObject>(statement);
    query.setParameter("parent", parent.getVertex().getRID());
    query.setParameter("child", child.getVertex().getRID());
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);

    return query.getSingleResult();
  }

  @Override
  public boolean exists(BusinessEdgeType type, String uid)
  {
    String statement = "SELECT FROM " + type.getMdEdgeDAO().getDBClassName();
    statement += " WHERE uid = :uid";

    GraphQuery<EdgeObject> query = new GraphQuery<EdgeObject>(statement);
    query.setParameter("uid", uid);

    return ( query.getSingleResult() != null );
  }

  @Override
  public Optional<EdgeObject> addParent(VertexComponent object, BusinessEdgeType type, VertexComponent parent, String uid, Date startDate, Date endDate, DataSource source)
  {
    return this.addParent(object, type, parent, uid, startDate, endDate, source, true);
  }

  @Override
  public Optional<EdgeObject> addParent(VertexComponent object, BusinessEdgeType type, VertexComponent parent, String uid, Date startDate, Date endDate, DataSource source, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    if (parent != null && !this.exists(object, type, parent))
    {
      EdgeObject newEdge = object.getVertex().addParent(parent.getVertex(), type.getMdEdgeDAO());
      newEdge.setValue(DefaultAttribute.UID.getName(), uid);
      newEdge.setValue(DefaultAttribute.DATA_SOURCE.getName(), source);
      newEdge.setValue(EdgeType.START_DATE, startDate);
      newEdge.setValue(EdgeType.END_DATE, endDate);
      newEdge.apply();

      return Optional.of(newEdge);
    }

    return Optional.empty();
  }

  @Override
  public void removeParent(VertexComponent object, BusinessEdgeType type, VertexComponent parent, Date startDate, Date endDate)
  {
    this.removeParent(object, type, parent, startDate, endDate, true);
  }

  @Override
  public void removeParent(VertexComponent object, BusinessEdgeType type, VertexComponent parent, Date startDate, Date endDate, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    if (parent != null)
    {
      EdgeObject edge = this.getEdge(type, parent, object, startDate, endDate);

      if (edge != null)
      {
        edge.delete();
      }
    }
  }

  @Override
  public List<VertexComponent> getParents(BusinessObject object, BusinessEdgeType type, Date date)
  {
    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
    statement.append("  SELECT EXPAND(inE('" + type.getMdEdge().getDbClassName() + "')[:date BETWEEN startDate AND endDate].out)");
    statement.append("  FROM :rid " + "\n");
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
  public Optional<EdgeObject> addChild(VertexComponent object, BusinessEdgeType type, VertexComponent child, String uid, Date startDate, Date endDate, DataSource source)
  {
    return this.addChild(object, type, child, uid, startDate, endDate, source, true);
  }

  @Override
  public Optional<EdgeObject> addChild(VertexComponent object, BusinessEdgeType type, VertexComponent child, String uid, Date startDate, Date endDate, DataSource source, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    if (child != null && !this.exists(child, type, object))
    {
      EdgeObject newEdge = object.getVertex().addChild(child.getVertex(), type.getMdEdgeDAO());
      newEdge.setValue(DefaultAttribute.UID.getName(), uid);
      newEdge.setValue(DefaultAttribute.DATA_SOURCE.getName(), source);
      newEdge.setValue(EdgeType.START_DATE, startDate);
      newEdge.setValue(EdgeType.END_DATE, endDate);
      newEdge.apply();

      return Optional.of(newEdge);
    }

    return Optional.empty();
  }

  @Override
  public void removeChild(VertexComponent object, BusinessEdgeType type, VertexComponent child, Date startDate, Date endDate)
  {
    this.removeChild(object, type, child, startDate, endDate, true);
  }

  @Override
  public void removeChild(VertexComponent object, BusinessEdgeType type, VertexComponent child, Date startDate, Date endDate, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    if (child != null)
    {
      EdgeObject edge = this.getEdge(type, object, child, startDate, endDate);

      if (edge != null)
      {
        edge.delete();
      }
    }
  }

  @Override
  public List<VertexComponent> getChildren(BusinessObject object, BusinessEdgeType type, Date date)
  {
    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
    statement.append("  SELECT EXPAND(outE('" + type.getMdEdge().getDbClassName() + "')[:date BETWEEN startDate AND endDate].in)");
    statement.append("  FROM :rid " + "\n");
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
