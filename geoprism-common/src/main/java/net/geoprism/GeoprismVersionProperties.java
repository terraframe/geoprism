/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
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
