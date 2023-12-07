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
package net.geoprism.registry.graph;

import java.util.List;

import org.commongeoregistry.adapter.metadata.OrganizationDTO;

import com.google.gson.JsonElement;
import com.runwaysdk.business.graph.EdgeObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.registry.Organization;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.view.JsonSerializable;

public class GraphOrganization extends GraphOrganizationBase implements JsonSerializable
{
  public static String      EDGE_CLASS       = "net.geoprism.registry.graph.OrganizationHierarchy";

  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1448688633;

  public GraphOrganization()
  {
    super();
  }

  public EdgeObject getEdge(GraphOrganization parent)
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(EDGE_CLASS);

    String statement = "SELECT FROM " + mdEdge.getDBClassName();
    statement += " WHERE out = :parent";
    statement += " AND in = :child";

    GraphQuery<EdgeObject> query = new GraphQuery<EdgeObject>(statement);
    query.setParameter("parent", parent.getRID());
    query.setParameter("child", this.getRID());

    return query.getSingleResult();
  }

  private boolean exists(GraphOrganization parent)
  {
    EdgeObject edge = this.getEdge(parent);

    return ( edge != null );
  }

  @Transaction
  public void addParent(GraphOrganization parent)
  {
    if (this.isNew() || !this.exists(parent))
    {
      MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(EDGE_CLASS);
      EdgeObject edge = this.addParent(parent, mdEdge);
      edge.apply();
    }
  }

  @Transaction
  public void addChild(GraphOrganization child)
  {
    child.addParent(this);
  }

  @Transaction
  public void removeParent(GraphOrganization parent)
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(EDGE_CLASS);

    this.removeParent(parent, mdEdge);
  }

  @Transaction
  public void removeChild(GraphOrganization child)
  {
    child.removeParent(this);
  }

  @Transaction
  public void move(GraphOrganization newParent)
  {
    GraphOrganization oldParent = this.getParent();

    if (oldParent != null)
    {
      this.removeParent(oldParent);
    }

    this.addParent(newParent);
  }

  public List<GraphOrganization> getChildren()
  {
    return this.getChildren(20, 1);
  }

  public Integer getCount()
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(EDGE_CLASS);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT out('" + mdEdge.getDBClassName() + "').size()");
    statement.append(" FROM :rid");

    GraphQuery<Integer> query = new GraphQuery<Integer>(statement.toString());
    query.setParameter("rid", this.getRID());

    return query.getSingleResult();
  }

  public List<GraphOrganization> getChildren(Integer pageSize, Integer pageNumber)
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(EDGE_CLASS);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT EXPAND(out('" + mdEdge.getDBClassName() + "')");
    statement.append(") FROM :rid");
    statement.append(" ORDER BY code");

    if (pageSize != null && pageNumber != null)
    {
      int first = pageSize * ( pageNumber - 1 );
      int rows = pageSize;

      statement.append(" SKIP " + first + " LIMIT " + rows);
    }

    GraphQuery<GraphOrganization> query = new GraphQuery<GraphOrganization>(statement.toString());
    query.setParameter("rid", this.getRID());

    return query.getResults();
  }

  public static List<GraphOrganization> getRoots()
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(EDGE_CLASS);
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(GraphOrganization.CLASS);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE IN('" + mdEdge.getDBClassName() + "').size() = :size");

    GraphQuery<GraphOrganization> query = new GraphQuery<GraphOrganization>(statement.toString());
    query.setParameter("size", 0);

    return query.getResults();
  }

  public GraphOrganization getParent()
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(EDGE_CLASS);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT EXPAND(in('" + mdEdge.getDBClassName() + "')");
    statement.append(") FROM :rid");

    GraphQuery<GraphOrganization> query = new GraphQuery<GraphOrganization>(statement.toString());
    query.setParameter("rid", this.getRID());

    return query.getSingleResult();
  }

  public List<GraphOrganization> getAncestors(String code)
  {
    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(EDGE_CLASS);
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(GraphOrganization.CLASS);
    MdAttributeDAOIF mdAttribute = mdVertex.definesAttribute(GraphOrganization.CODE);

    GraphQuery<GraphOrganization> query = null;

    if (code != null && code.length() > 0)
    {
      StringBuilder statement = new StringBuilder();
      statement.append("SELECT expand($res)");
      statement.append(" LET $a = (TRAVERSE in(\"" + mdEdge.getDBClassName() + "\") FROM :rid WHILE (" + mdAttribute.getColumnName() + " != :code))");
      statement.append(", $b = (SELECT FROM " + mdVertex.getDBClassName() + " WHERE " + mdAttribute.getColumnName() + " = :code)");
      statement.append(", $res = (UNIONALL($a,$b))");

      query = new GraphQuery<GraphOrganization>(statement.toString());
      query.setParameter("rid", this.getRID());
      query.setParameter("code", code);
    }
    else
    {
      StringBuilder statement = new StringBuilder();
      statement.append("TRAVERSE in(\"" + mdEdge.getDBClassName() + "\") FROM :rid");

      query = new GraphQuery<GraphOrganization>(statement.toString());
      query.setParameter("rid", this.getRID());
    }

    return query.getResults();
  }

  public static GraphOrganization get(Organization org)
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(GraphOrganization.CLASS);
    MdAttributeDAOIF mdAttribute = mdVertex.definesAttribute(GraphOrganization.ORGANIZATION);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE " + mdAttribute.getColumnName() + " = :organization");

    GraphQuery<GraphOrganization> query = new GraphQuery<GraphOrganization>(statement.toString());
    query.setParameter("organization", org.getOid());

    return query.getSingleResult();
  }

  public OrganizationDTO toDTO()
  {
    return new OrganizationDTO(this.getCode(), LocalizedValueConverter.convert(this.getEmbeddedComponent(DISPLAYLABEL)), LocalizedValueConverter.convert(this.getEmbeddedComponent(CONTACTINFO)));
  }

  @Override
  public JsonElement toJSON()
  {
    return this.toDTO().toJSON();
  }
}
