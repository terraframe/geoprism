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

import org.commongeoregistry.adapter.metadata.GraphTypeDTO;
import org.springframework.stereotype.Component;

import net.geoprism.graph.GraphTypeReference;
import net.geoprism.registry.model.GraphType;

@Component
public interface GraphTypeBusinessServiceIF
{

  public GraphTypeDTO toDTO(GraphType graphType);

  public List<GraphType> getTypes(String... typeCodes);

  GraphType getByCode(String relationshipType, String code);

  GraphType resolve(GraphTypeReference ref);

}
