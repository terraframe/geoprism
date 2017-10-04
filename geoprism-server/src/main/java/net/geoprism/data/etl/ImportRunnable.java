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
package net.geoprism.data.etl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONObject;

import com.runwaysdk.RunwayException;
import com.runwaysdk.business.SmartException;
import com.runwaysdk.constants.MdAttributeDecInfo;
import com.runwaysdk.constants.VaultProperties;
import com.runwaysdk.dataaccess.MdAttributeDecDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.util.IDGenerator;

import net.geoprism.MappableClass;
import net.geoprism.data.etl.ImportValidator.DecimalAttribute;
import net.geoprism.data.etl.excel.CountSheetHandler;
import net.geoprism.data.etl.excel.ExcelDataFormatter;
import net.geoprism.data.etl.excel.ExcelSheetReader;
import net.geoprism.data.etl.excel.SourceContentHandler;
import net.geoprism.gis.geoserver.SessionPredicate;

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

  private ProgressMonitorIF monitor;

  private String            configuration;

  private File              file;

  public ImportRunnable(String configuration, File file, ProgressMonitorIF monitor)
  {
    this.configuration = configuration;
    this.file = file;

    this.monitor = monitor;
  }

  public ImportResponseIF run()
  {
    try
    {
      int total = this.getRowNum(this.file);
      
      monitor.setTotal(total);
      
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

      ImportResponseIF response = this.validateAndConfigure(sContext, tContext);

      if (response != null)
      {
        return response;
      }

      /*
       * Import the data
       */
      monitor.setState(DataImportState.DATAIMPORT);

      JSONObject summary = this.importData(file, sContext, tContext);

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

      monitor.setState(DataImportState.COMPLETE);

      // Return the new data set definition
      return new SuccessResponse(datasets, summary);
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

  @Transaction
  private ImportResponseIF validateAndConfigure(SourceContextIF sContext, TargetContextIF tContext) throws FileNotFoundException, Exception, IOException
  {
    /*
     * Before importing the data we must validate that the location text information
     */
    monitor.setState(DataImportState.VALIDATION);
    ValidationResult result = this.validateData(file, sContext, tContext);

    if (result.getResponse() != null)
    {
      monitor.setState(DataImportState.VALIDATIONFAIL);
      return result.getResponse();
    }

    /*
     * Update any scale or precision which is greater than its current definition
     */
    this.updateScaleAndPrecision(result.getAttributes());

    return null;
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

  private JSONObject importData(File file, SourceContextIF sContext, TargetContextIF tContext) throws FileNotFoundException, IOException, Exception
  {
    ConverterIF converter = new Converter(tContext);

    FileInputStream istream = new FileInputStream(file);

    try
    {
      SourceContentHandler handler = new SourceContentHandler(converter, sContext, this.monitor, DataImportState.DATAIMPORT);
      ExcelDataFormatter formatter = new ExcelDataFormatter();

      ExcelSheetReader reader = new ExcelSheetReader(handler, formatter);
      reader.process(istream);

      JSONObject summary = new JSONObject();
      summary.put("total", handler.getTotalRows());
      summary.put("failures", handler.getNumberOfErrors());

      Workbook errors = converter.getErrors();

      if (errors != null)
      {
        String name = SessionPredicate.generateId();

        File directory = new File(new File(VaultProperties.getPath("vault.default"), "files"), name);
        directory.mkdirs();

        String fileName = IDGenerator.nextID();
        File dest = new File(directory, fileName);

        FileOutputStream fos = new FileOutputStream(dest);

        try
        {
          errors.write(fos);
        }
        finally
        {
          fos.close();
        }

        summary.put("file", name + "/" + fileName);

        return summary;
      }

      return summary;
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
      SourceContentHandler handler = new SourceContentHandler(converter, sContext, this.monitor, DataImportState.VALIDATION);
      ExcelDataFormatter formatter = new ExcelDataFormatter();

      ExcelSheetReader reader = new ExcelSheetReader(handler, formatter);
      reader.process(istream);
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

  private int getRowNum(File file) throws FileNotFoundException, IOException, Exception
  {
    FileInputStream istream = new FileInputStream(file);

    try
    {
      CountSheetHandler handler = new CountSheetHandler();
      ExcelDataFormatter formatter = new ExcelDataFormatter();

      ExcelSheetReader reader = new ExcelSheetReader(handler, formatter);
      reader.process(istream);

      return handler.getRowNum();
    }
    finally
    {
      istream.close();
    }
  }
}
