/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.oda.driver;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Implementation class of IResultSet for an ODA runtime driver. <br>
 * For demo purpose, the auto-generated method stubs have hard-coded
 * implementation that returns a pre-defined set of meta-data and query results.
 * A custom ODA driver is expected to implement own data source specific
 * behavior in its place.
 */
public class ResultSet implements IResultSet
{
  private int                m_maxRows;

  private int                m_currentRowId;

  private IResultSetMetaData metadata;

  private String[]           results;

  public ResultSet(IResultSetMetaData metadata, String[] results)
  {
    this.metadata = metadata;
    this.results = results;
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IResultSet#getMetaData()
   */
  public IResultSetMetaData getMetaData() throws OdaException
  {
    /*
     * TODO Auto-generated method stub Replace with implementation to return an
     * instance based on this result set.
     */
    return this.metadata;
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IResultSet#setMaxRows(int)
   */
  public void setMaxRows(int max) throws OdaException
  {
    m_maxRows = max;
  }

  /**
   * Returns the maximum number of rows that can be fetched from this result
   * set.
   * 
   * @return the maximum number of rows to fetch.
   */
  protected int getMaxRows()
  {
    return m_maxRows;
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IResultSet#next()
   */
  public boolean next() throws OdaException
  {
    // TODO replace with data source specific implementation

    // simple implementation done below for demo purpose only
    int maxRows = getMaxRows();
    if (maxRows <= 0) // no limit is specified
      maxRows = 5; // hard-coded for demo purpose

    if (m_currentRowId < maxRows)
    {
      m_currentRowId++;
      return true;
    }

    return false;
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IResultSet#close()
   */
  public void close() throws OdaException
  {
    // TODO Auto-generated method stub
    m_currentRowId = 0; // reset row counter
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IResultSet#getRow()
   */
  public int getRow() throws OdaException
  {
    return m_currentRowId;
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IResultSet#getString(int)
   */
  public String getString(int index) throws OdaException
  {
    // TODO replace with data source specific implementation

    if ( ( this.m_currentRowId - 1 ) < results.length)
    {
      return results[ ( this.m_currentRowId - 1 )];
    }

    // hard-coded for demo purpose
    return "row" + getRow() + "_column" + index + " value";
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSet#getString(java.lang.String
   * )
   */
  public String getString(String columnName) throws OdaException
  {
    return getString(findColumn(columnName));
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IResultSet#getInt(int)
   */
  public int getInt(int index) throws OdaException
  {
    // TODO replace with data source specific implementation

    // hard-coded for demo purpose
    return getRow();
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSet#getInt(java.lang.String)
   */
  public int getInt(String columnName) throws OdaException
  {
    return getInt(findColumn(columnName));
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDouble(int)
   */
  public double getDouble(int index) throws OdaException
  {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSet#getDouble(java.lang.String
   * )
   */
  public double getDouble(String columnName) throws OdaException
  {
    return getDouble(findColumn(columnName));
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBigDecimal(int)
   */
  public BigDecimal getBigDecimal(int index) throws OdaException
  {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSet#getBigDecimal(java.lang
   * .String)
   */
  public BigDecimal getBigDecimal(String columnName) throws OdaException
  {
    return getBigDecimal(findColumn(columnName));
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDate(int)
   */
  public Date getDate(int index) throws OdaException
  {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSet#getDate(java.lang.String)
   */
  public Date getDate(String columnName) throws OdaException
  {
    return getDate(findColumn(columnName));
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTime(int)
   */
  public Time getTime(int index) throws OdaException
  {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSet#getTime(java.lang.String)
   */
  public Time getTime(String columnName) throws OdaException
  {
    return getTime(findColumn(columnName));
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTimestamp(int)
   */
  public Timestamp getTimestamp(int index) throws OdaException
  {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSet#getTimestamp(java.lang
   * .String)
   */
  public Timestamp getTimestamp(String columnName) throws OdaException
  {
    return getTimestamp(findColumn(columnName));
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBlob(int)
   */
  public IBlob getBlob(int index) throws OdaException
  {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSet#getBlob(java.lang.String)
   */
  public IBlob getBlob(String columnName) throws OdaException
  {
    return getBlob(findColumn(columnName));
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IResultSet#getClob(int)
   */
  public IClob getClob(int index) throws OdaException
  {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSet#getClob(java.lang.String)
   */
  public IClob getClob(String columnName) throws OdaException
  {
    return getClob(findColumn(columnName));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBoolean(int)
   */
  public boolean getBoolean(int index) throws OdaException
  {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSet#getBoolean(java.lang.
   * String)
   */
  public boolean getBoolean(String columnName) throws OdaException
  {
    return getBoolean(findColumn(columnName));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.IResultSet#getObject(int)
   */
  public Object getObject(int index) throws OdaException
  {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSet#getObject(java.lang.String
   * )
   */
  public Object getObject(String columnName) throws OdaException
  {
    return getObject(findColumn(columnName));
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IResultSet#wasNull()
   */
  public boolean wasNull() throws OdaException
  {
    // TODO Auto-generated method stub

    // hard-coded for demo purpose
    return false;
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IResultSet#findColumn(java.lang.
   * String)
   */
  public int findColumn(String columnName) throws OdaException
  {
    // TODO replace with data source specific implementation

    // hard-coded for demo purpose
    int columnId = 1; // dummy column oid
    if (columnName == null || columnName.length() == 0)
    {
      return columnId;
    }

    String lastChar = columnName.substring(columnName.length() - 1, 1);
    try
    {
      columnId = Integer.parseInt(lastChar);
    }
    catch (NumberFormatException e)
    {
      // ignore, use dummy column oid
    }
    return columnId;
  }

}
