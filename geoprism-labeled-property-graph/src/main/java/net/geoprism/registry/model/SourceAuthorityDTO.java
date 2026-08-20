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
package net.geoprism.registry.model;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.serialization.LocalizedValueDeserializer;
import org.commongeoregistry.adapter.serialization.LocalizedValueSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class SourceAuthorityDTO
{
  private String         oid;

  private String         code;

  @JsonSerialize(using = LocalizedValueSerializer.class)
  @JsonDeserialize(using = LocalizedValueDeserializer.class)
  private LocalizedValue label;

  @JsonSerialize(using = LocalizedValueSerializer.class)
  @JsonDeserialize(using = LocalizedValueDeserializer.class)
  private LocalizedValue description;

  private AuthorityType  authorityType;

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

  public LocalizedValue getLabel()
  {
    return label;
  }

  public void setLabel(LocalizedValue label)
  {
    this.label = label;
  }

  public LocalizedValue getDescription()
  {
    return description;
  }

  public void setDescription(LocalizedValue description)
  {
    this.description = description;
  }

  public AuthorityType getAuthorityType()
  {
    return authorityType;
  }

  public void setAuthorityType(AuthorityType authorityType)
  {
    this.authorityType = authorityType;
  }
}
