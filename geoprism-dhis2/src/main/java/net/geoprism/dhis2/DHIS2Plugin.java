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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Map;


import net.geoprism.account.ExternalProfile;
import net.geoprism.data.etl.DataImportRunnable;
import net.geoprism.data.etl.ImportResponseIF;
import net.geoprism.data.etl.ProgressMonitorIF;
import net.geoprism.data.etl.excel.ExcelDataFormatter;
import net.geoprism.data.etl.excel.ExcelSheetReader;
import net.geoprism.data.etl.excel.ExtraSpreadsheetTabException;
import net.geoprism.data.etl.excel.MissingSpreadsheetTabException;
import net.geoprism.dhis2.connector.AbstractDHIS2Connector;
import net.geoprism.dhis2.connector.DHIS2OAuthConnector;
import net.geoprism.dhis2.palestine.PalestineContentHandler;
import net.geoprism.dhis2.palestine.PalestineConverter;
import net.geoprism.dhis2.palestine.PalestineResponse;
import net.geoprism.dhis2.response.OAuthLoginRequiredException;
import net.geoprism.dhis2.util.DHIS2IdFinder;
import net.geoprism.data.etl.excel.ExtraSpreadsheetTabColumnException;
import net.geoprism.data.etl.excel.ExtraSpreadsheetTabException;
import net.geoprism.data.etl.excel.FieldInfoContentsHandler;
import net.geoprism.data.etl.excel.InvalidExcelFileException;
import net.geoprism.data.etl.excel.InvalidSpreadsheetTabNameException;
import net.geoprism.data.etl.excel.MissingSpreadsheetTabColumnException;
import net.geoprism.data.etl.excel.MissingSpreadsheetTabException;
import net.geoprism.dhis2.palestine.PalestineHardcoded;

import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.common.util.BasicEMap;
import org.json.JSONArray;
import org.json.JSONObject;

import com.lowagie.text.pdf.hyphenation.TernaryTree.Iterator;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.system.VaultFile;

public class DHIS2Plugin implements DHIS2PluginIF
{

  @Override
  public String findAttributes()
  {
    return DHIS2IdFinder.findAttributes();
  }

  @Override
  public String findPrograms()
  {
    return DHIS2IdFinder.findPrograms();
  }

  @Override
  public String findTrackedEntities()
  {
    return DHIS2IdFinder.findTrackedEntities();
  }
  
  public static boolean validateSpreadheet(String config)
  {
	  JSONObject configObj = new JSONObject(config);
	  JSONArray sheets = configObj.getJSONArray("sheets");
	  
	  if(sheets.length() < 5)
	  {
		  ArrayList<String> hcTabs = getHardcodedTabs();
		  ArrayList<String> tabs = getTabNames(sheets);
		  
		  String missingTabNames = "";
		  for(int i = 0; i<hcTabs.size(); i++)
		  {
			  String thisTabName = hcTabs.get(i);
			  
			  Boolean contains = Arrays.asList(tabs).contains(thisTabName);
			  if(!contains)
			  {
				  if(missingTabNames.length() > 0)
				  {
					  missingTabNames = missingTabNames.concat(", ");
				  }
				  missingTabNames = missingTabNames.concat(thisTabName);
			  }
		  }
		  
		  MissingSpreadsheetTabException misTabErr =  new MissingSpreadsheetTabException();
		  misTabErr.setTabLabel(missingTabNames);
		  throw misTabErr;
	  }
	  else if(sheets.length() > 5)
	  {
		  ArrayList<String> hcTabs = getHardcodedTabs();
		  ArrayList<String> tabs = getTabNames(sheets);
		  
		  String extraTabNames = "";
		  for(int i = 0; i<tabs.size(); i++)
		  {
			  String thisTabName = tabs.get(i);
			  
			  Boolean contains = Arrays.asList(hcTabs).contains(thisTabName);
			  if(!contains)
			  {
				  if(extraTabNames.length() > 0)
				  {
					  extraTabNames = extraTabNames.concat(", ");
				  }
				  extraTabNames = extraTabNames.concat(thisTabName);
			  }
		  }
		  
		  ExtraSpreadsheetTabException extraTabErr =  new ExtraSpreadsheetTabException();
		  extraTabErr.setTabLabel(extraTabNames);
		  throw extraTabErr;
	  }
	  
	  
	  for(int i=0; i<sheets.length(); i++)
	  {
		  JSONObject tab = sheets.getJSONObject(i);
		  String tabName = tab.getString("name");
		  
		  System.out.println("Validating tab: ".concat(tabName));
		  
		  String[] hcTabCols = getHardcodedTabColumns(tabName);
		  if(hcTabCols.length > 0)
		  {
			  JSONArray fields = tab.getJSONArray("fields");
			  if(fields.length() < hcTabCols.length)
			  {
				  
				  String missingFieldNames = "";
				  for(int index = 0; index<hcTabCols.length; index++)
				  {
					  String thisFieldName = hcTabCols[index];
					  
					  Boolean contains = Arrays.asList(fields).contains(thisFieldName);
					  if(!contains)
					  {
						  if(missingFieldNames.length() > 0)
						  {
							  missingFieldNames = missingFieldNames.concat(", ");
						  }
						  missingFieldNames = missingFieldNames.concat(thisFieldName);
					  }
				  }
				  
				  MissingSpreadsheetTabColumnException misTabColErr = new MissingSpreadsheetTabColumnException();
				  misTabColErr.setTabLabel(tabName);
				  misTabColErr.setColumnLabel(missingFieldNames);
				  throw misTabColErr;
			  }
			  else if(fields.length() > hcTabCols.length)
			  {
				  
				  String extraFieldNames = "";
				  for(int index = 0; index<fields.length(); index++)
				  {
					  JSONObject thisFieldNameObj = fields.getJSONObject(index);
					  String thisFieldName = thisFieldNameObj.getString("name");
					  
					  Boolean contains = Arrays.asList(hcTabCols).contains(thisFieldName);
					  if(!contains)
					  {
						  if(extraFieldNames.length() > 0)
						  {
							  extraFieldNames = extraFieldNames.concat(", ");
						  }
						  extraFieldNames = extraFieldNames.concat(thisFieldName);
					  }
				  }
				  
				  ExtraSpreadsheetTabColumnException extraTabColErr =  new ExtraSpreadsheetTabColumnException();
				  extraTabColErr.setTabLabel(tabName);
				  extraTabColErr.setColumnLabel(extraFieldNames);
				  throw extraTabColErr;
			  }
		  }
		  else
		  {
			  InvalidSpreadsheetTabNameException invTabNameErr = new InvalidSpreadsheetTabNameException();
			  invTabNameErr.setTabLabel(tabName);
			  throw invTabNameErr;
		  }
	  }
	  
	  return true;
  }
  
  private static ArrayList<String> getHardcodedTabs()
  {
	  ArrayList<String> hcTabs = new ArrayList<String>();;
	  
	  HashMap<String, String[]> tabsLookup = PalestineHardcoded.map;
	  java.util.Iterator<Entry<String, String[]>> it = tabsLookup.entrySet().iterator();
	  while(it.hasNext())
	  {
		  Map.Entry pair = (Map.Entry)it.next();
		  hcTabs.add(pair.getKey().toString());
	  }
	  
	  return hcTabs;
  }
  
  private static ArrayList<String> getTabNames(JSONArray tabs)
  {
	  ArrayList<String> tabNames = new ArrayList<String>();;
	  
	  for(int i=0; i<tabs.length(); i++)
	  {
		  JSONObject tab = (JSONObject) tabs.get(i);
		  tabNames.add(tab.getString("name"));
	  }
	  
	  return tabNames;
  }
  
  private static String[] getHardcodedTabColumns(String tabName)
  {
	  HashMap<String, String[]> tabsLookup = PalestineHardcoded.map;
	  
	  String[] hardcodedTab = tabsLookup.get(tabName);
	  
	  if(hardcodedTab != null)
	  {
		  return hardcodedTab;
	  }
	  
	  return hardcodedTab;
  }

  @Override
  public ImportResponseIF importData(File file, String filename, ProgressMonitorIF monitor, String configuration)
  {
    FileInputStream istream = null;
    
    boolean valid = validateSpreadheet(configuration);
    
    try
    {
      istream = new FileInputStream(file);
      
      AbstractDHIS2Connector dhis2 = null;
//      AbstractDHIS2Connector dhis2 = new DHIS2OAuthConnector();
//      dhis2.readConfigFromDB();
//      
//      if (ExternalProfile.getAccessToken() == null)
//      {
//        OAuthLoginRequiredException ex = new OAuthLoginRequiredException();
//        throw ex;
//      }
      
      PalestineConverter converter = new PalestineConverter(dhis2, monitor);
      
      PalestineContentHandler handler = new PalestineContentHandler(converter, monitor);
      ExcelDataFormatter formatter = new ExcelDataFormatter();

      ExcelSheetReader reader = new ExcelSheetReader(handler, formatter);
      reader.process(istream);

      PalestineResponse summary = new PalestineResponse();
      summary.setTotal(monitor.getImportCount());
      summary.setFailures(handler.getNumberOfErrors());

      Workbook errors = converter.getErrors();

      if (errors != null)
      {
        try (PipedInputStream pis = new PipedInputStream())
        {
          DataImportRunnable.CopyRunnable runnable = new DataImportRunnable.CopyRunnable(pis, errors);
          Thread t = new Thread(runnable);
          t.setUncaughtExceptionHandler(runnable);
          t.setDaemon(true);
          t.start();

          VaultFile vf2 = VaultFile.createAndApply(filename, pis);

          t.join();

          summary.setFileId(vf2.getId());

          if (runnable.getThrowable() != null)
          {
            throw new ProgrammingErrorException(runnable.getThrowable());
          }
        }
      }

      if (converter.getProblems().size() > 0)
      {
        summary.setProblems(converter.getProblems());
      }

      return summary;
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }
    finally
    {
      try
      {
        istream.close();
      }
      catch (IOException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }
  }

}
