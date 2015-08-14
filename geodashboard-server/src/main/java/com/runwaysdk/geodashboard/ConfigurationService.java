package com.runwaysdk.geodashboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.DelegatingClassLoader;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.Reloadable;

public class ConfigurationService implements Reloadable
{
  /**
   * Retrieve all implementations of ConfigurationIF.
   */
  public static List<ConfigurationIF> getConfigurations()
  {
    List<ConfigurationIF> configurations = new ArrayList<ConfigurationIF>();

    ServiceLoader<ConfigurationIF> loader = ServiceLoader.load(ConfigurationIF.class, ( (DelegatingClassLoader) LoaderDecorator.instance() ));

    try
    {
      Iterator<ConfigurationIF> it = loader.iterator();

      while (it.hasNext())
      {
        configurations.add(it.next());
      }
    }
    catch (ServiceConfigurationError serviceError)
    {
      throw new ProgrammingErrorException(serviceError);
    }

    return configurations;
  }
}
