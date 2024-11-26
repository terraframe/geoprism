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

import java.util.Date;
import java.util.Map;

import com.runwaysdk.business.graph.VertexObject;

import net.geoprism.registry.graph.AttributeType;

public abstract class AbstractValueStrategy implements ValueStrategy
{
  private AttributeType type;

  public AbstractValueStrategy(AttributeType type)
  {
    this.type = type;
  }

  public AttributeType getType()
  {
    return type;
  }
  
  @Override
  public void setValue(VertexObject vertex, Map<String, AttributeState> valueNodeMap, Object value, Date startDate, Date endDate)
  {
    this.setValue(vertex, valueNodeMap, value, startDate, endDate, true);
  }

  public void setValue(VertexObject vertex, Object value)
  {
    this.setValue(vertex, null, value, null, null, true);
  }
}
