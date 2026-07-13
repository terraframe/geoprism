package net.geoprism.registry.view;

import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.geoprism.registry.view.serialization.DateDeserializer;
import net.geoprism.registry.view.serialization.DateSerializer;

public class ValueOverTimeEntryDTO<T>
{
  @JsonSerialize(using = DateSerializer.class)
  @JsonDeserialize(using = DateDeserializer.class)
  private Date   startDate;

  @JsonSerialize(using = DateSerializer.class)
  @JsonDeserialize(using = DateDeserializer.class)
  private Date   endDate;

  private String oid;

  private T      value;

  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDate(Date startDate)
  {
    this.startDate = startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }

  public String getOid()
  {
    return oid;
  }

  public void setOid(String oid)
  {
    this.oid = oid;
  }

  public T getValue()
  {
    return value;
  }

  public void setValue(T value)
  {
    this.value = value;
  }

  @JsonIgnore
  public boolean isValid(Date date)
  {
    return ( this.startDate.before(date) || this.startDate.equals(date) ) && ( this.endDate.after(date) || this.endDate.equals(date) );
  }

  public static <T> ValueOverTimeEntryDTO<T> of(String oid, Date startDate, Date endDate, T value)
  {
    ValueOverTimeEntryDTO<T> entry = new ValueOverTimeEntryDTO<T>();
    entry.setOid(oid);
    entry.setStartDate(startDate);
    entry.setEndDate(endDate);
    entry.setValue(value);

    return entry;
  }

  public static <T> ValueOverTimeEntryDTO<T> of(String oid, Calendar startDate, Calendar endDate, T value)
  {
    ValueOverTimeEntryDTO<T> entry = new ValueOverTimeEntryDTO<T>();
    entry.setOid(oid);
    entry.setStartDate(startDate.getTime());
    entry.setEndDate(endDate.getTime());
    entry.setValue(value);

    return entry;
  }
}
