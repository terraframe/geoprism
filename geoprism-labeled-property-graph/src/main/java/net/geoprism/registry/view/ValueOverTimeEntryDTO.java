package net.geoprism.registry.view;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ValueOverTimeEntryDTO
{
  private Date   startDate;

  private Date   endDate;

  private String oid;

  private Object value;

  public ValueOverTimeEntryDTO()
  {
  }

  public ValueOverTimeEntryDTO(String oid, Date startDate, Date endDate, Object value)
  {
    super();
    this.oid = oid;
    this.startDate = startDate;
    this.endDate = endDate;
    this.value = value;
  }

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

  public Object getValue()
  {
    return value;
  }

  public void setValue(Object value)
  {
    this.value = value;
  }

  @JsonIgnore
  public boolean isValid(Date date)
  {
    return ( this.startDate.before(date) || this.startDate.equals(date) ) && ( this.endDate.after(date) || this.endDate.equals(date) );
  }

}
