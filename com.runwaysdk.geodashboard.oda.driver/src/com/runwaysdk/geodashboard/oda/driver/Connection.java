/*
 * ************************************************************************
 * Copyright (c) 2013 <<Your Company Name here>>
 * 
 * ************************************************************************
 */

package com.runwaysdk.geodashboard.oda.driver;

import java.util.Properties;

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;

import com.ibm.icu.util.ULocale;

/**
 * Implementation class of IConnection for an ODA runtime driver.
 */
public class Connection implements IConnection
{
  /**
   * define constants ODAURL, ODAPassword, ODAUser, ODADataSource
   */
  public static class Constants
  {
    public static final String ODA_URL                   = "odaURL";

    public static final String ODA_PASSWORD              = "odaPassword";

    public static final String ODA_USER                  = "odaUser";

    public static final String ODA_DATA_SOURCE           = "odaDataSource";

    public static final String CONNECTION_PROPERTIES_STR = "connectionProps";

    public static final String PASS_IN_CONNECTION        = "connection";
  }

  private ClientSessionProxy session;

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties
   * )
   */
  public void open(Properties props) throws OdaException
  {
    if (this.session == null)
    {
      String url = props.getProperty(Constants.ODA_URL);
      String username = props.getProperty(Constants.ODA_USER);
      String password = props.getProperty(Constants.ODA_PASSWORD);

      this.session = ClientSessionCache.getClientSession(url, username, password);
    }

    if (props == null)
    {
      throw new IllegalArgumentException("Connection properties cannot be null");
    }
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IConnection#setAppContext(java.lang
   * .Object)
   */
  public void setAppContext(Object context) throws OdaException
  {
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IConnection#close()
   */
  public void close() throws OdaException
  {
    if (this.session != null)
    {
      this.session.logout();
    }
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IConnection#isOpen()
   */
  public boolean isOpen() throws OdaException
  {
    return ( this.session != null );
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IConnection#getMetaData(java.lang
   * .String)
   */
  public IDataSetMetaData getMetaData(String dataSetType) throws OdaException
  {
    // assumes that this driver supports only one type of data set,
    // ignores the specified dataSetType
    return new DataSetMetaData(this);
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IConnection#newQuery(java.lang.String
   * )
   */
  public IQuery newQuery(String dataSetType) throws OdaException
  {
    if (this.session != null)
    {
      return new Query(this.session.getRequest());
    }

    throw new OdaException("Connection has not been established");
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IConnection#getMaxQueries()
   */
  public int getMaxQueries() throws OdaException
  {
    return 0; // no limit
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IConnection#commit()
   */
  public void commit() throws OdaException
  {
    // do nothing; assumes no transaction support needed
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IConnection#rollback()
   */
  public void rollback() throws OdaException
  {
    // do nothing; assumes no transaction support needed
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.datatools.connectivity.oda.IConnection#setLocale(com.ibm.icu
   * .util.ULocale)
   */
  public void setLocale(ULocale locale) throws OdaException
  {
    // do nothing; assumes no locale support
  }

}
