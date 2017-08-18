package net.geoprism;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import com.runwaysdk.generation.loader.DelegatingClassLoader;
import com.runwaysdk.generation.loader.LoaderDecorator;

import net.geoprism.dhis2.DHIS2PluginIF;

public class PluginUtil extends PluginUtilBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 682516980;
  
  public PluginUtil()
  {
    super();
  }
  
  public static java.lang.Boolean isDHIS2Enabled()
  {
    ServiceLoader<DHIS2PluginIF> loader = ServiceLoader.load(DHIS2PluginIF.class, ( (DelegatingClassLoader) LoaderDecorator.instance() ));

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
  
}
