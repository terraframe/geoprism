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
package net.geoprism.registry.model.graph;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.map.HashedMap;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;

import net.geoprism.registry.model.EdgeConstant;
import net.geoprism.registry.model.ServerChildGraphNode;
import net.geoprism.registry.model.ServerGraphNode;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.model.ServerParentGraphNode;

public class ServerHierarchyStrategy extends AbstractGraphStrategy implements GraphStrategy
{
  private ServerHierarchyType hierarchy;

  public ServerHierarchyStrategy(ServerHierarchyType hierarchy)
  {
    super(hierarchy);
    this.hierarchy = hierarchy;
  }

  @SuppressWarnings("unchecked")
  @Override
  public ServerChildGraphNode getChildren(VertexServerGeoObject parent, Boolean recursive, Date date, String boundsWKT, Long skip, Long limit)
  {
    ServerChildGraphNode tnRoot = new ServerChildGraphNode(parent, this.hierarchy, date, null, null, null);
    
    if (limit != null && limit <= 0)
    {
      return tnRoot;
    }

    Map<String, Object> parameters = new HashedMap<String, Object>();
    parameters.put("rid", parent.getVertex().getRID());

    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
    statement.append("SELECT EXPAND( out(");
    statement.append("'" + this.hierarchy.getObjectEdge().getDBClassName() + "'");
    statement.append(")");    

    if (date != null)
    {
      statement.append("[:date BETWEEN startDate AND endDate]");
      parameters.put("date", date);
    }

    statement.append(") FROM :rid");
    
    if (boundsWKT != null)
    {
      statement = new StringBuilder(this.wrapQueryWithBounds(statement.toString(), "in", date, boundsWKT, parameters));
    }
    
    if (skip != null)
    {
      if (recursive)
      {
        throw new UnsupportedOperationException();
      }
      
      statement.append(" SKIP " + skip);
    }
    
    if (limit != null)
    {
      statement.append(" LIMIT " + limit);
    }
    
    statement.append(") ");

    
    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString(), parameters);

    List<VertexServerGeoObject> children = VertexServerGeoObject.processTraverseResults(query.getResults(), date);
    
    long resultsCount = children.size();

    for (VertexServerGeoObject child : children)
    {
      ServerChildGraphNode tnParent;

      if (recursive && (limit == null || limit - resultsCount > 0))
      {
        tnParent = this.getChildren(child, recursive, date, boundsWKT, null, (limit == null ? null : limit - resultsCount));
        tnParent.setOid(UUID.randomUUID().toString());
        
        resultsCount += tnParent.getChildren().size();
      }
      else
      {
        tnParent = new ServerChildGraphNode(child, this.hierarchy, date, null, UUID.randomUUID().toString(), null);
      }

      tnRoot.addChild(tnParent);
    }

    return tnRoot;
  }

  @SuppressWarnings("unchecked")
  @Override
  public ServerParentGraphNode getParents(VertexServerGeoObject child, Boolean recursive, Date date, String boundsWKT, Long skip, Long limit)
  {
    ServerParentGraphNode tnRoot = new ServerParentGraphNode(child, this.hierarchy, date, null, null, null);
    
    if (limit != null && limit <= 0)
    {
      return tnRoot;
    }

    Map<String, Object> parameters = new HashedMap<String, Object>();
    parameters.put("rid", child.getVertex().getRID());

    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");    
    statement.append("SELECT EXPAND( in(");
    statement.append("'" + this.hierarchy.getObjectEdge().getDBClassName() + "'");
    statement.append(")");

    if (date != null)
    {
      statement.append("[:date BETWEEN startDate AND endDate]");
      parameters.put("date", date);
    }

    statement.append(") FROM :rid");
    
    if (boundsWKT != null)
    {
      statement = new StringBuilder(this.wrapQueryWithBounds(statement.toString(), "out", date, boundsWKT, parameters));
    }
    
    if (skip != null)
    {
      if (recursive)
      {
        throw new UnsupportedOperationException();
      }
      
      statement.append(" SKIP " + skip);
    }
    
    if (limit != null)
    {
      statement.append(" LIMIT " + limit);
    }
    
    statement.append(") ");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString(), parameters);

    List<VertexServerGeoObject> parents = VertexServerGeoObject.processTraverseResults(query.getResults(), date);
    
    long resultsCount = parents.size();

    for (VertexServerGeoObject parent : parents)
    {
      ServerParentGraphNode tnParent;

      if (recursive && (limit == null || limit - resultsCount > 0))
      {
        tnParent = this.getParents(parent, recursive, date, boundsWKT, null, (limit == null ? null : limit - resultsCount));
        tnParent.setOid(UUID.randomUUID().toString());
        
        resultsCount += tnParent.getParents().size();
      }
      else
      {
        tnParent = new ServerParentGraphNode(parent, this.hierarchy, date, null, UUID.randomUUID().toString(), null);
      }

      tnRoot.addParent(tnParent);
    }

    return tnRoot;
  }

  @SuppressWarnings("unchecked")
  @Override
  public ServerGraphNode addChild(VertexServerGeoObject geoObject, VertexServerGeoObject child, Date startDate, Date endDate, String uid, boolean validate)
  {
    throw new UnsupportedOperationException();
  }

  @SuppressWarnings("unchecked")
  @Override
  public ServerGraphNode addParent(VertexServerGeoObject geoObject, VertexServerGeoObject parent, Date startDate, Date endDate, String uid, boolean validate)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void removeParent(VertexServerGeoObject geoObject, VertexServerGeoObject parent, Date startDate, Date endDate)
  {
    throw new UnsupportedOperationException();
  }

}
