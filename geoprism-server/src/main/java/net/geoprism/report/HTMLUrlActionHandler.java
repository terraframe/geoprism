/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.report;

import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.RenderOption;

import com.runwaysdk.generation.loader.Reloadable;

public class HTMLUrlActionHandler extends AbstractUrlActionHandler implements Reloadable
{
  public HTMLUrlActionHandler(IReportDocument document, String baseURL, String reportURL)
  {
    super(document, baseURL, reportURL);
  }

  @Override
  protected String getDefaultFormat()
  {
    return RenderOption.OUTPUT_FORMAT_HTML;
  }

}
