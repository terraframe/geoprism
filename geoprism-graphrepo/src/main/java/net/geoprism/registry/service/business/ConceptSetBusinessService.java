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

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.runwaysdk.business.graph.EdgeObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Session;

import net.geoprism.registry.cache.TransactionLRUCache;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.graph.ConceptClass;
import net.geoprism.registry.graph.ConceptEdgeType;
import net.geoprism.registry.graph.ConceptSet;
import net.geoprism.registry.model.EdgeConstant;
import net.geoprism.registry.model.GeoObjectMetadata;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.service.permission.PermissionServiceIF;
import net.geoprism.registry.view.ConceptSetDTO;

@Service
public abstract class ConceptSetBusinessService<T extends ConceptSet, D extends ConceptSetDTO> implements ConceptSetBusinessServiceIF<T, D>
{
  @Autowired
  protected PermissionServiceIF                permissions;

  private final String                         vertexClass;

  private final TransactionLRUCache<String, T> cache;

  public ConceptSetBusinessService(String vertexClass)
  {
    this(vertexClass, UUID.randomUUID().toString());
  }

  public ConceptSetBusinessService(String vertexClass, String cacheName)
  {
    this.vertexClass = vertexClass;

    this.cache = new TransactionLRUCache<String, T>(cacheName, (v) -> {

      return new String[] { v.getCode(), v.getOid() };
    }, 20);

  }

  protected abstract T createInstance();

  protected abstract D createDTO();

  public TransactionLRUCache<String, T> getCache()
  {
    return cache;
  }

  @Override
  @Transaction
  public void delete(T type)
  {
    type.delete();

    this.getCache().remove(type);
  }

  @Override
  @Transaction
  public T apply(D dto)
  {
    T type = this.getByCode(dto.getCode()).orElseGet(() -> {
      T t = this.createInstance();
      t.setCode(dto.getCode());

      return t;
    });

    this.fromDTO(type, dto);

    type.apply();

    this.getCache().put(type);

    return type;
  }

  protected void fromDTO(T type, D dto)
  {
    RegistryLocalizedValueConverter.populate(type, ConceptSet.DISPLAYLABEL, dto.getDisplayLabel());
    RegistryLocalizedValueConverter.populate(type, ConceptSet.DESCRIPTION, dto.getDescription());
  }

  @Override
  public Optional<T> get(String oid)
  {
    return this.cache.get(oid, () -> {

      MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(this.vertexClass);

      StringBuilder statement = new StringBuilder();
      statement.append("SELECT FROM " + mdVertex.getDBClassName());
      statement.append(" WHERE oid = :oid");

      GraphQuery<T> query = new GraphQuery<T>(statement.toString());
      query.setParameter("oid", oid);

      return Optional.ofNullable(query.getSingleResult());
    });
  }

  @Override
  public Optional<T> getByCode(String code)
  {
    return this.cache.get(code, () -> {

      MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(this.vertexClass);

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
      MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(this.vertexClass);

      net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
      ex.setTypeLabel(mdVertex.getDisplayLabel(Session.getCurrentLocale()));
      ex.setDataIdentifier(code);
      ex.setAttributeLabel(GeoObjectMetadata.get().getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));

      return ex;
    });
  }

  @Override
  public List<T> getAll()
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(this.vertexClass);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" ORDER BY code DESC");

    GraphQuery<T> query = new GraphQuery<T>(statement.toString());

    return query.getResults().stream() //
        .sorted((a, b) -> a.getLabel().getValue().compareTo(b.getLabel().getValue())) //
        .toList(); //
  }

  @Override
  public D toDTO(T type)
  {
    D dto = this.createDTO();

    this.toDTO(dto, type);

    return dto;
  }

  protected void toDTO(D dto, T type)
  {
    dto.setCode(type.getCode());
    dto.setDisplayLabel(type.getLabel());
  }

  @Override
  public EdgeObject addConceptClass(T type, ConceptClass conceptClass)
  {
    EdgeObject edge = type.addChild(conceptClass, EdgeConstant.HAS_CONCEPT.getMdEdge());
    edge.apply();

    return edge;
  }

  @Override
  public EdgeObject addConceptEdgeType(T type, ConceptEdgeType conceptEdgeType)
  {
    EdgeObject edge = type.addChild(conceptEdgeType, EdgeConstant.HAS_CONCEPT_EDGE.getMdEdge());
    edge.apply();

    return edge;
  }

  @Override
  public List<EdgeObject> getConceptClassEdges(T type)
  {
    return type.getChildEdges(EdgeConstant.HAS_CONCEPT.getMdEdge(), EdgeObject.class);
  }

  @Override
  public List<EdgeObject> getConceptEdgeTypeEdges(T type)
  {
    return type.getChildEdges(EdgeConstant.HAS_CONCEPT_EDGE.getMdEdge(), EdgeObject.class);
  }

  @Override
  public List<ConceptClass> getConceptClasses(T type)
  {
    return type.getChildren(EdgeConstant.HAS_CONCEPT.getMdEdge(), ConceptClass.class);
  }

  @Override
  public List<ConceptEdgeType> getConceptEdgeTypes(T type)
  {
    return type.getChildren(EdgeConstant.HAS_CONCEPT_EDGE.getMdEdge(), ConceptEdgeType.class);
  }

}
