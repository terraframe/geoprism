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

import org.springframework.stereotype.Service;

import com.runwaysdk.business.graph.EdgeObject;

import net.geoprism.registry.graph.ConceptClass;
import net.geoprism.registry.graph.ConceptEdgeType;
import net.geoprism.registry.graph.Ontology;
import net.geoprism.registry.view.ConceptSetDTO;

@Service
public class OntologyBusinessService extends ConceptSetBusinessService<Ontology, ConceptSetDTO> implements OntologyBusinessServiceIF
{
  public OntologyBusinessService()
  {
    super(Ontology.CLASS);
  }

  @Override
  protected Ontology createInstance()
  {
    return new Ontology();
  }

  @Override
  protected ConceptSetDTO createDTO()
  {
    return new ConceptSetDTO();
  }

  @Override
  public EdgeObject addConceptClass(Ontology type, ConceptClass conceptClass)
  {
    List<ConceptClass> existing = this.getConceptClasses(type);

    if (existing.stream().anyMatch(e -> e.getOid().equals(conceptClass.getOid())))
    {
      throw new UnsupportedOperationException("The concept class is already part of the ontology");
    }

    return super.addConceptClass(type, conceptClass);
  }

  @Override
  public EdgeObject addConceptEdgeType(Ontology type, ConceptEdgeType edgeType)
  {
    List<ConceptEdgeType> existing = this.getConceptEdgeTypes(type);

    if (existing.stream().anyMatch(e -> e.getOid().equals(edgeType.getOid())))
    {
      throw new UnsupportedOperationException("The concept class is already part of the ontology");
    }

    return super.addConceptEdgeType(type, edgeType);
  }

}
