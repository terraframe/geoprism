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
import java.util.Optional;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.constants.DefaultAttribute;

import com.runwaysdk.business.graph.EdgeObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.OriginException;
import net.geoprism.registry.graph.ConceptEdgeType;
import net.geoprism.registry.graph.DataSource;
import net.geoprism.registry.graph.EdgeClass;
import net.geoprism.registry.graph.ObjectClass;
import net.geoprism.registry.model.ConceptObject;
import net.geoprism.registry.model.EdgeConstant;
import net.geoprism.registry.model.EdgeType;
import net.geoprism.registry.model.graph.ServerObjectVertex;
import net.geoprism.registry.model.graph.VertexComponent;
import net.geoprism.registry.view.ObjectClassDTO;

public abstract class ObjectEdgeBusinessService<V extends ServerObjectVertex, T extends ObjectClass, D extends ObjectClassDTO, E extends EdgeClass, N extends VertexComponent> extends ObjectBusinessService<V, T, D> implements ObjectEdgeBusinessServiceIF<V, T, D, E, N>
{

  public ObjectEdgeBusinessService(ObjectClassBusinessServiceIF<T, D> typeService, String baseVertexClass)
  {
    super(typeService, baseVertexClass);
  }

  public ObjectEdgeBusinessService(ObjectClassBusinessServiceIF<T, D> typeService, String baseVertexClass, String cacheName)
  {
    super(typeService, baseVertexClass, cacheName);
  }

  @Override
  public boolean exists(N object, E edgeType, N parent)
  {
    return getEdge(object, edgeType, parent) != null;
  }

  protected EdgeObject getEdge(N object, E edgeType, N parent)
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
  public boolean exists(E type, N parent, N child, Date startDate, Date endDate)
  {
    return getEdge(type, parent, child, startDate, endDate) != null;
  }

  protected EdgeObject getEdge(E type, N parent, N child, Date startDate, Date endDate)
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
  public boolean exists(E type, String uid)
  {
    String statement = "SELECT FROM " + type.getMdEdgeDAO().getDBClassName();
    statement += " WHERE uid = :uid";

    GraphQuery<EdgeObject> query = new GraphQuery<EdgeObject>(statement);
    query.setParameter("uid", uid);

    return ( query.getSingleResult() != null );
  }

  @Override
  public Optional<EdgeObject> addParent(N object, E type, N parent, String uid, Date startDate, Date endDate, DataSource source)
  {
    return this.addParent(object, type, parent, uid, startDate, endDate, source, true);
  }

  @Override
  public Optional<EdgeObject> addParent(N object, E type, N parent, String uid, Date startDate, Date endDate, DataSource source, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    if (!this.isValidEdge(object, type, parent, startDate, endDate))
    {
      throw new UnsupportedOperationException();
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
  public void removeParent(N object, E type, N parent, Date startDate, Date endDate)
  {
    this.removeParent(object, type, parent, startDate, endDate, true);
  }

  @Override
  public void removeParent(N object, E type, N parent, Date startDate, Date endDate, boolean validateOrigin)
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
  public Optional<EdgeObject> addChild(N object, E type, N child, String uid, Date startDate, Date endDate, DataSource source)
  {
    return this.addChild(object, type, child, uid, startDate, endDate, source, true);
  }

  @Override
  public Optional<EdgeObject> addChild(N object, E type, N child, String uid, Date startDate, Date endDate, DataSource source, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    if (!this.isValidEdge(child, type, object, startDate, endDate))
    {
      throw new UnsupportedOperationException();
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

  protected boolean isValidEdge(N child, E type, N object, Date startDate, Date endDate)
  {
    return true;
  }

  @Override
  public void removeChild(N object, E type, N child, Date startDate, Date endDate)
  {
    this.removeChild(object, type, child, startDate, endDate, true);
  }

  @Override
  public void removeChild(N object, E type, N child, Date startDate, Date endDate, boolean validateOrigin)
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
  @SuppressWarnings("unchecked")
  public List<N> getParents(V object, E type, Date date)
  {
    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
    statement.append("  SELECT EXPAND(inE('" + type.getMdEdge().getDbClassName() + "')[:date BETWEEN startDate AND endDate].out)");
    statement.append("  FROM :rid " + "\n");
    statement.append(")");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("rid", object.getVertex().getRID());
    query.setParameter("date", date);

    return this.processTraverseResults(query.getResults(), date).stream().sorted((a, b) -> {
      return a.getCode().compareTo(b.getCode());
    }).map(v -> (N) v).collect(Collectors.toList());
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<N> getChildren(V object, E type, Date date)
  {
    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
    statement.append("  SELECT EXPAND(outE('" + type.getMdEdge().getDbClassName() + "')[:date BETWEEN startDate AND endDate].in)");
    statement.append("  FROM :rid " + "\n");
    statement.append(")");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("rid", object.getVertex().getRID());
    query.setParameter("date", date);

    return this.processTraverseResults(query.getResults(), date).stream().sorted((a, b) -> {
      return a.getCode().compareTo(b.getCode());
    }).map(v -> (N) v).collect(Collectors.toList());
  }

  protected boolean isCycle(N child, E type, N parent, Date startDate, Date endDate)
  {
    VertexObject vertex = child.getVertex();
    MdGraphClassDAOIF mdClass = (MdGraphClassDAOIF) vertex.getMdClass();

    StringBuffer statement = new StringBuffer();
    statement.append("SELECT count(*) FROM (");
    statement.append("MATCH {class: " + mdClass.getDBClassName() + ", where: (@rid = :rid)}.(outE('" + type.getMdEdgeDAO().getDBClassName() + "')");
    statement.append(" {where: (:startDate BETWEEN startDate AND endDate OR :endDate BETWEEN startDate AND endDate)}.inV())");
    statement.append(" {as: friend, while: ($depth < 10000)} RETURN friend.code AS code");
    statement.append(")");
    statement.append(" WHERE code = :code");

    GraphQuery<Long> query = new GraphQuery<Long>(statement.toString());
    query.setParameter("rid", vertex.getRID());
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);
    query.setParameter("code", parent.getCode());

    Long count = query.getSingleResult();

    return ( count != null && count > 0 );
  }

  
}
