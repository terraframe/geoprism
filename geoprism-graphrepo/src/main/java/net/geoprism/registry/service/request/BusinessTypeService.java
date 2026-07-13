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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.registry.graph.BusinessEdgeType;
import net.geoprism.registry.graph.BusinessType;
import net.geoprism.registry.service.business.BusinessEdgeTypeBusinessServiceIF;
import net.geoprism.registry.service.business.BusinessTypeBusinessServiceIF;
import net.geoprism.registry.view.BusinessEdgeTypeView;
import net.geoprism.registry.view.BusinessTypeDTO;

@Service
public class BusinessTypeService extends ObjectClassService<BusinessType, BusinessTypeDTO> implements BusinessTypeServiceIF
{
  @Autowired
  private BusinessTypeBusinessServiceIF     typeService;

  @Autowired
  private BusinessEdgeTypeBusinessServiceIF edgeService;

  protected BusinessTypeBusinessServiceIF getTypeService()
  {
    return typeService;
  }

  @Override
  @Request(RequestType.SESSION)
  public List<BusinessEdgeTypeView> getEdgeTypes(String sessionId, String businessTypeCode)
  {
    BusinessType type = this.typeService.getByCodeOrThrow(businessTypeCode);
    List<BusinessEdgeType> edgeTypes = this.typeService.getEdgeTypes(type);

    return edgeTypes.stream().map(object -> this.edgeService.toDTO(object)).toList();
  }

}
