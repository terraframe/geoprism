package com.runwaysdk.geodashboard.oda.driver.ui.provider;

import java.util.LinkedList;
import java.util.List;
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

  public DataSetType[] getTypes(long milliSeconds)
  {
    class TempThread extends Thread
    {
      private List<DataSetType> results;

      private Throwable         throwable;

      @Override
      public void run()
      {
        this.results = new LinkedList<DataSetType>();

        try
        {
          IConnection connection = GeodashboardMetaDataProvider.this.getConnection();

          IQuery query = connection.newQuery(null);
          query.prepare(QueryFacadeUtil.getDashboardQueryText());

          IResultSet results = query.executeQuery();

          while (results.next())
          {
            String queryId = results.getString("queryId");
            String queryLabel = results.getString("queryLabel");
            int maxDepth = results.getInt("maxDepth");

            this.results.add(new DataSetType(queryId, queryLabel, maxDepth));
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

      public List<DataSetType> getResults()
      {
        return this.results;
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

    List<DataSetType> results = tt.getResults();

    return results.toArray(new DataSetType[results.size()]);
  }
}
