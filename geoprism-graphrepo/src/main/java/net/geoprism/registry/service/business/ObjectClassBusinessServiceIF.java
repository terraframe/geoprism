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

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.commongeoregistry.adapter.metadata.AttributeType;
import org.springframework.stereotype.Component;

import com.runwaysdk.dataaccess.MdVertexDAOIF;

import net.geoprism.registry.Organization;
import net.geoprism.registry.graph.ObjectClass;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.view.ObjectClassDTO;
import net.geoprism.registry.view.OrganizationGroup;

@Component
public interface ObjectClassBusinessServiceIF<T extends ObjectClass, D extends ObjectClassDTO>
{
  public void delete(T type);

  public T apply(D dto);

  public <K extends AttributeType> K createAttributeType(T type, AttributeType attributeType);

  public <K extends AttributeType> K updateAttributeType(T serverType, AttributeType dto);

  public void removeAttributeType(T type, String attributeName);

  public T get(String oid);

  public Optional<T> getByCode(String code);

  public T getByCodeOrThrow(String code);

  public T getByMdVertex(MdVertexDAOIF mdVertex);

  public List<T> getAll();

  public List<OrganizationGroup<D>> listByOrg();

  public List<T> getForOrganization(ServerOrganization organization);

  public List<T> getForOrganization(Organization organization);

  D toDTO(T type);

  D toDTO(T type, boolean includeAttribute, boolean flattenLocalAttributes);

  D toDTO(T type, boolean includeAttribute, boolean flattenLocalAttributes, Predicate<net.geoprism.registry.graph.AttributeType> filter);

}
