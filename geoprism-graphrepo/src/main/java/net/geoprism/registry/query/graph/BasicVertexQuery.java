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
package net.geoprism.registry.query.graph;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.ProgrammingErrorException;

import net.geoprism.registry.model.EdgeConstant;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.graph.VertexServerGeoObject;

public class BasicVertexQuery
{
  private ServerGeoObjectType    type;

  private Date                   date;

  private BasicVertexRestriction restriction;

  private Integer                limit;

  private Long                   skip;

  public BasicVertexQuery(ServerGeoObjectType type, Date date)
  {
    this.type = type;
    this.date = date;
  }

  public ServerGeoObjectType getType()
  {
    return type;
  }

  public void setType(ServerGeoObjectType type)
  {
    this.type = type;
  }

  public BasicVertexRestriction getRestriction()
  {
    return restriction;
  }

  public void setRestriction(BasicVertexRestriction restriction)
  {
    this.restriction = restriction;
  }

  public Integer getLimit()
  {
    return limit;
  }

  public void setLimit(Integer limit)
  {
    this.limit = limit;
  }

  public Long getSkip()
  {
    return skip;
  }

  public void setSkip(Long skip)
  {
    this.skip = skip;
  }

  public Date getDate()
  {
    return date;
  }

  public GraphQuery<VertexObject> getQuery()
  {
    HashMap<String, Object> parameters = new HashMap<String, Object>();

    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
    statement.append("SELECT FROM " + this.type.getMdVertex().getDBClassName());

    if (this.restriction != null)
    {
      this.restriction.restrict(statement, parameters);
    }

    statement.append(" ORDER BY code ASC");

    if (this.skip != null)
    {
      statement.append(" SKIP " + this.skip);
    }

    if (this.limit != null)
    {
      statement.append(" LIMIT " + this.limit);
    }

    statement.append(")");

    return new GraphQuery<VertexObject>(statement.toString(), parameters);
  }

  public GraphQuery<Long> getCountQuery()
  {
    HashMap<String, Object> parameters = new HashMap<String, Object>();

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT COUNT(*) FROM " + this.type.getMdVertex().getDBClassName());

    if (this.restriction != null)
    {
      this.restriction.restrict(statement, parameters);
    }

    return new GraphQuery<Long>(statement.toString(), parameters);
  }

  public ServerGeoObjectIF getSingleResult()
  {
    GraphQuery<VertexObject> query = this.getQuery();

    List<ServerGeoObjectIF> results = VertexServerGeoObject.processTraverseResults(query.getResults(), this.date);

    if (results.size() == 0)
    {
      return null;
    }
    else if (results.size() == 1)
    {
      return results.get(0);
    }
    else
    {
      throw new ProgrammingErrorException("Multiple results were returned when only one is allowed");
    }
  }

  public List<ServerGeoObjectIF> getResults()
  {
    GraphQuery<VertexObject> query = this.getQuery();

    List<VertexObject> results = query.getResults();

    return VertexServerGeoObject.processTraverseResults(results, this.date);
  }

  public Long getCount()
  {
    return this.getCountQuery().getSingleResult();
  }

}
