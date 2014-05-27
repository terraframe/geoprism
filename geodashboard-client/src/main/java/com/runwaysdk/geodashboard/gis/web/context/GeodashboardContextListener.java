package com.runwaysdk.geodashboard.gis.web.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.runwaysdk.generation.loader.LoaderDecorator;

public class GeodashboardContextListener implements ServletContextListener
{
  @Override
  public void contextInitialized(ServletContextEvent arg0) {
    try
    {
      LoaderDecorator.load("com.runwaysdk.geodashboard.ServerInitializer").getMethod("initialize").invoke(null);
    }
    catch(Throwable t)
    {
      throw new RuntimeException(t);
    }
  }
  
  @Override
  public void contextDestroyed(ServletContextEvent arg0) {
    try
    {
      LoaderDecorator.load("com.runwaysdk.geodashboard.ServerInitializer").getMethod("destroy").invoke(null);
    }
    catch(Throwable t)
    {
      throw new RuntimeException(t);
    }
  }
}
