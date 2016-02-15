package com.runwayskd.geodashboard.etl;

import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.util.IDGenerator;

public class TargetFieldGenerated extends TargetField implements TargetFieldIF
{

  @Override
  public Object getValue(MdAttributeConcreteDAOIF mdAttribute, Transient source)
  {
    return IDGenerator.nextID();
  }

}
