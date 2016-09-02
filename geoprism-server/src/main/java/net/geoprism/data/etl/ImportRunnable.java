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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.geoprism.MappableClass;
import net.geoprism.data.etl.ImportValidator.DecimalAttribute;
import net.geoprism.data.etl.excel.ExcelDataFormatter;
import net.geoprism.data.etl.excel.ExcelSheetReader;
import net.geoprism.data.etl.excel.SourceContentHandler;

import org.json.JSONArray;

import com.runwaysdk.RunwayException;
import com.runwaysdk.business.SmartException;
import com.runwaysdk.constants.MdAttributeDecInfo;
import com.runwaysdk.dataaccess.MdAttributeDecDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

public class ImportRunnable
{
  static class ValidationResult
  {
    private ImportResponseIF              response;

    private Map<String, DecimalAttribute> attributes;

    public ValidationResult(ImportResponseIF response, Map<String, DecimalAttribute> attributes)
    {
      this.response = response;
      this.attributes = attributes;
    }

    public Map<String, DecimalAttribute> getAttributes()
    {
      return attributes;
    }

    public ImportResponseIF getResponse()
    {
      return response;
    }
  }

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
      ValidationResult result = this.validateData(file, sContext, tContext);

      if (result.getResponse() != null)
      {
        return result.getResponse();
      }

      /*
       * Update any scale or precision which is greater than its current definition
       */
      this.updateScaleAndPrecision(result.getAttributes());

      /*
       * Import the data
       */
      this.importData(file, sContext, tContext);

      /*
       * Return a JSONArray of the datasets which were created as part of the import. Do not include datasets which have
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

  private void updateScaleAndPrecision(Map<String, DecimalAttribute> attributes)
  {
    Set<Entry<String, DecimalAttribute>> entries = attributes.entrySet();

    for (Entry<String, DecimalAttribute> entry : entries)
    {
      String mdAttributeId = entry.getKey();
      DecimalAttribute attribute = entry.getValue();

      int scale = attribute.getScale();
      int total = attribute.getPrecision() + scale;

      MdAttributeDecDAOIF mdAttributeIF = (MdAttributeDecDAOIF) MdAttributeDecDAO.get(mdAttributeId);

      Integer length = new Integer(mdAttributeIF.getLength());
      Integer decimal = new Integer(mdAttributeIF.getDecimal());

      if (total > length || scale > decimal)
      {
        MdAttributeDecDAO mdAttribute = (MdAttributeDecDAO) mdAttributeIF.getBusinessDAO();
        mdAttribute.setValue(MdAttributeDecInfo.LENGTH, new Integer(total).toString());
        mdAttribute.setValue(MdAttributeDecInfo.DECIMAL, new Integer(scale).toString());
        mdAttribute.apply();
      }
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
      reader.process(istream, this.configuration);
    }
    finally
    {
      istream.close();
    }
  }

  private ValidationResult validateData(File file, SourceContextIF sContext, TargetContextIF tContext) throws FileNotFoundException, Exception, IOException
  {
    ImportValidator converter = new ImportValidator(tContext);

    FileInputStream istream = new FileInputStream(file);

    try
    {
      SourceContentHandler handler = new SourceContentHandler(converter, sContext);
      ExcelDataFormatter formatter = new ExcelDataFormatter();

      ExcelSheetReader reader = new ExcelSheetReader(handler, formatter);
      reader.process(istream, configuration);
    }
    finally
    {
      istream.close();
    }

    ImportResponseIF response = null;

    if (converter.getProblems().size() > 0)
    {
      response = new ProblemResponse(converter.getProblems(), sContext, tContext);
    }

    return new ValidationResult(response, converter.getAttributes());
  }
}
