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

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.registry.graph.ObjectClass;
import net.geoprism.registry.model.GeoObjectMetadata;
import net.geoprism.registry.model.graph.ServerObjectVertex;
import net.geoprism.registry.service.business.ObjectBusinessServiceIF;
import net.geoprism.registry.service.business.ObjectClassBusinessServiceIF;
import net.geoprism.registry.view.ObjectAndTypeDTO;
import net.geoprism.registry.view.ObjectClassDTO;
import net.geoprism.registry.view.ObjectOverTimeDTO;

public abstract class ObjectService<V extends ServerObjectVertex, T extends ObjectClass, D extends ObjectClassDTO>
{
  @Autowired
  private ObjectClassBusinessServiceIF<T, D> typeService;

  @Autowired
  private ObjectBusinessServiceIF<V, T, D>   objectService;

  public ObjectService(ObjectClassBusinessServiceIF<T, D> typeService, ObjectBusinessServiceIF<V, T, D> objectService)
  {
    super();
    this.typeService = typeService;
    this.objectService = objectService;
  }

  protected ObjectBusinessServiceIF<V, T, D> getObjectService()
  {
    return objectService;
  }

  protected ObjectClassBusinessServiceIF<T, D> getTypeService()
  {
    return typeService;
  }

  @Request(RequestType.SESSION)
  public ObjectOverTimeDTO get(String sessionId, String typeCode, String code)
  {
    T type = this.typeService.getByCodeOrThrow(typeCode);
    V object = this.objectService.getByCode(type, code).orElseThrow(() -> {
      net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
      ex.setTypeLabel(type.getLabel().getValue());
      ex.setDataIdentifier(code);
      ex.setAttributeLabel(GeoObjectMetadata.get().getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));

      return ex;
    });

    return this.objectService.toDTO(object);
  }

  @Request(RequestType.SESSION)
  public ObjectAndTypeDTO getTypeAndObject(String sessionId, String typeCode, String code)
  {
    T type = this.typeService.getByCodeOrThrow(typeCode);
    V object = this.objectService.getByCode(type, code).orElseThrow(() -> {
      net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
      ex.setTypeLabel(type.getLabel().getValue());
      ex.setDataIdentifier(code);
      ex.setAttributeLabel(GeoObjectMetadata.get().getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));

      return ex;
    });

    D dto = this.typeService.toDTO(type, true, false);

    ObjectAndTypeDTO response = new ObjectAndTypeDTO();
    response.setType(dto);
    response.setObject(this.objectService.toDTO(object));

    return response;
  }

  @Request(RequestType.SESSION)
  public JsonObject data(String sessionId, String typeCode, String json)
  {
    T type = this.typeService.getByCodeOrThrow(typeCode);

    return this.objectService.data(type, JsonParser.parseString(json).getAsJsonObject()).toJSON();
  }

}
