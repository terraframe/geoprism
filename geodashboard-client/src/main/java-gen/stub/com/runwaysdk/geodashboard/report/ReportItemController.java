package com.runwaysdk.geodashboard.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.MultipartFileParameter;

public class ReportItemController extends ReportItemControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR = "/WEB-INF/com/runwaysdk/geodashboard/report/ReportItem/";

  public static final String LAYOUT  = "WEB-INF/templates/layout.jsp";

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

      this.view(dto.getId());
    }
    catch (Throwable t)
    {
      boolean redirect = false; // com.runwaysdk.geodashboard.util.ErrorUtility.prepareThrowable(t, req, resp, this.isAsynchronous());
      if (!redirect)
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
      boolean redirected = false; // ErrorUtility.prepareThrowable(t, req, resp, this.isAsynchronous());

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
      boolean redirect = false; // com.runwaysdk.geodashboard.util.ErrorUtility.prepareThrowable(t, req, resp, this.isAsynchronous());
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
      req.setAttribute("outputFormat", OutputFormatDTO.allItems(this.getClientRequest()));
      req.setAttribute("item", dto);

      render("editComponent.jsp");
    }
    catch (Throwable t)
    {
      boolean redirect = false; // com.runwaysdk.geodashboard.util.ErrorUtility.prepareThrowable(t, req, resp, this.isAsynchronous());
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
      boolean redirect = false; // com.runwaysdk.geodashboard.util.ErrorUtility.prepareThrowable(t, req, resp, this.isAsynchronous());
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
      req.setAttribute("outputFormat", OutputFormatDTO.allItems(this.getClientRequest()));
      req.setAttribute("item", dto);

      render("createComponent.jsp");
    }
    catch (Throwable t)
    {
      boolean redirect = false; // com.runwaysdk.geodashboard.util.ErrorUtility.prepareThrowable(t, req, resp, this.isAsynchronous());
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

      this.view(dto.getId());
    }
    catch (Throwable t)
    {
      boolean redirect = false; // com.runwaysdk.geodashboard.util.ErrorUtility.prepareThrowable(t, req, resp, this.isAsynchronous());

      if (!redirect)
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
      boolean redirect = false; // com.runwaysdk.geodashboard.util.ErrorUtility.prepareThrowable(t, req, resp, this.isAsynchronous());
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
//      RedirectUtility utility = new RedirectUtility(req, resp);
//      utility.put("id", dto.getId());
//      utility.checkURL(this.getClass().getSimpleName(), "view");

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
    ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.report.ReportItemQueryDTO query = ReportItemDTO.getAllInstances(clientRequest, null, true, 20, 1);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }

  public void failViewAll() throws IOException, ServletException
  {
    resp.sendError(500);
  }

  public void viewPage(String sortAttribute, Boolean isAscending, Integer pageSize, Integer pageNumber) throws IOException, ServletException
  {
    ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.geodashboard.report.ReportItemQueryDTO query = ReportItemDTO.getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }

  public void failViewPage(String sortAttribute, String isAscending, String pageSize, String pageNumber) throws IOException, ServletException
  {
    resp.sendError(500);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void generate(String report) throws IOException, ServletException
  {
    try
    {
      ReportItemDTO item = ReportItemDTO.get(this.getClientRequest(), report);
      /*
       * First validate permissions, this must be done before
       * response.getOutputStream() is called otherwise redirecting on the error
       * case will not work
       */
      item.validatePermissions();

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
       * request and response objects. However, if an error occurs while
       * rendering the report we need to delegate to the standard error handling
       * mechanism. As such we can't call resp.getOutputStream() until we are
       * sure the report has rendered. Therefore, first render the report to a
       * temp byte array stream. Once that has rendered, copy the bytes from the
       * byte array to the servlet output stream. Note, this may cause memory
       * problems if the report being rendered is too big.
       */
      ByteArrayOutputStream rStream = new ByteArrayOutputStream();

      try
      {
        String url = this.req.getRequestURL().toString();
        String baseURL = url.substring(0, url.lastIndexOf('/'));

        item.render(rStream, parameters.toArray(new ReportParameterDTO[parameters.size()]), baseURL);

        if (item.getOutputFormatEnumNames().contains(OutputFormatDTO.PDF.name()))
        {
          String fileName = item.getReportLabel().getValue().replaceAll("\\s", "_");
          resp.setHeader("Content-Type", "application/pdf");
          resp.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".pdf");
        }

        ServletOutputStream oStream = resp.getOutputStream();

        try
        {
          oStream.write(rStream.toByteArray());
        }
        finally
        {
          oStream.flush();
          oStream.close();
        }
      }
      finally
      {
        rStream.close();
      }
    }
    catch (Throwable t)
    {
      this.failGenerate(report);
    }
  }

  public void failGenerate(java.lang.String report) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }
}
