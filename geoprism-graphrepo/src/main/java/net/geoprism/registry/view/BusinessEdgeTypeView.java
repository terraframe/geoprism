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

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.google.gson.JsonObject;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.BusinessType;

public class BusinessEdgeTypeView
{
  private String         organizationCode;

  private String         code;

  private LocalizedValue label;

  private LocalizedValue description;

  private String         parentTypeCode;

  private String         childTypeCode;

  private String         origin;

  private Long           seq;

  public BusinessEdgeTypeView()
  {
    this.origin = GeoprismProperties.getOrigin();
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

  public static BusinessEdgeTypeView fromJSON(JsonObject object)
  {
    String code = object.get(BusinessEdgeType.CODE).getAsString();
    String parentTypeCode = object.get(BusinessEdgeType.PARENTTYPE).getAsString();
    String childTypeCode = object.get(BusinessEdgeType.CHILDTYPE).getAsString();
    LocalizedValue label = LocalizedValue.fromJSON(object.getAsJsonObject(BusinessEdgeType.JSON_LABEL));
    LocalizedValue description = LocalizedValue.fromJSON(object.getAsJsonObject(BusinessEdgeType.DESCRIPTION));
    String organizationCode = object.get(BusinessType.ORGANIZATION).getAsString();
    Long seq = object.has(BusinessType.SEQ) ? object.get(BusinessType.SEQ).getAsLong() : 0L;

    BusinessEdgeTypeView view = new BusinessEdgeTypeView();
    view.setCode(code);
    view.setParentTypeCode(parentTypeCode);
    view.setChildTypeCode(childTypeCode);
    view.setLabel(label);
    view.setDescription(description);
    view.setOrganizationCode(organizationCode);
    view.setSeq(seq);

    return view;
  }

}
