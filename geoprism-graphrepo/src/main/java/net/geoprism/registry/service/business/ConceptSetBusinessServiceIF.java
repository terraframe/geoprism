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

import org.springframework.stereotype.Component;

import com.runwaysdk.business.graph.EdgeObject;

import net.geoprism.registry.graph.ConceptClass;
import net.geoprism.registry.graph.ConceptEdgeType;
import net.geoprism.registry.graph.ConceptSet;
import net.geoprism.registry.view.ConceptSetDTO;

@Component
public interface ConceptSetBusinessServiceIF<T extends ConceptSet, D extends ConceptSetDTO>
{
  public void delete(T type);

  public T apply(D dto);

  public Optional<T> get(String oid);

  public Optional<T> getByCode(String code);

  public T getByCodeOrThrow(String code);

  public List<T> getAll();

  public D toDTO(T type);

  public EdgeObject addConceptClass(T type, ConceptClass conceptClass);

  public EdgeObject addConceptEdgeType(T type, ConceptEdgeType conceptEdgeType);

  public List<EdgeObject> getConceptClassEdges(T type);

  public List<EdgeObject> getConceptEdgeTypeEdges(T type);

  public List<ConceptClass> getConceptClasses(T type);

  public List<ConceptEdgeType> getConceptEdgeTypes(T type);
}
