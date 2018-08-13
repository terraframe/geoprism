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
package net.geoprism.dhis2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PipedInputStream;

import net.geoprism.data.etl.DataImportRunnable;
import net.geoprism.data.etl.ImportResponseIF;
import net.geoprism.data.etl.ProgressMonitorIF;
import net.geoprism.data.etl.excel.ExcelDataFormatter;
import net.geoprism.data.etl.excel.ExcelSheetReader;
import net.geoprism.dhis2.connector.AbstractDHIS2Connector;
import net.geoprism.dhis2.connector.DHIS2HTTPCredentialConnector;
import net.geoprism.dhis2.palestine.PalestineContentHandler;
import net.geoprism.dhis2.palestine.PalestineConverter;
import net.geoprism.dhis2.palestine.PalestineResponse;
import net.geoprism.dhis2.util.DHIS2IdFinder;

import org.apache.poi.ss.usermodel.Workbook;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.system.VaultFile;

public class DHIS2Plugin implements DHIS2PluginIF
{

  @Override
  public String findAttributes()
  {
    return DHIS2IdFinder.findAttributes();
  }

  @Override
  public String findPrograms()
  {
    return DHIS2IdFinder.findPrograms();
  }

  @Override
  public String findTrackedEntities()
  {
    return DHIS2IdFinder.findTrackedEntities();
  }

  @Override
  public ImportResponseIF importData(File file, String filename, ProgressMonitorIF monitor)
  {
    FileInputStream istream = null;
    
    try
    {
      istream = new FileInputStream(file);
      
      // TODO : Use OAuth instead
      AbstractDHIS2Connector dhis2 = new DHIS2HTTPCredentialConnector();
      dhis2.setServerUrl("http://127.0.0.1:8085/");
      dhis2.setCredentials("admin", "district");
      
      PalestineConverter converter = new PalestineConverter(dhis2, monitor);
      
      PalestineContentHandler handler = new PalestineContentHandler(converter, monitor);
      ExcelDataFormatter formatter = new ExcelDataFormatter();

      ExcelSheetReader reader = new ExcelSheetReader(handler, formatter);
      reader.process(istream);

      PalestineResponse summary = new PalestineResponse();
      summary.setTotal(monitor.getImportCount());
      summary.setFailures(handler.getNumberOfErrors());

      Workbook errors = converter.getErrors();

      if (errors != null)
      {
        try (PipedInputStream pis = new PipedInputStream())
        {
          DataImportRunnable.CopyRunnable runnable = new DataImportRunnable.CopyRunnable(pis, errors);
          Thread t = new Thread(runnable);
          t.setUncaughtExceptionHandler(runnable);
          t.setDaemon(true);
          t.start();

          VaultFile vf2 = VaultFile.createAndApply(filename, pis);

          t.join();

          summary.setFileId(vf2.getId());

          if (runnable.getThrowable() != null)
          {
            throw new ProgrammingErrorException(runnable.getThrowable());
          }
        }
      }

      if (converter.getProblems().size() > 0)
      {
        summary.setProblems(converter.getProblems());
      }

      return summary;
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }
    finally
    {
      try
      {
        istream.close();
      }
      catch (IOException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }
  }

}
