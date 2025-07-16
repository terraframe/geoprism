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

import java.util.List;

import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.OriginException;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.permission.PermissionContext;
import net.geoprism.registry.service.business.GeoObjectTypeBusinessServiceIF;
import net.geoprism.registry.service.business.ServiceFactory;

@Service
public class GeoObjectTypeService implements GeoObjectTypeServiceIF
{
  @Autowired
  private GeoObjectTypeBusinessServiceIF service;

  @Override
  @Request(RequestType.SESSION)
  public List<GeoObjectType> getAncestors(String sessionId, String code, String hierarchyCode, Boolean includeInheritedTypes, Boolean includeChild)
  {
    return this.service.getAncestors(code, hierarchyCode, includeInheritedTypes, includeChild);
  }

  @Override
  @Request(RequestType.SESSION)
  public void deleteGeoObjectType(String sessionId, String code)
  {
    this.service.deleteGeoObjectType(code);
  }

  @Override
  @Request(RequestType.SESSION)
  public AttributeType createAttributeType(String sessionId, String geoObjectTypeCode, String attributeTypeJSON)
  {
    return this.service.createAttributeType(geoObjectTypeCode, attributeTypeJSON);
  }

  @Override
  @Request(RequestType.SESSION)
  public AttributeType updateAttributeType(String sessionId, String geoObjectTypeCode, String attributeTypeJSON)
  {
    return this.service.updateAttributeType(geoObjectTypeCode, attributeTypeJSON);
  }

  @Override
  @Request(RequestType.SESSION)
  public void deleteAttributeType(String sessionId, String gtId, String attributeName)
  {
    this.service.deleteAttributeType(gtId, attributeName);
  }

  @Override
  @Request(RequestType.SESSION)
  public GeoObjectType updateGeoObjectType(String sessionId, String gtJSON)
  {
    GeoObjectType dto = GeoObjectType.fromJSON(gtJSON, ServiceFactory.getAdapter());
    ServerGeoObjectType type = ServerGeoObjectType.get(dto.getCode());
    
    if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      throw new OriginException();
    }

    return this.service.updateGeoObjectType(type, dto);
  }

  @Override
  @Request(RequestType.SESSION)
  public GeoObjectType createGeoObjectType(String sessionId, String gtJSON)
  {
    return this.service.create(gtJSON).toDTO();
  }

  @Override
  @Request(RequestType.SESSION)
  public GeoObjectType[] getGeoObjectTypes(String sessionId, String[] codes, PermissionContext context)
  {
    List<GeoObjectType> types = this.service.getGeoObjectTypes(codes, context);
    return types.toArray(new GeoObjectType[types.size()]);
  }

}
