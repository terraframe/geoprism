package com.runwayskd.geodashboard.excel;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.View;
import com.runwaysdk.dataaccess.ProgrammingErrorException;

public class ViewContext implements ViewContextIF
{
  private Map<String, ViewSheetIF> sheets;

  private String                   directory;

  private String                   filename;

  public ViewContext(String configuration)
  {
    try
    {
      this.sheets = new HashMap<String, ViewSheetIF>();

      JSONObject object = new JSONObject(configuration);

      this.directory = object.getString("directory");
      this.filename = object.getString("filename");

      JSONArray sheets = object.getJSONArray("sheets");

      for (int i = 0; i < sheets.length(); i++)
      {
        JSONObject sheet = sheets.getJSONObject(i);

        if (sheet.has("sheetName"))
        {
          String sheetName = sheet.getString("sheetName");

          this.sheets.put(sheetName, new ViewSheet(sheet));
        }
      }

    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private ViewSheetIF getSheet(String sheetName)
  {
    return this.sheets.get(sheetName);
  }

  @Override
  public View newView(String sheetName)
  {
    ViewSheetIF sheet = this.getSheet(sheetName);
    String type = sheet.getType();

    View view = (View) BusinessFacade.newSessionComponent(type);

    return view;
  }

  @Override
  public String getAttributeName(String sheetName, String columnName)
  {
    ViewSheetIF sheet = this.getSheet(sheetName);
    String attributeName = sheet.getAttributeName(columnName);

    return attributeName;
  }

  public String getDirectory()
  {
    return this.directory;
  }

  public String getFilename()
  {
    return this.filename;
  }

}
