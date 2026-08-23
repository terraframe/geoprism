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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.geoprism.registry.graph.BusinessEdgeType;
import net.geoprism.registry.service.business.BusinessEdgeTypeBusinessServiceIF;
import net.geoprism.registry.service.business.EdgeClassBusinessServiceIF;
import net.geoprism.registry.view.BusinessEdgeTypeDTO;

@Service
public class BusinessEdgeTypeService extends EdgeClassService<BusinessEdgeType, BusinessEdgeTypeDTO> implements BusinessEdgeTypeServiceIF
{
  @Autowired
  private BusinessEdgeTypeBusinessServiceIF service;

  @Override
  protected EdgeClassBusinessServiceIF<BusinessEdgeType, BusinessEdgeTypeDTO> getService()
  {
    return this.service;
  }
}
