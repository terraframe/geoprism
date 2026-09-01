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

import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.springframework.stereotype.Component;

import net.geoprism.registry.graph.ConceptClass;
import net.geoprism.registry.graph.ConceptEdgeType;
import net.geoprism.registry.graph.ConceptSet;
import net.geoprism.registry.model.ConceptObject;
import net.geoprism.registry.view.ConceptClassDTO;
import net.geoprism.registry.view.NodeDTO;
import net.geoprism.registry.view.ObjectOverTimeDTO;

@Component
public interface ConceptObjectBusinessServiceIF extends ObjectEdgeBusinessServiceIF<ConceptObject, ConceptClass, ConceptClassDTO, ConceptEdgeType, ConceptObject>
{
  Optional<ConceptObject> getByCode(ConceptSet conceptSet, String type, String code);

  Optional<ConceptObject> getByCode(AttributeClassificationType attribute, String code);

  List<ConceptObject> search(AttributeClassificationType attribute, String text);

  List<ConceptObject> getChildren(ConceptObject object, AttributeClassificationType attribute, Integer pageSize, Integer pageNumber);

  Integer getChildCount(ConceptObject object, AttributeClassificationType attribute);

  List<ConceptObject> getAncestors(AttributeClassificationType attribute, ConceptObject object);

  NodeDTO<ObjectOverTimeDTO> getAncestorTree(AttributeClassificationType attribute, ConceptObject object, Integer pageSize);
}
