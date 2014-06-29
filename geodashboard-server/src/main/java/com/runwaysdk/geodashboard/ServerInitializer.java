package com.runwaysdk.geodashboard;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.Reflections;

import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverInitializer;
import com.runwaysdk.geodashboard.gis.persist.DashboardMap;
import com.runwaysdk.gis.StrategyInitializer;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.scheduler.SchedulerManager;

public class ServerInitializer implements Reloadable
{

  private static final Log log = LogFactory.getLog(ServerInitializer.class);

  @Request
  public static void initialize()
  {
    StrategyInitializer.startUp();
    log.debug("COMLPETE: StrategyInitializer.startUp()");

    SchedulerManager.start();
    log.debug("COMLPETE: SchedulerManager.start();");

    SessionEntry.deleteAll();
    log.debug("COMLPETE: SessionEntry.deleteAll();");

    GeoserverInitializer.setup();
    log.debug("COMLPETE: GeoserverInitializer.setup();");

    DashboardMap.cleanup();
    log.debug("COMLPETE: DashboardMap.cleanup();");

    Reflections reflections = new Reflections("com", LoaderDecorator.instance());

    Set<Class<? extends ServerContextListener>> subTypes = reflections.getSubTypesOf(ServerContextListener.class);

    for (Class<? extends ServerContextListener> subType : subTypes)
    {
      try
      {
        ServerContextListener listener = subType.newInstance();
        listener.startup();
      }
      catch (Exception e)
      {
        // TODO change this

        e.printStackTrace();
      }
    }
  }

  @Request
  public static void destroy()
  {
    StrategyInitializer.shutDown();

    SchedulerManager.shutdown();

    SessionEntry.deleteAll();

    Reflections reflections = new Reflections("com", LoaderDecorator.instance());

    Set<Class<? extends ServerContextListener>> subTypes = reflections.getSubTypesOf(ServerContextListener.class);

    for (Class<? extends ServerContextListener> subType : subTypes)
    {
      try
      {
        ServerContextListener listener = subType.newInstance();
        listener.shutdown();
      }
      catch (Exception e)
      {
        // TODO change this
        e.printStackTrace();
      }
    }
  }
}
