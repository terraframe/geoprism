package net.geoprism.dhis2;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.DelegatingClassLoader;
import com.runwaysdk.generation.loader.LoaderDecorator;

public class DHIS2IdMapping extends DHIS2IdMappingBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1512057276;
  
  public DHIS2IdMapping()
  {
    super();
  }
  
  /**
   * MdMethod
   * 
   * @return JSON for displaying attributes pulled from DHIS2.
   */
  public static java.lang.String findAttributes()
  {
    return getDhis2Plugin().findAttributes();
  }
  
  /**
   * MdMethod
   * 
   * @return JSON for displaying programs pulled from DHIS2.
   */
  public static java.lang.String findPrograms()
  {
    return getDhis2Plugin().findPrograms();
  }
  
  /**
   * MdMethod
   * 
   * @return JSON for displaying Tracked entities pulled from DHIS2.
   */
  public static java.lang.String findTrackedEntityIds()
  {
    return getDhis2Plugin().findTrackedEntities();
  }
  
  public static DHIS2PluginIF getDhis2Plugin()
  {
    ServiceLoader<DHIS2PluginIF> loader = ServiceLoader.load(DHIS2PluginIF.class, ( (DelegatingClassLoader) LoaderDecorator.instance() ));

    try
    {
      Iterator<DHIS2PluginIF> it = loader.iterator();

      if (it.hasNext())
      {
        return it.next();
      }
      else
      {
        return null;
      }
    }
    catch (ServiceConfigurationError serviceError)
    {
      throw new ProgrammingErrorException(serviceError);
    }
  }
}
