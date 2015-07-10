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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.SortSpec;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;

import com.runwaysdk.constants.ClientRequestIF;

/**
 * Implementation class of IQuery for an ODA runtime driver. <br>
 * For demo purpose, the auto-generated method stubs have hard-coded implementation that returns a pre-defined set of
 * meta-data and query results. A custom ODA driver is expected to implement own data source specific behavior in its
 * place.
 */
public class Query implements IQuery
{

  private String              queryText;

  private ClientRequestIF     request;

  private IResultSet          resultSet;

  private IParameterMetaData  parameterMetadata;

  private Map<String, Object> parameters;

  private Map<String, String> properties;

  public Query(ClientRequestIF request)
  {
    this.request = request;
    this.parameterMetadata = null;
    this.parameters = new HashMap<String, Object>();
    this.properties = new HashMap<String, String>();
  }

  private String getParameterName(int parameterId) throws OdaException
  {
    return this.getParameterMetaData().getParameterName(parameterId);
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#prepare(java.lang.String)
   */
  public void prepare(String queryText) throws OdaException
  {
    this.queryText = queryText;
    this.parameterMetadata = null;
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setAppContext(java.lang.Object )
   */
  public void setAppContext(Object appContext) throws OdaException
  {
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#close()
   */
  public void close() throws OdaException
  {
    this.queryText = null;
    this.resultSet = null;
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#getMetaData()
   */
  public IResultSetMetaData getMetaData() throws OdaException
  {
    String key = MetadataManager.getKey(this.queryText);

    if (!MetadataManager.hasMetadata(key))
    {
      IResultSet resultSet = new QueryFacade().invoke(this.request, this.queryText, this.parameters, true);
      IResultSetMetaData metadata = resultSet.getMetaData();

      MetadataManager.putMetadata(key, metadata);
    }

    return MetadataManager.getMetadata(key);
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#executeQuery()
   */
  public IResultSet executeQuery() throws OdaException
  {
    boolean queryMetadata = this.properties.containsKey(Connection.Constants.METADATA_QUERY);

    this.resultSet = new QueryFacade().invoke(this.request, this.queryText, this.parameters, queryMetadata);

    return this.resultSet;
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setProperty(java.lang.String, java.lang.String)
   */
  public void setProperty(String name, String value) throws OdaException
  {
    this.properties.put(name, value);
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setMaxRows(int)
   */
  public void setMaxRows(int max) throws OdaException
  {
    // Do nothing
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#getMaxRows()
   */
  public int getMaxRows() throws OdaException
  {
    return 0;
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#clearInParameters()
   */
  public void clearInParameters() throws OdaException
  {
    this.parameters.clear();
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(java.lang.String, int)
   */
  public void setInt(String parameterName, int value) throws OdaException
  {
    this.parameters.put(parameterName, new Integer(value));
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(int, int)
   */
  public void setInt(int parameterId, int value) throws OdaException
  {
    this.setInt(this.getParameterName(parameterId), value);
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(java.lang.String, double)
   */
  public void setDouble(String parameterName, double value) throws OdaException
  {
    this.parameters.put(parameterName, new Double(value));
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(int, double)
   */
  public void setDouble(int parameterId, double value) throws OdaException
  {
    this.setDouble(this.getParameterName(parameterId), value);
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(java.lang.String , java.math.BigDecimal)
   */
  public void setBigDecimal(String parameterName, BigDecimal value) throws OdaException
  {
    this.parameters.put(parameterName, value);
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(int, java.math.BigDecimal)
   */
  public void setBigDecimal(int parameterId, BigDecimal value) throws OdaException
  {
    this.setBigDecimal(this.getParameterName(parameterId), value);
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(java.lang.String, java.lang.String)
   */
  public void setString(String parameterName, String value) throws OdaException
  {
    this.parameters.put(parameterName, value);
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(int, java.lang.String)
   */
  public void setString(int parameterId, String value) throws OdaException
  {
    this.setString(this.getParameterName(parameterId), value);
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(java.lang.String, java.sql.Date)
   */
  public void setDate(String parameterName, Date value) throws OdaException
  {
    this.parameters.put(parameterName, value);
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(int, java.sql.Date)
   */
  public void setDate(int parameterId, Date value) throws OdaException
  {
    this.setDate(this.getParameterName(parameterId), value);
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(java.lang.String, java.sql.Time)
   */
  public void setTime(String parameterName, Time value) throws OdaException
  {
    this.parameters.put(parameterName, value);
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(int, java.sql.Time)
   */
  public void setTime(int parameterId, Time value) throws OdaException
  {
    this.setTime(this.getParameterName(parameterId), value);
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(java.lang.String , java.sql.Timestamp)
   */
  public void setTimestamp(String parameterName, Timestamp value) throws OdaException
  {
    this.parameters.put(parameterName, value);
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(int, java.sql.Timestamp)
   */
  public void setTimestamp(int parameterId, Timestamp value) throws OdaException
  {
    this.setTimestamp(this.getParameterName(parameterId), value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(java.lang.String, boolean)
   */
  public void setBoolean(String parameterName, boolean value) throws OdaException
  {
    this.parameters.put(parameterName, new Boolean(value));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(int, boolean)
   */
  public void setBoolean(int parameterId, boolean value) throws OdaException
  {
    this.setBoolean(this.getParameterName(parameterId), value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setObject(java.lang.String, java.lang.Object)
   */
  public void setObject(String parameterName, Object value) throws OdaException
  {
    this.parameters.put(parameterName, value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setObject(int, java.lang.Object)
   */
  public void setObject(int parameterId, Object value) throws OdaException
  {
    this.setObject(this.getParameterName(parameterId), value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(java.lang.String)
   */
  public void setNull(String parameterName) throws OdaException
  {
    this.parameters.remove(parameterName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(int)
   */
  public void setNull(int parameterId) throws OdaException
  {
    this.setNull(this.getParameterName(parameterId));
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#findInParameter(java.lang .String)
   */
  public int findInParameter(String parameterName) throws OdaException
  {
    return 0;
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#getParameterMetaData()
   */
  public IParameterMetaData getParameterMetaData() throws OdaException
  {
    if (this.parameterMetadata != null)
    {
      return this.parameterMetadata;
    }

    if (this.queryText != null)
    {
      return new QueryFacade().getParameterMetaData(this.queryText);
    }

    throw new OdaException("Cannot get parameter metadata without a query");
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setSortSpec(org.eclipse.datatools .connectivity.oda.SortSpec)
   */
  public void setSortSpec(SortSpec sortBy) throws OdaException
  {
    // only applies to sorting, assumes not supported
    throw new UnsupportedOperationException();
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IQuery#getSortSpec()
   */
  public SortSpec getSortSpec() throws OdaException
  {
    // only applies to sorting
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.IQuery#setSpecification(org.eclipse
   * .datatools.connectivity.oda.spec.QuerySpecification)
   */
  public void setSpecification(QuerySpecification querySpec) throws OdaException, UnsupportedOperationException
  {
    // assumes no support
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.IQuery#getSpecification()
   */
  public QuerySpecification getSpecification()
  {
    // assumes no support
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.IQuery#getEffectiveQueryText()
   */
  public String getEffectiveQueryText()
  {
    return queryText;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.IQuery#cancel()
   */
  public void cancel() throws OdaException, UnsupportedOperationException
  {
    // assumes unable to cancel while executing a query
    throw new UnsupportedOperationException();
  }

}
