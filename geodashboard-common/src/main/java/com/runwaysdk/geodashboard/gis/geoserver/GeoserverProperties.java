package com.runwaysdk.geodashboard.gis.geoserver;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;

import java.net.MalformedURLException;

import org.apache.commons.logging.LogFactory;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationReaderIF;
import com.runwaysdk.configuration.RunwayConfigurationException;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.GDBConfigurationManager.GDBConfigGroup;

public class GeoserverProperties implements Reloadable
{
  private static final String          GEOSERVER_PROPERTIES = "geoserver.properties";
  
  private ConfigurationReaderIF reader;

  private static GeoServerRESTPublisher publisher;

  private static GeoServerRESTReader    restReader;
  
  public static final String SLD_EXTENSION = ".sld";
  
  private GeoserverProperties()
  {
    reader = ConfigurationManager.getReader(GDBConfigGroup.SERVER, GEOSERVER_PROPERTIES);
  }
  
  private static class Singleton implements Reloadable
  {
    private static GeoserverProperties INSTANCE = new GeoserverProperties();
  }

  private static ConfigurationReaderIF getBundle()
  {
    return Singleton.INSTANCE.reader;
  }
  
  public static Integer getDecimalLength()
  {
    return Integer.valueOf(getBundle().getString("geoserver.decimal.length"));
  }
  
  public static Integer getDecimalPrecision()
  {
    return Integer.valueOf(getBundle().getString("geoserver.decimal.precision"));
  }

  public static String getWorkspace()
  {
    return Singleton.INSTANCE.reader.getString("geoserver.workspace");
  }
  
  public static Integer getSessionMapLimit()
  {
    return Integer.valueOf(Singleton.INSTANCE.reader.getString("geoserver.session.map.limit"));
  }

  public static Integer getSavedMapLimit()
  {
    return Integer.valueOf(Singleton.INSTANCE.reader.getString("geoserver.saved.map.limit"));
  }

  public static String getStore()
  {
    return Singleton.INSTANCE.reader.getString("geoserver.store");
  }

  public static String getAdminUser()
  {
    return Singleton.INSTANCE.reader.getString("admin.user");
  }

  public static String getAdminPassword()
  {
    return Singleton.INSTANCE.reader.getString("admin.password");
  }

  public static String getRemotePath()
  {
    return Singleton.INSTANCE.reader.getString("geoserver.remote.path");
  }

  public static String getLocalPath()
  {
    return Singleton.INSTANCE.reader.getString("geoserver.local.path");
  }

  public static String getGeoserverSLDDir()
  {
    return Singleton.INSTANCE.reader.getString("geoserver.sld.dir");
  }

  public static String getGeoserverGWCDir()
  {
    return Singleton.INSTANCE.reader.getString("geoserver.gwc.dir");
  }

  /**
   * Returns the Geoserver REST publisher.
   * 
   * @return
   */
  public static synchronized GeoServerRESTPublisher getPublisher()
  {
    if(publisher == null)
    {
      publisher = new GeoServerRESTPublisher(getLocalPath(), getAdminUser(), getAdminPassword());
    }
    
    return publisher;
  }

  /**
   * Returns the Geoserver REST reader.
   */
  public static synchronized GeoServerRESTReader getReader()
  {
    if(restReader == null)
    {
      try
      {
        restReader = new GeoServerRESTReader(getLocalPath(), getAdminUser(), getAdminPassword());
      }
      catch (MalformedURLException e)
      {
        // We don't know if this is being called via client or server code, so log
        // the error and throw an exception to the calling code for its error handling mechanism.
        String msg = "The "+GeoserverProperties.class.getSimpleName()+"."+GeoServerRESTReader.class.getSimpleName()+" is null.";
        LogFactory.getLog(GeoserverProperties.class.getClass()).error(msg, e);
        
        throw new RunwayConfigurationException(msg);
      }
    }
    
    return restReader;
  }
}
