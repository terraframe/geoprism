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

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import tools.jackson.databind.json.JsonMapper;

@JsonTypeName(BusinessTypeDTO.TYPE)
public class BusinessTypeDTO extends ObjectClassDTO
{
  public static final String TYPE = "business-type";

  @JsonProperty("type")
  private final String       type = TYPE;

  private String             labelAttribute;

  public String getLabelAttribute()
  {
    return labelAttribute;
  }

  public void setLabelAttribute(String labelAttribute)
  {
    this.labelAttribute = labelAttribute;
  }

  public boolean hasLabelAttribute()
  {
    return !StringUtils.isBlank(this.getLabelAttribute());
  }

  public static String toJson(BusinessTypeDTO dto)
  {
    JsonMapper mapper = JsonMapper.shared();
    return mapper.writeValueAsString(dto);
  }

  public static String toJson(List<BusinessTypeDTO> dtos)
  {
    JsonMapper mapper = JsonMapper.shared();
    return mapper.writeValueAsString(dtos);
  }

  public static BusinessTypeDTO parseJson(String json)
  {
    JsonMapper mapper = JsonMapper.shared();
    return mapper.readValue(json, BusinessTypeDTO.class);
  }

  public static List<BusinessTypeDTO> parseList(String json)
  {
    JsonMapper mapper = JsonMapper.shared();
    return mapper.readerForListOf(BusinessTypeDTO.class).readValue(json);
  }
}
