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
package net.geoprism.registry.service.request;

import java.util.List;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.metadata.GraphTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.registry.model.GraphType;
import net.geoprism.registry.service.business.GraphTypeBusinessServiceIF;

@Service
public class GraphTypeService implements GraphTypeServiceIF
{
  @Autowired
  protected GraphTypeBusinessServiceIF service;

  /**
   * Returns the {@link GraphType}s with the given codes or all
   * {@link GraphType}s if no codes are provided.
   * 
   * @param sessionId
   * @param codes
   *          codes of the {@link GraphType}s.
   * @param context
   * @return the {@link GraphType}s with the given codes or all
   *         {@link GraphType}s if no codes are provided.
   */
  @Request(RequestType.SESSION)
  public List<GraphTypeDTO> getGraphTypes(String sessionId, String[] codes)
  {
    List<GraphType> types = service.getTypes(codes);

    return types.stream().map(type -> service.toDTO(type)).collect(Collectors.toList());
  }
}
