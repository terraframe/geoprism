package net.geoprism.dhis2;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.util.CellReference;

import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdBusiness;

import net.geoprism.data.etl.ColumnType;
import net.geoprism.data.etl.excel.SpreadsheetImporterHeaderModifierIF;

public class SpreadsheetImportModifier implements SpreadsheetImporterHeaderModifierIF
{
  private boolean isDhis2Spreadsheet = false;
  
  private String programId = null;
  
  // We have to hold onto this until after we read the 3rd row because we don't know the attributes till then
  private Map<Integer, String> attrDhis2Ids = new HashMap<Integer, String>();
  
  /**
   * Service lodaer paradigm : we're required to have a 0 argument constructor.
   */
  public SpreadsheetImportModifier()
  {
    
  }
  
  @Override
  public int checkCell(String cellReference, String contentValue, String formattedValue, ColumnType cellType, int rowNum)
  {
    CellReference reference = new CellReference(cellReference);
    Integer column = new Integer(reference.getCol());
    
    if (rowNum == 0)
    {
      if (column == 0 && formattedValue.equals("programId"))
      {
        isDhis2Spreadsheet = true;
        return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_IGNORE;
      }
      else if (column == 0)
      {
        isDhis2Spreadsheet = false;
        return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_DEFAULT;
      }
      
      if (isDhis2Spreadsheet)
      {
        return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_IGNORE;
      }
    }
    else if (isDhis2Spreadsheet && rowNum == 1)
    {
      return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_IGNORE;
    }
    else if (isDhis2Spreadsheet && rowNum == 2)
    {
      return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_HEADER;
    }
    else
    {
      return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_BODY;
    }
    
    return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_DEFAULT;
  }
  
  @Override
  public int processCell(String cellReference, String contentValue, String formattedValue, ColumnType cellType, int rowNum, String type, String attrName)
  {
    CellReference reference = new CellReference(cellReference);
    Integer column = new Integer(reference.getCol());
    
    if (rowNum == 0)
    {
      if (column == 0)
      {
        if (formattedValue.equals("programId"))
        {
          isDhis2Spreadsheet = true;
          return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_IGNORE;
        }
        else
        {
          isDhis2Spreadsheet = false;
          return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_DEFAULT;
        }
      }
      else if (column == 1 && isDhis2Spreadsheet)
      {
        programId = contentValue;
        
        return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_IGNORE;
      }
      else if (column == 2 && isDhis2Spreadsheet)
      {
        if (!formattedValue.equals("trackedEntityId"))
        {
          isDhis2Spreadsheet = false;
          return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_DEFAULT;
//          throw new RuntimeException("");
        }
        else
        {
          return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_IGNORE;
        }
      }
      else if (column == 3 && isDhis2Spreadsheet)
      {
        String runwayId = MdBusiness.getMdBusiness(type).getId();
        
        DHIS2IdMapping mapping = new DHIS2IdMapping();
        mapping.setDhis2Id(programId + ":" + contentValue);
        mapping.setRunwayId(runwayId);
        mapping.apply();
        
        return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_IGNORE;
      }
    }
    else if (isDhis2Spreadsheet && rowNum == 1)
    {
      attrDhis2Ids.put(column, contentValue);
      
      return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_IGNORE;
    }
    else if (isDhis2Spreadsheet && rowNum == 2)
    {
      return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_HEADER;
    }
    else if (isDhis2Spreadsheet && rowNum == 3 && attrName != null)
    {
      if (attrDhis2Ids.containsKey(column))
      {
        String dhis2Id = attrDhis2Ids.get(column);
        if (!dhis2Id.equals(""))
        {
          String attrId = MdAttributeConcrete.getByKey(type + "." + attrName).getId();
          
          DHIS2IdMapping mapping = new DHIS2IdMapping();
          mapping.setDhis2Id(dhis2Id);
          mapping.setRunwayId(attrId);
          mapping.apply();
        }
      }
      
      return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_BODY;
    }
    else if (isDhis2Spreadsheet)
    {
      return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_BODY;
    }
    
    return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_DEFAULT;
  }
  
}
