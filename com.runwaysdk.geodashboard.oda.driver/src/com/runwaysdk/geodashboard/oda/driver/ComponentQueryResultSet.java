package com.runwaysdk.geodashboard.oda.driver;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.ComponentQueryDTO;
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
  private ComponentQueryDTO                  query;

  /**
   * Result set of the query
   */
  private List<? extends ComponentDTOIF>     resultSet;

  /**
   * Result set iterator
   */
  private Iterator<? extends ComponentDTOIF> iterator;

  /**
   * The current row
   */
  private ComponentDTOIF                     current;

  /**
   * Row number of the current row
   */
  private int                                rowNumber;

  private List<String>                       attributeNames;

  public ComponentQueryResultSet(ComponentQueryDTO query)
  {
    this.query = query;
    this.resultSet = this.query.getResultSet();
    this.iterator = this.resultSet.iterator();
    this.attributeNames = this.query.getAttributeNames();

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
