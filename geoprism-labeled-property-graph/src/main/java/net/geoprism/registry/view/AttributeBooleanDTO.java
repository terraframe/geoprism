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

import org.commongeoregistry.adapter.metadata.AttributeBooleanType;

public class AttributeBooleanDTO extends AttributeDTO<Boolean>
{

  @Override
  public String getType()
  {
    return AttributeBooleanType.TYPE;
  }

  public static AttributeBooleanDTO of(Boolean value)
  {
    AttributeBooleanDTO dto = new AttributeBooleanDTO();
    dto.setChangeOverTime(false);
    dto.setValue(value);

    return dto;
  }

  public static AttributeBooleanDTO of(ValueOverTimeEntryDTO<Boolean> entry)
  {
    AttributeBooleanDTO dto = new AttributeBooleanDTO();
    dto.setChangeOverTime(true);
    dto.addValue(entry);

    return dto;
  }

  public static AttributeBooleanDTO of(List<ValueOverTimeEntryDTO<Boolean>> entries)
  {
    AttributeBooleanDTO dto = new AttributeBooleanDTO();
    dto.setChangeOverTime(true);
    entries.forEach(dto::addValue);

    return dto;
  }

}
