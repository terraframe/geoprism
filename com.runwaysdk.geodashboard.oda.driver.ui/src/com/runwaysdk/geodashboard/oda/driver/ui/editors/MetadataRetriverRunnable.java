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
