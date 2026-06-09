package net.geoprism.registry.model;

import java.util.Date;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;

import net.geoprism.registry.graph.AttributeValue;
import net.geoprism.registry.model.graph.VertexServerGeoObject;

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

  public void apply(VertexServerGeoObject object)
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

}
