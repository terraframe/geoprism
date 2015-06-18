package com.runwaysdk.geodashboard.report;

import org.json.JSONException;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.ValueQuery;

public interface ReportProviderIF extends Reloadable
{
  /**
   * Dashboard name
   */
  public static final String DASHBOARD_NAME = "dashboardName";

  /**
   * Dashboard id
   */
  public static final String DASHBOARD_ID   = "dashboardId";

  public ValueQuery getReportQuery(String context) throws JSONException;

  public PairView getReportQueryDescriptor();

  public PairView[] getSupportedAggregation();
}
