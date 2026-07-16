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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.serialization.LocalizedValueDeserializer;
import org.commongeoregistry.adapter.serialization.LocalizedValueSerializer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, // use logical type name
    include = JsonTypeInfo.As.PROPERTY, property = "type")

@JsonSubTypes({ //
    @JsonSubTypes.Type(value = BusinessTypeDTO.class, name = BusinessTypeDTO.TYPE), //
    @JsonSubTypes.Type(value = ConceptClassDTO.class, name = ConceptClassDTO.TYPE), //
})
public abstract class ObjectClassDTO
{
  private String              oid;

  private String              code;

  private String              organization;

  private String              organizationLabel;

  private String              origin;

  private Long                sequence;

  @JsonSerialize(using = LocalizedValueSerializer.class)
  @JsonDeserialize(using = LocalizedValueDeserializer.class)
  private LocalizedValue      displayLabel;

  private List<AttributeType> attributes;

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

  public String getOrganization()
  {
    return organization;
  }

  public void setOrganization(String organization)
  {
    this.organization = organization;
  }

  public String getOrganizationLabel()
  {
    return organizationLabel;
  }

  public void setOrganizationLabel(String organizationLabel)
  {
    this.organizationLabel = organizationLabel;
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

  public LocalizedValue getDisplayLabel()
  {
    return displayLabel;
  }

  public void setDisplayLabel(LocalizedValue displayLabel)
  {
    this.displayLabel = displayLabel;
  }

  public List<AttributeType> getAttributes()
  {
    return attributes;
  }

  public void setAttributes(List<AttributeType> attributes)
  {
    this.attributes = attributes;
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

}
