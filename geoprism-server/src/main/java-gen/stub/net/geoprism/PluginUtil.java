/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.constants.VaultInfo;
import com.runwaysdk.constants.VaultProperties;

import net.geoprism.build.GeoprismDatabaseBuilder;
import net.geoprism.build.GeoprismDatabaseBuilderIF;
import net.geoprism.dhis2.DHIS2PluginIF;
import net.geoprism.gis.geoserver.GeoserverInitializerIF;

public class PluginUtil extends PluginUtilBase 
{
  private static final Logger logger = LoggerFactory.getLogger(PluginUtil.class);
  
  private static final long serialVersionUID = 682516980;
  
  public PluginUtil()
  {
    super();
  }
  
  public static java.lang.Boolean isDHIS2Enabled()
  {
    ServiceLoader<DHIS2PluginIF> loader = ServiceLoader.load(DHIS2PluginIF.class, Thread.currentThread().getContextClassLoader());

    try
    {
      Iterator<DHIS2PluginIF> it = loader.iterator();

      return it.hasNext();
    }
    catch (ServiceConfigurationError serviceError)
    {
      return false;
    }
  }
  
  public static GeoprismDatabaseBuilderIF getDatabaseBuilder()
  {
    logger.debug("Default vault path : " + VaultProperties.getPath(VaultInfo.DEFAULT));
    logger.debug("geoprism file storage path : " + GeoprismProperties.getGeoprismFileStorage().getAbsolutePath());
    
    ServiceLoader<GeoprismDatabaseBuilderIF> loader = ServiceLoader.load(GeoprismDatabaseBuilderIF.class, Thread.currentThread().getContextClassLoader());

    GeoprismDatabaseBuilderIF patcher;
    
    try
    {
      Iterator<GeoprismDatabaseBuilderIF> it = loader.iterator();

      patcher = it.next();
    }
    catch (ServiceConfigurationError | NoSuchElementException ex)
    {
      patcher = new GeoprismDatabaseBuilder();
    }
    
    logger.debug("Database builder resolved to " + patcher.getClass().getName());
    
    return patcher;
  }
  
  public static Collection<GeoserverInitializerIF> getGeoserverInitializers()
  {
    ServiceLoader<GeoserverInitializerIF> loader = ServiceLoader.load(GeoserverInitializerIF.class, Thread.currentThread().getContextClassLoader());

    ArrayList<GeoserverInitializerIF> initializers = new ArrayList<GeoserverInitializerIF>();
    
    try
    {
      Iterator<GeoserverInitializerIF> it = loader.iterator();

      GeoserverInitializerIF initializer = it.next();
      
      initializers.add(initializer);
    }
    catch (ServiceConfigurationError | NoSuchElementException ex)
    {
      
    }
    
    return initializers;
  }  
}
