package com.runwaysdk.geodashboard.report;

import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.RenderOption;

import com.runwaysdk.generation.loader.Reloadable;

public class PDFUrlActionHandler extends AbstractUrlActionHandler implements Reloadable
{
  public PDFUrlActionHandler(IReportDocument document, String baseURL, String reportURL)
  {
    super(document, baseURL, reportURL);
  }

  @Override
  protected String getDefaultFormat()
  {
    return RenderOption.OUTPUT_FORMAT_PDF;
  }

}
