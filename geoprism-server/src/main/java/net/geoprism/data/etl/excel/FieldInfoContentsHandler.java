/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.data.etl.excel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.ss.util.CellReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdView;

import net.geoprism.data.etl.ColumnType;
import net.geoprism.data.etl.ExcelFieldBindingQuery;
import net.geoprism.data.etl.ExcelSourceBinding;
import net.geoprism.data.etl.ExcelSourceBindingQuery;
import net.geoprism.localization.LocalizationFacade;

public class FieldInfoContentsHandler implements SheetHandler
{
  public static class Field
  {
    /**
     * Number of unique values
     */
    private static final int LIMIT = 10;

    private String           name;

    private int              precision;

    private int              scale;

    private int              inputPosition;

    private Set<ColumnType>  dataTypes;

    private Set<String>      values;
    
    private String           categoryId;
    
    private ColumnType       realType; // this is read from dhis2
    
    private String           label;

    public Field()
    {
      this.dataTypes = new TreeSet<ColumnType>();
      this.precision = 0;
      this.scale = 0;
      this.values = new HashSet<String>(LIMIT);
    }

    public void setName(String name)
    {
      this.name = name.trim();
    }
    
    public void setLabel(String label)
    {
      this.label = label.trim();
    }

    public String getName()
    {
      return name.trim();
    }

    public void setInputPosition(int position)
    {
      this.inputPosition = position;
    }

    public int getInputPosition()
    {
      return this.inputPosition;
    }

    public void addDataType(ColumnType dataType)
    {
      this.dataTypes.add(dataType);
    }

    public void setPrecision(Integer precision)
    {
      this.precision = Math.max(this.precision, precision);
    }

    public void setScale(Integer scale)
    {
      this.scale = Math.max(this.scale, scale);
    }

    public void addValue(String value)
    {
      if (this.values.size() < LIMIT)
      {
        this.values.add(value);
      }
    }
    
    public void setCategoryId(String oid)
    {
      this.categoryId = oid;
    }
    
    public void setRealType(ColumnType realType)
    {
      this.realType = realType;
    }

    public JSONObject toJSON() throws JSONException
    {
      JSONObject object = new JSONObject();
      object.put("name", this.name.trim());
      
      if (label == null)
      {
        object.put("label", this.name.trim());
      }
      else
      {
        object.put("label", this.label.trim());
      }
      
      object.put("aggregatable", true);
      object.put("fieldPosition", this.getInputPosition());
      
      if (this.dataTypes.size() == 1)
      {
        ColumnType type = this.dataTypes.iterator().next();
        if (this.realType != null)
        {
          type = this.realType;
        }
        
        object.put("type", type.name());
        object.put("columnType", type.name());
        object.put("accepted", false);

        if (realType != null)
        {
          if (this.categoryId != null)
          {
            object.put("type", ColumnType.CATEGORY.name());
            object.put("root", this.categoryId);
          }
        }
        else if (type.equals(ColumnType.NUMBER))
        {
          if (this.scale > 0)
          {
            object.put("precision", ( this.precision + this.scale ));
            object.put("scale", this.scale);
            object.put("type", ColumnType.DOUBLE.name());
            object.put("ratio", false);
          }
          else
          {
            object.put("type", ColumnType.LONG.name());
          }
        }
        else if ((type.equals(ColumnType.TEXT) && this.values.size() < LIMIT))
        {
          object.put("type", ColumnType.CATEGORY.name());
        }
      }
      else
      {
        object.put("columnType", ColumnType.TEXT.name());
        object.put("type", ColumnType.TEXT.name());
        object.put("accepted", false);

        if (this.categoryId != null || this.values.size() < LIMIT)
        {
          object.put("type", ColumnType.CATEGORY.name());
          
          if (this.categoryId != null)
          {
            object.put("root", this.categoryId);
          }
        }
      }

      return object;
    }
  }

  /**
   * Current row number
   */
  private int                                 rowNum;

  /**
   * Current sheet name
   */
  private String                              sheetName;

  /**
   * Column-Attribute Map for the current sheet
   */
  private Map<Integer, Field>                 map;

  /**
   * Set of the attributes names in the current sheet
   */
  private Set<String>                         attributeNames;

  /**
   * JSONArray containing attribute information for all of the sheets.
   */
  private JSONArray                           information;

  private SpreadsheetImporterHeaderModifierIF headerModifier;

  private String                              dataset;

  public FieldInfoContentsHandler()
  {
    this.information = new JSONArray();

    this.headerModifier = this.getHeaderModifier();
  }

  public SpreadsheetImporterHeaderModifierIF getHeaderModifier()
  {
    ServiceLoader<SpreadsheetImporterHeaderModifierIF> loader = ServiceLoader.load(SpreadsheetImporterHeaderModifierIF.class, Thread.currentThread().getContextClassLoader());

    try
    {
      Iterator<SpreadsheetImporterHeaderModifierIF> it = loader.iterator();

      if (it.hasNext())
      {
        return it.next();
      }
      else
      {
        return null;
      }
    }
    catch (ServiceConfigurationError serviceError)
    {
      throw new ProgrammingErrorException(serviceError);
    }
  }

  public Field getField(String cellReference)
  {
    CellReference reference = new CellReference(cellReference);
    Integer column = new Integer(reference.getCol());

    if (!this.map.containsKey(column))
    {
      this.map.put(column, new Field());
    }

    return this.map.get(column);
  }

  public int getFieldPosition(String cellReference)
  {
    CellReference reference = new CellReference(cellReference);

    return reference.getCol();
  }

  public JSONArray getSheets()
  {
    return this.information;
  }

  @Override
  public void startSheet(String sheetName)
  {
    this.sheetName = sheetName;
    this.rowNum = 0;
    this.map = new HashMap<Integer, Field>();
    this.attributeNames = new TreeSet<String>();
  }

  private boolean validateField(Field field, Integer columnPosition)
  {
    if (field.name == null)
    {
      ExcelHeaderException exception = new ExcelHeaderException();
      exception.setRow(Integer.toString(field.getInputPosition()));
      exception.setColumn(Integer.toString(columnPosition));

      throw exception;
    }

    return true;
  }

  @Override
  public void endSheet()
  {
    try
    {
      JSONArray fields = new JSONArray();

      Set<Integer> keys = this.map.keySet();

      for (Integer key : keys)
      {
        validateField(this.map.get(key), key);

        fields.put(this.map.get(key).toJSON());
      }

      /*
       * Search for potential existing upload matches
       */
      JSONArray matches = this.findMatches();

      JSONObject sheet = new JSONObject();
      sheet.put("name", this.sheetName);
      sheet.put("label", this.sheetName);
      sheet.put("fields", fields);
      sheet.put("matches", matches);
      
      if (this.headerModifier != null)
      {
        sheet.put("format", this.headerModifier.getSpreadsheetFormat());
      }
      else
      {
        sheet.put("format", SpreadsheetImporterHeaderModifierIF.FORMAT_DEFAULT);
      }

      this.information.put(sheet);
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public void startRow(int rowNum)
  {
    this.rowNum = rowNum;
  }

  @Override
  public void endRow()
  {
  }

  @Override
  public void cell(String cellReference, String contentValue, String formattedValue, ColumnType cellType)
  {
    if (cellType.equals(ColumnType.FORMULA))
    {
      throw new ExcelFormulaException();
    }

    int headerModifierCommand = SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_DEFAULT;
    if (headerModifier != null)
    {
      headerModifierCommand = headerModifier.checkCell(this, cellReference, contentValue, formattedValue, cellType, rowNum);
    }

    if ( ( headerModifierCommand == SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_DEFAULT && this.rowNum == 0 ) || headerModifierCommand == SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_HEADER)
    {
      if (! ( cellType.equals(ColumnType.TEXT) || cellType.equals(ColumnType.INLINE_STRING) ) || !this.attributeNames.add(formattedValue))
      {
        throw new InvalidHeaderRowException();
      }

      Field attribute = this.getField(cellReference);
      attribute.setName(formattedValue);
      attribute.setInputPosition(this.getFieldPosition(cellReference));
    }
    else if (headerModifierCommand == SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_DEFAULT || headerModifierCommand == SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_BODY)
    {
      Field attribute = this.getField(cellReference);
      attribute.addDataType(cellType);

      if (cellType.equals(ColumnType.NUMBER))
      {
        BigDecimal decimal = new BigDecimal(contentValue).stripTrailingZeros();

        /*
         * Precision is the total number of digits. Scale is the number of digits after the decimal place.
         */
        int precision = decimal.precision();
        int scale = decimal.scale();

        attribute.setPrecision(precision - scale);
        attribute.setScale(scale);
      }
      else if (cellType.equals(ColumnType.TEXT) || cellType.equals(ColumnType.INLINE_STRING))
      {
        attribute.addValue(contentValue);
      }
    }
  }

  @Override
  public void headerFooter(String text, boolean isHeader, String tagName)
  {
  }

  private JSONArray findMatches() throws JSONException
  {
    if (this.dataset != null)
    {
      try
      {
        ExcelSourceBinding binding = ExcelSourceBinding.get(this.dataset);

        JSONArray options = new JSONArray();

        JSONObject option = this.getOption(binding);

        options.put(option);

        return options;
      }
      catch (DataNotFoundException e)
      {
        // Do nothing
      }
    }

    String label = LocalizationFacade.getFromBundles("dataUploader.causeOfFailure");

    QueryFactory factory = new QueryFactory();

    ExcelSourceBindingQuery query = new ExcelSourceBindingQuery(factory);

    Set<Integer> keys = this.map.keySet();

    for (Integer key : keys)
    {
      Field field = this.map.get(key);
      String name = field.getName();

      if (!name.equals(label))
      {
        ExcelFieldBindingQuery fieldQuery = new ExcelFieldBindingQuery(factory);
        fieldQuery.WHERE(fieldQuery.getColumnHeader().EQ(name));

        query.WHERE(query.EQ(fieldQuery.getSourceDefinition()));
      }
    }

    OIterator<? extends ExcelSourceBinding> iterator = null;

    try
    {
      iterator = query.getIterator();

      JSONArray options = new JSONArray();

      while (iterator.hasNext())
      {
        ExcelSourceBinding binding = iterator.next();
        JSONObject option = this.getOption(binding);

        options.put(option);
      }

      return options;
    }
    finally
    {
      if (iterator != null)
      {
        iterator.close();
      }
    }

  }

  private JSONObject getOption(ExcelSourceBinding binding)
  {
    MdView mdView = binding.getMdView();

    JSONObject option = new JSONObject();
    option.put("oid", binding.getOid());
    option.put("label", mdView.getDisplayLabel().getValue());

    return option;
  }

  @Override
  public void setDatasetProperty(String dataset)
  {
    this.dataset = dataset;
  }
}
