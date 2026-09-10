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

import org.springframework.stereotype.Component;

import net.geoprism.graph.ConceptClassSnapshot;
import net.geoprism.graph.ConceptEdgeTypeSnapshot;
import net.geoprism.graph.ConceptSetSnapshot;
import net.geoprism.graph.ConceptSetSnapshotHasClass;
import net.geoprism.graph.ConceptSetSnapshotHasEdge;
import net.geoprism.registry.model.SnapshotContainer;
import net.geoprism.registry.view.ConceptSetDTO;

@Component
public interface ConceptSetSnapshotBusinessServiceIF
{

  void delete(ConceptSetSnapshot snapshot);

  ConceptSetSnapshot create(SnapshotContainer<?> version, ConceptSetDTO type);

  ConceptSetSnapshot get(SnapshotContainer<?> version, String code);

  ConceptSetDTO toDTO(ConceptSetSnapshot snapshot);

  public ConceptSetSnapshotHasClass addConceptClass(ConceptSetSnapshot set, ConceptClassSnapshot conceptClass);

  public ConceptSetSnapshotHasEdge addConceptEdgeType(ConceptSetSnapshot set, ConceptEdgeTypeSnapshot conceptEdgeType);

  public List<ConceptClassSnapshot> getConceptClasses(ConceptSetSnapshot set);

  public List<ConceptEdgeTypeSnapshot> getConceptEdgeTypes(ConceptSetSnapshot set);

}
