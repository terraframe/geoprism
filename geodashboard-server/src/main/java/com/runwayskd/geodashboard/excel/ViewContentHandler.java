package com.runwayskd.geodashboard.excel;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.util.CellReference;

import com.runwaysdk.business.Transient;
import com.runwaysdk.geodashboard.excel.ExcelFormulaException;
import com.runwaysdk.geodashboard.excel.InvalidHeaderRowException;
import com.runwayskd.geodashboard.excel.XSSFSheetXMLHandler.DataType;

public class ViewContentHandler implements SheetHandler
{
  /**
   * View import context
   */
  private ViewContextIF        context;

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

  public ViewContentHandler(ViewContextIF context)
  {
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
  public void cell(String cellReference, String formattedValue, DataType cellType)
  {
    if (cellType.equals(DataType.FORMULA))
    {
      throw new ExcelFormulaException();
    }

    if (this.rowNum == 0)
    {
      if (!cellType.equals(DataType.TEXT))
      {
        throw new InvalidHeaderRowException();
      }

      this.setColumnName(cellReference, formattedValue);
    }
    else if (this.view != null)
    {
      String columnName = this.getColumnName(cellReference);
      String attributeName = this.context.getAttributeName(this.sheetName, columnName);

      this.view.setValue(attributeName, formattedValue);
    }
  }

  @Override
  public void headerFooter(String text, boolean isHeader, String tagName)
  {
  }
}
