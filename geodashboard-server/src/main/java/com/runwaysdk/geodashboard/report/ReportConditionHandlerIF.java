package com.runwaysdk.geodashboard.report;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.generation.loader.Reloadable;

public interface ReportConditionHandlerIF extends Reloadable
{

  /**
   * Greater than comparison
   */
  public static final String GT  = "gt";

  /**
   * Greater than or equal comparison
   */
  public static final String GE  = "ge";

  /**
   * Less than comparison
   */
  public static final String LT  = "lt";

  /**
   * Less than or equal comparison
   */
  public static final String LE  = "le";

  /**
   * Equal comparison
   */
  public static final String EQ  = "eq";

  /**
   * Equal comparison
   */
  public static final String NEQ = "neq";

  public void handleAttributeCondition(MdAttributeDAOIF _mdAttribute, String _operation, String _value);

  public void handleLocationCondition(String _value);
}
