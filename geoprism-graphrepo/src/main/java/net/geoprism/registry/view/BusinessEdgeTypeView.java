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
import org.commongeoregistry.adapter.serialization.LocalizedValueDeserializer;
import org.commongeoregistry.adapter.serialization.LocalizedValueSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.model.GraphType;

public class BusinessEdgeTypeView
{
  public static final String GEO_OBJECT_TYPE = "~#GO#~";

  private String             organizationCode;

  private String             oid;

  private String             code;

  @JsonSerialize(using = LocalizedValueSerializer.class)
  @JsonDeserialize(using = LocalizedValueDeserializer.class)
  private LocalizedValue     label;

  @JsonSerialize(using = LocalizedValueSerializer.class)
  @JsonDeserialize(using = LocalizedValueDeserializer.class)
  private LocalizedValue     description;

  private String             parentTypeCode;

  private String             childTypeCode;

  private String             origin;

  private Long               seq;

  public BusinessEdgeTypeView()
  {
    this.origin = GeoprismProperties.getOrigin();
    this.seq = 0L;
  }

  public String getOid()
  {
    return oid;
  }

  public void setOid(String oid)
  {
    this.oid = oid;
  }

  public String getOrganizationCode()
  {
    return organizationCode;
  }

  public void setOrganizationCode(String organizationCode)
  {
    this.organizationCode = organizationCode;
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

  public String getParentTypeCode()
  {
    return parentTypeCode;
  }

  public void setParentTypeCode(String parentTypeCode)
  {
    this.parentTypeCode = parentTypeCode;
  }

  public String getChildTypeCode()
  {
    return childTypeCode;
  }

  public void setChildTypeCode(String childTypeCode)
  {
    this.childTypeCode = childTypeCode;
  }

  public String getOrigin()
  {
    return origin;
  }

  public void setOrigin(String origin)
  {
    this.origin = origin;
  }

  public Long getSeq()
  {
    return seq;
  }

  public void setSeq(Long seq)
  {
    this.seq = seq;
  }

  public String getType()
  {
    return GraphType.BUSINESS_EDGE_TYPE;
  }

  @JsonIgnore
  public boolean hasGeoObject()
  {
    return isChildGeObjectType() || isParentGeoObjectType();
  }

  public boolean isParentGeoObjectType()
  {
    return this.getParentTypeCode().equals(GEO_OBJECT_TYPE);
  }

  @JsonIgnore
  public boolean isChildGeObjectType()
  {
    return this.getChildTypeCode().equals(GEO_OBJECT_TYPE);
  }

  public static BusinessEdgeTypeView build(String organizationCode, String code, LocalizedValue label, LocalizedValue description, String parentTypeCode, String childTypeCode)
  {
    BusinessEdgeTypeView view = new BusinessEdgeTypeView();
    view.setCode(code);
    view.setParentTypeCode(parentTypeCode);
    view.setChildTypeCode(childTypeCode);
    view.setLabel(label);
    view.setDescription(description);
    view.setOrganizationCode(organizationCode);
    view.setSeq(0L);

    return view;
  }

}
