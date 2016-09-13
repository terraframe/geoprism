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


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.configuration.CommonsConfigurationResolver;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroupIF;
import com.runwaysdk.configuration.RunwayConfigurationException;

/**
 * Using IOC, extends Runway's configuration resolving mechanism to allow support for resolving configuration files external to the deployable artifact.
 * 
 * @author Richard Rowlands
 */
public class GeoprismConfigurationResolver extends CommonsConfigurationResolver
{
  private static Logger logger = LoggerFactory.getLogger(GeoprismConfigurationResolver.class);
  
  private static File externalConfigDir;

  public GeoprismConfigurationResolver()
  {
    String sConfigDir = System.getProperty("geoprism.config.dir");
    
    if (sConfigDir != null)
    {
      externalConfigDir = new File(sConfigDir);
      logger.info("Geoprism external config set to [" + sConfigDir + "].");
      
      // No funny business!
      if (!externalConfigDir.exists() || !externalConfigDir.isDirectory())
      {
        logger.error("geoprism.config.dir was specified as [" + sConfigDir + "] but that directory does not exist.");
        externalConfigDir = null;
      }
    }
    
    if (externalConfigDir == null)
    {
      logger.info("Geoprism external config dir not set. Using default resource loader strategy.");
    }
  }

  @Override
  public URL getResource(ConfigGroupIF location, String name)
  {
    URL urlBase = null;
    RunwayConfigurationException ex = null;
    try
    {
      urlBase = super.getResource(location, name);
    }
    catch (RunwayConfigurationException e)
    {
      ex = e;
    }
    
    if (externalConfigDir != null)
    {
      File fOverride = new File(externalConfigDir, location.getPath() + name);
      
      if (fOverride.exists())
      {
        try
        {
          if (urlBase == null)
          {
            return fOverride.toURI().toURL();
          }
          
          ByteArrayOutputStream os = new ByteArrayOutputStream();
          
          MergeUtility merger = new MergeUtility();
          merger.mergeStreams(urlBase.openStream(), new FileInputStream(fOverride), os, FilenameUtils.getExtension(fOverride.getAbsolutePath()));
          
          InputStream is = new ByteArrayInputStream(os.toByteArray());
          
          return new URL(null, "inputstream://" + name, new InputStreamURLStreamHandler(is));
        }
        catch (IOException e)
        {
          logger.error("Unexpected error.", e);
        }
      }
    }
    
    if (urlBase == null && ex != null)
    {
      throw ex;
    }
    
    return urlBase;
  }

  private class InputStreamURLStreamHandler extends URLStreamHandler {
    InputStream is;
    
    public InputStreamURLStreamHandler(InputStream is)
    {
      this.is = is;
    }
    
    @Override
    protected URLConnection openConnection(URL u) throws IOException
    {
      return new InputStreamURLConnection(is, u);
    }
  }

  private class InputStreamURLConnection extends URLConnection
  {
    InputStream is;
    
    public InputStreamURLConnection(InputStream is, URL url)
    {
      super(url);
      
      this.is = is;
    }

    @Override
    public void connect() throws IOException
    {
      
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
      return is;
    }
  }
}
