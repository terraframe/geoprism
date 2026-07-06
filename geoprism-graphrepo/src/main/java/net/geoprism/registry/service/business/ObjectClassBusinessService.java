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
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.metadata.AttributeBooleanType;
import org.commongeoregistry.adapter.metadata.AttributeCharacterType;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.commongeoregistry.adapter.metadata.AttributeDataSourceType;
import org.commongeoregistry.adapter.metadata.AttributeDateType;
import org.commongeoregistry.adapter.metadata.AttributeFloatType;
import org.commongeoregistry.adapter.metadata.AttributeIntegerType;
import org.commongeoregistry.adapter.metadata.AttributeLocalType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdClass;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.Organization;
import net.geoprism.registry.graph.ObjectClass;
import net.geoprism.registry.model.GeoObjectMetadata;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.service.permission.PermissionServiceIF;
import net.geoprism.registry.view.ObjectClassDTO;
import net.geoprism.registry.view.OrganizationGroup;

@Service
public abstract class ObjectClassBusinessService<T extends ObjectClass, D extends ObjectClassDTO> implements ObjectClassBusinessServiceIF<T, D>
{
  @Autowired
  protected PermissionServiceIF permissions;
  
  private String vertexClass;
  
  public ObjectClassBusinessService(String vertexClass)
  {
    this.vertexClass = vertexClass;
  }

  protected abstract void put(T type);

  protected abstract Optional<T> get(String code, Supplier<Optional<T>> supplier);

  @Override
  public void removeAttributeType(T type, String attributeName)
  {
    type.removeAttribute(attributeName);

    // Update the sequence number of the type
    if (type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      type.setSequence(type.getSequence() + 1);
      type.apply();
    }

    this.put(type);

    // If this did not error out then add to the cache
    // Refresh the users session
    Session session = (Session) Session.getCurrentSession();

    if (session != null)
    {
      session.reloadPermissions();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public <K extends AttributeType> K updateAttributeType(T type, AttributeType dto)
  {
    net.geoprism.registry.graph.AttributeType attributeType = this.updateAttributeTypeFromDTO(type, dto);
    type.refresh();

    dto = attributeType.toDTO();

    return (K) dto;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <K extends AttributeType> K createAttributeType(T type, AttributeType dto)
  {
    net.geoprism.registry.graph.AttributeType attributeType = createAttributeTypeFromDTO(type, dto);
    type.refresh();

    dto = attributeType.toDTO();

    // Refresh the users session
    if (Session.getCurrentSession() != null)
    {
      ( (Session) Session.getCurrentSession() ).reloadPermissions();
    }

    return (K) dto;
  }

  protected MdAttributeConcreteDAOIF getMdAttribute(MdClass mdClass, String attributeName)
  {
    MdClassDAOIF mdClassDAO = (MdClassDAOIF) BusinessFacade.getEntityDAO(mdClass);

    return (MdAttributeConcreteDAOIF) mdClassDAO.definesAttribute(attributeName);
  }

  @Transaction
  protected net.geoprism.registry.graph.AttributeType createAttributeTypeFromDTO(T type, AttributeType dto)
  {
    if (type.getAttributeMap().containsKey(dto.getCode()))
    {
      // TODO: Change exception type
      throw new UnsupportedOperationException("Duplicate attribute");
    }

    net.geoprism.registry.graph.AttributeType attributeType = null;

    if (dto.getType().equals(AttributeCharacterType.TYPE))
    {
      attributeType = new net.geoprism.registry.graph.AttributeCharacterType();
    }
    else if (dto.getType().equals(AttributeDateType.TYPE))
    {
      attributeType = new net.geoprism.registry.graph.AttributeDateType();
    }
    else if (dto.getType().equals(AttributeIntegerType.TYPE))
    {
      attributeType = new net.geoprism.registry.graph.AttributeLongType();
    }
    else if (dto.getType().equals(AttributeFloatType.TYPE))
    {
      attributeType = new net.geoprism.registry.graph.AttributeDoubleType();
    }
    else if (dto.getType().equals(AttributeClassificationType.TYPE))
    {
      attributeType = new net.geoprism.registry.graph.AttributeClassificationType();
    }
    else if (dto.getType().equals(AttributeBooleanType.TYPE))
    {
      attributeType = new net.geoprism.registry.graph.AttributeBooleanType();
    }
    else if (dto.getType().equals(AttributeDataSourceType.TYPE))
    {
      attributeType = new net.geoprism.registry.graph.AttributeDataSourceType();
    }
    else if (dto.getType().equals(AttributeLocalType.TYPE))
    {
      attributeType = new net.geoprism.registry.graph.AttributeLocalType();
    }
    else
    {
      throw new UnsupportedOperationException();
    }

    attributeType.setObjectType(type);
    attributeType.fromDTO(dto);
    attributeType.setIsDefault(false);
    attributeType.apply();

    // Update the sequence number of the type
    if (type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      type.setSequence(type.getSequence() + 1);
      type.apply();

    }

    this.put(type);

    // mdAttribute.setAttributeName(dto.getName());
    // mdAttribute.setValue(MdAttributeConcreteInfo.REQUIRED,
    // Boolean.toString(dto.isRequired()));
    //
    // if (dto.isUnique())
    // {
    // mdAttribute.addIndexType(MdAttributeIndices.UNIQUE_INDEX);
    // }
    //
    // RegistryLocalizedValueConverter.populate(mdAttribute.getDisplayLabel(),
    // dto.getLabel());
    // RegistryLocalizedValueConverter.populate(mdAttribute.getDescription(),
    // dto.getDescription());
    //
    // mdAttribute.setDefiningMdClass(mdClass);
    // mdAttribute.apply();
    //
    return attributeType;
  }

  /**
   * Creates an {@link MdAttributeConcrete} for the given {@link MdBusiness}
   * from the given {@link AttributeType}
   * 
   * @pre assumes no attribute has been defined on the type with the given name.
   * 
   * @param mdBusiness
   *          Type to receive attribute definition
   * @param attributeType
   *          newly defined attribute
   * 
   * @return {@link AttributeType}
   */
  @Transaction
  protected net.geoprism.registry.graph.AttributeType updateAttributeTypeFromDTO(T type, AttributeType dto)
  {
    Optional<net.geoprism.registry.graph.AttributeType> optional = type.getAttribute(dto.getCode());

    if (optional.isPresent())
    {
      net.geoprism.registry.graph.AttributeType attribute = optional.get();
      attribute.fromDTO(dto);
      attribute.apply();

      this.put(type);

      return attribute;
    }

    return null;
  }

  @Override
  public Optional<T> getByCode(String code)
  {
    return this.get(code, () -> {

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
  public List<OrganizationGroup<D>> listByOrg()
  {
    List<OrganizationGroup<D>> response = new LinkedList<>();

    List<ServerOrganization> organizations = ServerOrganization.getSortedOrganizations().stream() //
        .filter(org -> org.getEnabled()) //
        .collect(Collectors.toList());

    for (ServerOrganization org : organizations)
    {
      MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(this.vertexClass);

      StringBuilder statement = new StringBuilder();
      statement.append("SELECT FROM " + mdVertex.getDBClassName());
      statement.append(" WHERE organization = :organization");
      statement.append(" ORDER BY code DESC");

      GraphQuery<T> query = new GraphQuery<T>(statement.toString());
      query.setParameter("organization", org.getGraphOrganization().getRID());

      List<D> types = query.getResults().stream() //
          .filter(type -> this.permissions.canRead(type)) //
          .sorted((a, b) -> a.getLabel().getValue().compareTo(b.getLabel().getValue())) //
          .map(type -> this.toDTO(type)) //
          .toList();

      OrganizationGroup<D> group = new OrganizationGroup<D>();
      group.setOid(org.getOid());
      group.setCode(org.getCode());
      group.setLabel(org.getDisplayLabel().getValue());
      group.setWrite(this.permissions.isAdmin(org));
      group.setTypes(types);

      response.add(group);
    }

    return response;
  }

  @Override
  public List<T> getAll()
  {
    List<T> response = new LinkedList<>();

    ServerOrganization.getSortedOrganizations().stream().filter(o -> this.permissions.isMember(o)).forEach(org -> {

      MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(this.vertexClass);

      StringBuilder statement = new StringBuilder();
      statement.append("SELECT FROM " + mdVertex.getDBClassName());
      statement.append(" WHERE organization = :organization");
      statement.append(" ORDER BY code DESC");

      GraphQuery<T> query = new GraphQuery<T>(statement.toString());
      query.setParameter("organization", org.getGraphOrganization().getRID());

      query.getResults().stream() //
          .sorted((a, b) -> a.getLabel().getValue().compareTo(b.getLabel().getValue())) //
          .forEach(type -> response.add(type)); //
    });

    return response;
  }

  @Override
  public List<T> getForOrganization(ServerOrganization organization)
  {
    return this.getForOrganization(organization.getOrganization());
  }

  @Override
  public List<T> getForOrganization(Organization organization)
  {
    ServerOrganization org = ServerOrganization.get(organization);

    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(this.vertexClass);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE organization = :organization");
    statement.append(" ORDER BY code DESC");

    GraphQuery<T> query = new GraphQuery<T>(statement.toString());
    query.setParameter("organization", org.getGraphOrganization().getRID());

    return query.getResults();
  }

  @Override
  public T getByMdVertex(MdVertexDAOIF mdVertex)
  {
    return this.get(mdVertex.getOid(), () -> {
      MdVertexDAOIF table = MdVertexDAO.getMdVertexDAO(this.vertexClass);

      StringBuilder statement = new StringBuilder();
      statement.append("SELECT FROM " + table.getDBClassName());
      statement.append(" WHERE mdVertex = :mdVertex");
      statement.append(" ORDER BY code DESC");

      GraphQuery<T> query = new GraphQuery<T>(statement.toString());
      query.setParameter("mdVertex", mdVertex.getOid());

      return Optional.ofNullable(query.getSingleResult());
    }).orElse(null);
  }

}
