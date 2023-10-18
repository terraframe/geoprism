/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism Registry(tm). If not, see <http://www.gnu.org/licenses/>.
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
import net.geoprism.registry.DateUtil;
import net.geoprism.registry.model.BusinessObject;
import net.geoprism.registry.model.graph.VertexServerGeoObject;
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
    BusinessType type = this.typeService.getByCode(businessTypeCode);
    BusinessObject object = this.objectService.getByCode(type, code);

    return this.objectService.toJSON(object);
  }

  @Request(RequestType.SESSION)
  public JsonObject getTypeAndObject(String sessionId, String businessTypeCode, String code)
  {
    BusinessType type = this.typeService.getByCode(businessTypeCode);
    BusinessObject object = this.objectService.getByCode(type, code);

    JsonObject response = new JsonObject();
    response.add("type", this.typeService.toJSON(type, true, false));
    response.add("object", this.objectService.toJSON(object));

    return response;
  }

  @Request(RequestType.SESSION)
  public JsonArray getParents(String sessionId, String businessTypeCode, String code, String businessEdgeTypeCode)
  {
    BusinessType type = this.typeService.getByCode(businessTypeCode);
    BusinessEdgeType relationshipType = this.edgeService.getByCode(businessEdgeTypeCode);

    BusinessObject object = this.objectService.getByCode(type, code);

    List<BusinessObject> parents = this.objectService.getParents(object, relationshipType);
    return parents.stream().map(parent -> {
      return this.objectService.toJSON(parent);
    }).collect(() -> new JsonArray(), (array, element) -> array.add(element), (listA, listB) -> {
    });
  }

  @Request(RequestType.SESSION)
  public JsonArray getChildren(String sessionId, String businessTypeCode, String code, String businessEdgeTypeCode)
  {
    BusinessType type = this.typeService.getByCode(businessTypeCode);
    BusinessEdgeType relationshipType = this.edgeService.getByCode(businessEdgeTypeCode);

    BusinessObject object = this.objectService.getByCode(type, code);

    List<BusinessObject> children = this.objectService.getChildren(object, relationshipType);
    return children.stream().map(parent -> {
      return this.objectService.toJSON(parent);
    }).collect(() -> new JsonArray(), (array, element) -> array.add(element), (listA, listB) -> {
    });
  }

  @Request(RequestType.SESSION)
  public JsonArray getGeoObjects(String sessionId, String businessTypeCode, String code, String dateStr)
  {
    BusinessType type = this.typeService.getByCode(businessTypeCode);
    BusinessObject object = this.objectService.getByCode(type, code);
    Date date = DateUtil.parseDate(dateStr, true);

    List<VertexServerGeoObject> geoObjects = this.objectService.getGeoObjects(object);
    return geoObjects.stream().map(child -> {
      GeoObject geoObject = this.geoObjectService.toGeoObject(child, date);
      return geoObject.toJSON();
    }).collect(() -> new JsonArray(), (array, element) -> array.add(element), (listA, listB) -> {
    });
  }

}
