package com.runwaysdk.geodashboard.oda.driver;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;

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
}
