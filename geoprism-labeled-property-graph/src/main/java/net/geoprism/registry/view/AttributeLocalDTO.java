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

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeLocalType;

public class AttributeLocalDTO extends AttributeDTO<LocalizedValue>
{

  @Override
  public String getType()
  {
    return AttributeLocalType.TYPE;
  }

  public static AttributeLocalDTO of(LocalizedValue value)
  {
    AttributeLocalDTO dto = new AttributeLocalDTO();
    dto.setChangeOverTime(false);
    dto.setValue(value);

    return dto;
  }

  public static AttributeLocalDTO of(ValueOverTimeEntryDTO<LocalizedValue> entry)
  {
    AttributeLocalDTO dto = new AttributeLocalDTO();
    dto.setChangeOverTime(true);
    dto.addValue(entry);

    return dto;
  }

  public static AttributeLocalDTO of(List<ValueOverTimeEntryDTO<LocalizedValue>> entries)
  {
    AttributeLocalDTO dto = new AttributeLocalDTO();
    dto.setChangeOverTime(true);
    entries.forEach(dto::addValue);

    return dto;
  }

}
