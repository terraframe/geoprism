package com.runwaysdk.geodashboard.report;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.IAction;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.script.IReportContext;

import com.runwaysdk.generation.loader.Reloadable;

public abstract class AbstractUrlActionHandler extends HTMLActionHandler implements Reloadable
{
  private static final String RPTDOCUMENT = ".rptdocument";

  private static final String RPTDESIGN   = ".rptdesign";

  /** logger */
  protected Logger            log         = Logger.getLogger(AbstractUrlActionHandler.class.getName());

  private String              baseURL;

  public AbstractUrlActionHandler(String baseURL)
  {
    this.baseURL = baseURL;
  }

  protected abstract String getDefaultFormat();

  /**
   * Get URL of the action.
   * 
   * @param actionDefn
   * @param context
   * @return URL
   */
  public String getURL(IAction actionDefn, IReportContext context)
  {
    Object renderContext = this.getRenderContext(context);

    return getURL(actionDefn, renderContext);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.birt.report.engine.api2.IHTMLActionHandler#getURL(org.eclipse
   * .birt.report.engine.api2.IAction, java.lang.Object)
   */
  public String getURL(IAction actionDefn, Object context)
  {
    if (actionDefn != null && actionDefn.getType() == IAction.ACTION_DRILLTHROUGH)
    {
      return this.buildDrillAction(actionDefn, context);
    }

    return super.getURL(actionDefn, context);
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
    StringBuffer link = new StringBuffer();

    ReportItem item = this.getReportItem(action);

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
      int index = reportPath.lastIndexOf(File.separator);
      String reportName = reportPath.substring(index + 1);

      if (! ( reportName.endsWith(RPTDESIGN) || reportName.endsWith(RPTDOCUMENT) ))
      {
        throw new RuntimeException("Drill through report must end in .rptdesign or .rptdocument");
      }

      if (reportName.endsWith(RPTDOCUMENT))
      {
        reportName = reportName.replace(RPTDOCUMENT, RPTDESIGN);
      }

      /*
       * Get the report item from the name of the report and the output format
       */
      String format = this.getFormat(action);

      ReportItem item = ReportItem.find(reportName, ReportItem.getOutputFormat(format));

      if (item != null)
      {
        return item;
      }
      else
      {
        String message = "Unable to find a report in the system with the report name [" + reportName + "] and output format [" + format + "]";

        UnknownReportException e = new UnknownReportException(message);
        e.setReportName(reportName);
        e.setFormat(format);

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

  private String getFormat(IAction action)
  {
    String format = action.getFormat();

    if (format == null || format.length() == 0)
    {
      return this.getDefaultFormat();
    }
    else if (! ( format.equals(RenderOption.OUTPUT_FORMAT_HTML) || format.equals(RenderOption.OUTPUT_FORMAT_PDF) ))
    {
      String message = "Unsupported drill through report format [" + format + "]";

      UnsupportedDrillThroughFormatException e = new UnsupportedDrillThroughFormatException(message);
      e.setOutputFormat(format);
      e.apply();

      throw e;
    }

    return format;
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

    // if the system id is the URL, merge the report name with it
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

  /**
   * Get render context.
   * 
   * @param context
   * @return
   */
  protected Object getRenderContext(IReportContext context)
  {
    if (context != null)
    {
      Map<?, ?> appContext = context.getAppContext();

      if (appContext != null)
      {
        String renderContextKey = EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT;
        String format = context.getOutputFormat();

        if (RenderOption.OUTPUT_FORMAT_PDF.equalsIgnoreCase(format))
        {
          renderContextKey = EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT;
        }

        return appContext.get(renderContextKey);
      }
    }

    return null;
  }
}
