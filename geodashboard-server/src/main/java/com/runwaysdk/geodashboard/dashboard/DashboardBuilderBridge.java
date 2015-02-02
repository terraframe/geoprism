package com.runwaysdk.geodashboard.dashboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.DelegatingClassLoader;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.Reloadable;

public class DashboardBuilderBridge implements Reloadable
{
  /**
   * Retrieve all implementations of ReportProviderIF.
   */
  public static List<DashboardBuilderIF> getDashboardBuilders()
  {
    List<DashboardBuilderIF> builders = new ArrayList<DashboardBuilderIF>();

    ServiceLoader<DashboardBuilderIF> loader = ServiceLoader.load(DashboardBuilderIF.class, ( (DelegatingClassLoader) LoaderDecorator.instance() ));

    try
    {
      Iterator<DashboardBuilderIF> it = loader.iterator();

      while (it.hasNext())
      {
        builders.add(it.next());
      }
    }
    catch (ServiceConfigurationError serviceError)
    {
      throw new ProgrammingErrorException(serviceError);
    }

    return builders;
  }
}
