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
package net.geoprism.registry.service.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.DataNotFoundException;
import net.geoprism.registry.JsonCollectors;
import net.geoprism.registry.OriginException;
import net.geoprism.registry.service.business.BusinessEdgeTypeBusinessServiceIF;

@Service
public class BusinesEdgeTypeService implements BusinessEdgeTypeServiceIF
{
  @Autowired
  private BusinessEdgeTypeBusinessServiceIF service;

  @Override
  @Request(RequestType.SESSION)
  public void update(String sessionId, String code, JsonObject object)
  {
    BusinessEdgeType type = this.service.getByCodeOrThrow(code);

    if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      throw new OriginException();
    }

    this.service.update(type, object);
  }

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
  public JsonArray getAll(String sessionId)
  {
    return this.service.getAll().stream().map(type -> this.service.toJSON(type)).collect(JsonCollectors.toJsonArray());
  }

  @Override
  @Request(RequestType.SESSION)
  public JsonObject getByCode(String sessionId, String code)
  {
    return this.service.getByCode(code).map(type -> this.service.toJSON(type)).orElseThrow(() -> {
      throw new DataNotFoundException("Unable to find business edge type with code [" + code + "]");
    });
  }

  @Override
  @Request(RequestType.SESSION)
  public JsonObject create(String sessionId, JsonObject object)
  {
    BusinessEdgeType type = this.service.create(object);

    return this.service.toJSON(type);
  }
}
