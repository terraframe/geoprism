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

import java.util.Date;
import java.util.List;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.registry.JsonCollectors;
import net.geoprism.registry.graph.BusinessEdgeType;
import net.geoprism.registry.graph.BusinessType;
import net.geoprism.registry.model.BusinessObject;
import net.geoprism.registry.model.GeoObjectMetadata;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.graph.VertexComponent;
import net.geoprism.registry.service.business.BusinessEdgeTypeBusinessServiceIF;
import net.geoprism.registry.service.business.BusinessObjectBusinessServiceIF;
import net.geoprism.registry.service.business.BusinessTypeBusinessServiceIF;
import net.geoprism.registry.service.business.GeoObjectBusinessServiceIF;
import net.geoprism.registry.view.BusinessTypeDTO;
import net.geoprism.registry.view.ObjectAtTimeDTO;

@Service
public class BusinessObjectService extends ObjectService<BusinessObject, BusinessType, BusinessTypeDTO>
{
  @Autowired
  private BusinessEdgeTypeBusinessServiceIF edgeService;

  @Autowired
  private GeoObjectBusinessServiceIF        geoObjectService;

  public BusinessObjectService(BusinessTypeBusinessServiceIF typeService, BusinessObjectBusinessServiceIF objectService)
  {
    super(typeService, objectService);
  }

  protected BusinessObjectBusinessServiceIF getObjectService()
  {
    return (BusinessObjectBusinessServiceIF) super.getObjectService();
  }

  protected BusinessTypeBusinessServiceIF getTypeService()
  {
    return (BusinessTypeBusinessServiceIF) super.getTypeService();
  }

  @Request(RequestType.SESSION)
  public JsonArray getParents(String sessionId, String businessTypeCode, String code, String businessEdgeTypeCode, Date date)
  {
    BusinessType type = this.getTypeService().getByCodeOrThrow(businessTypeCode);
    BusinessEdgeType relationshipType = this.edgeService.getByCodeOrThrow(businessEdgeTypeCode);

    BusinessObject object = this.getObjectService().getByCode(type, code).orElseThrow(() -> {
      net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
      ex.setTypeLabel(type.getLabel().getValue());
      ex.setDataIdentifier(code);
      ex.setAttributeLabel(GeoObjectMetadata.get().getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));

      return ex;
    });

    List<VertexComponent> parents = this.getObjectService().getParents(object, relationshipType, date);

    return serialize(date, parents, relationshipType.getIsParentGeoObject());
  }

  @Request(RequestType.SESSION)
  public JsonArray getChildren(String sessionId, String businessTypeCode, String code, String businessEdgeTypeCode, Date date)
  {
    BusinessType type = this.getTypeService().getByCodeOrThrow(businessTypeCode);
    BusinessEdgeType relationshipType = this.edgeService.getByCodeOrThrow(businessEdgeTypeCode);

    BusinessObject object = this.getObjectService().getByCode(type, code).orElseThrow(() -> {
      net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
      ex.setTypeLabel(type.getLabel().getValue());
      ex.setDataIdentifier(code);
      ex.setAttributeLabel(GeoObjectMetadata.get().getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));

      return ex;
    });

    List<VertexComponent> children = this.getObjectService().getChildren(object, relationshipType, date);

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

      ObjectAtTimeDTO dto = this.getObjectService().toDTO((BusinessObject) parent, date);

      return JsonParser.parseString(ObjectAtTimeDTO.toJson(dto));
    }).collect(JsonCollectors.toJsonArray());
  }

}
