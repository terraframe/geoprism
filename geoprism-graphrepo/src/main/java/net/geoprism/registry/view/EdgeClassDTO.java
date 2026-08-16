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
import org.commongeoregistry.adapter.metadata.GraphTypeDTO;

import net.geoprism.configuration.GeoprismProperties;

public abstract class EdgeClassDTO extends GraphTypeDTO
{
  private static final long serialVersionUID = 8230387872268568863L;

  private String            organizationCode;

  private String            childType;

  private String            parentType;

  public EdgeClassDTO()
  {
    super();

    this.setOrigin(GeoprismProperties.getOrigin());
  }

  public EdgeClassDTO(String typeCode, String code, LocalizedValue label, LocalizedValue description)
  {
    super(typeCode, code, label, description);

    this.setOrigin(GeoprismProperties.getOrigin());
  }

  public abstract String getType();

  public String getOrganizationCode()
  {
    return organizationCode;
  }

  public void setOrganizationCode(String organizationCode)
  {
    this.organizationCode = organizationCode;
  }

  public String getChildType()
  {
    return childType;
  }

  public void setChildType(String childType)
  {
    this.childType = childType;
  }

  public String getParentType()
  {
    return parentType;
  }

  public void setParentType(String parentType)
  {
    this.parentType = parentType;
  }

}
