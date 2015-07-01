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

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Implementation class of IDataSetMetaData for an ODA runtime driver. <br>
 * For demo purpose, the auto-generated method stubs have hard-coded
 * implementation that assume this custom ODA data set is capable of handling a
 * query that returns a single result set and accepts scalar input parameters by
 * index. A custom ODA driver is expected to implement own data set specific
 * behavior in its place.
 */
public class DataSetMetaData implements IDataSetMetaData
{
  private IConnection connection;

  DataSetMetaData(IConnection connection)
  {
    this.connection = connection;
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getConnection()
   */
  public IConnection getConnection() throws OdaException
  {
    return connection;
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getDataSourceObjects
   * (java.lang.String, java.lang.String, java.lang.String, java.lang.String)
   */
  public IResultSet getDataSourceObjects(String catalog, String schema, String object, String version) throws OdaException
  {
    throw new UnsupportedOperationException();
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#
   * getDataSourceMajorVersion()
   */
  public int getDataSourceMajorVersion() throws OdaException
  {
    return 1;
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#
   * getDataSourceMinorVersion()
   */
  public int getDataSourceMinorVersion() throws OdaException
  {
    return 0;
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#
   * getDataSourceProductName()
   */
  public String getDataSourceProductName() throws OdaException
  {
    return "Geodashboard Data Source";
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#
   * getDataSourceProductVersion()
   */
  public String getDataSourceProductVersion() throws OdaException
  {
    return Integer.toString(getDataSourceMajorVersion()) + "." + //$NON-NLS-1$
        Integer.toString(getDataSourceMinorVersion());
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getSQLStateType()
   */
  public int getSQLStateType() throws OdaException
  {
    return IDataSetMetaData.sqlStateSQL99;
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#
   * supportsMultipleResultSets()
   */
  public boolean supportsMultipleResultSets() throws OdaException
  {
    return true;
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#
   * supportsMultipleOpenResults()
   */
  public boolean supportsMultipleOpenResults() throws OdaException
  {
    return true;
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsNamedResultSets
   * ()
   */
  public boolean supportsNamedResultSets() throws OdaException
  {
    return false;
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsNamedParameters
   * ()
   */
  public boolean supportsNamedParameters() throws OdaException
  {
    return false;
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsInParameters
   * ()
   */
  public boolean supportsInParameters() throws OdaException
  {
    return true;
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsOutParameters
   * ()
   */
  public boolean supportsOutParameters() throws OdaException
  {
    return false;
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getSortMode()
   */
  public int getSortMode()
  {
    return IDataSetMetaData.sortModeNone;
  }

}
