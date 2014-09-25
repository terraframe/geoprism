package com.runwaysdk.geodashboard.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.ErrorUtility;
import com.runwaysdk.controller.MultipartFileParameter;
import com.runwaysdk.geodashboard.DashboardDTO;
import com.runwaysdk.transport.conversion.json.JSONReturnObject;

public class ReportItemController extends ReportItemControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR = "/WEB-INF/com/runwaysdk/geodashboard/report/ReportItem/";

  public static final String LAYOUT  = "/WEB-INF/templates/layout.jsp";

  public ReportItemController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }

  public void cancel(ReportItemDTO dto) throws IOException, ServletException
  {
    dto.unlock();
    this.view(dto.getId());
  }

  public void failCancel(ReportItemDTO dto) throws IOException, ServletException
  {
    this.edit(dto);
  }

  @Override
  public void create(ReportItemDTO dto, MultipartFileParameter design) throws IOException, ServletException
  {
    try
    {
      if (design != null)
      {
        dto.setReportName(design.getFilename());
        dto.applyWithFile(design.getInputStream());
      }
      else
      {
        dto.applyWithFile(null);
      }

      this.resp.getWriter().print(new JSONReturnObject(dto).toString());
    }
    catch (Throwable t)
    {
      boolean needsRedirect = ErrorUtility.handleFormError(t, req, resp);

      if (needsRedirect)
      {
        this.failCreate(dto, design);
      }
    }
  }

  @Override
  public void failCreate(ReportItemDTO dto, MultipartFileParameter design) throws IOException, ServletException
  {
    this.newInstance(dto);
  }

  public void delete(ReportItemDTO dto) throws IOException, ServletException
  {
    try
    {
      dto.delete();
      this.viewAll();
    }
    catch (Throwable t)
    {
      boolean redirected = false; // ErrorUtility.prepareThrowable(t, req, resp,
                                  // this.isAsynchronous());

      if (!redirected)
      {
        this.failDelete(dto);
      }
    }
  }

  public void failDelete(ReportItemDTO dto) throws IOException, ServletException
  {
    this.edit(dto);
  }

  public void edit(String id) throws IOException, ServletException
  {
    try
    {
      this.edit(ReportItemDTO.lock(super.getClientRequest(), id));
    }
    catch (Throwable t)
    {
      boolean redirect = false; // com.runwaysdk.geodashboard.util.ErrorUtility.prepareThrowable(t,
                                // req, resp, this.isAsynchronous());
      if (!redirect)
      {
        this.failEdit(id);
      }
    }
  }

  private void edit(ReportItemDTO dto) throws IOException, ServletException
  {
    try
    {
      req.setAttribute("item", dto);
      req.setAttribute("dashboards", DashboardDTO.getSortedDashboards(this.getClientRequest()).getResultSet());

      render("editComponent.jsp");
    }
    catch (Throwable t)
    {
      boolean redirect = false; // com.runwaysdk.geodashboard.util.ErrorUtility.prepareThrowable(t,
                                // req, resp, this.isAsynchronous());
      if (!redirect)
      {
        this.failEdit(dto.getId());
      }
    }
  }

  public void failEdit(String id) throws IOException, ServletException
  {
    this.view(id);
  }

  public void newInstance() throws IOException, ServletException
  {
    try
    {
      this.newInstance(new ReportItemDTO(this.getClientRequest()));
    }
    catch (Throwable t)
    {
      boolean redirect = false; // com.runwaysdk.geodashboard.util.ErrorUtility.prepareThrowable(t,
                                // req, resp, this.isAsynchronous());
      if (!redirect)
      {
        this.failNewInstance();
      }
    }
  }

  private void newInstance(ReportItemDTO dto) throws IOException, ServletException
  {
    try
    {
      req.setAttribute("item", dto);
      req.setAttribute("dashboards", DashboardDTO.getSortedDashboards(this.getClientRequest()).getResultSet());

      render("createComponent.jsp");
    }
    catch (Throwable t)
    {
      boolean redirect = false;
      if (!redirect)
      {
        this.failNewInstance();
      }
    }
  }

  public void failNewInstance() throws IOException, ServletException
  {
    this.viewAll();
  }

  @Override
  public void update(ReportItemDTO dto, MultipartFileParameter design) throws IOException, ServletException
  {
    try
    {
      if (design != null)
      {
        dto.setReportName(design.getFilename());
        dto.applyWithFile(design.getInputStream());
      }
      else
      {
        dto.applyWithFile(null);
      }

      this.resp.getWriter().print(new JSONReturnObject(dto).toString());
    }
    catch (Throwable t)
    {
      boolean needsRedirect = ErrorUtility.handleFormError(t, req, resp);

      if (needsRedirect)
      {
        this.failUpdate(dto, design);
      }
    }
  }

  public void failUpdate(ReportItemDTO dto) throws IOException, ServletException
  {
    this.edit(dto);
  }

  public void view(String id) throws IOException, ServletException
  {
    try
    {
      this.view(ReportItemDTO.get(super.getClientRequest(), id));
    }
    catch (Throwable t)
    {
      boolean redirect = false; // com.runwaysdk.geodashboard.util.ErrorUtility.prepareThrowable(t,
                                // req, resp, this.isAsynchronous());
      if (!redirect)
      {
        this.failView(id);
      }
    }
  }

  private void view(ReportItemDTO dto) throws IOException, ServletException
  {
    try
    {
      req.setAttribute("item", dto);
      render("viewComponent.jsp");
    }
    catch (Throwable t)
    {
      boolean redirect = false; // com.runwaysdk.geodashboard.util.ErrorUtility.prepareThrowable(t,
                                // req, resp, this.isAsynchronous());
      if (!redirect)
      {
        this.failView(dto.getId());
      }
    }
  }

  public void failView(String id) throws IOException, ServletException
  {
    this.viewAll();
  }

  public void viewAll() throws IOException, ServletException
  {
    try
    {
      ClientRequestIF clientRequest = super.getClientRequest();
      ReportItemQueryDTO query = ReportItemDTO.getAllInstances(clientRequest, null, true, 20, 1);
      req.setAttribute("query", query);

      this.render("viewAllComponent.jsp");
    }
    catch (Throwable t)
    {
      boolean redirect = ErrorUtility.prepareThrowable(t, req, resp, this.isAsynchronous());

      if (!redirect)
      {
        this.failViewAll();
      }
    }
  }

  public void failViewAll() throws IOException, ServletException
  {
    this.req.getRequestDispatcher("/index.jsp").forward(this.req, this.resp);
  }

  public void viewPage(String sortAttribute, Boolean isAscending, Integer pageSize, Integer pageNumber) throws IOException, ServletException
  {
    try
    {
      ClientRequestIF clientRequest = super.getClientRequest();
      ReportItemQueryDTO query = ReportItemDTO.getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
      req.setAttribute("query", query);
      render("viewAllComponent.jsp");
    }
    catch (Exception e)
    {
      this.failViewAll();
    }
  }

  @Override
  public void run(String report) throws IOException, ServletException
  {
    try
    {
      run(ReportItemDTO.getReportItemForDashboard(this.getClientRequest(), report));
    }
    catch (Throwable t)
    {
      boolean redirect = ErrorUtility.prepareThrowable(t, req, resp, this.isAsynchronous());

      if (!redirect)
      {
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
      }
    }
  }

  public void run(ReportItemDTO item) throws UnsupportedEncodingException, ServletException, IOException
  {
    /*
     * First validate permissions, this must be done before
     * response.getOutputStream() is called otherwise redirecting on the error
     * case will not work
     */
    item.validatePermissions();

    String reportUrl = this.getReportURL();

    List<ReportParameterDTO> parameters = new LinkedList<ReportParameterDTO>();

    Enumeration<String> parameterNames = req.getParameterNames();

    while (parameterNames.hasMoreElements())
    {
      String parameterName = parameterNames.nextElement();
      String[] parameterValues = req.getParameterValues(parameterName);

      ReportParameterDTO parameter = new ReportParameterDTO(this.getClientRequest());
      parameter.setParameterName(parameterName);
      parameter.setParameterValue(parameterValues[0]);

      parameters.add(parameter);
    }

    /*
     * Important: Calling resp.getOutputStream() changes the state of the HTTP
     * request and response objects. However, if an error occurs while rendering
     * the report we need to delegate to the standard error handling mechanism.
     * As such we can't call resp.getOutputStream() until we are sure the report
     * has rendered. Therefore, first render the report to a temp byte array
     * stream. Once that has rendered, copy the bytes from the byte array to the
     * servlet output stream. Note, this may cause memory problems if the report
     * being rendered is too big.
     */
    ByteArrayOutputStream rStream = new ByteArrayOutputStream();

    try
    {
      String url = this.req.getRequestURL().toString();
      String baseURL = url.substring(0, url.lastIndexOf("/"));

      item.render(rStream, parameters.toArray(new ReportParameterDTO[parameters.size()]), baseURL, reportUrl);

      req.setAttribute("report", rStream.toString());

      req.getRequestDispatcher("/WEB-INF/com/runwaysdk/geodashboard/report/report.jsp").forward(req, resp);
    }
    finally
    {
      rStream.close();
    }
  }

  public String getReportURL() throws UnsupportedEncodingException
  {
    String str = "com.runwaysdk.geodashboard.report.ReportItemController.generate.mojo?";
    boolean isFirst = true;

    Enumeration<String> paramNames = req.getParameterNames();
    while (paramNames.hasMoreElements())
    {
      String paramName = paramNames.nextElement();

      if (!paramName.equals("pageNumber"))
      {
        if (!isFirst)
        {
          str = str + "&";
        }

        String[] paramValues = req.getParameterValues(paramName);

        for (int i = 0; i < paramValues.length; i++)
        {
          String paramValue = paramValues[i];
          str = str + URLEncoder.encode(paramName, "UTF-8") + "=" + URLEncoder.encode(paramValue, "UTF-8");
        }

        isFirst = false;
      }
    }
    return str;
  }
}
