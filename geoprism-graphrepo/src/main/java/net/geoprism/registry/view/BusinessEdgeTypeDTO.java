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
package net.geoprism.registry.view;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.geoprism.registry.model.EdgeDirection;

public class BusinessEdgeTypeDTO extends EdgeClassDTO
{
  private static final long  serialVersionUID = 530365668208123034L;

  public static final String GEO_OBJECT_TYPE  = "~#GO#~";

  public BusinessEdgeTypeDTO()
  {
    super();
  }

  public BusinessEdgeTypeDTO(String code, LocalizedValue label, LocalizedValue description)
  {
    super(TypeClass.BUSINESS_EDGE.getCode(), code, label, description);
  }

  public String getType()
  {
    return TypeClass.BUSINESS_EDGE.getCode();
  }

  @JsonIgnore
  public boolean hasGeoObject()
  {
    return isChildGeObjectType() || isParentGeoObjectType();
  }

  public boolean isParentGeoObjectType()
  {
    return this.getParentType().equals(GEO_OBJECT_TYPE);
  }

  @JsonIgnore
  public boolean isChildGeObjectType()
  {
    return this.getChildType().equals(GEO_OBJECT_TYPE);
  }

  public static BusinessEdgeTypeDTO build(String organizationCode, String code, LocalizedValue label, LocalizedValue description, String parentTypeCode, String childTypeCode)
  {
    BusinessEdgeTypeDTO dto = new BusinessEdgeTypeDTO();
    dto.setCode(code);
    dto.setParentType(parentTypeCode);
    dto.setChildType(childTypeCode);
    dto.setLabel(label);
    dto.setDescription(description);
    dto.setOrganizationCode(organizationCode);
    dto.setSeq(0L);

    return dto;
  }

  public static BusinessEdgeTypeDTO build(String organizationCode, String code, LocalizedValue label, LocalizedValue description, String typeCode, EdgeDirection direction)
  {
    BusinessEdgeTypeDTO dto = new BusinessEdgeTypeDTO();
    dto.setCode(code);
    dto.setLabel(label);
    dto.setDescription(description);
    dto.setOrganizationCode(organizationCode);

    if (direction.equals(EdgeDirection.PARENT))
    {
      dto.setParentType(BusinessEdgeTypeDTO.GEO_OBJECT_TYPE);
      dto.setChildType(typeCode);
    }
    else
    {
      dto.setChildType(BusinessEdgeTypeDTO.GEO_OBJECT_TYPE);
      dto.setParentType(typeCode);
    }
    dto.setSeq(0L);

    return dto;
  }

}
