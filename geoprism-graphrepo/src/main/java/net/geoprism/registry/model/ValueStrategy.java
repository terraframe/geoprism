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
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTimeCollection;

public interface ValueStrategy
{

  public void setValue(VertexObject vertex, Object value);

  public void setValue(VertexObject vertex, Map<String, AttributeState> valueNodeMap, Object value, Date startDate, Date endDate);

  public void setValue(VertexObject vertex, Map<String, AttributeState> valueNodeMap, Object value, Date startDate, Date endDate, boolean validate);

  public boolean isModified(VertexObject vertex, Map<String, AttributeState> valueNodeMap);

  public <T> T getValue(VertexObject vertex, Map<String, AttributeState> valueNodeMap, Date date);

  public ValueOverTimeCollection getValueOverTimeCollection(VertexObject vertex, Map<String, AttributeState> valueNodeMap);

  public void setValuesOverTime(VertexObject vertex, Map<String, AttributeState> valueNodeMap, ValueOverTimeCollection collection);

  public List<MdAttributeDAOIF> getValueAttributes();
}
