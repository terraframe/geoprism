package com.runwaysdk.geodashboard.report;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.ValueQuery;

public class ReportProviderBridge implements Reloadable
{
  private static ReportProviderBridge instance;

  private ReportProviderIF            provider;

  private ReportProviderBridge()
  {

  }

  private void setProvider(ReportProviderIF provider)
  {
    this.provider = provider;
  }

  private ReportProviderIF getProvider()
  {
    if (this.provider == null)
    {
      throw new RuntimeException("Provider must be set");
    }

    return this.provider;
  }

  public static synchronized ReportProviderBridge getInstance()
  {
    if (instance == null)
    {
      instance = new ReportProviderBridge();
      instance.setProvider(new DefaultReportProvider());
    }

    return instance;
  }

  public static void setReportProvider(ReportProviderIF provider)
  {
    getInstance().setProvider(provider);
  }

  public static ValueQuery getValuesForReporting(String type, String category, String criteria)
  {
    return getInstance().getProvider().getValuesForReporting(type, category, criteria);
  }

  public static ValueQuery getTypesForReporting()
  {
    return getInstance().getProvider().getTypesForReporting();
  }
}
