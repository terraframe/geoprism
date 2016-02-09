package com.runwayskd.geodashboard.excel;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;

public class ViewField implements ViewFieldIF
{
  private String fieldName;

  private String attributeName;

  public ViewField(JSONObject field)
  {
    try
    {
      this.fieldName = field.getString("fieldName");
      this.attributeName = field.getString("attributeName");
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public String getFieldName()
  {
    return fieldName;
  }

  public String getAttributeName()
  {
    return attributeName;
  }

}
