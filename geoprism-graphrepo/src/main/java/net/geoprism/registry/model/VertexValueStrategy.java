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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTimeCollection;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

import net.geoprism.registry.graph.AttributeType;

/**
 * Strategy used for setting a value directly on the vertex object as opposed to
 * an object value node
 * 
 * @author jsmethie
 */
public class VertexValueStrategy extends AbstractValueStrategy implements ValueStrategy
{

  public VertexValueStrategy(AttributeType type)
  {
    super(type);
  }

  @Override
  public void setValue(VertexObject vertex, Map<String, AttributeState> valueNodeMap, Object value, Date startDate, Date endDate, boolean validate)
  {
    vertex.setValue(this.getType().getCode(), value);
  }

  @Override
  public <T> T getValue(VertexObject vertex, Map<String, AttributeState> valueNodeMap, Date date)
  {
    return vertex.getObjectValue(this.getType().getCode());
  }

  @Override
  public boolean isModified(VertexObject vertex, Map<String, AttributeState> valueNodeMap)
  {
    return vertex.isModified(this.getType().getCode());
  }

  @Override
  public ValueOverTimeCollection getValueOverTimeCollection(VertexObject vertex, Map<String, AttributeState> valueNodeMap)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setValuesOverTime(VertexObject vertex, Map<String, AttributeState> valueNodeMap, ValueOverTimeCollection collection)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<MdAttributeDAOIF> getValueAttributes()
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.get(this.getType().getGeoObjectType().getMdVertexOid());

    List<MdAttributeDAOIF> list = new LinkedList<MdAttributeDAOIF>();
    list.add(mdVertex.definesAttribute(this.getType().getCode()));

    return list;
  }

}
