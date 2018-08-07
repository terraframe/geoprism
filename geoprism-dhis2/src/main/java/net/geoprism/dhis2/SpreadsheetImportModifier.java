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
package net.geoprism.dhis2;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.poi.ss.util.CellReference;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdBusiness;

import net.geoprism.data.etl.ColumnType;
import net.geoprism.data.etl.excel.FieldInfoContentsHandler;
import net.geoprism.data.etl.excel.FieldInfoContentsHandler.Field;
import net.geoprism.data.etl.excel.SpreadsheetImporterHeaderModifierIF;
import net.geoprism.dhis2.connector.AbstractDHIS2Connector;
import net.geoprism.dhis2.connector.DHIS2HTTPCredentialConnector;
import net.geoprism.dhis2.importer.OptionSetJsonToClassifier;
import net.geoprism.dhis2.response.DHIS2TrackerResponseProcessor;
import net.geoprism.dhis2.response.HTTPResponse;
import net.geoprism.ontology.Classifier;

public class SpreadsheetImportModifier implements SpreadsheetImporterHeaderModifierIF
{
  private static final Logger logger = LoggerFactory.getLogger(SpreadsheetImportModifier.class);
  
  private boolean isDhis2Spreadsheet = false;
  
  private String programId = null;
  
  private AbstractDHIS2Connector dhis2;
  
  // We have to hold onto this until after we read the 3rd row because we don't know the attributes till then
  private Map<Integer, String> attrDhis2Ids;
  
  /**
   * Service lodaer paradigm : we're required to have a 0 argument constructor.
   */
  public SpreadsheetImportModifier()
  {
    
  }
  
  public int getSpreadsheetFormat()
  {
    if (isDhis2Spreadsheet)
    {
      return SpreadsheetImporterHeaderModifierIF.FORMAT_DHIS2;
    }
    else
    {
      return SpreadsheetImporterHeaderModifierIF.FORMAT_DEFAULT;
    }
  }
  
  public int checkRow(int rowNum)
  {
    if (!isDhis2Spreadsheet)
    {
      if (rowNum == 0)
      {
        return SpreadsheetImporterHeaderModifierIF.HEADER_ROW;
      }
      else
      {
        return SpreadsheetImporterHeaderModifierIF.BODY_ROW;
      }
    }
    
    if (rowNum <= 2)
    {
      return SpreadsheetImporterHeaderModifierIF.HEADER_ROW;
    }
    
    return SpreadsheetImporterHeaderModifierIF.BODY_ROW;
  }
  
  public int getColumnNameRowNum()
  {
    if (isDhis2Spreadsheet)
    {
      return 2;
    }
    return 0;
  }
  
  public void connectDhis2()
  {
    if (dhis2 == null)
    {
      dhis2 = new DHIS2HTTPCredentialConnector();
      
      dhis2.readConfigFromDB();
    }
  }
  
  @Override
  public int checkCell(FieldInfoContentsHandler importer, String cellReference, String contentValue, String formattedValue, ColumnType cellType, int rowNum)
  {
    CellReference reference = new CellReference(cellReference);
    Integer column = new Integer(reference.getCol());
    
    if (rowNum == 0)
    {
      if (column == 0 && formattedValue.equals("programId"))
      {
        attrDhis2Ids = new HashMap<Integer, String>();
        isDhis2Spreadsheet = true;
        connectDhis2();
        
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
      attrDhis2Ids.put(column, contentValue);
      
      return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_IGNORE;
    }
    else if (isDhis2Spreadsheet && rowNum == 2)
    {
      if (attrDhis2Ids.containsKey(column) && importer != null)
      {
        String oid = attrDhis2Ids.get(column);
        
        try
        {
          HTTPResponse response = dhis2.httpGet("api/25/metadata.json", new NameValuePair[] {
              new NameValuePair("assumeTrue", "false"),
              new NameValuePair("trackedEntityAttributes", "true"),
              new NameValuePair("filter", "oid:eq:" + oid)
            });
          DHIS2TrackerResponseProcessor.validateStatusCode(response);
          
          JSONObject json = response.getJSON();
          if (json.has("trackedEntityAttributes"))
          {
            JSONArray trackedEntityAttributes = json.getJSONArray("trackedEntityAttributes");
            
            if (trackedEntityAttributes.length() == 1)
            {
              JSONObject trackedEntityAttr = trackedEntityAttributes.getJSONObject(0);
              
              if (trackedEntityAttr.has("name"))
              {
                String name = trackedEntityAttr.getString("name");
                
                Field attribute = importer.getField(cellReference);
                attribute.setLabel(name);
                attribute.setName(formattedValue);
                attribute.setInputPosition(importer.getFieldPosition(cellReference));
                
                if (trackedEntityAttr.has("optionSet"))
                {
                  String optionSetId = trackedEntityAttr.getJSONObject("optionSet").getString("oid");
                  try
                  {
                    Classifier classy = Classifier.getByKey(OptionSetJsonToClassifier.DHIS2_CLASSIFIER_PACKAGE_PREFIX + optionSetId + Classifier.KEY_CONCATENATOR + optionSetId);
                    attribute.setCategoryId(classy.getOid());
                  }
                  catch(DataNotFoundException e)
                  {
                    
                  }
                }
                
                if (trackedEntityAttr.has("valueType"))
                {
                  String valueType = trackedEntityAttr.getString("valueType");
                  
                  if (valueType.equals("TEXT"))
                  {
                    attribute.setRealType(ColumnType.TEXT);
                  }
                  else if (valueType.equals("DATE"))
                  {
                    attribute.setRealType(ColumnType.DATE);
                  }
                  else if (valueType.equals("NUMBER"))
                  {
                    attribute.setRealType(ColumnType.NUMBER);
                  }
                  else if (valueType.equals("BOOLEAN"))
                  {
                    attribute.setRealType(ColumnType.BOOLEAN);
                  }
                  else if (valueType.equals("LONG_TEXT"))
                  {
                    attribute.setRealType(ColumnType.TEXT);
                  }
                }
                
                return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_IGNORE;
              }
            }
          }
        }
        catch(RuntimeException ex)
        {
          logger.warn("Error happened while trying to fetch DHIS2 tracked entity attribute info on row:column [3:" + column + "]", ex);
        }
        
        return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_HEADER;
      }
      else
      {
        return SpreadsheetImporterHeaderModifierIF.PROCESS_CELL_AS_HEADER;
      }
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
          connectDhis2();
          attrDhis2Ids = new HashMap<Integer, String>();
          
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
        String runwayId = MdBusiness.getMdBusiness(type).getOid();
        
//        Savepoint sp = Database.setSavepoint(); // We aren't in a transaction
        try
        {
          DHIS2IdMapping mapping = new DHIS2IdMapping();
          mapping.setDhis2Id(programId + ":" + contentValue);
          mapping.setRunwayId(runwayId);
          mapping.apply();
        }
        catch (DuplicateDataException ex)
        {
//          Database.rollbackSavepoint(sp);
          
          DHIS2IdMapping mapping = DHIS2IdMapping.getByRunwayId(runwayId);
          mapping.appLock();
          mapping.setDhis2Id(programId + ":" + contentValue);
          mapping.setRunwayId(runwayId);
          mapping.apply();
        }
        catch (RuntimeException ex)
        {
//          Database.rollbackSavepoint(sp);
          throw ex;
        }
        finally
        {
//          Database.releaseSavepoint(sp);
        }
        
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
          String attrId = MdAttributeConcrete.getByKey(type + "." + attrName).getOid();
          
//          Savepoint sp = Database.setSavepoint(); // We aren't in a transaction
          try
          {
            DHIS2IdMapping mapping = new DHIS2IdMapping();
            mapping.setDhis2Id(dhis2Id);
            mapping.setRunwayId(attrId);
            mapping.apply();
          }
          catch (DuplicateDataException ex)
          {
//            Database.rollbackSavepoint(sp);
            
            DHIS2IdMapping mapping = DHIS2IdMapping.getByRunwayId(attrId);
            mapping.appLock();
            mapping.setDhis2Id(dhis2Id);
            mapping.setRunwayId(attrId);
            mapping.apply();
          }
          catch (RuntimeException ex)
          {
//            Database.rollbackSavepoint(sp);
            throw ex;
          }
          finally
          {
//            Database.releaseSavepoint(sp);
          }
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
