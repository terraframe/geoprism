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
package net.geoprism.data.etl.excel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.geoprism.data.etl.ColumnType;
import net.geoprism.data.etl.ExcelFieldBindingQuery;
import net.geoprism.data.etl.ExcelSourceBinding;
import net.geoprism.data.etl.ExcelSourceBindingQuery;

import org.apache.poi.ss.util.CellReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdView;

public class FieldInfoContentsHandler implements SheetHandler
{
  private static class Field
  {
    /**
     * Number of unique values
     */
    private static final int LIMIT = 10;

    private String           name;

    private int              precision;

    private int              scale;

    private Set<ColumnType>  dataTypes;

    private Set<String>      values;

    public Field()
    {
      this.dataTypes = new TreeSet<ColumnType>();
      this.precision = 0;
      this.scale = 0;
      this.values = new HashSet<String>(LIMIT);
    }

    public void setName(String name)
    {
      this.name = name;
    }

    public String getName()
    {
      return name;
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

    public JSONObject toJSON() throws JSONException
    {
      JSONObject object = new JSONObject();
      object.put("name", this.name);
      object.put("label", this.name);
      object.put("aggregatable", true);

      if (this.dataTypes.size() == 1)
      {
        ColumnType type = this.dataTypes.iterator().next();

        object.put("type", type.name());
        object.put("columnType", type.name());
        object.put("accepted", false);

        if (type.equals(ColumnType.NUMBER))
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
        else if (type.equals(ColumnType.TEXT) && this.values.size() < LIMIT)
        {
          object.put("type", ColumnType.CATEGORY.name());
        }
      }
      else
      {
        object.put("columnType", ColumnType.TEXT.name());
        object.put("type", ColumnType.TEXT.name());
        object.put("accepted", false);

        if (this.values.size() < LIMIT)
        {
          object.put("type", ColumnType.CATEGORY.name());
        }
      }

      return object;
    }
  }

  /**
   * Current row number
   */
  private int                 rowNum;

  /**
   * Current sheet name
   */
  private String              sheetName;

  /**
   * Column-Attribute Map for the current sheet
   */
  private Map<Integer, Field> map;

  /**
   * Set of the attributes names in the current sheet
   */
  private Set<String>         attributeNames;

  /**
   * JSONArray containing attribute information for all of the sheets.
   */
  private JSONArray           information;

  public FieldInfoContentsHandler()
  {
    this.information = new JSONArray();
  }

  private Field getField(String cellReference)
  {
    CellReference reference = new CellReference(cellReference);
    Integer column = new Integer(reference.getCol());

    if (!this.map.containsKey(column))
    {
      this.map.put(column, new Field());
    }

    return this.map.get(column);
  }

  public JSONArray getSheets()
  {
    return this.information;
  }

  @Override
  public void startSheet(String sheetName, String configuration)
  {
    this.sheetName = sheetName;
    this.rowNum = 0;
    this.map = new HashMap<Integer, Field>();
    this.attributeNames = new TreeSet<String>();
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

    if (this.rowNum == 0)
    {
      if (!cellType.equals(ColumnType.TEXT) || !this.attributeNames.add(formattedValue))
      {
        throw new InvalidHeaderRowException();
      }

      Field attribute = this.getField(cellReference);
      attribute.setName(formattedValue);
    }
    else
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
      else if (cellType.equals(ColumnType.TEXT))
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
    JSONArray options = new JSONArray();

    QueryFactory factory = new QueryFactory();

    ExcelSourceBindingQuery query = new ExcelSourceBindingQuery(factory);

    Set<Integer> keys = this.map.keySet();

    for (Integer key : keys)
    {
      Field field = this.map.get(key);

      ExcelFieldBindingQuery fieldQuery = new ExcelFieldBindingQuery(factory);
      fieldQuery.WHERE(fieldQuery.getColumnHeader().EQ(field.getName()));

      query.WHERE(query.EQ(fieldQuery.getSourceDefinition()));
    }

    OIterator<? extends ExcelSourceBinding> iterator = null;

    try
    {
      iterator = query.getIterator();

      while (iterator.hasNext())
      {
        ExcelSourceBinding binding = iterator.next();
        MdView mdView = binding.getMdView();

        JSONObject option = new JSONObject();
        option.put("id", binding.getId());
        option.put("label", mdView.getDisplayLabel().getValue());

        options.put(option);
      }
    }
    finally
    {
      if (iterator != null)
      {
        iterator.close();
      }
    }

    return options;
  }
}
