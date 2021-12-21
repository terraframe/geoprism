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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;

import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.ProgrammingErrorException;

import net.geoprism.ExceptionUtil;
import net.geoprism.data.etl.ColumnType;
import net.geoprism.data.etl.ConverterIF;
import net.geoprism.data.etl.DataImportState;
import net.geoprism.data.etl.ProgressMonitorIF;
import net.geoprism.data.etl.SourceContextIF;
import net.geoprism.data.etl.SourceFieldIF;
import net.geoprism.localization.LocalizationFacade;

public class SourceContentHandler implements SheetHandler
{
  private static Logger                       logger = LoggerFactory.getLogger(SourceContentHandler.class);

  /**
   * Handles progress reporting
   */
  private ProgressMonitorIF                   monitor;

  /**   
   * Handler which handles the view object once they have been created.
   */
  private ConverterIF                         converter;

  /**
   * View import context
   */
  private SourceContextIF                     context;

  /**
   * Current sheet name
   */
  private String                              sheetName;

  /**
   * Column index-Column Name Map for the current sheet
   */
  private Map<Integer, String>                map;

  /**
   * Current row number
   */
  private int                                 rowNum;

  /**
   * Current error row number
   */
  private int                                 errorNum;

  /**
   * Current view
   */
  private Transient                           view;

  /**
   * Decimal format used to remove trailing 0s from the long values
   */
  private DecimalFormat                       nFormat;

  /**
   * Format used for parsing and formatting dateTime fields
   */
  private DateFormat                          dateTimeFormat;

  /**
   * Format used for parsing and formatting dateTime fields
   */
  private DateFormat                          dateFormat;

  private SpreadsheetImporterHeaderModifierIF headerModifier;

  /**
   * Workbook containing error rows
   */
  private SXSSFWorkbook                       workbook;

  /**
   * Date style format
   */
  private CellStyle                           style;

  private HashMap<String, Object>             values;

  private DataImportState                     mode;

  boolean                                     isFirstSheet;

  public SourceContentHandler(ConverterIF converter, SourceContextIF context, ProgressMonitorIF monitor, DataImportState mode)
  {
    this.isFirstSheet = true;

    this.converter = converter;
    this.context = context;
    this.mode = mode;

    this.map = new HashMap<Integer, String>();
    this.nFormat = new DecimalFormat("###.#");

    this.dateTimeFormat = new SimpleDateFormat(ExcelDataFormatter.DATE_TIME_FORMAT);
    this.dateFormat = new SimpleDateFormat(ExcelDataFormatter.DATE_FORMAT);

    this.monitor = monitor;

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

  @Override
  public void startSheet(String sheetName)
  {
    this.sheetName = sheetName;
    this.errorNum = 1;

    this.getWorkbook().createSheet(sheetName);
  }

  @Override
  public void endSheet()
  {
    Sheet sheet = this.getWorkbook().getSheet(this.sheetName);

    int headerNum = 0;

    if (this.headerModifier != null)
    {
      headerNum = this.headerModifier.getColumnNameRowNum();
    }

    if (sheet.getLastRowNum() > headerNum)
    {
      this.converter.setErrors(this.getWorkbook());
    }

    this.isFirstSheet = false;
  }

  @Override
  public void startRow(int rowNum)
  {
    if (this.isFirstSheet)
    {
      this.rowNum = rowNum;
      this.values = new HashMap<String, Object>();

      boolean isHeader = ( rowNum == 0 );
      if (this.headerModifier != null)
      {
        isHeader = this.headerModifier.checkRow(rowNum) == SpreadsheetImporterHeaderModifierIF.HEADER_ROW;
      }

      if (!isHeader)
      {
        this.view = this.context.newView(this.sheetName);
        
        this.monitor.setCurrentProgressUnit(rowNum);
      }
    }
  }

  @Override
  public void endRow()
  {
    if (this.isFirstSheet)
    {

      try
      {
        if (this.view != null)
        {
          this.converter.create(this.view);

          this.view = null;
        }

        /*
         * Write the header row
         */
        boolean isHeader = ( rowNum == 0 );
        if (this.headerModifier != null)
        {
          isHeader = this.headerModifier.checkRow(rowNum) == SpreadsheetImporterHeaderModifierIF.HEADER_ROW;
        }

        if (isHeader)
        {
          Sheet sheet = this.getWorkbook().getSheet(sheetName);

          this.writeRow(sheet, rowNum);
        }
      }
      catch (Exception e)
      {
        this.writeException(e);

        // // Wrap all exceptions with information about the cell and row
        // ExcelObjectException exception = new ExcelObjectException(e);
        // exception.setRow(new Long(this.rowNum));
        // exception.setMsg(ExceptionUtil.getLocalizedException(e));
        //
        // throw exception;
      }
    }
  }

  private void writeException(Exception e)
  {
    Sheet sheet = this.getWorkbook().getSheet(sheetName);

    int headerRowNum = 0;

    if (this.headerModifier != null)
    {
      headerRowNum = this.headerModifier.getColumnNameRowNum();
    }

    Row row = this.writeRow(sheet, headerRowNum + this.errorNum++);

    /*
     * Add exception
     */
    row.createCell(row.getLastCellNum()).setCellValue(ExceptionUtil.getLocalizedException(e));
  }

  private Row writeRow(Sheet sheet, int rowNum)
  {
    CreationHelper helper = this.getWorkbook().getCreationHelper();

    Row row = sheet.createRow(rowNum);

    Set<Entry<String, Object>> entries = this.values.entrySet();

    for (Entry<String, Object> entry : entries)
    {
      String celRef = entry.getKey();
      Object value = entry.getValue();

      int column = new CellReference(celRef).getCol();

      Cell cell = row.createCell(column);

      if (value instanceof Double)
      {
        cell.setCellValue((Double) value);
      }
      else if (value instanceof Date)
      {
        cell.setCellValue((Date) value);
        cell.setCellStyle(this.style);
      }
      else
      {
        cell.setCellValue(helper.createRichTextString((String) value));
      }
    }

    int headerRowNum = 0;
    if (this.headerModifier != null)
    {
      headerRowNum = this.headerModifier.getColumnNameRowNum();
    }

    if (rowNum == headerRowNum)
    {
      String label = LocalizationFacade.getFromBundles("dataUploader.causeOfFailure");

      row.createCell(row.getLastCellNum()).setCellValue(helper.createRichTextString(label));
    }

    return row;
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
  public void cell(String cellReference, String contentValue, String formattedValue, ColumnType cellType)
  {
    if (this.isFirstSheet)
    {

      try
      {
        if (cellType.equals(ColumnType.FORMULA))
        {
          throw new ExcelFormulaException();
        }

        // Invoke DHIS2 header modifier processing code (if the DHIS2 plugin exists)
        int headerModifierCommand = SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_DEFAULT;

        if (headerModifier != null)
        {
          String attributeName = null;
          String columnName = this.getColumnName(cellReference);
          SourceFieldIF field = this.context.getFieldByName(this.sheetName, columnName);

          if (field != null)
          {
            attributeName = field.getAttributeName();
          }

          if (this.mode.equals(DataImportState.DATAIMPORT))
          {
            if (field != null && field.getType().equals(ColumnType.IGNORE))
            {
              headerModifierCommand = SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_IGNORE;
            }
            else
            {
              headerModifierCommand = headerModifier.processCell(cellReference, contentValue, formattedValue, cellType, rowNum, this.converter.getTargetContext().getType(this.context.getType(this.sheetName)), attributeName);
            }
          }
          else
          {
            headerModifierCommand = headerModifier.checkCell(null, cellReference, contentValue, formattedValue, cellType, rowNum);
          }

          if (headerModifierCommand == SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_IGNORE)
          {
            this.values.put(cellReference, contentValue);
          }
        }

        if ( ( headerModifierCommand == SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_DEFAULT && this.rowNum == 0 ) || headerModifierCommand == SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_HEADER)
        {
          if (! ( cellType.equals(ColumnType.TEXT) || cellType.equals(ColumnType.INLINE_STRING) ))
          {
            throw new InvalidHeaderRowException();
          }

          this.setColumnName(cellReference, formattedValue);

          // Store the original value in a temp map in case there is an error
          String label = LocalizationFacade.getFromBundles("dataUploader.causeOfFailure");

          if (!contentValue.equals(label))
          {
            this.values.put(cellReference, contentValue);
          }
        }
        else if ( ( headerModifierCommand == SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_DEFAULT && this.view != null ) || headerModifierCommand == SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_BODY)
        {
          String columnName = this.getColumnName(cellReference);
          SourceFieldIF field = this.context.getFieldByName(this.sheetName, columnName);

          if (field != null && !field.getType().equals(ColumnType.IGNORE))
          {
            String attributeName = field.getAttributeName();

            Object original = contentValue;

            if (field.isNumber())
            {
              original = Double.parseDouble(contentValue);
            }

            if (field.getType().equals(ColumnType.LONG))
            {
              double value = Double.parseDouble(contentValue);

              formattedValue = this.nFormat.format(value);
            }
            else if (field.getType().equals(ColumnType.DATE))
            {
              Date date = this.dateTimeFormat.parse(contentValue);

              formattedValue = this.dateFormat.format(date);

              original = date;
            }

            this.view.setValue(attributeName, formattedValue);

            // Store the original value in a temp map in case there is an error
            this.values.put(cellReference, original);
          }
        }
      }
      catch (Exception e)
      {
        logger.error("An error occurred while importing cell [" + cellReference + "].", e);

        // Wrap all exceptions with information about the cell and row
        ExcelValueException exception = new ExcelValueException();
        exception.setCell(cellReference);
        exception.setMsg(ExceptionUtil.getLocalizedException(e));

        throw exception;
      }
    }
  }

  @Override
  public void headerFooter(String text, boolean isHeader, String tagName)
  {
  }

  public synchronized Workbook getWorkbook()
  {
    if (this.workbook == null)
    {
      this.workbook = new SXSSFWorkbook(10);
      this.style = this.workbook.createCellStyle();

      CreationHelper helper = this.workbook.getCreationHelper();
      this.style.setDataFormat(helper.createDataFormat().getFormat("MM/DD/YY"));

      XSSFWorkbook wb = this.workbook.getXSSFWorkbook();
      POIXMLProperties props = wb.getProperties();

      POIXMLProperties.CustomProperties custProp = props.getCustomProperties();
      custProp.addProperty("dataset", this.context.getOid(this.sheetName));
    }

    return this.workbook;
  }

  public int getTotalRows()
  {
    return rowNum;
  }

  public int getNumberOfErrors()
  {
    return ( this.errorNum - 1 );
  }

  @Override
  public void setDatasetProperty(String dataset)
  {
    // Do nothing
  }
}
