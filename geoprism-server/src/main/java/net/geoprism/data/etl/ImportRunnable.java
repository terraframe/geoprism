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
package net.geoprism.data.etl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import net.geoprism.MappableClass;
import net.geoprism.data.etl.excel.ExcelDataFormatter;
import net.geoprism.data.etl.excel.ExcelSheetReader;
import net.geoprism.data.etl.excel.SourceContentHandler;

import org.json.JSONArray;

import com.runwaysdk.RunwayException;
import com.runwaysdk.business.SmartException;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

public class ImportRunnable
{
  private String configuration;

  private File   file;

  public ImportRunnable(String configuration, File file)
  {
    this.configuration = configuration;
    this.file = file;
  }

  @Transaction
  public ImportResponseIF run()
  {
    try
    {
      /*
       * First create the data types from the configuration
       */
      DataSetBuilderIF builder = new DataSetBuilder(configuration);
      builder.build();

      /*
       * Create and import the view objects from the configuration
       */
      SourceContextIF sContext = builder.getSourceContext();
      TargetContextIF tContext = builder.getTargetContext();

      /*
       * Before importing the data we must validate that the location text information
       */
      ImportResponseIF response = this.validateData(file, sContext, tContext);

      if (response != null)
      {
        return response;
      }

      /*
       * Import the data
       */
      this.importData(file, sContext, tContext);

      /*
       * Return a JSONArray of the datasets which were created a part of the import. Do not include datasets which have
       * already been created.
       */
      JSONArray datasets = new JSONArray();

      List<TargetDefinitionIF> definitions = tContext.getDefinitions();

      for (TargetDefinitionIF definition : definitions)
      {
        String type = definition.getTargetType();

        MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(type);
        MappableClass mClass = MappableClass.getMappableClass(mdBusiness);

        datasets.put(mClass.toJSON());
      }

      // Return the new data set definition
      return new SuccessResponse(datasets);
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

  private void importData(File file, SourceContextIF sContext, TargetContextIF tContext) throws FileNotFoundException, Exception, IOException
  {
    ConverterIF converter = new Converter(tContext);

    FileInputStream istream = new FileInputStream(file);

    try
    {
      SourceContentHandler handler = new SourceContentHandler(converter, sContext);
      ExcelDataFormatter formatter = new ExcelDataFormatter();

      ExcelSheetReader reader = new ExcelSheetReader(handler, formatter);
      reader.process(istream);
    }
    finally
    {
      istream.close();
    }
  }

  private ImportResponseIF validateData(File file, SourceContextIF sContext, TargetContextIF tContext) throws FileNotFoundException, Exception, IOException
  {
    LocationValidator converter = new LocationValidator(tContext);

    // while (!converter.isFinished())
    {
      converter.next();

      FileInputStream istream = new FileInputStream(file);

      try
      {
        SourceContentHandler handler = new SourceContentHandler(converter, sContext);
        ExcelDataFormatter formatter = new ExcelDataFormatter();

        ExcelSheetReader reader = new ExcelSheetReader(handler, formatter);
        reader.process(istream);
      }
      finally
      {
        istream.close();
      }
    }

    if (converter.getProblems().size() > 0)
    {
      return new ProblemResponse(converter.getProblems(), sContext, tContext);
    }

    return null;
  }

}
