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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.GraphTypeDTO;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.metadata.MdEdge;

import net.geoprism.registry.DataNotFoundException;
import net.geoprism.registry.cache.TransactionLRUCache;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.graph.EdgeClass;

public abstract class EdgeClassBusinessService<T extends EdgeClass, D extends GraphTypeDTO> implements EdgeClassBusinessServiceIF<T, D>
{
  private final TransactionLRUCache<String, T> cache;

  private final String                         className;

  public EdgeClassBusinessService(String className)
  {
    this(className, UUID.randomUUID().toString());
  }

  public EdgeClassBusinessService(String className, String cacheName)
  {
    this.className = className;
    this.cache = new TransactionLRUCache<String, T>(cacheName, (v) -> {

      return new String[] { v.getCode(), v.getMdEdgeOid() };
    }, 20);
  }

  protected abstract D createDTO();

  public TransactionLRUCache<String, T> getCache()
  {
    return cache;
  }

  public String getClassName()
  {
    return className;
  }

  @Override
  @Transaction
  public void update(T edgeType, D dto)
  {
    this.update(edgeType, dto.getLabel(), dto.getDescription());
  }

  @Override
  @Transaction
  public void update(T edgeType, LocalizedValue label, LocalizedValue description)
  {
    if (label != null)
    {
      RegistryLocalizedValueConverter.populate(edgeType, EdgeClass.DISPLAYLABEL, label);
    }

    if (description != null)
    {
      RegistryLocalizedValueConverter.populate(edgeType, EdgeClass.DESCRIPTION, description);
    }

    edgeType.setSequence(edgeType.getSequence() + 1);
    edgeType.apply();

    this.cache.put(edgeType);
  }

  @Override
  @Transaction
  public void delete(T edgeType)
  {
    MdEdge mdEdge = edgeType.getMdEdge();

    edgeType.delete();

    mdEdge.delete();

    this.cache.remove(edgeType);
  }

  @Override
  public D toDTO(T edgeType)
  {
    D object = this.createDTO();

    this.populate(edgeType, object);

    return object;
  }

  protected void populate(T edgeType, D dto)
  {
    dto.setOid(edgeType.getOid());
    dto.setCode(edgeType.getCode());
    dto.setOrigin(edgeType.getOrigin());
    dto.setSeq(edgeType.getSequence());
    dto.setLabel(edgeType.getLabel());
    dto.setDescription(edgeType.getDescriptionLV());
  }

  @Override
  public List<T> getAll()
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(this.className);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());

    GraphQuery<T> query = new GraphQuery<T>(statement.toString());

    return query.getResults();
  }

  @Override
  public Optional<T> getByCode(String code)
  {
    return this.cache.get(code, () -> {
      MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(this.className);

      StringBuilder statement = new StringBuilder();
      statement.append("SELECT FROM " + mdVertex.getDBClassName());
      statement.append(" WHERE code = :code");

      GraphQuery<T> query = new GraphQuery<T>(statement.toString());
      query.setParameter("code", code);

      return Optional.ofNullable(query.getSingleResult());
    });

  }

  @Override
  public T getByCodeOrThrow(String code)
  {
    return this.getByCode(code).orElseThrow(() -> {
      throw new DataNotFoundException("Unable to find " + T.CLASS + " with code [" + code + "]");
    });
  }

  @Override
  public Optional<T> getByMdEdge(MdEdge mdEdge)
  {
    return this.cache.get(mdEdge.getOid(), () -> {
      MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(this.className);

      StringBuilder statement = new StringBuilder();
      statement.append("SELECT FROM " + mdVertex.getDBClassName());
      statement.append(" WHERE mdEdge = :mdEdge");

      GraphQuery<T> query = new GraphQuery<T>(statement.toString());
      query.setParameter("mdEdge", mdEdge.getOid());

      return Optional.ofNullable(query.getSingleResult());

    });
  }

  protected void createPermissions(MdEdgeDAO mdEdgeDAO)
  {
    // None in the graph repo
  }
}
