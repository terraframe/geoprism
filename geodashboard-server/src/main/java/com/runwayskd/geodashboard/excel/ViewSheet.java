package com.runwayskd.geodashboard.excel;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;

public class ViewSheet implements ViewSheetIF
{
  private String                   type;

  private Map<String, ViewFieldIF> fields;

  public ViewSheet(JSONObject sheet)
  {
    try
    {
      this.type = sheet.getString("type");

      JSONArray fields = sheet.getJSONArray("fields");

      for (int i = 0; i < fields.length(); i++)
      {
        JSONObject object = fields.getJSONObject(i);
        ViewField field = new ViewField(object);

        this.fields.put(field.getFieldName(), field);
      }
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public String getType()
  {
    return this.type;
  }

  @Override
  public String getAttributeName(String columnName)
  {
    ViewFieldIF field = this.fields.get(columnName);

    return field.getAttributeName();
  }
}
