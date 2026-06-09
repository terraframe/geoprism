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
package net.geoprism.registry.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.dataaccess.ValueOverTimeDTO;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTimeCollection;

import net.geoprism.registry.graph.AttributeType;
import net.geoprism.registry.graph.AttributeValue;

/**
 * Strategy used for setting a value on an object value node
 * 
 * @author jsmethie
 */
public class ValueNodeStrategy extends AbstractValueStrategy implements ValueStrategy
{
  private MdVertexDAOIF nodeVertex;

  private String        nodeAttribute;

  public ValueNodeStrategy(AttributeType attributeType, MdVertexDAOIF nodeVertex, String nodeAttribute)
  {
    super(attributeType);

    this.nodeVertex = nodeVertex;
    this.nodeAttribute = nodeAttribute;
  }

  protected String getNodeAttribute()
  {
    return nodeAttribute;
  }

  protected MdVertexDAOIF getNodeVertex()
  {
    return nodeVertex;
  }

  private AttributeState getState(Map<String, AttributeState> valueNodeMap)
  {
    String key = this.getAttributeType().getCode();

    return valueNodeMap.get(key);
  }

  @Override
  public void setValue(VertexObject vertex, Map<String, AttributeState> valueNodeMap, Object value, Date startDate, Date endDate, boolean validate)
  {
    AttributeState state = getState(valueNodeMap);

    if (startDate != null && endDate != null)
    {
      StateValue node = this.createNode(state.getType(), value, startDate, endDate, validate);

      this.add(state, node, false);
    }
    else
    {
      // TODO: HEADS UP - Determine if we want to support the use case for
      // setting values with no dates
      StateValue last = state.last().orElse(null);

      if (last != null)
      {
        this.setNodeValue(last, value, validate);
      }
      else
      {
        state.add(this.createNode(state.getType(), value, new Date(), ValueOverTimeDTO.INFINITY_END_DATE, validate));
      }
    }
  }

  protected StateValue createNode(ServerGeoObjectType type, Object value, Date startDate, Date endDate, Boolean validate)
  {
    VertexObject node = new VertexObject(nodeVertex.definesType());
    node.setValue(AttributeValue.ATTRIBUTENAME, this.getAttributeType().getCode());
    node.setValue(AttributeValue.STARTDATE, startDate);
    node.setValue(AttributeValue.ENDDATE, endDate);

    StateValue state = this.construct(type, node);

    setNodeValue(state, null, false);
    setNodeValue(state, value, validate);

    return state;
  }

  @Override
  public List<MdAttributeDAOIF> getValueAttributes()
  {
    List<MdAttributeDAOIF> list = new LinkedList<MdAttributeDAOIF>();
    list.add(nodeVertex.definesAttribute(this.nodeAttribute));

    return list;
  }

  protected void setNodeValue(StateValue state, Object value, Boolean validate)
  {
    state.setValue(value);
  }

  @Override
  public boolean isModified(VertexObject vertex, Map<String, AttributeState> valueNodeMap)
  {
    return getState(valueNodeMap).isModified();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getValue(VertexObject vertex, Map<String, AttributeState> valueNodeMap, Date date)
  {
    AttributeState state = getState(valueNodeMap);

    if (date != null)
    {
      Optional<StateValue> optional = state.stream().filter(node -> {
        return ( node.getStartDate().before(date) || node.getStartDate().equals(date) ) && ( node.getEndDate().after(date) || node.getEndDate().equals(date) );
      }).findFirst();

      return (T) optional.map(StateValue::getValue).orElse(null);
    }

    return (T) state.last().map(StateValue::getValue).orElse(null);
  }

  @Override
  public Optional<StateValue> getState(VertexObject vertex, Map<String, AttributeState> valueNodeMap, Date date)
  {
    AttributeState state = getState(valueNodeMap);

    if (date != null)
    {
      Optional<StateValue> optional = state.stream().filter(node -> {
        return ( node.getStartDate().before(date) || node.getStartDate().equals(date) ) && ( node.getEndDate().after(date) || node.getEndDate().equals(date) );
      }).findFirst();

      return optional;
    }

    return state.last();
  }

  // @SuppressWarnings("unchecked")
  // protected <T> T getNodeValue(StateValue node)
  // {
  // return (T) node.getValue();
  // }

  @Override
  public ValueOverTimeCollection getValueOverTimeCollection(VertexObject vertex, Map<String, AttributeState> valueNodeMap)
  {
    ValueOverTimeCollection collection = new ValueOverTimeCollection();

    getState(valueNodeMap).stream().map(node -> node.toValueOverTime()).forEach(vot -> collection.add(vot, false));

    return collection;
  }

  /**
   * This method assumes that the collection has already resolved all overlaps
   * and that it completely replaces the existing entries
   */
  @Override
  public void setValuesOverTime(VertexObject vertex, Map<String, AttributeState> valueNodeMap, ValueOverTimeCollection collection)
  {
    // First delete all values which do not exist in the vot collection
    AttributeState state = getState(valueNodeMap);

    state.stream().filter(node -> {
      return !collection.stream().anyMatch(vot -> {
        return node.getOid().equals(vot.getOid());
      });
    }).collect(Collectors.toList()).forEach(node -> state.delete(node));

    collection.forEach(vot -> {
      state.stream().filter(node -> vot.getOid().equals(node.getOid())).findAny().ifPresentOrElse(node -> {
        // Update the corresponding existing node if it exists for this vot
        node.setStartDate(vot.getStartDate());
        node.setEndDate(vot.getEndDate());

        this.setNodeValue(node, vot.getValue(), true);

      }, () -> {
        // No existing node corresponds to the vot, create a new one
        state.add(this.createNode(state.getType(), vot.getValue(), vot.getStartDate(), vot.getEndDate(), true));
      });
    });
  }

  private boolean add(AttributeState state, StateValue vot, boolean updateOnCollision)
  {
    boolean skip = this.replaceExistingValues(state, vot);

    if (!skip)
    {

      // TODO: HEADS UP
      // // Check if the value needs to be overwritten
      // if (updateOnCollision && !success && this.values.remove(vot))
      // {
      // success = this.values.add(vot);
      // }

      return state.add(vot);
    }
    else
    {
      return false;
    }
  }

  private boolean replaceExistingValues(AttributeState state, StateValue inVot)
  {
    Date startDate = inVot.getStartDate();
    Date endDate = inVot.getEndDate();

    boolean skip = true;

    if (startDate != null && endDate != null)
    {
      skip = false;

      Iterator<StateValue> it = state.iterator();
      LocalDate iStartDate = startDate.toInstant().atZone(ZoneId.of("Z")).toLocalDate();
      LocalDate iEndDate = endDate.toInstant().atZone(ZoneId.of("Z")).toLocalDate();

      List<StateValue> segments = new LinkedList<StateValue>();

      while (it.hasNext())
      {
        StateValue vot = it.next();

        Date vStartDate = vot.getStartDate();
        Date vEndDate = vot.getEndDate();
        LocalDate vLocalStartDate = vStartDate.toInstant().atZone(ZoneId.of("Z")).toLocalDate();
        LocalDate vLocalEndDate = vEndDate.toInstant().atZone(ZoneId.of("Z")).toLocalDate();

        // Remove the entry completely if its a subset of the startDate and
        // endDate range
        if (isAfterOrEqual(vLocalStartDate, iStartDate) && isBeforeOrEqual(vLocalEndDate, iEndDate))
        {
          state.delete(vot);

          it.remove();
        }

        // If the entry is extends after the end date then move the start
        // date of the entry to the end of the clear range
        else if (isAfterOrEqual(vLocalStartDate, iStartDate) && vLocalEndDate.isAfter(iEndDate) && isAfterOrEqual(iEndDate, vLocalStartDate))
        {
          Object existingValue = vot.getValue();
          Object newValue = inVot.getValue();

          if (this.areValuesEqual(existingValue, newValue))
          {
            inVot.setEndDate(vEndDate);

            state.delete(vot);

            it.remove();
          }
          else
          {
            vot.setStartDate(this.calculateDatePlusOneDay(endDate));
          }
        }

        // If the entry is extends before the start date then move the end
        // date of the entry to the start of the clear range
        else if (vLocalStartDate.isBefore(iStartDate) && isBeforeOrEqual(vLocalEndDate, iEndDate) && isBeforeOrEqual(iStartDate, vLocalEndDate))
        {
          Object existingValue = vot.getValue();
          Object newValue = inVot.getValue();

          if (this.areValuesEqual(existingValue, newValue))
          {
            inVot.setStartDate(vStartDate);

            state.delete(vot);

            it.remove();
          }
          else
          {
            vot.setEndDate(this.calculateDateMinusOneDay(startDate));
          }
        }

        // The incoming range is completely covered by the existing range
        else if (vLocalStartDate.isBefore(iStartDate) && vLocalEndDate.isAfter(iEndDate))
        {
          Object existingValue = vot.getValue();
          Object newValue = inVot.getValue();

          if (this.areValuesEqual(existingValue, newValue))
          {
            skip = true;
          }
          else
          {
            state.delete(vot);

            it.remove();

            segments.add(this.createNode(state.getType(), existingValue, vStartDate, this.calculateDateMinusOneDay(startDate), true));
            segments.add(this.createNode(state.getType(), existingValue, this.calculateDatePlusOneDay(endDate), vEndDate, true));
          }
        }
      }

      for (StateValue vot : segments)
      {
        state.add(vot);
      }
    }

    return skip;
  }

  private boolean areValuesEqual(Object val1, Object val2)
  {
    if (val1 == null && val2 == null)
    {
      return true;
    }
    else if (val1 == null || val2 == null)
    {
      return false;
    }

    // TODO: HEADS UP
    // if (val1 instanceof Iterator<?> && val2 instanceof Iterator<?>)
    // {
    // ArrayList<Object> val1s = toList((Iterator<?>) val1);
    // ArrayList<Object> val2s = toList((Iterator<?>) val2);
    //
    // if (val1s.size() != val2s.size())
    // {
    // return false;
    // }
    //
    // for (int i = 0; i < val1s.size(); ++i)
    // {
    // if (!areValuesEqual(val1s.get(i), val2s.get(i)))
    // {
    // return false;
    // }
    // }
    //
    // return true;
    // }

    return val1.equals(val2);
  }

  private boolean isAfterOrEqual(LocalDate date1, LocalDate date2)
  {
    return date1.isAfter(date2) || date2.isEqual(date1);
  }

  private boolean isBeforeOrEqual(LocalDate date1, LocalDate date2)
  {
    return date1.isBefore(date2) || date2.isEqual(date1);
  }

  private Date calculateDateMinusOneDay(Date source)
  {
    LocalDate localEnd = source.toInstant().atZone(ZoneId.of("Z")).toLocalDate().minusDays(1);
    Instant instant = localEnd.atStartOfDay().atZone(ZoneId.of("Z")).toInstant();

    return Date.from(instant);
  }

  private Date calculateDatePlusOneDay(Date source)
  {
    LocalDate localEnd = source.toInstant().atZone(ZoneId.of("Z")).toLocalDate().plusDays(1);
    Instant instant = localEnd.atStartOfDay().atZone(ZoneId.of("Z")).toInstant();

    return Date.from(instant);
  }

  @Override
  public StateValue construct(ServerGeoObjectType type, VertexObject vertex)
  {
    return new PrimitiveStateValue(vertex, nodeAttribute);
  }

}
