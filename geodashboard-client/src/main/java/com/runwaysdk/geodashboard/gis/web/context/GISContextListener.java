package com.runwaysdk.geodashboard.gis.web.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.runwaysdk.generation.loader.LoaderDecorator;

public class GISContextListener implements ServletContextListener
{
  @Override
  public void contextInitialized(ServletContextEvent arg0) {
    try
    {
      LoaderDecorator.load("com.runwaysdk.gis.StrategyInitializer").getMethod("startUp").invoke(null);
      LoaderDecorator.load("com.runwaysdk.system.scheduler.SchedulerManager").getMethod("start").invoke(null);
      LoaderDecorator.load("com.runwaysdk.geodashboard.SessionEntryManager").getMethod("initialize").invoke(null);
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
      LoaderDecorator.load("com.runwaysdk.gis.StrategyInitializer").getMethod("tearDown").invoke(null);
      LoaderDecorator.load("com.runwaysdk.system.scheduler.SchedulerManager").getMethod("shutdown").invoke(null);
      LoaderDecorator.load("com.runwaysdk.geodashboard.SessionEntryManager").getMethod("destroy").invoke(null);
    }
    catch(Throwable t)
    {
      throw new RuntimeException(t);
    }
  }
}
