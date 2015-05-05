package com.runwaysdk.geodashboard.oda.driver;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.geodashboard.report.ReportItemDTO;

public class BlockQueryResultSet implements IResultSet
{
  private ClientRequestIF         request;

  private String                  queryId;

  private String                  category;

  private String                  criteria;

  private String                  aggregation;

  private int                     pageSize;

  private int                     currentPage;

  private ComponentQueryResultSet query;

  private int                     maxPage;

  public BlockQueryResultSet(ClientRequestIF request, String queryId, String category, String criteria, String aggregation)
  {
    this.request = request;
    this.queryId = queryId;
    this.category = category;
    this.criteria = criteria;
    this.aggregation = aggregation;

    this.pageSize = -1;
  }

  private synchronized ComponentQueryResultSet getQuery()
  {
    if (this.query == null)
    {
      this.maxPage = ReportItemDTO.getPageCount(this.request, this.queryId, this.category, this.criteria, this.aggregation, this.pageSize);
      this.maxPage = 0;

      this.nextQuery();
    }

    return this.query;
  }

  private synchronized void nextQuery()
  {
    this.currentPage = this.currentPage + 1;

    ValueQueryDTO values = ReportItemDTO.getValuesForReporting(this.request, this.queryId, this.category, this.criteria, this.aggregation, this.pageSize, this.currentPage);

    this.query = new ComponentQueryResultSet(values);
  }

  @Override
  public void close() throws OdaException
  {
    this.getQuery().close();
  }

  @Override
  public int findColumn(String columnName) throws OdaException
  {
    return this.getQuery().findColumn(columnName);
  }

  @Override
  public BigDecimal getBigDecimal(int index) throws OdaException
  {
    return this.getQuery().getBigDecimal(index);
  }

  @Override
  public BigDecimal getBigDecimal(String columnName) throws OdaException
  {
    return this.getQuery().getBigDecimal(columnName);
  }

  @Override
  public IBlob getBlob(int index) throws OdaException
  {
    return this.getQuery().getBlob(index);
  }

  @Override
  public IBlob getBlob(String columnName) throws OdaException
  {
    return this.getQuery().getBlob(columnName);
  }

  @Override
  public boolean getBoolean(int index) throws OdaException
  {
    return this.getQuery().getBoolean(index);
  }

  @Override
  public boolean getBoolean(String columnName) throws OdaException
  {
    return this.getQuery().getBoolean(columnName);
  }

  @Override
  public IClob getClob(int index) throws OdaException
  {
    return this.getQuery().getClob(index);
  }

  @Override
  public IClob getClob(String columnName) throws OdaException
  {
    return this.getQuery().getClob(columnName);
  }

  @Override
  public Date getDate(int index) throws OdaException
  {
    return this.getQuery().getDate(index);
  }

  @Override
  public Date getDate(String columnName) throws OdaException
  {
    return this.getQuery().getDate(columnName);
  }

  @Override
  public double getDouble(int index) throws OdaException
  {
    return this.getQuery().getDouble(index);
  }

  @Override
  public double getDouble(String columnName) throws OdaException
  {
    return this.getQuery().getDouble(columnName);
  }

  @Override
  public int getInt(int index) throws OdaException
  {
    return this.getQuery().getInt(index);
  }

  @Override
  public int getInt(String columnName) throws OdaException
  {
    return this.getQuery().getInt(columnName);
  }

  @Override
  public IResultSetMetaData getMetaData() throws OdaException
  {
    return this.getQuery().getMetaData();
  }

  @Override
  public Object getObject(int index) throws OdaException
  {
    return this.getQuery().getObject(index);
  }

  @Override
  public Object getObject(String columnName) throws OdaException
  {
    return this.getQuery().getObject(columnName);
  }

  @Override
  public int getRow() throws OdaException
  {
    return ( ( this.currentPage - 1 ) * this.pageSize ) + this.getQuery().getRow();
  }

  @Override
  public String getString(int index) throws OdaException
  {
    return this.getQuery().getString(index);
  }

  @Override
  public String getString(String columnName) throws OdaException
  {
    return this.getQuery().getString(columnName);
  }

  @Override
  public Time getTime(int index) throws OdaException
  {
    return this.getQuery().getTime(index);
  }

  @Override
  public Time getTime(String columnName) throws OdaException
  {
    return this.getQuery().getTime(columnName);
  }

  @Override
  public Timestamp getTimestamp(int index) throws OdaException
  {
    return this.getQuery().getTimestamp(index);
  }

  @Override
  public Timestamp getTimestamp(String columnName) throws OdaException
  {
    return this.getQuery().getTimestamp(columnName);
  }

  @Override
  public boolean next() throws OdaException
  {
    if (this.getQuery().next())
    {
      return true;
    }

    if (this.currentPage < this.maxPage)
    {
      this.nextQuery();

      return this.getQuery().next();
    }

    return false;
  }

  @Override
  public void setMaxRows(int index) throws OdaException
  {
    this.getQuery().setMaxRows(index);
  }

  @Override
  public boolean wasNull() throws OdaException
  {
    return this.getQuery().wasNull();
  }
}
