package net.geoprism.data.etl.excel;

import net.geoprism.data.etl.ColumnType;

public interface SpreadsheetImporterHeaderModifierIF
{
  public static final int PROCESS_CELL_AS_DEFAULT = 0;
  
  public static final int PROCESS_CELL_AS_HEADER = 1;
  
  public static final int PROCESS_CELL_AS_BODY = 2;
  
  public static final int PROCESS_CELL_AS_IGNORE = 3;
  
  public int processCell(String cellReference, String contentValue, String formattedValue, ColumnType cellType, int rowNum, String type, String attrName);
  
  public int checkCell(String cellReference, String contentValue, String formattedValue, ColumnType cellType, int rowNum);
}
