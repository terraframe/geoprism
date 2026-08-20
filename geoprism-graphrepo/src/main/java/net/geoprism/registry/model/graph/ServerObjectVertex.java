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
package net.geoprism.registry.model.graph;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTimeCollection;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.graph.AttributeLocalType;
import net.geoprism.registry.graph.AttributeType;
import net.geoprism.registry.model.AttributeState;

public abstract class ServerObjectVertex
{
  protected ObjectClassIF               type;

  protected VertexObject                vertex;

  // Current state of values. May not have been applied to the database.
  protected Map<String, AttributeState> valueNodeMap;

  protected Date                        date;

  public ServerObjectVertex(ObjectClassIF type, VertexObject vertex, Map<String, List<VertexObject>> valueNodeMap)
  {
    this(type, vertex, valueNodeMap, null);
  }

  public ServerObjectVertex(ObjectClassIF type, VertexObject vertex, Map<String, List<VertexObject>> valueNodeMap, Date date)
  {
    this.type = type;
    this.vertex = vertex;

    // Add attribute states for attributes with previous entries
    this.valueNodeMap = valueNodeMap.entrySet().stream().map(entry -> {

      Optional<AttributeType> attribute = type.getAttribute(entry.getKey());

      if (attribute.isPresent())
      {
        return new AttributeState(type, attribute.get(), entry.getValue());
      }

      return null;
    }).filter(t -> t != null).collect(Collectors.toMap(s -> s.getAttributeType().getCode(), s -> s));

    // Add attribute states for any attribute that doesn't have any entries
    this.type.getAttributeMap().forEach((name, attributeType) -> {
      this.valueNodeMap.putIfAbsent(name, new AttributeState(type, attributeType, new LinkedList<>()));
    });

    this.date = date;
  }

  public Object getRID()
  {
    return this.vertex.getRID();
  }

  public String getOid()
  {
    return this.vertex.getOid();
  }

  @SuppressWarnings("unchecked")
  public <T extends ObjectClassIF> T getType()
  {
    return (T) type;
  }

  public void setType(ObjectClassIF type)
  {
    this.type = type;
  }

  public VertexObject getVertex()
  {
    return vertex;
  }

  public void setVertex(VertexObject vertex)
  {
    this.vertex = vertex;
  }

  public Map<String, AttributeState> getValueNodeMap()
  {
    return valueNodeMap;
  }

  public void setValueNodeMap(Map<String, AttributeState> valueNodeMap)
  {
    this.valueNodeMap = valueNodeMap;
  }

  public Date getDate()
  {
    return date;
  }

  public void setDate(Date date)
  {
    this.date = date;
  }

  @Transaction
  public void apply()
  {
    // TODO: HEADS UP: Add a version check to ensure this object is current,
    // otherwise the value node map may create duplicates
    this.vertex.apply();

    this.valueNodeMap.forEach((attributeName, state) -> {
      state.persit(this, this.vertex);

      // TODO: HEADS UP: Handle rollback of object on persist failure
      // Only clear the state after the transaction has passed
      // state.clear();
    });

  }

  @Transaction
  public void delete()
  {
    this.valueNodeMap.forEach((attributeName, state) -> {
      state.delete();

      // TODO: HEADS UP: Handle rollback of object on persist failure
      // Only clear the state after the transaction has passed
      // state.clear();
    });

    this.vertex.delete();
  }

  public boolean isNew()
  {
    return this.vertex.isNew();
  }

  protected Object getMostRecentValue(String attributeName)
  {
    ValueOverTimeCollection votc = this.getValuesOverTime(attributeName);

    if (votc.size() > 0)
    {
      return votc.get(votc.size() - 1).getValue();
    }
    else
    {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public <T> T getValue(String attributeName)
  {
    Optional<AttributeType> optional = this.type.getAttribute(attributeName);

    if (optional.isPresent())
    {
      AttributeType attributeType = optional.get();

      Object value = !attributeType.getIsChangeOverTime() ? attributeType.getStrategy().getValue(vertex, valueNodeMap, null) : this.getMostRecentValue(attributeName);

      return (T) value;
    }

    if (isSystemAttribute(attributeName))
    {
      return this.vertex.getObjectValue(attributeName);
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  public <T> T getValue(String attributeName, Date date)
  {
    AttributeType at = this.type.getAttribute(attributeName).orElse(null);

    if (at != null)
    {
      Object value = at.getStrategy().getValue(this.vertex, this.valueNodeMap, date);

      return (T) value;
    }

    if (isSystemAttribute(attributeName))
    {
      return this.vertex.getObjectValue(attributeName);
    }

    return null;
  }

  private boolean isSystemAttribute(String attributeName)
  {
    // Attributes created by the runway that do not have actual attribute type
    // metadata
    return this.vertex.hasAttribute(attributeName) && ( attributeName.equals(DefaultAttribute.CREATE_DATE.getName()) || attributeName.equals(DefaultAttribute.LAST_UPDATE_DATE.getName()) );
  }

  public ValueOverTimeCollection getValuesOverTime(String attributeName)
  {
    Optional<AttributeType> attribute = this.type.getAttribute(attributeName);

    if (attribute.isPresent())
    {
      return attribute.get().getStrategy().getValueOverTimeCollection(this.vertex, this.valueNodeMap);
    }

    return new ValueOverTimeCollection();
  }

  public void setValue(String attributeName, Object value)
  {
    AttributeType at = this.type.getAttribute(attributeName).orElse(null);

    if (at instanceof AttributeLocalType)
    {
      RegistryLocalizedValueConverter.populate(this.vertex, attributeName, (LocalizedValue) value, this.date, null);
    }
    else
    {
      this.vertex.setValue(attributeName, value, this.date, this.date);
    }
  }

  public void setValue(String attributeName, Object value, Date startDate, Date endDate)
  {
    this.setValue(attributeName, value, startDate, endDate, true);
  }

  public void setValue(String attributeName, Object value, Date startDate, Date endDate, boolean validate)
  {
    this.type.getAttribute(attributeName).ifPresent( ( attr -> {
      attr.getStrategy().setValue(this.vertex, this.valueNodeMap, value, startDate, endDate, validate);
    } ));
  }

  public void setValuesOverTime(String attributeName, ValueOverTimeCollection collection)
  {
    this.type.getAttribute(attributeName).ifPresent( ( attr -> {
      attr.getStrategy().setValuesOverTime(this.vertex, this.valueNodeMap, collection);
    } ));
  }

  public String getCode()
  {
    return this.getValue(DefaultAttribute.CODE.getName());
  }

  public void setCode(String code)
  {
    this.setValue(DefaultAttribute.CODE.getName(), code);
  }

}
