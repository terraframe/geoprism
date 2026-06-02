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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.DataNotFoundException;
import net.geoprism.registry.OriginException;
import net.geoprism.registry.service.business.BusinessEdgeTypeBusinessServiceIF;
import net.geoprism.registry.view.BusinessEdgeTypeView;

@Service
public class BusinessEdgeTypeService implements BusinessEdgeTypeServiceIF
{
  @Autowired
  private BusinessEdgeTypeBusinessServiceIF service;

  @Override
  @Request(RequestType.SESSION)
  public void delete(String sessionId, String code)
  {
    BusinessEdgeType type = this.service.getByCodeOrThrow(code);

    if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      throw new OriginException();
    }

    this.service.delete(type);
  }

  @Override
  @Request(RequestType.SESSION)
  public List<BusinessEdgeTypeView> getAll(String sessionId)
  {
    return this.service.getAll().stream().map(type -> this.service.toDTO(type)).toList();
  }

  @Override
  @Request(RequestType.SESSION)
  public BusinessEdgeTypeView getByCode(String sessionId, String code)
  {
    return this.service.getByCode(code).map(type -> this.service.toDTO(type)).orElseThrow(() -> {
      throw new DataNotFoundException("Unable to find business edge type with code [" + code + "]");
    });
  }

  @Override
  @Request(RequestType.SESSION)
  public BusinessEdgeTypeView apply(String sessionId, BusinessEdgeTypeView object)
  {
    if (StringUtils.isBlank(object.getOid()))
    {
      BusinessEdgeType type = this.service.create(object);

      return this.service.toDTO(type);
    }

    BusinessEdgeType type = this.service.getByCodeOrThrow(object.getCode());

    this.service.update(type, object);

    return this.service.toDTO(type);
  }
}
