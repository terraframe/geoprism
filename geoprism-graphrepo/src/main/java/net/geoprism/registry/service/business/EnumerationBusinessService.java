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
import net.geoprism.registry.graph.Enumeration;
import net.geoprism.registry.view.ConceptSetDTO;

@Service
public class EnumerationBusinessService extends ConceptSetBusinessService<Enumeration, ConceptSetDTO> implements EnumerationBusinessServiceIF
{
  public EnumerationBusinessService()
  {
    super(Enumeration.CLASS);
  }

  @Override
  protected Enumeration createInstance()
  {
    return new Enumeration();
  }

  @Override
  protected ConceptSetDTO createDTO()
  {
    return new ConceptSetDTO();
  }

  @Override
  public EdgeObject addConceptClass(Enumeration type, ConceptClass conceptClass)
  {
    List<EdgeObject> existing = this.getConceptClassEdges(type);

    if (existing.size() > 0)
    {
      throw new UnsupportedOperationException("An enumeration may only be assigned a single concept class");
    }

    return super.addConceptClass(type, conceptClass);
  }

  @Override
  public EdgeObject addConceptEdgeType(Enumeration type, ConceptEdgeType conceptEdgeType)
  {
    List<EdgeObject> existing = this.getConceptEdgeTypeEdges(type);

    if (existing.size() > 0)
    {
      throw new UnsupportedOperationException("An enumeration may only be assigned a single concept edge class");
    }

    return super.addConceptEdgeType(type, conceptEdgeType);
  }

}
