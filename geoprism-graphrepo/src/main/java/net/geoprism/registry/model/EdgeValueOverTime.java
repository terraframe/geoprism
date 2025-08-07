package net.geoprism.registry.model;

import java.util.Date;

import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;

public class EdgeValueOverTime extends ValueOverTime
{
  private String uid;

  public EdgeValueOverTime(Date startDate, Date endDate, Object value, String uid)
  {
    super(startDate, endDate, value);

    this.uid = uid;
  }

  public EdgeValueOverTime(String oid, Date startDate, Date endDate, Object value, String uid)
  {
    super(oid, startDate, endDate, value);

    this.uid = uid;
  }

  public String getUid()
  {
    return uid;
  }

  public void setUid(String uid)
  {
    this.uid = uid;
  }
}
