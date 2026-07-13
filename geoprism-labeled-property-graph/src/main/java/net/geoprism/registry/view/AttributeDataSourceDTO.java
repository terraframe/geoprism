/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Common Geo Registry Adapter(tm).
 *
 * Common Geo Registry Adapter(tm) is free software: you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Common Geo Registry Adapter(tm) is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Common Geo Registry Adapter(tm). If not, see
 * <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.view;

import java.util.List;

import org.commongeoregistry.adapter.metadata.AttributeDataSourceType;

public class AttributeDataSourceDTO extends AttributeDTO<String>
{

  @Override
  public String getType()
  {
    return AttributeDataSourceType.TYPE;
  }

  public static AttributeDataSourceDTO of(String value)
  {
    AttributeDataSourceDTO dto = new AttributeDataSourceDTO();
    dto.setChangeOverTime(false);
    dto.setValue(value);

    return dto;
  }

  public static AttributeDataSourceDTO of(ValueOverTimeEntryDTO<String> entry)
  {
    AttributeDataSourceDTO dto = new AttributeDataSourceDTO();
    dto.setChangeOverTime(true);
    dto.addValue(entry);

    return dto;
  }

  public static AttributeDataSourceDTO of(List<ValueOverTimeEntryDTO<String>> entries)
  {
    AttributeDataSourceDTO dto = new AttributeDataSourceDTO();
    dto.setChangeOverTime(true);
    entries.forEach(dto::addValue);

    return dto;
  }
}
