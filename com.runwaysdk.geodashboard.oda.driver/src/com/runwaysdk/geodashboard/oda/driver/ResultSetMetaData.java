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
package com.runwaysdk.geodashboard.oda.driver;

import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.json.JSONObject;

/**
 * Implementation class of IResultSetMetaData for an ODA runtime driver. <br>
 * For demo purpose, the auto-generated method stubs have hard-coded
 * implementation that returns a pre-defined set of meta-data and query results.
 * A custom ODA driver is expected to implement own data source specific
 * behavior in its place.
 */
public class ResultSetMetaData implements IResultSetMetaData
{

  private String dashboardName;

  public ResultSetMetaData(JSONObject json)
  {
    this.dashboardName = json.getString("dashboardName");
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnCount()
   */
  public int getColumnCount() throws OdaException
  {
    // TODO replace with data source specific implementation

    // hard-coded for demo purpose

    if (dashboardName.equals("Dashboard 1"))
    {
      return 2;
    }

    return 3;
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnName
   * (int)
   */
  public String getColumnName(int index) throws OdaException
  {
    // TODO replace with data source specific implementation

    // hard-coded for demo purpose
    return this.dashboardName + " Column " + index;
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnLabel
   * (int)
   */
  public String getColumnLabel(int index) throws OdaException
  {
    return getColumnName(index); // default
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnType
   * (int)
   */
  public int getColumnType(int index) throws OdaException
  {
    // TODO replace with data source specific implementation

    // hard-coded for demo purpose
    if (index == 1)
    {
      return java.sql.Types.INTEGER; // as defined in data set extension
                                     // manifest
    }

    return java.sql.Types.CHAR; // as defined in data set extension manifest
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnTypeName
   * (int)
   */
  public String getColumnTypeName(int index) throws OdaException
  {
    int nativeTypeCode = getColumnType(index);
    return Driver.getNativeDataTypeName(nativeTypeCode);
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#
   * getColumnDisplayLength(int)
   */
  public int getColumnDisplayLength(int index) throws OdaException
  {
    // TODO replace with data source specific implementation

    // hard-coded for demo purpose
    return 8;
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getPrecision(int)
   */
  public int getPrecision(int index) throws OdaException
  {
    // TODO Auto-generated method stub
    return -1;
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getScale(int)
   */
  public int getScale(int index) throws OdaException
  {
    // TODO Auto-generated method stub
    return -1;
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSetMetaData#isNullable(int)
   */
  public int isNullable(int index) throws OdaException
  {
    // TODO Auto-generated method stub
    return IResultSetMetaData.columnNullableUnknown;
  }

}
