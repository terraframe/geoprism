package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.GreaterThan;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeDate;
import com.runwaysdk.query.AttributeNumber;
import com.runwaysdk.query.ValueQuery;

public class DashboardGreaterThan extends DashboardGreaterThanBase implements com.runwaysdk.generation.loader.Reloadable, GreaterThan
{
  /**
   * Greater than comparison
   */
  public static final String OPERATION        = "gt";

  private static final long  serialVersionUID = 815122248;

  public DashboardGreaterThan()
  {
    super();
  }

  @Override
  public void restrictQuery(ValueQuery query, Attribute attr)
  {
    if (attr instanceof AttributeNumber)
    {
      query.AND( ( (AttributeNumber) attr ).GT(this.getComparisonValue()));
    }
    else if (attr instanceof AttributeDate)
    {
      query.AND( ( (AttributeDate) attr ).GT(this.getComparisonValueAsDate()));
    }
    else
    {
      // Unsupported
      throw new ProgrammingErrorException("Unsupported condition attribute type [" + attr.getClass().getName() + "]");
    }
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public String getOperation()
  {
    return OPERATION;
  }

  @Override
  protected DashboardCondition newInstance()
  {
    return new DashboardGreaterThan();
  }
}
