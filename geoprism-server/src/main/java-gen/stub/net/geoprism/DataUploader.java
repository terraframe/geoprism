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
package net.geoprism;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.geoprism.data.etl.ConfigurationBuilder;
import net.geoprism.data.etl.Converter;
import net.geoprism.data.etl.ConverterIF;
import net.geoprism.data.etl.DataSetBuilder;
import net.geoprism.data.etl.DataSetBuilderIF;
import net.geoprism.data.etl.ExcelSourceBinding;
import net.geoprism.data.etl.SourceContextIF;
import net.geoprism.data.etl.SourceDefinitionIF;
import net.geoprism.data.etl.TargetContextIF;
import net.geoprism.data.etl.TargetDefinitionIF;
import net.geoprism.data.etl.excel.ExcelDataFormatter;
import net.geoprism.data.etl.excel.ExcelSheetReader;
import net.geoprism.data.etl.excel.FieldInfoContentsHandler;
import net.geoprism.data.etl.excel.InvalidExcelFileException;
import net.geoprism.data.etl.excel.SourceContentHandler;
import net.geoprism.gis.geoserver.SessionPredicate;
import net.geoprism.localization.LocalizationFacade;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.RunwayException;
import com.runwaysdk.business.SmartException;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.constants.VaultProperties;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.ReservedWords;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.AllowedInQuery;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.geo.UniversalQuery;

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
          // children.add(0, universal);

          JSONArray options = new JSONArray();

          JSONObject root = new JSONObject();
          root.put("value", universal.getId());
          root.put("label", LocalizationFacade.getFromBundles("country"));
          options.put(root);

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

  @Authenticate
  public static String importData(String configuration)
  {
    try
    {
      JSONObject object = new JSONObject(configuration);

      String name = object.getString("directory");
      String filename = object.getString("filename");

      File directory = new File(new File(VaultProperties.getPath("vault.default"), "files"), name);
      File file = new File(directory, filename);

      String result = DataUploader.importData(configuration, file);

      FileUtils.deleteDirectory(directory);

      return result;
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Transaction
  private static String importData(String configuration, File file)
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

      JSONArray datasets = new JSONArray();

      List<TargetDefinitionIF> definitions = tContext.getDefinitions();

      for (TargetDefinitionIF definition : definitions)
      {
        if (definition.isNew())
        {
          String type = definition.getTargetType();

          MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(type);
          MappableClass mClass = MappableClass.getMappableClass(mdBusiness);

          datasets.put(mClass.toJSON());
        }
      }

      // Return the new data set definition
      return datasets.toString();
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

  /**
   * Deletes the temp files of the import
   * 
   * @param configuration
   */
  public static void cancelImport(String configuration)
  {
    try
    {
      JSONObject object = new JSONObject(configuration);

      String name = object.getString("directory");

      File directory = new File(new File(VaultProperties.getPath("vault.default"), "files"), name);

      FileUtils.deleteDirectory(directory);
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public static String getSavedConfiguration(String id, String sheetName)
  {
    try
    {
      ExcelSourceBinding binding = ExcelSourceBinding.get(id);

      SourceDefinitionIF sDefinition = binding.getDefinition(sheetName);
      TargetDefinitionIF tDefinition = binding.getTargetBinding().getDefinition();

      ConfigurationBuilder builder = new ConfigurationBuilder(sDefinition, tDefinition);
      JSONObject configuration = builder.getConfiguration();

      return configuration.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public static String getSystemName(String description)
  {
    return getSystemName(description, "", true);
  }

  /**
   * 
   * @param description
   * @param suffix
   * @param isClassName
   * @param partDelimiter
   *          The character used between the split segments (words) of the name.
   * @return
   */
  public static String getSystemName(String description, String suffix, boolean isClassName, String partDelimiter)
  {
    String systemName = description;
    String name = description.replace("/", " Or ").replace("&", " And ");
    // String[] parts = name.split("[^a-zA-Z0-9]");
    String[] parts = name.split("[^\\p{L}0-9]");
    StringBuffer sb = new StringBuffer();
    if (parts.length == 1 && description.equals(description.toUpperCase()))
    {
      // It's an acronym, so use it as is.
      systemName = description;
    }
    else
    {
      // Create a camelcase representation of the description
      for (int i = 0; i < parts.length; i++)
      {
        String part = parts[i];
        if (part.length() > 0)
        {
          if (i == parts.length - 1)
          {
            // Last part
            String arabicPart = convertRomanToArabic(part);
            if (arabicPart.equals(part))
            {
              // Not a roman numeral
              sb.append(part.substring(0, 1).toUpperCase());
              sb.append(part.substring(1).toLowerCase());
            }
            else
            {
              // Roman numeral converted to arabic
              sb.append(arabicPart);
            }
          }
          else
          {
            sb.append(part.substring(0, 1).toUpperCase());
            sb.append(part.substring(1).toLowerCase());
          }

          if (i < parts.length - 1)
          {
            sb.append(partDelimiter);
          }
        }

      }
      systemName = sb.toString();

      if (ReservedWords.javaContains(systemName) || ReservedWords.sqlContains(systemName))
      {
        systemName += suffix;
      }

      if (!isClassName)
      {
        systemName = systemName.substring(0, 1).toLowerCase() + systemName.substring(1);
      }
    }
    return systemName;
  }

  public static String getSystemName(String description, String suffix, boolean isClassName)
  {
    return getSystemName(description, suffix, isClassName, "");
  }

  private static String convertRomanToArabic(String part)
  {
    if ("IV".equals(part.toUpperCase()))
    {
      return part.substring(0, part.length() - 2) + "4";
    }
    if ("V".equals(part.toUpperCase()))
    {
      return part.substring(0, part.length() - 1) + "5";
    }
    if ("III".equals(part.toUpperCase()))
    {
      return part.substring(0, part.length() - 3) + "3";
    }
    if ("II".equals(part.toUpperCase()))
    {
      return part.substring(0, part.length() - 2) + "2";
    }
    if ("I".equals(part.toUpperCase()))
    {
      return part.substring(0, part.length() - 1) + "1";
    }
    return part;
  }

}