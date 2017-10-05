package net.geoprism.data.etl.excel;

import net.geoprism.data.etl.ColumnType;

public class CountSheetHandler implements SheetHandler
{
  private int rowNum;

  public CountSheetHandler()
  {
    this.rowNum = 0;
  }

  @Override
  public void startSheet(String sheetName)
  {
  }

  @Override
  public void endSheet()
  {
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
  }

  @Override
  public void headerFooter(String text, boolean isHeader, String tagName)
  {
  }

  public int getRowNum()
  {
    return rowNum;
  }
}
