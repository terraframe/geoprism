/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import net.geoprism.data.etl.ColumnType;

public interface SheetHandler
{
  public void setDatasetProperty(String dataset);

  /**
   * A sheet with the given name has started
   * 
   * @param sheetName
   *          Name of the sheet
   */
  public void startSheet(String sheetName);

  /**
   * A sheet has ended
   */
  public void endSheet();

  /**
   * A row with the (zero based) row number has started
   * 
   * @param rowNum
   *          Zero based role number
   */
  public void startRow(int rowNum);

  /**
   * A row with the (zero based) row number has ended
   */
  public void endRow();

  /**
   * A cell, with the given formatted value, was encountered
   * 
   * @param cellReference
   *          Reference of the cell
   * @param contentValue
   *          Content value of the cell
   * @param formattedValue
   *          Formatted value of the cell as seen by the user
   * @param cellType
   *          Cell type
   */
  public void cell(String cellReference, String contentValue, String formattedValue, ColumnType cellType);

  /**
   * A header or footer has been encountered
   * 
   * @param text
   *          Text of the header or footer
   * @param isHeader
   *          Flag denoting if it is a header or a footer
   * @param tagName
   *          Tag name
   */
  public void headerFooter(String text, boolean isHeader, String tagName);
}
