package net.geoprism.dhis2.palestine;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.DelegatingClassLoader;
import com.runwaysdk.generation.loader.LoaderDecorator;

import net.geoprism.ExceptionUtil;
import net.geoprism.data.etl.ColumnType;
import net.geoprism.data.etl.DataImportState;
import net.geoprism.data.etl.ProgressMonitorIF;
import net.geoprism.data.etl.excel.ExcelDataFormatter;
import net.geoprism.data.etl.excel.ExcelFormulaException;
import net.geoprism.data.etl.excel.ExcelValueException;
import net.geoprism.data.etl.excel.InvalidHeaderRowException;
import net.geoprism.data.etl.excel.SheetHandler;
import net.geoprism.data.etl.excel.SourceContentHandler;
import net.geoprism.data.etl.excel.SpreadsheetImporterHeaderModifierIF;
import net.geoprism.localization.LocalizationFacade;

public class PalestineContentHandler implements SheetHandler
{
  private static Logger                       logger = LoggerFactory.getLogger(SourceContentHandler.class);

  /**
   * Handles progress reporting
   */
  private ProgressMonitorIF                   monitor;

  /**
   * Current sheet name
   */
  private String                              sheetName;

  /**
   * Column index-Column Name Map for the current sheet
   */
  private Map<Integer, String>                columnNameMap;

  /**
   * Attribute name to value for the current row
   */
  private PalestineRow                        currentRow;
  
  private PalestineSheet                      currentSheet;
  
  private Integer                             sheetNum;
  
  /**
   * Current row number
   */
  private int                                 rowNum;

  /**
   * Current error row number
   */
  private int                                 errorNum;
  
  /**
   * The index of the column which stores error information
   */
  private int                                 errorHeaderColNum;

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

  /**
   * Workbook containing error rows
   */
  private SXSSFWorkbook                       workbook;

  /**
   * Date style format
   */
  private CellStyle                           style;

  private HashMap<String, Object>             values;
  
  private PalestineConverter                  converter;

  public PalestineContentHandler(PalestineConverter converter, ProgressMonitorIF monitor, DataImportState mode)
  {
    this.converter = converter;

    this.nFormat = new DecimalFormat("###.#");

    this.dateTimeFormat = new SimpleDateFormat(ExcelDataFormatter.DATE_TIME_FORMAT);
    this.dateFormat = new SimpleDateFormat(ExcelDataFormatter.DATE_FORMAT);

    this.monitor = monitor;
    
    this.sheetNum = -1;
  }
  
  public class PalestineSheet
  {
    private String sheetName;
    
    private Integer sheetNumber;
    
    public PalestineSheet(String sheetName, Integer sheetNumber)
    {
      this.sheetName = sheetName;
      this.sheetNumber= sheetNumber;
    }
    
    public String getSheetName()
    {
      return this.sheetName;
    }
    
    public Integer getSheetNumber()
    {
      return this.sheetNumber;
    }
  }
  
  public class PalestineRow
  {
    private ArrayList<String> headers;
    
    private ArrayList<String> values;
    
    private Integer rowNum;
    
    public PalestineRow(Integer rowNum)
    {
      this.headers = new ArrayList<String>();
      this.values = new ArrayList<String>();
      this.rowNum = rowNum;
    }
    
    public Integer getRowNum()
    {
      return this.rowNum;
    }

    public String getHeaderName(Integer column) {
      return this.headers.get(column);
    }

    public void addHeaderName(String name) {
      this.headers.add(name);
    }

    public String getValue(Integer column) {
      return values.get(column);
    }

    public void addValue(String value) {
      this.values.add(value);
    }
    
    public ArrayList<String> getValues()
    {
      return this.values;
    }
    
    public ArrayList<String> getHeaders()
    {
      return this.headers;
    }
  }

  @Override
  public void startSheet(String sheetName)
  {
    this.columnNameMap = new HashMap<Integer, String>();
    this.sheetName = sheetName;
    this.errorNum = 1;
    this.sheetNum++;

    this.getWorkbook().createSheet(sheetName);
    this.currentSheet = new PalestineSheet(sheetName, this.sheetNum);
  }

  @Override
  public void endSheet()
  {
    Sheet sheet = this.getWorkbook().getSheet(this.sheetName);

    int headerNum = 0;

    if (sheet.getLastRowNum() > headerNum)
    {
//      this.converter.setErrors(this.getWorkbook());
    }
  }

  @Override
  public void startRow(int rowNum)
  {
    this.rowNum = rowNum;
    this.values = new HashMap<String, Object>();
    
    boolean isHeader = ( rowNum == 0 );
    
    if (!isHeader)
    {
      this.currentRow = new PalestineRow(rowNum);
      
      this.monitor.setCurrentProgressUnit(rowNum);
    }
  }

  @Override
  public void endRow()
  {
    try
    {
      this.converter.processRow(this.currentRow, this.currentSheet);

      /*
       * Write the header row
       */
      boolean isHeader = ( rowNum == 0 );

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

  private void writeException(Exception e)
  {
    Sheet sheet = this.getWorkbook().getSheet(sheetName);

    int headerRowNum = 0;

    Row row = this.writeRow(sheet, headerRowNum + this.errorNum++);

    /*
     * Add exception
     */
    row.createCell(errorHeaderColNum).setCellValue(ExceptionUtil.getLocalizedException(e));
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

    if (rowNum == headerRowNum)
    {
      String label = LocalizationFacade.getFromBundles("dataUploader.causeOfFailure");
      
      errorHeaderColNum = row.getLastCellNum();

      row.createCell(errorHeaderColNum).setCellValue(helper.createRichTextString(label));
    }

    return row;
  }

  private String setColumnName(String cellReference, String columnName)
  {
    CellReference reference = new CellReference(cellReference);
    Integer column = new Integer(reference.getCol());

    return this.columnNameMap.put(column, columnName);
  }

  private String getColumnName(String cellReference)
  {
    CellReference reference = new CellReference(cellReference);
    Integer column = new Integer(reference.getCol());

    return this.columnNameMap.get(column);
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
      
      if ( this.rowNum == 0 )
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
      else
      {
        CellReference reference = new CellReference(cellReference);
        Integer column = new Integer(reference.getCol());
        
        this.currentRow.addHeaderName(this.columnNameMap.get(column));
        this.currentRow.addValue(formattedValue);

        // Store the original value in a temp map in case there is an error
        this.values.put(cellReference, contentValue);
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

  @Override
  public void setDatasetProperty(String dataset)
  {
    // Do nothing
  }
}
