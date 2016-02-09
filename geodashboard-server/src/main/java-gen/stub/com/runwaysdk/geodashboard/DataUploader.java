/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Runway SDK(tm). If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.geodashboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.RunwayException;
import com.runwaysdk.business.SmartException;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.constants.VaultProperties;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.excel.InvalidExcelFileException;
import com.runwaysdk.geodashboard.gis.geoserver.SessionPredicate;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.AllowedInQuery;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.geo.UniversalQuery;
import com.runwayskd.geodashboard.excel.ExcelDataFormatter;
import com.runwayskd.geodashboard.excel.ExcelSheetReader;
import com.runwayskd.geodashboard.excel.FieldInfoContentsHandler;
import com.runwayskd.geodashboard.excel.ViewContentHandler;
import com.runwayskd.geodashboard.excel.ViewContext;

public class DataUploader extends DataUploaderBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1960517297;

  public DataUploader()
  {
    super();
  }

  public static String getAttributeInformation(String fileName, InputStream fileStream)
  {
    // Save the file to the file system
    try
    {
      String name = SessionPredicate.generateId();

      File directory = new File(new File(VaultProperties.getPath("vault.default"), "files"), name);
      directory.mkdirs();

      File file = new File(directory, fileName);

      FileUtils.copyInputStreamToFile(fileStream, file);

      FieldInfoContentsHandler handler = new FieldInfoContentsHandler();
      ExcelDataFormatter formatter = new ExcelDataFormatter();

      ExcelSheetReader reader = new ExcelSheetReader(handler, formatter);
      reader.process(new FileInputStream(file));

      JSONObject object = new JSONObject();
      object.put("sheets", handler.getSheets());
      object.put("directory", directory.getName());
      object.put("filename", fileName);

      return object.toString();
    }
    catch (InvalidFormatException e)
    {
      InvalidExcelFileException ex = new InvalidExcelFileException(e);
      ex.setFileName(fileName);

      throw ex;
    }
    catch (RunwayException | SmartException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public static String getOptionsJSON()
  {
    try
    {
      JSONArray countries = new JSONArray();

      QueryFactory factory = new QueryFactory();
      AllowedInQuery aiQuery = new AllowedInQuery(factory);
      aiQuery.WHERE(aiQuery.getParent().EQ(Universal.getRoot()));

      UniversalQuery uQuery = new UniversalQuery(factory);
      uQuery.WHERE(uQuery.EQ(aiQuery.getChild()));
      uQuery.ORDER_BY_ASC(uQuery.getDisplayLabel().localize());

      OIterator<? extends Universal> it = uQuery.getIterator();

      try
      {
        while (it.hasNext())
        {
          Universal universal = it.next();

          List<Term> children = universal.getAllDescendants(AllowedIn.CLASS).getAll();
          children.add(0, universal);

          JSONArray options = new JSONArray();

          for (Term child : children)
          {
            JSONObject option = new JSONObject();
            option.put("value", child.getId());
            option.put("label", child.getDisplayLabel().getValue());

            options.put(option);
          }

          JSONObject country = new JSONObject();
          country.put("label", universal.getDisplayLabel().getValue());
          country.put("value", universal.getId());
          country.put("options", options);

          countries.put(country);
        }
      }
      finally
      {
        it.close();
      }

      JSONObject options = new JSONObject();
      options.put("countries", countries);

      return options.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Transaction
  public static String importData(String configuration)
  {
    try
    {
      /*
       * First create the data types from the configuration
       */

      /*
       * Create and import the view objects from the configuration
       */
      ViewContext context = new ViewContext(configuration);

      String name = context.getDirectory();
      String filename = context.getFilename();

      File directory = new File(new File(VaultProperties.getPath("vault.default"), "files"), name);
      File file = new File(directory, filename);

      FileInputStream istream = new FileInputStream(file);

      try
      {
        ViewContentHandler handler = new ViewContentHandler(context);
        ExcelDataFormatter formatter = new ExcelDataFormatter();

        ExcelSheetReader reader = new ExcelSheetReader(handler, formatter);
        reader.process(istream);
      }
      finally
      {
        istream.close();
      }

      FileUtils.deleteDirectory(directory);

      return "";
    }
    catch (RunwayException | SmartException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }
  }
}
