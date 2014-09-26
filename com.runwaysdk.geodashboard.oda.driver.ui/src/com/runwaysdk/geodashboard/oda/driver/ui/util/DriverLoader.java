package com.runwaysdk.geodashboard.oda.driver.ui.util;

import java.util.Properties;

import org.eclipse.datatools.connectivity.oda.OdaException;

import com.runwaysdk.geodashboard.oda.driver.Connection;

public class DriverLoader
{

  private DriverLoader()
  {
  }

  public static Connection getConnection(Properties props) throws OdaException
  {
    Connection connection = new Connection();
    connection.open(props);

    return connection;
  }

  public static boolean testConnection(String url, String userName, String password, Properties props)
  {
    props.setProperty(Constants.ODAURL, url);
    props.setProperty(Constants.ODAUser, userName);
    props.setProperty(Constants.ODAPassword, password);

    try
    {
      Connection connection = DriverLoader.getConnection(props);
      connection.close();

      return true;
    }
    catch (Exception e)
    {
      return false;
    }
  }

}
