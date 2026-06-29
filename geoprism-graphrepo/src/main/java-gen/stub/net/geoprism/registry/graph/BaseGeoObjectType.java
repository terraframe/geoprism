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
package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.registry.model.ServerOrganization;

public class BaseGeoObjectType extends BaseGeoObjectTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 57875307;

  public BaseGeoObjectType()
  {
    super();
  }

  public static BaseGeoObjectType getByCode(String code)
  {
    MdVertexDAOIF metadata = MdVertexDAO.getMdVertexDAO(BaseGeoObjectType.CLASS);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + metadata.getDBClassName());
    statement.append(" WHERE code = :code");

    GraphQuery<GeoObjectType> query = new GraphQuery<GeoObjectType>(statement.toString());
    query.setParameter("code", code);

    return query.getSingleResult();
  }

  @Override
  public LocalizedValue getLabel()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getOrigin()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Long getSequence()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setSequence(Long sequence)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public MdVertex getMdVertex()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getMdVertexOid()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public ServerOrganization getServerOrganization()
  {
    return null;
  }

}
