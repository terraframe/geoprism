package com.runwaysdk.gis.web.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.runwaysdk.gis.StrategyInitializer;

public class GISContextListener implements ServletContextListener
{
  @Override
  public void contextInitialized(ServletContextEvent arg0) {
    StrategyInitializer.startUp();
  }
  
  @Override
  public void contextDestroyed(ServletContextEvent arg0) {
    StrategyInitializer.tearDown();
  }
}
