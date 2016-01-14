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
package com.runwaysdk.geodashboard;

import java.io.InputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONArray;

import com.runwaysdk.RunwayException;
import com.runwaysdk.business.SmartException;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.geodashboard.excel.InvalidExcelFileException;
import com.runwayskd.geodashboard.excel.AttributeInfoContentsHandler;
import com.runwayskd.geodashboard.excel.ExcelSheetReader;

public class DataUploader extends DataUploaderBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1960517297;

  public DataUploader()
  {
    super();
  }

  public static String getAttributeInformation(String fileName, InputStream fileStream)
  {
    try
    {
      AttributeInfoContentsHandler handler = new AttributeInfoContentsHandler();

      ExcelSheetReader reader = new ExcelSheetReader(handler);
      reader.process(fileStream);

      JSONArray information = handler.getAttributeInformation();

      return information.toString();
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
}
