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
package com.runwayskd.geodashboard.etl.excel;

import com.runwayskd.geodashboard.etl.ColumnType;

public interface SheetHandler
{
  public void startSheet(String sheetName);

  public void endSheet();

  /** A row with the (zero based) row number has started */
  public void startRow(int rowNum);

  /** A row with the (zero based) row number has ended */
  public void endRow();

  /** A cell, with the given formatted value, was encountered */
  public void cell(String cellReference, String formattedValue, ColumnType cellType);

  /** A header or footer has been encountered */
  public void headerFooter(String text, boolean isHeader, String tagName);
}
