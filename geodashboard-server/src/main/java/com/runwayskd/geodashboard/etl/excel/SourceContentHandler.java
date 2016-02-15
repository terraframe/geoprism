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
package com.runwayskd.geodashboard.etl.excel;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.util.CellReference;

import com.runwaysdk.business.Transient;
import com.runwaysdk.geodashboard.excel.ExcelFormulaException;
import com.runwaysdk.geodashboard.excel.InvalidHeaderRowException;
import com.runwayskd.geodashboard.etl.ColumnType;
import com.runwayskd.geodashboard.etl.ConverterIF;
import com.runwayskd.geodashboard.etl.SourceContextIF;
import com.runwayskd.geodashboard.etl.SourceFieldIF;

public class SourceContentHandler implements SheetHandler
{
  /**
   * Handler which handles the view object once they have been created.
   */
  private ConverterIF          converter;

  /**
   * View import context
   */
  private SourceContextIF      context;

  /**
   * Current sheet name
   */
  private String               sheetName;

  /**
   * Column index-Column Name Map for the current sheet
   */
  private Map<Integer, String> map;

  /**
   * Current row number
   */
  private int                  rowNum;

  /**
   * Current view
   */
  private Transient            view;

  public SourceContentHandler(ConverterIF converter, SourceContextIF context)
  {
    this.converter = converter;
    this.context = context;

    this.map = new HashMap<Integer, String>();
  }

  @Override
  public void startSheet(String sheetName)
  {
    this.sheetName = sheetName;
  }

  @Override
  public void endSheet()
  {
  }

  @Override
  public void startRow(int rowNum)
  {
    this.rowNum = rowNum;

    if (rowNum != 0)
    {
      this.view = this.context.newView(this.sheetName);
    }
  }

  @Override
  public void endRow()
  {
    if (this.view != null)
    {
      this.converter.create(this.view);

      this.view = null;
    }
  }

  private String setColumnName(String cellReference, String columnName)
  {
    CellReference reference = new CellReference(cellReference);
    Integer column = new Integer(reference.getCol());

    return this.map.put(column, columnName);
  }

  private String getColumnName(String cellReference)
  {
    CellReference reference = new CellReference(cellReference);
    Integer column = new Integer(reference.getCol());

    return this.map.get(column);
  }

  @Override
  public void cell(String cellReference, String formattedValue, ColumnType cellType)
  {
    if (cellType.equals(ColumnType.FORMULA))
    {
      throw new ExcelFormulaException();
    }

    if (this.rowNum == 0)
    {
      if (!cellType.equals(ColumnType.TEXT))
      {
        throw new InvalidHeaderRowException();
      }

      this.setColumnName(cellReference, formattedValue);
    }
    else if (this.view != null)
    {
      String columnName = this.getColumnName(cellReference);
      SourceFieldIF field = this.context.getFieldByName(this.sheetName, columnName);
      String attributeName = field.getAttributeName();

      this.view.setValue(attributeName, formattedValue);
    }
  }

  @Override
  public void headerFooter(String text, boolean isHeader, String tagName)
  {
  }
}
