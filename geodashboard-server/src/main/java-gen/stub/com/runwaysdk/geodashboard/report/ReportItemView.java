package com.runwaysdk.geodashboard.report;

public class ReportItemView extends ReportItemViewBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -149941815;

  public ReportItemView()
  {
    super();
  }

  public void remove()
  {
    if (this.getReportId() != null && this.getReportId().length() > 0)
    {
      ReportItem item = ReportItem.lock(this.getReportId());
      item.delete();
    }
  }
}
