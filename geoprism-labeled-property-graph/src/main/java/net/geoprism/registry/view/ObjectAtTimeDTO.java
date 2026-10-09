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
package net.geoprism.registry.view;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.geoprism.registry.view.serialization.DateDeserializer;
import net.geoprism.registry.view.serialization.DateSerializer;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.json.JsonMapper;

public class ObjectAtTimeDTO
{
  private TypeInfo            type;

  private String              code;

  private String              label;

  @JsonSerialize(using = DateSerializer.class)
  @JsonDeserialize(using = DateDeserializer.class)
  private Date                date;

  /**
   * For attributes that do change over time, they will be stored here.
   */
  private Map<String, Object> data = new HashMap<>();

  public TypeInfo getType()
  {
    return type;
  }

  public void setType(TypeInfo type)
  {
    this.type = type;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
    this.code = code;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public Date getDate()
  {
    return date;
  }

  public void setDate(Date date)
  {
    this.date = date;
  }

  public Map<String, Object> getData()
  {
    return data;
  }

  public void setData(Map<String, Object> data)
  {
    this.data = data;
  }

  @JsonIgnore
  public void setValue(String attributeName, Object value)
  {
    this.data.put(attributeName, value);
  }

  @SuppressWarnings("unchecked")
  @JsonIgnore
  public <T> T getValue(String attributeName)
  {
    return (T) this.data.get(attributeName);
  }

  @JsonIgnore
  public boolean has(String attributeName)
  {
    return this.data.containsKey(attributeName);
  }

  @Override
  public String toString()
  {
    return this.getCode();
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof ObjectAtTimeDTO)
    {
      return this.getCode().equals( ( (ObjectAtTimeDTO) obj ).getCode());
    }

    return super.equals(obj);
  }

  public static String toJson(ObjectAtTimeDTO dto)
  {
    JsonMapper mapper = JsonMapper.shared();
    return mapper.writeValueAsString(dto);
  }

  public static String toJson(List<ObjectAtTimeDTO> dtos)
  {
    JsonMapper mapper = JsonMapper.shared();
    return mapper.writeValueAsString(dtos);
  }

  public static ObjectAtTimeDTO parseJson(String json)
  {
    JsonMapper mapper = JsonMapper.shared();
    return mapper.readValue(json, ObjectAtTimeDTO.class);
  }

  public static List<ObjectAtTimeDTO> parseList(String json)
  {
    JsonMapper mapper = JsonMapper.shared();
    return mapper.readerForListOf(ObjectAtTimeDTO.class).readValue(json);
  }

}
