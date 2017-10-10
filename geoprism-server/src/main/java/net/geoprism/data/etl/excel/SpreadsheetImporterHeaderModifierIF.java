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

import net.geoprism.data.etl.ColumnType;

public interface SpreadsheetImporterHeaderModifierIF
{
  public static final int PROCESS_CELL_AS_DEFAULT = 0;
  
  public static final int PROCESS_CELL_AS_HEADER = 1;
  
  public static final int PROCESS_CELL_AS_BODY = 2;
  
  public static final int PROCESS_CELL_AS_IGNORE = 3;
  
  public static final int HEADER_ROW = 0;
  
  public static final int BODY_ROW = 1;
  
  public static final int INGORE_ROW = 2;
  
  public static final int FORMAT_DEFAULT = 0;
  
  public static final int FORMAT_DHIS2 = 1;
  
  public int getSpreadsheetFormat();
  
  public int processCell(String cellReference, String contentValue, String formattedValue, ColumnType cellType, int rowNum, String type, String attrName);
  
  public int checkCell(String cellReference, String contentValue, String formattedValue, ColumnType cellType, int rowNum);
  
  /**
   * Returns the row number where the column headers are stored.
   */
  public int getColumnNameRowNum();
  
  public int checkRow(int rowNum);
}
