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
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUConceptSet ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service.business;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.runwaysdk.business.graph.EdgeObject;

import net.geoprism.registry.graph.ConceptClass;
import net.geoprism.registry.graph.ConceptEdgeType;
import net.geoprism.registry.graph.ConceptSet;
import net.geoprism.registry.view.ConceptSetDTO;

@Component
public interface ConceptSetBusinessServiceIF
{
  public void delete(ConceptSet type);

  public ConceptSet apply(ConceptSetDTO dto);

  public Optional<ConceptSet> get(String oid);

  public Optional<ConceptSet> getByCode(String code);

  public ConceptSet getByCodeOrThrow(String code);

  public List<ConceptSet> getAll();

  public ConceptSetDTO toDTO(ConceptSet type);

  public EdgeObject addConceptClass(ConceptSet type, ConceptClass conceptClass);

  public EdgeObject addConceptEdgeType(ConceptSet type, ConceptEdgeType conceptEdgeType);

  public List<EdgeObject> getConceptClassEdges(ConceptSet type);

  public List<EdgeObject> getConceptEdgeTypeEdges(ConceptSet type);

  public List<ConceptClass> getConceptClasses(ConceptSet type);

  public List<ConceptEdgeType> getConceptEdgeTypes(ConceptSet type);
}
