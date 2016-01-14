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
package com.runwayskd.geodashboard.excel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.ss.util.CellReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.geodashboard.excel.InvalidHeaderRowException;
import com.runwayskd.geodashboard.excel.XSSFSheetXMLHandler.DataType;

public class AttributeInfoContentsHandler implements SheetHandler
{
  /**
   * Current row number
   */
  private int                      rowNum;

  /**
   * Current sheet name
   */
  private String                   sheetName;

  /**
   * Column-Attribute Map for the current sheet
   */
  private Map<Integer, JSONObject> map;
  
  /**
   * Set of the attributes names in the current sheet
   */
  private Set<String>              attributeNames;  

  /**
   * JSONArray containing attribute information for all of the sheets.
   */
  private JSONArray                information;

  public AttributeInfoContentsHandler()
  {
    this.information = new JSONArray();
  }

  private JSONObject getAttribute(String cellReference)
  {
    CellReference reference = new CellReference(cellReference);
    Integer column = new Integer(reference.getCol());

    if (!this.map.containsKey(column))
    {
      this.map.put(column, new JSONObject());
    }

    return this.map.get(column);
  }

  public JSONArray getAttributeInformation()
  {
    return this.information;
  }

  @Override
  public void startSheet(String sheetName)
  {
    this.sheetName = sheetName;
    this.rowNum = 0;
    this.map = new HashMap<Integer, JSONObject>();
    this.attributeNames = new TreeSet<String>();
  }

  @Override
  public void endSheet()
  {
    try
    {
      JSONArray attributes = new JSONArray();

      Set<Integer> keys = this.map.keySet();

      for (Integer key : keys)
      {
        attributes.put(this.map.get(key));
      }

      JSONObject sheet = new JSONObject();
      sheet.put("sheetName", this.sheetName);
      sheet.put("attributes", attributes);

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
  public void cell(String cellReference, String formattedValue, DataType cellType)
  {
    try
    {
      if (this.rowNum == 0)
      {
        if (!cellType.equals(DataType.SST_STRING) || !this.attributeNames.add(formattedValue))
        {
          throw new InvalidHeaderRowException();
        }

        JSONObject attribute = this.getAttribute(cellReference);
        attribute.put("name", formattedValue);
        attribute.put("values", new JSONArray());
      }
      else if (this.rowNum < 5)
      {
        JSONObject attribute = this.getAttribute(cellReference);

        JSONArray values = attribute.getJSONArray("values");
        values.put(formattedValue);
      }
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public void headerFooter(String text, boolean isHeader, String tagName)
  {
  }
}
