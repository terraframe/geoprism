package com.runwaysdk.geodashboard;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;

import java.net.MalformedURLException;
import java.util.ResourceBundle;

import org.apache.commons.logging.LogFactory;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.generation.loader.Reloadable;

public class GeoserverProperties implements Reloadable
{
  private static final String          GEOSERVER_PROPERTIES = "geoserver";

  private ResourceBundle               bundle;

  private static GeoServerRESTPublisher publisher;

  private static GeoServerRESTReader    reader;

  private GeoserverProperties()
  {
    bundle = ResourceBundle.getBundle(GEOSERVER_PROPERTIES, CommonProperties.getDefaultLocale(),
        BusinessDTO.class.getClassLoader());
  }

  private static class Singleton implements Reloadable
  {
    private static GeoserverProperties INSTANCE = new GeoserverProperties();
  }

  private static ResourceBundle getBundle()
  {
    return Singleton.INSTANCE.bundle;
  }

  public static String getWorkspace()
  {
    return getBundle().getString("geoserver.workspace");
  }

  public static String getStore()
  {
    return getBundle().getString("geoserver.store");
  }

  public static String getAdminUser()
  {
    return getBundle().getString("admin.user");
  }

  public static String getAdminPassword()
  {
    return getBundle().getString("admin.password");
  }

  public static String getRemotePath()
  {
    return getBundle().getString("geoserver.remote.path");
  }

  public static String getLocalPath()
  {
    return getBundle().getString("geoserver.local.path");
  }

  public static String getGeoserverSLDDir()
  {
    return getBundle().getString("geoserver.sld.dir");
  }

  public static String getGeoserverGWCDir()
  {
    return getBundle().getString("geoserver.gwc.dir");
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
    if(reader == null)
    {
      try
      {
        reader = new GeoServerRESTReader(getLocalPath(), getAdminUser(), getAdminPassword());
      }
      catch (MalformedURLException e)
      {
        // We don't know if this is being called via client or server code, so log
        // the error and throw an NPE to the calling code for its error handling mechanism.
        String msg = "The "+GeoserverProperties.class.getSimpleName()+"."+GeoServerRESTReader.class.getSimpleName()+" is null.";
        LogFactory.getLog(GeoserverProperties.class.getClass()).error(msg, e);
        
        throw new NullPointerException(msg);
      }
    }
    
    return reader;
  }
}