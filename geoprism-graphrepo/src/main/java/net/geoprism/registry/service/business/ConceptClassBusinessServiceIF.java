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
import net.geoprism.registry.graph.ConceptClass;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.view.ConceptClassDTO;

@Component
public interface ConceptClassBusinessServiceIF extends ObjectClassBusinessServiceIF<ConceptClass>
{

  void delete(ConceptClass type);

  ConceptClass apply(ConceptClassDTO object);

  Optional<ConceptClass> getByCode(String code);

  ConceptClass getByCodeOrThrow(String code);

  List<ConceptClass> getAll();

  List<ConceptClass> getForOrganization(ServerOrganization organization);

  List<ConceptClass> getForOrganization(Organization organization);

  ConceptClass getByMdVertex(MdVertexDAOIF mdVertex);

  ConceptClass apply(ConceptClass ConceptClass);

  ConceptClassDTO toDTO(ConceptClass type);

  ConceptClassDTO toDTO(ConceptClass type, boolean includeAttribute, boolean flattenLocalAttributes);

  ConceptClassDTO toDTO(ConceptClass type, boolean includeAttribute, boolean flattenLocalAttributes, Predicate<AttributeType> filter);

}
