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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import tools.jackson.databind.json.JsonMapper;

@JsonTypeName(ConceptClassDTO.TYPE)
public class ConceptClassDTO extends ObjectClassDTO
{
  public static final String TYPE = "concept-class";

  @JsonProperty("type")
  private final String       type = TYPE;

  public static String toJson(ConceptClassDTO dto)
  {
    JsonMapper mapper = JsonMapper.shared();
    return mapper.writeValueAsString(dto);
  }

  public static String toJson(List<ConceptClassDTO> dtos)
  {
    JsonMapper mapper = JsonMapper.shared();
    return mapper.writeValueAsString(dtos);
  }

  public static ConceptClassDTO parseJson(String json)
  {
    JsonMapper mapper = JsonMapper.shared();
    return mapper.readValue(json, ConceptClassDTO.class);
  }

  public static List<ConceptClassDTO> parseList(String json)
  {
    JsonMapper mapper = JsonMapper.shared();
    return mapper.readerForListOf(ConceptClassDTO.class).readValue(json);
  }
}
