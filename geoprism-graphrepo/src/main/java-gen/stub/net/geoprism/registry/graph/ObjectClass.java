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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.registry.model.ServerElement;
import net.geoprism.registry.model.graph.ObjectClassIF;

public abstract class ObjectClass extends ObjectClassBase implements ServerElement, ObjectClassIF
{
  @SuppressWarnings("unused")
  private static final long   serialVersionUID = 57875307;

  // Cached attributes
  private List<AttributeType> attributes;

  public ObjectClass()
  {
    super();
  }

  // TODO: Should these attributes be moved into the object class?
  public abstract String getOrigin();

  public abstract Long getSequence();

  public abstract void setSequence(Long sequence);

  public abstract MdVertex getMdVertex();

  public abstract String getMdVertexOid();

  public Optional<AttributeType> getAttribute(String attributeName)
  {
    return this.getAttributes().stream().filter(t -> t.getCode().equals(attributeName)).findFirst();
  }

  public Map<String, AttributeType> getAttributeMap()
  {
    return this.getAttributes().stream().collect(Collectors.toMap(t -> t.getCode(), t -> t));
  }

  public Map<String, org.commongeoregistry.adapter.metadata.AttributeType> getAttributeMapAsDTO()
  {
    return this.getAttributes().stream().map(t -> t.toDTO()).collect(Collectors.toMap(t -> t.getCode(), t -> t));
  }

  public List<AttributeType> getAttributes()
  {
//    if (this.attributes == null)
    {
      MdVertexDAOIF mdVertexDAO = MdVertexDAO.getMdVertexDAO(AttributeType.CLASS);
      MdAttributeDAOIF mdAttribute = mdVertexDAO.definesAttribute(AttributeType.OBJECTTYPE);

      StringBuilder statement = new StringBuilder();
      statement.append("SELECT FROM " + mdVertexDAO.getDBClassName());
      statement.append(" WHERE " + mdAttribute.getColumnName() + ".oid = :objectType");

      GraphQuery<AttributeType> query = new GraphQuery<AttributeType>(statement.toString());
      query.setParameter("objectType", this.getOid());

      this.attributes = query.getResults();
    }

    return this.attributes;
  }

  public synchronized void removeAttribute(String attributeName)
  {
    this.getAttribute(attributeName).ifPresent(attributeType -> {
      attributeType.delete();

      this.getAttributes().remove(attributeType);
    });
  }

  public void refresh()
  {
    this.attributes = null;
  }

}
