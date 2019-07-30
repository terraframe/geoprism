/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import java.util.Map;
import java.util.Properties;

import net.geoprism.oda.driver.session.ClientSessionCache;
import net.geoprism.oda.driver.session.ClientSessionProxy;
import net.geoprism.oda.driver.session.IClientSession;

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

    public static final String METADATA_QUERY            = "METADATA_QUERY";
  }

  private IClientSession      session;

  /**
   * Application context. This is used for sending in the user's session when the driver being run
   */
  private Map<Object, Object> context;

  /*
   * @see org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties )
   */
  public void open(Properties props) throws OdaException
  {
    if (this.session == null)
    {
      String url = props.getProperty(Constants.ODA_URL);

      if (this.context != null)
      {
        String sessionId = (String) this.context.get(ClientSessionProxy.SESSION_ID);

        if (sessionId != null)
        {
          this.session = ClientSessionCache.getClientSession(url, sessionId);
        }
      }

      if (this.session == null)
      {
        String username = props.getProperty(Constants.ODA_USER);
        String password = props.getProperty(Constants.ODA_PASSWORD);
        String decrypted = CryptographySingleton.decrypt(password);

        this.session = ClientSessionCache.getClientSession(url, username, decrypted);
      }
    }

    if (props == null)
    {
      throw new IllegalArgumentException("Connection properties cannot be null");
    }
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IConnection#setAppContext(java.lang .Object)
   */
  @SuppressWarnings("unchecked")
  public void setAppContext(Object context) throws OdaException
  {
    this.context = (Map<Object, Object>) context;
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IConnection#close()
   */
  public void close() throws OdaException
  {
    if (this.session != null)
    {
      this.session.logout();

      ClientSessionCache.close(this.session);
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
   * @see org.eclipse.datatools.connectivity.oda.IConnection#getMetaData(java.lang .String)
   */
  public IDataSetMetaData getMetaData(String dataSetType) throws OdaException
  {
    // assumes that this driver supports only one type of data set,
    // ignores the specified dataSetType
    return new DataSetMetaData(this);
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IConnection#newQuery(java.lang.String )
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
   * @see org.eclipse.datatools.connectivity.oda.IConnection#setLocale(com.ibm.icu .util.ULocale)
   */
  public void setLocale(ULocale locale) throws OdaException
  {
    // do nothing; assumes no locale support
  }

}
