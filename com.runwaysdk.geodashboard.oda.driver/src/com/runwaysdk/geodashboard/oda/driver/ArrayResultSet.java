package com.runwaysdk.geodashboard.oda.driver;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
import com.runwaysdk.geodashboard.report.ReportQueryViewDTO;

public class ArrayResultSet implements IResultSet
{

  private ReportQueryViewDTO[] results;

  /**
   * The current row
   */
  private int                  current;

  private List<String>         attributeNames;

  public ArrayResultSet(ReportQueryViewDTO[] results)
  {
    this.results = results;
    this.current = -1;

    if (results.length > 0)
    {
      this.attributeNames = new ArrayList<String>(this.results[0].getAttributeNames());
    }
    else
    {
      this.attributeNames = new ArrayList<String>();
    }
  }

  private String getAttributeName(int index)
  {
    return this.attributeNames.get( ( index - 1 ));
  }

  private ReportQueryViewDTO getCurrent()
  {
    if (this.results != null)
    {
      return this.results[this.current];
    }

    return null;
  }

  @Override
  public void close() throws OdaException
  {
    this.results = null;
    this.current = -1;
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
    return new BigDecimal(getCurrent().getValue(columnName));
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
    return new Boolean(getCurrent().getValue(columnName));
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
    return new Date(MdAttributeDateUtil.getTypeSafeValue(getCurrent().getValue(columnName)).getTime());
  }

  @Override
  public double getDouble(int index) throws OdaException
  {
    return this.getDouble(this.getAttributeName(index));
  }

  @Override
  public double getDouble(String columnName) throws OdaException
  {
    Double value = MdAttributeDoubleUtil.getTypeSafeValue(getCurrent().getValue(columnName));

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
    Integer value = MdAttributeIntegerUtil.getTypeSafeValue(getCurrent().getValue(columnName));

    return ( value != null ? value.intValue() : 0 );
  }

  @Override
  public IResultSetMetaData getMetaData() throws OdaException
  {
    return new ViewResultSetMetadaData(this.getCurrent());
  }

  @Override
  public Object getObject(int index) throws OdaException
  {
    return this.getObject(this.getAttributeName(index));
  }

  @Override
  public Object getObject(String columnName) throws OdaException
  {
    return getCurrent().getValue(columnName);
  }

  @Override
  public int getRow() throws OdaException
  {
    return ( this.current + 1 );
  }

  @Override
  public String getString(int index) throws OdaException
  {
    return this.getString(this.getAttributeName(index));
  }

  @Override
  public String getString(String columnName) throws OdaException
  {
    return getCurrent().getValue(columnName);
  }

  @Override
  public Time getTime(int index) throws OdaException
  {
    return this.getTime(this.getAttributeName(index));
  }

  @Override
  public Time getTime(String columnName) throws OdaException
  {
    return new Time(MdAttributeTimeUtil.getTypeSafeValue(getCurrent().getValue(columnName)).getTime());
  }

  @Override
  public Timestamp getTimestamp(int index) throws OdaException
  {
    return this.getTimestamp(this.getAttributeName(index));
  }

  @Override
  public Timestamp getTimestamp(String columnName) throws OdaException
  {
    return new Timestamp(MdAttributeDateTimeUtil.getTypeSafeValue(getCurrent().getValue(columnName)).getTime());
  }

  @Override
  public boolean next() throws OdaException
  {
    this.current++;

    return ( this.results.length > current );
  }

  @Override
  public void setMaxRows(int index) throws OdaException
  {
    // Do nothing
  }

  @Override
  public boolean wasNull() throws OdaException
  {
    return ( getCurrent() == null );
  }

}
