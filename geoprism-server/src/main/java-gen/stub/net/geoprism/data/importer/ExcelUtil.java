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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.geoprism.data.importer.ExcelUtilBase;
import net.geoprism.data.importer.GenericTypeExcelListenerBuilder;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.io.ExcelExporter;
import com.runwaysdk.dataaccess.io.ExcelImporter;
import com.runwaysdk.dataaccess.io.ExcelImporter.ImportContext;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class ExcelUtil extends ExcelUtilBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -410766854;

  public ExcelUtil()
  {
    super();
  }

  public static InputStream exportExcelFile(String type, String country)
  {
    GeoEntity entity = GeoEntity.get(country);

    GenericTypeExcelListenerBuilder builder = new GenericTypeExcelListenerBuilder(entity);

    ExcelExporter exporter = new ExcelExporter(new XSSFWorkbook());

    builder.configure(exporter, type);

    exporter.addTemplate(type);

    ByteArrayOutputStream ostream = new ByteArrayOutputStream();

    try
    {
      exporter.write(ostream);
    }
    finally
    {
      try
      {
        ostream.close();
      }
      catch (IOException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }

    return new ByteArrayInputStream(ostream.toByteArray());
  }

  public static InputStream importExcelFile(InputStream istream, String defaultEntity)
  {
    GeoEntity entity = GeoEntity.get(defaultEntity);

    GenericTypeExcelListenerBuilder builder = new GenericTypeExcelListenerBuilder(entity);

    ExcelImporter importer = new ExcelImporter(istream);

    for (ImportContext context : importer.getContexts())
    {
      builder.configure(context);
    }

    ByteArrayOutputStream ostream = new ByteArrayOutputStream();

    try
    {
      importer.read(ostream);
    }
    finally
    {
      try
      {
        ostream.close();
      }
      catch (IOException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }

    byte[] bytes = ostream.toByteArray();

    if (bytes.length > 0)
    {
      return new ByteArrayInputStream(bytes);
    }

    return null;
  }

}
