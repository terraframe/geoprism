package com.runwaysdk.geodashboard.report;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.persist.AllAggregationType;

public class ReportAttributeMetadata implements Reloadable
{
  private String             attributeName;

  private AllAggregationType aggregation;

  private Boolean            orderBy;

  public ReportAttributeMetadata(String attributeName)
  {
    this(null, attributeName, false);
  }

  public ReportAttributeMetadata(AllAggregationType aggregation, String attributeName)
  {
    this(aggregation, attributeName, false);
  }

  public ReportAttributeMetadata(AllAggregationType aggregation, String attributeName, Boolean orderBy)
  {
    super();
    this.aggregation = aggregation;
    this.attributeName = attributeName;
    this.orderBy = orderBy;
  }

  public AllAggregationType getAggregation()
  {
    return aggregation;
  }

  public void setAggregation(AllAggregationType aggregation)
  {
    this.aggregation = aggregation;
  }

  public String getAttributeName()
  {
    return attributeName;
  }

  public void setAttributeName(String attributeName)
  {
    this.attributeName = attributeName;
  }

  public void setOrderBy(Boolean orderBy)
  {
    this.orderBy = orderBy;
  }

  public Boolean isOrderBy()
  {
    return orderBy;
  }
}
