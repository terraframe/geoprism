package com.runwaysdk.geodashboard.oda.driver.ui.editors;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.runwaysdk.geodashboard.oda.driver.ui.GeodashboardPlugin;
import com.runwaysdk.geodashboard.oda.driver.ui.util.Constants;

public class MetadataRetriverRunnable implements IRunnableWithProgress
{
  private IConnection        conn;

  private String             queryText;

  private IResultSetMetaData queryMetadata;

  private IParameterMetaData parameterMetadata;

  private Throwable          throwable;

  public MetadataRetriverRunnable(IConnection conn, String queryText)
  {
    super();
    this.conn = conn;
    this.queryText = queryText;
  }

  @Override
  public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
  {
    try
    {
      monitor.beginTask(GeodashboardPlugin.getResourceString("retrieving.metadata"), IProgressMonitor.UNKNOWN);

      IQuery query = conn.newQuery(null);
      query.prepare(queryText);
      query.setProperty(Constants.METADATA_QUERY, "true");

      query.executeQuery();

      this.queryMetadata = query.getMetaData();
      this.parameterMetadata = query.getParameterMetaData();
    }
    catch (Throwable t)
    {
      this.throwable = t;
    }
  }

  public IResultSetMetaData getMetadata()
  {
    return queryMetadata;
  }

  public Throwable getThrowable()
  {
    return throwable;
  }

  public IParameterMetaData getParameterMetadata()
  {
    return this.parameterMetadata;
  }
}
