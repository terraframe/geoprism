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

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;

import net.geoprism.registry.graph.AttributeValue;
import net.geoprism.registry.model.graph.ServerObjectVertex;

public abstract class StateValue
{
  private VertexObject node;

  public StateValue(VertexObject node)
  {
    this.node = node;
  }

  public abstract <T> T getValue();

  public abstract void setValue(Object value);

  public String getOid()
  {
    return this.node.getOid();
  }

  public Date getStartDate()
  {
    return this.node.getObjectValue(AttributeValue.STARTDATE);
  }

  public void setStartDate(Date startDate)
  {
    this.node.setValue(AttributeValue.STARTDATE, startDate);
  }

  public Date getEndDate()
  {
    return this.node.getObjectValue(AttributeValue.ENDDATE);
  }

  public void setEndDate(Date endDate)
  {
    this.node.setValue(AttributeValue.ENDDATE, endDate);
  }

  public <T> T getValue(String attributeName)
  {
    return this.node.getObjectValue(attributeName);
  }

  public void setValue(String attributeName, Object value)
  {
    this.node.setValue(attributeName, value);
  }

  public boolean hasAttribute(String attributeName)
  {
    return this.node.hasAttribute(attributeName);
  }

  public ValueOverTime toValueOverTime()
  {
    Date startDate = this.getStartDate();
    Date endDate = this.getEndDate();
    Object value = this.getValue();

    return new ValueOverTime(node.getOid(), startDate, endDate, value);
  }

  public void apply(ServerObjectVertex object)
  {
    this.node.apply();
  }

  public VertexObject getVertex()
  {
    return this.node;
  }

  public void delete()
  {
    this.node.delete();
  }

  public boolean hasValue()
  {
    return ( this.getValue() != null );
  }

}
