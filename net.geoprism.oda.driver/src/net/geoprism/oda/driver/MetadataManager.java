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
package net.geoprism.oda.driver;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

public class MetadataManager
{
  private static MetadataManager          instance;

  private Map<String, IResultSetMetaData> cache;

  public MetadataManager()
  {
    this.cache = new HashMap<String, IResultSetMetaData>();
  }

  public Map<String, IResultSetMetaData> getCache()
  {
    return cache;
  }

  private static synchronized MetadataManager getInstance()
  {
    if (instance == null)
    {
      instance = new MetadataManager();
    }

    return instance;
  }

  public static IResultSetMetaData getMetadata(String queryId)
  {
    return getInstance().getCache().get(queryId);
  }

  public static boolean hasMetadata(String queryId)
  {
    return getInstance().getCache().containsKey(queryId);
  }

  public static IResultSetMetaData putMetadata(String queryId, IResultSetMetaData metadata)
  {
    return getInstance().getCache().put(queryId, metadata);
  }

  public static String getKey(String queryText) throws OdaException
  {
    QueryFacade facade = new QueryFacade();

    String queryId = facade.getQueryId(queryText);
    String aggregation = new QueryFacade().getAggregation(queryText);

    if (aggregation != null)
    {
      return queryId + "-" + aggregation;
    }

    return queryId;
  }
}
