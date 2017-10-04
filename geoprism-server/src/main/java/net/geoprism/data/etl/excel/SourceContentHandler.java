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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.DelegatingClassLoader;
import com.runwaysdk.generation.loader.LoaderDecorator;

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
  private Workbook                            workbook;

  /**
   * Date style format
   */
  private CellStyle                           style;

  private HashMap<String, Object>             values;

  public SourceContentHandler(ConverterIF converter, SourceContextIF context, ProgressMonitorIF monitor)
  {
    this.converter = converter;
    this.context = context;

    this.map = new HashMap<Integer, String>();
    this.nFormat = new DecimalFormat("###.#");

    this.dateTimeFormat = new SimpleDateFormat(ExcelDataFormatter.DATE_TIME_FORMAT);
    this.dateFormat = new SimpleDateFormat(ExcelDataFormatter.DATE_FORMAT);

    this.monitor = monitor;

    this.headerModifier = this.getHeaderModifier();
  }

  public SpreadsheetImporterHeaderModifierIF getHeaderModifier()
  {
    ServiceLoader<SpreadsheetImporterHeaderModifierIF> loader = ServiceLoader.load(SpreadsheetImporterHeaderModifierIF.class, ( (DelegatingClassLoader) LoaderDecorator.instance() ));

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

    if (sheet.getLastRowNum() > 0)
    {
      this.converter.setErrors(this.getWorkbook());
    }
  }

  @Override
  public void startRow(int rowNum)
  {
    this.rowNum = rowNum;
    this.values = new HashMap<String, Object>();

    if (rowNum != 0)
    {
      this.view = this.context.newView(this.sheetName);

      this.monitor.setCurrentRow(rowNum);
    }
  }

  @Override
  public void endRow()
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
      if (rowNum == 0)
      {
        Sheet sheet = this.getWorkbook().getSheet(sheetName);

        this.writeRow(sheet, 0);
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

  private void writeException(Exception e)
  {
    Sheet sheet = this.getWorkbook().getSheet(sheetName);

    Row row = this.writeRow(sheet, this.errorNum++);

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

    if (rowNum == 0)
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

        if (this.monitor.getState().equals(DataImportState.DATAIMPORT))
        {
          headerModifierCommand = headerModifier.processCell(cellReference, contentValue, formattedValue, cellType, rowNum, this.converter.getTargetContext().getType(this.context.getType(this.sheetName)), attributeName);
        }
        else
        {
          headerModifierCommand = headerModifier.checkCell(cellReference, contentValue, formattedValue, cellType, rowNum);
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

        if (field != null)
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
      // Wrap all exceptions with information about the cell and row
      ExcelValueException exception = new ExcelValueException();
      exception.setCell(cellReference);
      exception.setMsg(ExceptionUtil.getLocalizedException(e));

      throw exception;
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
}
