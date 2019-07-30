/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.report;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.IAction;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.script.IReportContext;



public abstract class AbstractUrlActionHandler extends HTMLActionHandler 
{
  private static final String RPTDOCUMENT = ".rptdocument";

  private static final String RPTDESIGN   = ".rptdesign";

  /** logger */
  protected Logger            log         = Logger.getLogger(AbstractUrlActionHandler.class.getName());

  private String              baseURL;

  private IReportDocument     document;

  private String              reportURL;

  public AbstractUrlActionHandler(IReportDocument document, String baseURL, String reportURL)
  {
    this.baseURL = baseURL;
    this.document = document;
    this.reportURL = reportURL;
  }

  protected abstract String getDefaultFormat();

  /**
   * Get URL of the action.
   * 
   * @param actionDefn
   * @param context
   * @return URL
   */
  public String getURL(IAction action, IReportContext context)
  {
    if (action != null && action.getType() == IAction.ACTION_DRILLTHROUGH)
    {
      return this.buildDrillAction(action, context);
    }
    else if (action != null && action.getType() == IAction.ACTION_BOOKMARK)
    {
      return this.buildBookmarkAction(action, context);
    }

    return super.getURL(action, context);
  }

  public String buildBookmarkAction(IAction action, IReportContext context)
  {
    if (action.getReportName() != null)
    {
      StringBuffer buffer = new StringBuffer(this.buildDrillAction(action, context));
      this.appendBookmark(buffer, action.getBookmark());

      return buffer.toString();
    }
    else
    {
      StringBuffer buffer = new StringBuffer();
      buffer.append("/");
      buffer.append(context.getAppContext());
      buffer.append("/");
      buffer.append(this.reportURL);

      if (this.document != null)
      {
        long pageNumber = this.document.getPageNumber(action.getBookmark());

        this.appendParamter(buffer, "pageNumber", pageNumber);
      }

      this.appendBookmark(buffer, action.getBookmark());

      return buffer.toString();
    }
  }

  public String getURL(IAction action, Object context)
  {
    if (action != null && action.getType() == IAction.ACTION_DRILLTHROUGH)
    {
      return this.buildDrillAction(action, context);
    }

    return super.getURL(action, context);
  }

  /**
   * builds URL for drillthrough action
   * 
   * @param action
   *          instance of the IAction instance
   * @param context
   *          the context for building the action string
   * @return a URL
   */
  protected String buildDrillAction(IAction action, Object context)
  {
    ReportItem item = this.getReportItem(action);

    StringBuffer link = new StringBuffer();
    link.append(this.baseURL + "/" + item.getURL());

    // Adds the parameters
    Map<?, ?> parameters = action.getParameterBindings();

    if (parameters != null)
    {
      Iterator<?> iterator = parameters.entrySet().iterator();

      while (iterator.hasNext())
      {
        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iterator.next();

        String parameterKey = (String) entry.getKey();
        Object parameterValue = entry.getValue();

        if (parameterValue != null && parameterValue instanceof List)
        {
          List<?> list = (List<?>) parameterValue;

          if (list.size() == 1)
          {
            this.appendParamter(link, parameterKey, list.get(0));
          }
          else
          {
            this.appendParamter(link, parameterKey, list);
          }
        }
        else
        {
          this.appendParamter(link, parameterKey, parameterValue);
        }
      }
    }

    return link.toString();
  }

  protected ReportItem getReportItem(IAction action)
  {
    String reportPath = this.getReportPath(action);

    if (reportPath != null && reportPath.length() > 0)
    {
      String reportName = new File(reportPath).getName();

      if (! ( reportName.endsWith(RPTDESIGN) || reportName.endsWith(RPTDOCUMENT) ))
      {
        RuntimeException exception = new RuntimeException("Drill through report must end in .rptdesign or .rptdocument");
        // exception.apply();

        throw exception;
      }

      if (reportName.endsWith(RPTDOCUMENT))
      {
        reportName = reportName.replace(RPTDOCUMENT, RPTDESIGN);
      }

      /*
       * Get the report item from the name of the report and the output format
       */
      ReportItem item = ReportItem.find(reportName);

      if (item != null)
      {
        return item;
      }
      else
      {
        String message = "Unable to find a report in the system with the report name [" + reportName + "]";

        UnknownReportException e = new UnknownReportException(message);
        e.setReportName(reportName);

        throw e;
      }
    }
    else
    {
      String message = "Invalid drill through report definition.  No sub report has been defined.";

      InvalidReportDefinitionException e = new InvalidReportDefinitionException(message);
      e.apply();

      throw e;
    }
  }

  /**
   * Get report name.
   * 
   * @param action
   * @return
   */
  @SuppressWarnings("deprecation")
  protected String getReportPath(IAction action)
  {
    String systemId = action.getSystemId();
    String reportName = action.getReportName();

    if (systemId == null)
    {
      return reportName;
    }

    if (reportName == null)
    {
      return null;
    }

    // if the reportName is an URL, use it directly
    try
    {
      URL url = new URL(reportName);

      if ("file".equals(url.getProtocol()))
      {
        return url.getFile();
      }

      return url.toExternalForm();
    }
    catch (MalformedURLException ex)
    {
      // DO NOTHING
    }

    // if the system oid is the URL, merge the report name with it
    try
    {
      URL root = new URL(systemId);
      URL url = new URL(root, reportName);

      if ("file".equals(url.getProtocol()))
      {
        return url.getFile();
      }

      return url.toExternalForm();
    }
    catch (MalformedURLException ex)
    {
      // DO NOTHING
    }

    // now the root should be a file and the report name is a file also
    File file = new File(reportName);

    if (file.isAbsolute())
    {
      return reportName;
    }

    try
    {
      URL root = new File(systemId).toURL();
      URL url = new URL(root, reportName);

      return url.getFile();
    }
    catch (MalformedURLException ex)
    {
      // DO NOTHING
    }
    return reportName;
  }
}
