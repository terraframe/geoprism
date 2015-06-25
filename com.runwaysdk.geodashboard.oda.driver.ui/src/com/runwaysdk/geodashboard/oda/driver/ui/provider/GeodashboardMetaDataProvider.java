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
  class TempThread extends Thread
  {
    private List<LabelValuePair> results;

    private Throwable            throwable;

    private String               queryText;

    public TempThread(String queryText)
    {
      this.queryText = queryText;
    }

    @Override
    public void run()
    {
      this.results = new LinkedList<LabelValuePair>();

      try
      {
        IConnection connection = GeodashboardMetaDataProvider.this.getConnection();

        IQuery query = connection.newQuery(null);
        query.prepare(this.queryText);

        IResultSet results = query.executeQuery();

        while (results.next())
        {
          String value = results.getString("value");
          String label = results.getString("label");

          this.results.add(new LabelValuePair(value, label));
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

    public List<LabelValuePair> getResults()
    {
      return this.results;
    }

    public List<LabelValuePair> execute(long milliSeconds)
    {
      this.start();

      try
      {
        this.join(milliSeconds);
      }
      catch (InterruptedException e)
      {
      }

      if (this.getThrowable() != null)
      {
        throw new ConnectionException(this.getThrowable());
      }

      List<LabelValuePair> results = this.getResults();
      return results;
    }

  }

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

  public LabelValuePair[] getSupportedAggregation(final String queryId, long milliSeconds)
  {
    String queryText = QueryFacadeUtil.getSupportedAggregationQueryText(queryId);

    List<LabelValuePair> results = new TempThread(queryText).execute(milliSeconds);

    return results.toArray(new LabelValuePair[results.size()]);
  }

  public LabelValuePair[] getTypes(long milliSeconds)
  {
    String queryText = QueryFacadeUtil.getDashboardQueryText();

    List<LabelValuePair> results = new TempThread(queryText).execute(milliSeconds);

    return results.toArray(new LabelValuePair[results.size()]);
  }

  public LabelValuePair[] getEntitySuggestions(final String text, long milliSeconds)
  {
    String queryText = QueryFacadeUtil.getEntitySuggestions(text);
    TempThread thread = new TempThread(queryText);
    List<LabelValuePair> results = thread.execute(milliSeconds);

    return results.toArray(new LabelValuePair[results.size()]);
  }
}
