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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import net.geoprism.dashboard.DashboardDTO;
import net.geoprism.localization.LocalizationFacadeDTO;
import net.geoprism.report.ReportItemControllerBase;
import net.geoprism.report.ReportItemQueryDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.ErrorUtility;
import com.runwaysdk.controller.MultipartFileParameter;
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
    this.edit(dto.getDashboardId());
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
    this.edit(dto.getDashboardId());
  }

  public void edit(String dashboardId) throws IOException, ServletException
  {
    try
    {
      this.edit(dashboardId, ReportItemDTO.lockOrCreateReport(super.getClientRequest(), dashboardId));
    }
    catch (Throwable t)
    {
      boolean redirect = false;
      if (!redirect)
      {
        this.failEdit(dashboardId);
      }
    }
  }

  private void edit(String dashboardId, ReportItemDTO dto) throws IOException, ServletException
  {
    try
    {
      req.setAttribute("item", dto);
      req.setAttribute("dashboardId", dashboardId);

      render("editComponent.jsp");
    }
    catch (Throwable t)
    {
      boolean redirect = false;

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

  @Override
  public void failUpdate(ReportItemDTO dto, MultipartFileParameter design) throws IOException, ServletException
  {
    this.edit(dto.getDashboardId(), dto);
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
    this.req.getRequestDispatcher("/index.jsp").forward(this.req, this.resp);
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
  public void run(String report, String configuration) throws IOException, ServletException
  {
    try
    {
      run(ReportItemDTO.getReportItemForDashboard(this.getClientRequest(), report), configuration);
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

  public void run(ReportItemDTO item, String configuration) throws UnsupportedEncodingException, ServletException, IOException
  {
    if (item != null)
    {
      try
      {
        /*
         * First validate permissions, this must be done before response.getOutputStream() is called otherwise
         * redirecting on the error case will not work
         */
        item.validatePermissions();

        String reportUrl = this.getReportURL();
        String format = null;

        JSONObject json = new JSONObject(configuration);

        List<ReportParameterDTO> parameters = new LinkedList<ReportParameterDTO>();

        JSONArray jsonArray = json.getJSONArray("parameters");

        for (int i = 0; i < jsonArray.length(); i++)
        {
          JSONObject object = jsonArray.getJSONObject(i);

          String name = object.getString("name");
          String value = object.getString("value");

          parameters.add(this.createReportParameter(name, value));

          if (name.equals("format"))
          {
            format = value;
          }
        }

        /*
         * Important: Calling resp.getOutputStream() changes the state of the HTTP request and response objects.
         * However, if an error occurs while rendering the report we need to delegate to the standard error handling
         * mechanism. As such we can't call resp.getOutputStream() until we are sure the report has rendered. Therefore,
         * first render the report to a temp byte array stream. Once that has rendered, copy the bytes from the byte
         * array to the servlet output stream. Note, this may cause memory problems if the report being rendered is too
         * big.
         */
        ByteArrayOutputStream rStream = new ByteArrayOutputStream();

        try
        {
          String url = this.req.getRequestURL().toString();
          String baseURL = url.substring(0, url.lastIndexOf("/"));

          item.render(rStream, parameters.toArray(new ReportParameterDTO[parameters.size()]), baseURL, reportUrl);

          if (format == null || format.equalsIgnoreCase("html"))
          {
            req.setAttribute("report", rStream.toString());

            req.getRequestDispatcher("/WEB-INF/com/runwaysdk/geodashboard/report/report.jsp").forward(req, resp);
          }
          else
          {
            String fileName = item.getReportLabel().getValue().replaceAll("\\s", "_");
            resp.setHeader("Content-Type", "application/" + format);
            resp.setHeader("Content-Disposition", "attachment;filename=" + fileName + "." + format);

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
        }
        finally
        {
          rStream.close();
        }
      }
      catch (JSONException e)
      {
        boolean redirect = ErrorUtility.prepareThrowable(e, req, resp, this.isAsynchronous());

        if (!redirect)
        {
          req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
      }
    }
    else
    {
      req.setAttribute("report", LocalizationFacadeDTO.getFromBundles(this.getClientRequest(), "report.empty"));
    }
  }

  private ReportParameterDTO createReportParameter(String parameterName, String parameterValue)
  {
    ReportParameterDTO parameter = new ReportParameterDTO(this.getClientRequest());
    parameter.setParameterName(parameterName);
    parameter.setParameterValue(parameterValue);

    return parameter;
  }

  public String getReportURL() throws UnsupportedEncodingException
  {
    String str = "net.geoprism.report.ReportItemController.generate.mojo?";
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
