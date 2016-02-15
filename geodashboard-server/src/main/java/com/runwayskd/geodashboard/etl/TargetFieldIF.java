package com.runwayskd.geodashboard.etl;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;

public interface TargetFieldIF
{
  public String getName();

  public String getKey();

  public String getLabel();

  /**
   * 
   * @param mdAttribute
   * @param source
   * @return
   */
  public Object getValue(MdAttributeConcreteDAOIF mdAttribute, Transient source);
}
