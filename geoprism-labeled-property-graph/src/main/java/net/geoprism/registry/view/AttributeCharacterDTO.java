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

import org.commongeoregistry.adapter.metadata.AttributeCharacterType;

public class AttributeCharacterDTO extends AttributeDTO<String>
{

  @Override
  public String getType()
  {
    return AttributeCharacterType.TYPE;
  }

  public static AttributeCharacterDTO of(String value)
  {
    AttributeCharacterDTO dto = new AttributeCharacterDTO();
    dto.setChangeOverTime(false);
    dto.setValue(value);

    return dto;
  }

  public static AttributeCharacterDTO of(ValueOverTimeEntryDTO<String> entry)
  {
    AttributeCharacterDTO dto = new AttributeCharacterDTO();
    dto.setChangeOverTime(true);
    dto.addValue(entry);

    return dto;
  }

  public static AttributeCharacterDTO of(List<ValueOverTimeEntryDTO<String>> entries)
  {
    AttributeCharacterDTO dto = new AttributeCharacterDTO();
    dto.setChangeOverTime(true);
    entries.forEach(dto::addValue);

    return dto;
  }
}
