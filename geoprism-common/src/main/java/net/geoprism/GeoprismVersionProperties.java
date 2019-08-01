package net.geoprism;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeoprismVersionProperties
{
  private Logger logger = LoggerFactory.getLogger(GeoprismVersionProperties.class);
  
  private Properties props;
  
  private static GeoprismVersionProperties instance;
  
  public GeoprismVersionProperties()
  {
    this.props = new Properties();
    try
    {
      this.props.load(GeoprismVersionProperties.class.getResourceAsStream("/geoprism-version.properties"));
    }
    catch (IOException e)
    {
      logger.error("Error occurred while reading geoprism-version.properties", e);
    }
  }
  
  public static synchronized GeoprismVersionProperties getInstance()
  {
    if (instance == null)
    {
      instance = new GeoprismVersionProperties();
    }
    
    return instance;
  }
  
  public String getVersion()
  {
    return this.props.getProperty("version");
  }
}
