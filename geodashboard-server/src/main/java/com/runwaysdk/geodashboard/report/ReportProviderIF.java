package com.runwaysdk.geodashboard.report;

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

  public ValueQuery getValuesForReporting(String type, String category, String criteria, Integer depth);

  public ReportQueryView[] getTypesForReporting();
}
