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

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.serialization.LocalizedValueDeserializer;
import org.commongeoregistry.adapter.serialization.LocalizedValueSerializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ConceptSetDTO
{
  private String         oid;

  private String         code;

  @JsonSerialize(using = LocalizedValueSerializer.class)
  @JsonDeserialize(using = LocalizedValueDeserializer.class)
  private LocalizedValue displayLabel;

  @JsonSerialize(using = LocalizedValueSerializer.class)
  @JsonDeserialize(using = LocalizedValueDeserializer.class)
  private LocalizedValue description;

  private DiscreteType   discreteType;

  private List<String>   conceptClasses   = new LinkedList<>();

  private List<String>   conceptEdgeTypes = new LinkedList<>();

  private String         origin;

  private Long           sequence;

  private String         rootTerm;

  public String getOid()
  {
    return oid;
  }

  public void setOid(String oid)
  {
    this.oid = oid;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
    this.code = code;
  }

  public LocalizedValue getDisplayLabel()
  {
    return displayLabel;
  }

  public void setDisplayLabel(LocalizedValue displayLabel)
  {
    this.displayLabel = displayLabel;
  }

  public LocalizedValue getDescription()
  {
    return description;
  }

  public void setDescription(LocalizedValue description)
  {
    this.description = description;
  }

  public DiscreteType getDiscreteType()
  {
    return discreteType;
  }

  public void setDiscreteType(DiscreteType discreteType)
  {
    this.discreteType = discreteType;
  }

  public List<String> getConceptClasses()
  {
    return conceptClasses;
  }

  public void setConceptClasses(List<String> conceptClasses)
  {
    this.conceptClasses = conceptClasses;
  }

  public List<String> getConceptEdgeTypes()
  {
    return conceptEdgeTypes;
  }

  public void setConceptEdgeTypes(List<String> conceptEdgeTypes)
  {
    this.conceptEdgeTypes = conceptEdgeTypes;
  }

  public String getRootTerm()
  {
    return rootTerm;
  }

  public void setRootTerm(String rootTerm)
  {
    this.rootTerm = rootTerm;
  }

  public String getOrigin()
  {
    return origin;
  }

  public void setOrigin(String origin)
  {
    this.origin = origin;
  }

  public Long getSequence()
  {
    return sequence;
  }

  public void setSequence(Long sequence)
  {
    this.sequence = sequence;
  }

  public boolean hasOrigin()
  {
    return !StringUtils.isBlank(this.getOrigin());
  }

  public boolean hasOid()
  {
    return !StringUtils.isBlank(this.getOid());
  }

  public boolean hasSequence()
  {
    return this.sequence != null;
  }

  public static String toJson(ConceptSetDTO dto)
  {
    try
    {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(dto);
    }
    catch (JsonProcessingException e)
    {
      throw new RuntimeException(e);
    }
  }

  public static <T extends ConceptSetDTO> String toJson(List<T> dtos)
  {
    try
    {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(dtos);
    }
    catch (JsonProcessingException e)
    {
      throw new RuntimeException(e);
    }
  }

  public static ConceptSetDTO parseJson(String json)
  {
    try
    {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(json, ConceptSetDTO.class);
    }
    catch (JsonProcessingException e)
    {
      throw new RuntimeException(e);
    }
  }

  public static List<ConceptSetDTO> parseList(String json)
  {
    try
    {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readerForListOf(ConceptSetDTO.class).readValue(json);
    }
    catch (JsonProcessingException e)
    {
      throw new RuntimeException(e);
    }
  }

}
