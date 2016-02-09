package com.runwayskd.geodashboard.excel;

import com.runwaysdk.business.View;

public interface ViewContextIF
{
  public View newView(String sheetName);

  public String getAttributeName(String sheetName, String columnName);
}
