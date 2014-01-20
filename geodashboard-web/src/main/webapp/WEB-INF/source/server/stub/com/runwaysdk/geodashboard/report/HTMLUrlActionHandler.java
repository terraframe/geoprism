package com.runwaysdk.geodashboard.report;

import org.eclipse.birt.report.engine.api.RenderOption;

import com.runwaysdk.generation.loader.Reloadable;

public class HTMLUrlActionHandler extends AbstractUrlActionHandler implements Reloadable
{
  public HTMLUrlActionHandler(String baseURL)
  {
    super(baseURL);
  }

  @Override
  protected String getDefaultFormat()
  {
    return RenderOption.OUTPUT_FORMAT_HTML;
  }

}
