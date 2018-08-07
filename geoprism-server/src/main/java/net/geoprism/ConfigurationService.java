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
package net.geoprism;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import com.runwaysdk.dataaccess.ProgrammingErrorException;


public class ConfigurationService 
{
  /**
   * Retrieve all implementations of ConfigurationIF.
   */
  public static List<ConfigurationIF> getConfigurations()
  {
    List<ConfigurationIF> configurations = new ArrayList<ConfigurationIF>();

    ServiceLoader<ConfigurationIF> loader = ServiceLoader.load(ConfigurationIF.class, Thread.currentThread().getContextClassLoader());

    try
    {
      Iterator<ConfigurationIF> it = loader.iterator();

      while (it.hasNext())
      {
        configurations.add(it.next());
      }

      if (configurations.size() == 0)
      {
        configurations.add(new DefaultConfiguration());
      }
    }
    catch (ServiceConfigurationError serviceError)
    {
      throw new ProgrammingErrorException(serviceError);
    }

    return configurations;
  }
}
