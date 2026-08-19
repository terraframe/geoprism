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

import java.util.Optional;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

import net.geoprism.registry.conversion.LocalizedValueConverter;

public class SourceAuthority extends SourceAuthorityBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 241315036;

  public SourceAuthority()
  {
    super();
  }

  public LocalizedValue getLabel()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(SourceAuthority.DISPLAYLABEL));
  }

  public LocalizedValue getDescriptionLV()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(SourceAuthority.DESCRIPTION));
  }

  public static Optional<SourceAuthority> getByCode(String code)
  {
    MdVertexDAOIF metadata = MdVertexDAO.getMdVertexDAO(SourceAuthority.CLASS);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + metadata.getDBClassName());
    statement.append(" WHERE " + metadata.definesAttribute(SourceAuthority.CODE).getColumnName() + " = :code");

    GraphQuery<SourceAuthority> query = new GraphQuery<SourceAuthority>(statement.toString());
    query.setParameter("code", code);

    return Optional.ofNullable(query.getSingleResult());
  }

  public static Optional<SourceAuthority> getByRid(String rid)
  {
    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + rid);

    GraphQuery<SourceAuthority> query = new GraphQuery<SourceAuthority>(statement.toString());

    return Optional.ofNullable(query.getSingleResult());
  }

}
