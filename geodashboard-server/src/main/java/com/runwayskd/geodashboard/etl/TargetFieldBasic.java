package com.runwayskd.geodashboard.etl;

import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;

public class TargetFieldBasic extends TargetField implements TargetFieldIF
{
  private String sourceAttributeName;

  public String getSourceAttributeName()
  {
    return sourceAttributeName;
  }

  public void setSourceAttributeName(String sourceAttributeName)
  {
    this.sourceAttributeName = sourceAttributeName;
  }

  @Override
  public Object getValue(MdAttributeConcreteDAOIF mdAttribute, Transient source)
  {
    String value = source.getValue(this.getSourceAttributeName());

    return value;
  }
}
