package net.geoprism.data;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.DelegatingClassLoader;
import com.runwaysdk.generation.loader.LoaderDecorator;

public class GeoprismDatasetExporterService
{
  public static Iterator<GeoprismDatasetExporterIF> getAllExporters()
  {
    ServiceLoader<GeoprismDatasetExporterIF> loader = ServiceLoader.load(GeoprismDatasetExporterIF.class, ( (DelegatingClassLoader) LoaderDecorator.instance() ));

    try
    {
      Iterator<GeoprismDatasetExporterIF> it = loader.iterator();

      return it;
    }
    catch (ServiceConfigurationError serviceError)
    {
      throw new ProgrammingErrorException(serviceError);
    }
  }
}
