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

import org.commongeoregistry.adapter.constants.DefaultAttribute;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;
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

  public ValueNodeStrategy(AttributeType type, MdVertexDAOIF nodeVertex, String nodeAttribute)
  {
    super(type);

    this.nodeVertex = nodeVertex;
    this.nodeAttribute = nodeAttribute;
  }

  private Date getEndDate(VertexObject node)
  {
    return node.getObjectValue(AttributeValue.ENDDATE);
  }

  private Date getStartDate(VertexObject node)
  {
    return node.getObjectValue(AttributeValue.STARTDATE);
  }

  private AttributeState getState(Map<String, AttributeState> valueNodeMap)
  {
    String key = this.getType().getCode();

    valueNodeMap.putIfAbsent(key, new AttributeState(this.getType(), new LinkedList<>()));

    return valueNodeMap.get(key);
  }

  private ValueOverTime toValueOverTime(VertexObject node)
  {
    Date startDate = this.getStartDate(node);
    Date endDate = this.getEndDate(node);
    Object value = this.getNodeValue(node);

    return new ValueOverTime(node.getOid(), startDate, endDate, value);
  }

  @Override
  public void setValue(VertexObject vertex, Map<String, AttributeState> valueNodeMap, Object value, Date startDate, Date endDate)
  {
    AttributeState state = getState(valueNodeMap);
    VertexObject node = this.createNode(value, startDate, endDate);

    this.add(state, node, false);
  }

  protected VertexObject createNode(Object value, Date startDate, Date endDate)
  {
    VertexObject node = new VertexObject(nodeVertex.definesType());
    node.setValue(AttributeValue.ATTRIBUTENAME, this.getType().getCode());
    node.setValue(AttributeValue.STARTDATE, startDate);
    node.setValue(AttributeValue.ENDDATE, endDate);

    setNodeValue(node, value);
    return node;
  }

  protected void setNodeValue(VertexObject node, Object value)
  {
    node.setValue(nodeAttribute, value);
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
      Optional<VertexObject> optional = state.stream().filter(node -> {
        return ( getStartDate(node).before(date) || getStartDate(node).equals(date) ) && ( getEndDate(node).after(date) || getEndDate(node).equals(date) );
      }).findFirst();

      if (optional.isPresent())
      {
        return getNodeValue(optional.get());
      }
    }
    else
    {
      ValueOverTimeCollection votc = this.getValueOverTimeCollection(vertex, valueNodeMap);
      
      if (votc.size() > 0)
      {
        return (T) votc.last().getValue();
      }
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  protected <T> T getNodeValue(VertexObject node)
  {
    return (T) node.getObjectValue(nodeAttribute);
  }

  @Override
  public ValueOverTimeCollection getValueOverTimeCollection(VertexObject vertex, Map<String, AttributeState> valueNodeMap)
  {
    ValueOverTimeCollection collection = new ValueOverTimeCollection();

    getState(valueNodeMap).stream().map(node -> toValueOverTime(node)).forEach(vot -> collection.add(vot, false));

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
        node.setValue(AttributeValue.STARTDATE, vot.getStartDate());
        node.setValue(AttributeValue.ENDDATE, vot.getEndDate());

        this.setNodeValue(node, vot.getValue());

      }, () -> {
        // No existing node corresponds to the vot, create a new one
        state.add(this.createNode(vot.getValue(), vot.getStartDate(), vot.getEndDate()));
      });
    });
  }

  private boolean add(AttributeState state, VertexObject vot, boolean updateOnCollision)
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

  private boolean replaceExistingValues(AttributeState state, VertexObject inVot)
  {
    Date startDate = inVot.getObjectValue(AttributeValue.STARTDATE);
    Date endDate = inVot.getObjectValue(AttributeValue.ENDDATE);

    boolean skip = true;

    if (startDate != null && endDate != null)
    {
      skip = false;

      Iterator<VertexObject> it = state.iterator();
      LocalDate iStartDate = startDate.toInstant().atZone(ZoneId.of("Z")).toLocalDate();
      LocalDate iEndDate = endDate.toInstant().atZone(ZoneId.of("Z")).toLocalDate();

      List<VertexObject> segments = new LinkedList<VertexObject>();

      while (it.hasNext())
      {
        VertexObject vot = it.next();

        Date vStartDate = vot.getObjectValue(AttributeValue.STARTDATE);
        Date vEndDate = vot.getObjectValue(AttributeValue.ENDDATE);
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
          Object existingValue = this.getNodeValue(vot);
          Object newValue = this.getNodeValue(inVot);

          if (this.areValuesEqual(existingValue, newValue))
          {
            inVot.setValue(AttributeValue.ENDDATE, vEndDate);

            state.delete(vot);

            it.remove();
          }
          else
          {
            vot.setValue(AttributeValue.STARTDATE, this.calculateDatePlusOneDay(endDate));
          }
        }

        // If the entry is extends before the start date then move the end
        // date of the entry to the start of the clear range
        else if (vLocalStartDate.isBefore(iStartDate) && isBeforeOrEqual(vLocalEndDate, iEndDate) && isBeforeOrEqual(iStartDate, vLocalEndDate))
        {
          Object existingValue = this.getNodeValue(vot);
          Object newValue = this.getNodeValue(inVot);

          if (this.areValuesEqual(existingValue, newValue))
          {
            inVot.setValue(AttributeValue.STARTDATE, vStartDate);

            state.delete(vot);

            it.remove();
          }
          else
          {
            vot.setValue(AttributeValue.ENDDATE, this.calculateDateMinusOneDay(startDate));
          }
        }

        // The incoming range is completely covered by the existing range
        else if (vLocalStartDate.isBefore(iStartDate) && vLocalEndDate.isAfter(iEndDate))
        {
          Object existingValue = this.getNodeValue(vot);
          Object newValue = this.getNodeValue(inVot);

          if (this.areValuesEqual(existingValue, newValue))
          {
            skip = true;
          }
          else
          {
            state.delete(vot);

            it.remove();

            segments.add(this.createNode(existingValue, vStartDate, this.calculateDateMinusOneDay(startDate)));
            segments.add(this.createNode(existingValue, this.calculateDatePlusOneDay(endDate), vEndDate));
          }
        }
      }

      for (VertexObject vot : segments)
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

}
