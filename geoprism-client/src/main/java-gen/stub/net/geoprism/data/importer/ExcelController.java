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
package net.geoprism.data.importer;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import net.geoprism.data.etl.excel.ExcelImportHistoryDTO;
import net.geoprism.localization.LocalizationFacadeDTO;
import net.geoprism.report.PairViewDTO;
import net.geoprism.report.ReportItemDTO;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.controller.ErrorUtility;
import com.runwaysdk.controller.MultipartFileParameter;
import com.runwaysdk.system.VaultFileDTO;

public class ExcelController extends ExcelControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String JSP_DIR = "/WEB-INF/net/geoprism/data/importer/Excel/";

  public static final String LAYOUT  = "/WEB-INF/templates/layout.jsp";

  public ExcelController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }
  
  @Override
  public void downloadErrorSpreadsheet(java.lang.String historyId) throws java.io.IOException, javax.servlet.ServletException
  {
    VaultFileDTO vfile = ExcelImportHistoryDTO.get(getClientRequest(), historyId).getErrorFile();
    
    String fileName = vfile.getFileName() + "." + vfile.getFileExtension();
    
    resp.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    resp.setHeader("Content-Disposition", "attachment;filename=" + fileName);
    
    ExcelImportHistoryDTO.downloadErrorSpreadsheet(getClientRequest(), historyId, resp.getOutputStream());
  }
  
  @Override
  public void failDownloadErrorSpreadsheet(java.lang.String historyId) throws java.io.IOException, javax.servlet.ServletException
  {
    // do nothing
  }
  
  public void excelImportFromVault(java.lang.String vaultId, String config) throws java.io.IOException, javax.servlet.ServletException
  {
    ExcelUtilDTO.excelImportFromVault(this.getClientRequest(), vaultId, config);
    
    resp.sendRedirect("prism/home#/data/uploadmanager");
  }
  
  public void failExcelImportFromVault(java.lang.String vaultId) throws java.io.IOException, javax.servlet.ServletException
  {
    // do NOTHING (as usual)
  }
  
  public void getReconstructionJSON(java.lang.String historyId) throws java.io.IOException, javax.servlet.ServletException
  {
    ExcelImportHistoryDTO history = ExcelImportHistoryDTO.get(this.getClientRequest(), historyId);
    
    JSONObject reconstructJSON;
    try {
      reconstructJSON = new JSONObject(history.getReconstructionJSON());
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
    
    resp.getWriter().write(reconstructJSON.toString());
  }
  
  @Override
  public void clearHistory() throws java.io.IOException, javax.servlet.ServletException
  {
    ExcelImportHistoryDTO.deleteAllHistory(getClientRequest());
  }
  
  @Override
  public void failClearHistory() throws java.io.IOException, javax.servlet.ServletException
  {
    // Do nothing! Intentionally empty.
  }
  
  @Override
  public void getAllHistory() throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      JSONArray jHistories = new JSONArray();
      
      ExcelImportHistoryDTO[] histories = ExcelImportHistoryDTO.getAllHistory(getClientRequest());
      
      for (int i = 0; i < histories.length; ++i)
      {
        ExcelImportHistoryDTO history = histories[i];
        
        JSONObject jHistory = new JSONObject();
        
        jHistory.put("id", history.getId());
        
        jHistory.put("name", history.getFileName());
        
        if (history.getImportCount() != null)
        {
          jHistory.put("importCount", history.getImportCount());
        }
        else
        {
          jHistory.put("importCount", 0);
        }
        
        jHistory.put("totalRecords", history.getTotalRecords());
        jHistory.put("status", history.getStatus().get(0).item(getClientRequest()).getDisplayLabel().getValue());
        jHistory.put("startTime", new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z").format(history.getStartTime()));
        if (history.getEndTime() != null)
        {
          jHistory.put("endTime", new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z").format(history.getEndTime()));
        }
        else
        {
          jHistory.put("endTime", "");
        }
        jHistory.put("hasErrorSpreadsheet", history.getErrorFile() != null);
        
        jHistory.put("errorMsg", history.getHistoryInformation().getValue());
        
        jHistory.put("geoSyns", history.getNumberUnknownGeos());
        
        jHistory.put("termSyns", history.getNumberUnknownTerms());
        
        jHistories.put(jHistory);
      }
      
      resp.getWriter().write(jHistories.toString());
    }
    catch (JSONException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  @Override
  public void failGetAllHistory() throws java.io.IOException, javax.servlet.ServletException
  {
    req.getRequestDispatcher("/index.jsp").forward(req, resp);
  }

  @Override
  public void exportExcelForm(String type) throws IOException, ServletException
  {
    PairViewDTO[] countries = ReportItemDTO.getGeoEntitySuggestions(this.getClientRequest(), "", 0);

    this.req.setAttribute("countries", countries);
    this.req.setAttribute("type", type);

    this.render("exportForm.jsp");
  }

  @Override
  public void failExportExcelForm(String type) throws IOException, ServletException
  {
    resp.sendError(500);
  }

  @Override
  public void exportExcelFile(String type, String country, String downloadToken) throws IOException, ServletException
  {

    try
    {
      // The reason we're including a cookie here is because the browser does not give us any indication of when our
      // response from the server is successful and its downloading the file.
      // This "hack" sends a downloadToken to the client, which the client then checks for the existence of every so
      // often. When the cookie exists, it knows its downloading it.
      // http://stackoverflow.com/questions/1106377/detect-when-browser-receives-file-download
      Cookie cookie = new Cookie("downloadToken", downloadToken);
      cookie.setMaxAge(10 * 60); // 10 minute cookie expiration
      resp.addCookie(cookie);

      InputStream istream = ExcelUtilDTO.exportExcelFile(this.getClientRequest(), type, country);

      try
      {
        // copy it to response's OutputStream
        this.resp.setContentType("application/xlsx");
        this.resp.setHeader("Content-Disposition", "attachment; filename=\"template.xlsx\"");

        IOUtils.copy(istream, this.resp.getOutputStream());

        this.resp.flushBuffer();
      }
      finally
      {
        istream.close();
      }
    }
    catch (RuntimeException e)
    {
      if (!resp.isCommitted())
      {
        resp.reset();
      }

      ErrorUtility.prepareThrowable(e, req, resp, false, true);
    }
  }

  @Override
  public void excelImportForm() throws IOException, ServletException
  {
    PairViewDTO[] countries = ReportItemDTO.getGeoEntitySuggestions(this.getClientRequest(), "", 0);

    this.req.setAttribute("countries", countries);

    this.render("importForm.jsp");
  }

  @Override
  public void failExcelImportForm() throws IOException, ServletException
  {
    resp.sendError(500);
  }

  @Override
  public void importExcelFile(MultipartFileParameter file, String country, String downloadToken) throws IOException, ServletException
  {
    // The reason we're including a cookie here is because the browser does not give us any indication of when our
    // response from the server is successful and its downloading the file.
    // This "hack" sends a downloadToken to the client, which the client then checks for the existence of every so
    // often. When the cookie exists, it knows its downloading it.
    // http://stackoverflow.com/questions/1106377/detect-when-browser-receives-file-download

    Cookie cookie = new Cookie("downloadToken", downloadToken);
    cookie.setMaxAge(10 * 60); // 10 minute cookie expiration
    resp.addCookie(cookie);

    try
    {
      if (file == null)
      {
        throw new RuntimeException(LocalizationFacadeDTO.getFromBundles(this.getClientRequest(), "file.required"));
      }

      InputStream istream = file.getInputStream();

      try
      {
        InputStream result = ExcelUtilDTO.importExcelFile(this.getClientRequest(), istream, country);

        if (result != null)
        {
          // copy it to response's OutputStream
          this.resp.setContentType("application/xlsx");
          this.resp.setHeader("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "\"");

          IOUtils.copy(result, this.resp.getOutputStream());
        }
        else
        {
          this.resp.getWriter().print("<p id=\"upload_result\" class=\"success\"></p>");
        }
        
        this.resp.flushBuffer();
      }
      finally
      {
        istream.close();
      }
    }
    catch (Throwable t)
    {
      this.resp.getWriter().print("<p id=\"upload_result\" class=\"error\">" + t.getLocalizedMessage() + "</p>");
    }
  }
}
