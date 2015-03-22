package com.runwaysdk.geodashboard.report;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.generation.loader.Reloadable;

public interface ReportConditionHandlerIF extends Reloadable
{
  public void handleAttributeCondition(MdAttributeDAOIF _mdAttribute, String _operation, String _value);

  public void handleLocationCondition(String _value);
}
