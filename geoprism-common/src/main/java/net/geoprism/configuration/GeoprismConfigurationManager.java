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
package net.geoprism.configuration;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroupIF;
import com.runwaysdk.configuration.RunwayConfigurationException;

public class GeoprismConfigurationManager
{
  private static Logger logger = LoggerFactory.getLogger(GeoprismConfigurationManager.class);
  
  private static File externalConfigDir;

  public static class Singleton
  {
    public static final GeoprismConfigurationManager INSTANCE = new GeoprismConfigurationManager();
  }
  
  public static enum GeoprismConfigGroup implements ConfigGroupIF
  {
    CLIENT("geoprism/", "client"), COMMON("geoprism/", "common"), SERVER("geoprism/", "server"), ROOT("", "root");

    private String path;

    private String identifier;

    GeoprismConfigGroup(String path, String identifier)
    {
      this.path = path;
      this.identifier = identifier;
    }

    public String getPath()
    {
      return this.path;
    }

    public String getIdentifier()
    {
      return identifier;
    }
  }
  
  public GeoprismConfigurationManager()
  {
    String sConfigDir = System.getProperty("geoprism.configuration.dir");
    
    if (sConfigDir != null)
    {
      externalConfigDir = new File(sConfigDir);
      
      // No funny business!
      if (!externalConfigDir.exists() || !externalConfigDir.isDirectory())
      {
        logger.error("geoprism.configuration.dir was specified as [" + sConfigDir + "] but that directory does not exist.");
        externalConfigDir = null;
      }
    }
  }
  
  public InputStream iGetResourceAsStream(ConfigGroupIF location, String name, boolean silentFail)
  {
    if (externalConfigDir != null)
    {
      File fExternal = new File(externalConfigDir, location.getPath() + name);
      
      if (fExternal.exists())
      {
        try
        {
          return new FileInputStream(fExternal);
        }
        catch (FileNotFoundException e)
        {
          logger.error("Unexpected error.", e);
        }
      }
    }
    
    try
    {
      return ConfigurationManager.getResourceAsStream(location, name);
    }
    catch (RunwayConfigurationException e)
    {
      if (silentFail)
      {
        return null;
      }
      else
      {
        throw e;
      }
    }
  }
  
  public static InputStream getResourceAsStream(ConfigGroupIF location, String name, boolean silentFail)
  {
    return Singleton.INSTANCE.iGetResourceAsStream(location, name, silentFail);
  }
}
