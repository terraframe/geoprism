package com.runwaysdk.geodashboard.oda.driver.ui.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;

import com.runwaysdk.geodashboard.oda.driver.Driver;

public class GeodashboardMetaDataProvider
{
  private static Logger logger = Logger.getLogger(GeodashboardMetaDataProvider.class.getName());

  private DataSetDesign design;

  private IConnection   connection;

  public GeodashboardMetaDataProvider(DataSetDesign design)
  {
    this.design = design;
  }

  private synchronized IConnection getConnection() throws OdaException
  {
    if (this.connection == null)
    {
      IDriver driver = new Driver();

      // obtain and open a live connection
      Properties props = DesignSessionUtil.getEffectiveDataSourceProperties(this.design.getDataSourceDesign());

      this.connection = driver.getConnection(null);
      this.connection.open(props);
    }

    return this.connection;
  }

  public void reconnect() throws OdaException
  {
  }

  public void release() throws OdaException
  {
    if (this.connection != null)
    {
      this.connection.close();
    }
  }

  public Map<String, String> getTypes(long milliSeconds)
  {
    class TempThread extends Thread
    {
      private Map<String, String> types = new HashMap<String, String>();

      private Throwable           throwable;

      @Override
      public void run()
      {
        try
        {
          IConnection connection = GeodashboardMetaDataProvider.this.getConnection();

          IQuery query = connection.newQuery(null);
          query.prepare(QueryFacadeUtil.getDashboardQueryText());

          IResultSet results = query.executeQuery();

          while (results.next())
          {
            String value = results.getString("dashboardId");
            String label = results.getString("dashboardName");

            types.put(value, label);
          }
        }
        catch (Throwable e)
        {
          logger.log(Level.SEVERE, e.getLocalizedMessage(), e);

          this.throwable = e;
        }
      }

      public Throwable getThrowable()
      {
        return this.throwable;
      }

      public Map<String, String> getResult()
      {
        return types;
      }
    }

    TempThread tt = new TempThread();
    tt.start();

    try
    {
      tt.join(milliSeconds);
    }
    catch (InterruptedException e)
    {
    }

    if (tt.getThrowable() != null)
    {
      throw new RuntimeException(tt.getThrowable());
    }

    return tt.getResult();
  }
}
