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

import java.util.Date;
import java.util.List;

import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.BusinessType;
import net.geoprism.registry.JsonCollectors;
import net.geoprism.registry.model.BusinessObject;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.graph.VertexComponent;
import net.geoprism.registry.service.business.BusinessEdgeTypeBusinessServiceIF;
import net.geoprism.registry.service.business.BusinessObjectBusinessServiceIF;
import net.geoprism.registry.service.business.BusinessTypeBusinessServiceIF;
import net.geoprism.registry.service.business.GeoObjectBusinessServiceIF;

@Service
public class BusinessObjectService
{
  @Autowired
  private BusinessTypeBusinessServiceIF     typeService;

  @Autowired
  private BusinessEdgeTypeBusinessServiceIF edgeService;

  @Autowired
  private BusinessObjectBusinessServiceIF   objectService;

  @Autowired
  private GeoObjectBusinessServiceIF        geoObjectService;

  @Request(RequestType.SESSION)
  public JsonObject get(String sessionId, String businessTypeCode, String code)
  {
    BusinessType type = this.typeService.getByCodeOrThrow(businessTypeCode);
    BusinessObject object = this.objectService.getByCode(type, code);

    return this.objectService.toJSON(object);
  }

  @Request(RequestType.SESSION)
  public JsonObject getTypeAndObject(String sessionId, String businessTypeCode, String code)
  {
    BusinessType type = this.typeService.getByCodeOrThrow(businessTypeCode);
    BusinessObject object = this.objectService.getByCode(type, code);

    JsonObject response = new JsonObject();
    response.add("type", this.typeService.toJSON(type, true, false));
    response.add("object", this.objectService.toJSON(object));

    return response;
  }

  @Request(RequestType.SESSION)
  public JsonArray getParents(String sessionId, String businessTypeCode, String code, String businessEdgeTypeCode, Date date)
  {
    BusinessType type = this.typeService.getByCodeOrThrow(businessTypeCode);
    BusinessEdgeType relationshipType = this.edgeService.getByCodeOrThrow(businessEdgeTypeCode);

    BusinessObject object = this.objectService.getByCode(type, code);

    List<VertexComponent> parents = this.objectService.getParents(object, relationshipType, date);

    return serialize(date, parents, relationshipType.getIsParentGeoObject());
  }

  @Request(RequestType.SESSION)
  public JsonArray getChildren(String sessionId, String businessTypeCode, String code, String businessEdgeTypeCode, Date date)
  {
    BusinessType type = this.typeService.getByCodeOrThrow(businessTypeCode);
    BusinessEdgeType relationshipType = this.edgeService.getByCodeOrThrow(businessEdgeTypeCode);

    BusinessObject object = this.objectService.getByCode(type, code);

    List<VertexComponent> children = this.objectService.getChildren(object, relationshipType, date);

    return serialize(date, children, relationshipType.getIsChildGeoObject());
  }

  public JsonArray serialize(Date date, List<VertexComponent> parents, Boolean isGeoObject)
  {
    return parents.stream().map(parent -> {
      if (isGeoObject)
      {
        GeoObject geoObject = this.geoObjectService.toGeoObject((ServerGeoObjectIF) parent, date);
        return geoObject.toJSON();
      }

      return this.objectService.toJSON((BusinessObject) parent);
    }).collect(JsonCollectors.toJsonArray());
  }

}
