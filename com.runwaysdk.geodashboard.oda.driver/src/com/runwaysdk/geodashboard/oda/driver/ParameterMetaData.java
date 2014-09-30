/*
 * ************************************************************************
 * Copyright (c) 2013 <<Your Company Name here>>
 * 
 * ************************************************************************
 */

package com.runwaysdk.geodashboard.oda.driver;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Implementation class of IParameterMetaData for an ODA runtime driver. <br>
 * For demo purpose, the auto-generated method stubs have hard-coded
 * implementation that returns a pre-defined set of meta-data and query results.
 * A custom ODA driver is expected to implement own data source specific
 * behavior in its place.
 */
public class ParameterMetaData implements IParameterMetaData
{
  private LinkedHashMap<String, IParameter> parameters;

  private List<String>                      parameterNames;

  public ParameterMetaData()
  {
    this(new LinkedHashMap<String, IParameter>());
  }

  public ParameterMetaData(LinkedHashMap<String, IParameter> parameters)
  {
    this.parameters = parameters;
    this.parameterNames = new ArrayList<String>(parameters.keySet());
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterCount
   * ()
   */
  public int getParameterCount() throws OdaException
  {
    return this.parameters.size();
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterMode
   * (int)
   */
  public int getParameterMode(int param) throws OdaException
  {
    return this.parameters.get(this.getParameterName(param)).getParameterMode();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterName
   * (int)
   */
  public String getParameterName(int param) throws OdaException
  {
    return this.parameterNames.get( ( param - 1 ));
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterType
   * (int)
   */
  public int getParameterType(int param) throws OdaException
  {
    return this.parameters.get(this.getParameterName(param)).getParameterType();
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterTypeName
   * (int)
   */
  public String getParameterTypeName(int param) throws OdaException
  {
    int nativeTypeCode = getParameterType(param);
    return Driver.getNativeDataTypeName(nativeTypeCode);
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getPrecision(int)
   */
  public int getPrecision(int param) throws OdaException
  {
    return this.parameters.get(this.getParameterName(param)).getPrecision();
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getScale(int)
   */
  public int getScale(int param) throws OdaException
  {
    return this.parameters.get(this.getParameterName(param)).getScale();
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IParameterMetaData#isNullable(int)
   */
  public int isNullable(int param) throws OdaException
  {
    return this.parameters.get(this.getParameterName(param)).isNullable();
  }

}
