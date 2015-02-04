package com.runwaysdk.geodashboard.gis.persist.condition;

import java.util.Date;

import com.runwaysdk.geodashboard.gis.model.condition.Primitive;
import com.runwaysdk.geodashboard.report.ReportProviderUtil;

public abstract class DashboardPrimitive extends DashboardPrimitiveBase implements com.runwaysdk.generation.loader.Reloadable, Primitive
{
  private static final long serialVersionUID = -1224425442;

  public DashboardPrimitive()
  {
    super();
  }

  @Override
  public Object getValue()
  {
    return this.getComparisonValue();
  }

  public Date getComparisonValueAsDate()
  {
    return ReportProviderUtil.parseDate(this.getComparisonValue());
  }

}
