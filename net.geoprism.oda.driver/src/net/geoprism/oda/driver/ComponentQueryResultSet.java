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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.geoprism.report.RemoteQueryIF;
import net.geoprism.report.RemoteResultIF;

import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

import com.runwaysdk.constants.MdAttributeDateTimeUtil;
import com.runwaysdk.constants.MdAttributeDateUtil;
import com.runwaysdk.constants.MdAttributeDoubleUtil;
import com.runwaysdk.constants.MdAttributeIntegerUtil;
import com.runwaysdk.constants.MdAttributeTimeUtil;

public class ComponentQueryResultSet implements IResultSet
{
  /**
   * Query containing the results and the metadata of the results
   */
  private RemoteQueryIF                      query;

  /**
   * Result set of the query
   */
  private List<? extends RemoteResultIF>     resultSet;

  /**
   * Result set iterator
   */
  private Iterator<? extends RemoteResultIF> iterator;

  /**
   * The current row
   */
  private RemoteResultIF                     current;

  /**
   * Row number of the current row
   */
  private int                                rowNumber;

  private List<String>                       attributeNames;

  public ComponentQueryResultSet(RemoteQueryIF query)
  {
    this.query = query;
    this.resultSet = this.query.getResultSet();
    this.iterator = this.resultSet.iterator();
    this.attributeNames = new ArrayList<String>(this.query.getAttributeNames());

    this.current = null;
    this.rowNumber = -1;
  }

  private String getAttributeName(int index)
  {
    return this.attributeNames.get( ( index - 1 ));
  }

  @Override
  public void close() throws OdaException
  {
    this.query.clearResultSet();
  }

  @Override
  public int findColumn(String columnName) throws OdaException
  {
    return this.attributeNames.indexOf(columnName);
  }

  @Override
  public BigDecimal getBigDecimal(int index) throws OdaException
  {
    return this.getBigDecimal(this.getAttributeName(index));
  }

  @Override
  public BigDecimal getBigDecimal(String columnName) throws OdaException
  {
    return new BigDecimal(this.current.getValue(columnName));
  }

  @Override
  public IBlob getBlob(int index) throws OdaException
  {
    return null;
  }

  @Override
  public IBlob getBlob(String columnName) throws OdaException
  {
    return null;
  }

  @Override
  public boolean getBoolean(int index) throws OdaException
  {
    return this.getBoolean(this.getAttributeName(index));
  }

  @Override
  public boolean getBoolean(String columnName) throws OdaException
  {
    return new Boolean(this.current.getValue(columnName));
  }

  @Override
  public IClob getClob(int index) throws OdaException
  {
    return null;
  }

  @Override
  public IClob getClob(String columnName) throws OdaException
  {
    return null;
  }

  @Override
  public Date getDate(int index) throws OdaException
  {
    return this.getDate(this.getAttributeName(index));
  }

  @Override
  public Date getDate(String columnName) throws OdaException
  {
    java.util.Date value = MdAttributeDateUtil.getTypeSafeValue(this.current.getValue(columnName));

    if (value != null)
    {
      return new Date(value.getTime());
    }

    return null;
  }

  @Override
  public double getDouble(int index) throws OdaException
  {
    return this.getDouble(this.getAttributeName(index));
  }

  @Override
  public double getDouble(String columnName) throws OdaException
  {
    Double value = MdAttributeDoubleUtil.getTypeSafeValue(this.current.getValue(columnName));

    return ( value != null ? value.doubleValue() : 0D );
  }

  @Override
  public int getInt(int index) throws OdaException
  {
    return this.getInt(this.getAttributeName(index));
  }

  @Override
  public int getInt(String columnName) throws OdaException
  {
    Integer value = MdAttributeIntegerUtil.getTypeSafeValue(this.current.getValue(columnName));

    return ( value != null ? value.intValue() : 0 );
  }

  @Override
  public IResultSetMetaData getMetaData() throws OdaException
  {
    return new ComponentQueryResultSetMetadaData(this.query);
  }

  @Override
  public Object getObject(int index) throws OdaException
  {
    return this.getObject(this.getAttributeName(index));
  }

  @Override
  public Object getObject(String columnName) throws OdaException
  {
    return this.current.getValue(columnName);
  }

  @Override
  public int getRow() throws OdaException
  {
    return ( this.rowNumber + 1 );
  }

  @Override
  public String getString(int index) throws OdaException
  {
    return this.getString(this.getAttributeName(index));
  }

  @Override
  public String getString(String columnName) throws OdaException
  {
    return this.current.getValue(columnName);
  }

  @Override
  public Time getTime(int index) throws OdaException
  {
    return this.getTime(this.getAttributeName(index));
  }

  @Override
  public Time getTime(String columnName) throws OdaException
  {
    java.util.Date value = MdAttributeTimeUtil.getTypeSafeValue(this.current.getValue(columnName));

    if (value != null)
    {
      return new Time(value.getTime());
    }

    return null;
  }

  @Override
  public Timestamp getTimestamp(int index) throws OdaException
  {
    return this.getTimestamp(this.getAttributeName(index));
  }

  @Override
  public Timestamp getTimestamp(String columnName) throws OdaException
  {
    java.util.Date value = MdAttributeDateTimeUtil.getTypeSafeValue(this.current.getValue(columnName));

    if (value != null)
    {
      return new Timestamp(value.getTime());
    }

    return null;
  }

  @Override
  public boolean next() throws OdaException
  {
    if (this.iterator.hasNext())
    {
      this.current = this.iterator.next();
      this.rowNumber++;

      return true;
    }

    return false;
  }

  @Override
  public void setMaxRows(int index) throws OdaException
  {
    // Do nothing
  }

  @Override
  public boolean wasNull() throws OdaException
  {
    return ( this.current == null );
  }

}
