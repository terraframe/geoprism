package com.runwaysdk.geodashboard;

import com.runwaysdk.generation.loader.Reloadable;

public class StrategyContextListener implements ServerContextListener, Reloadable
{
  @Override
  public void startup()
  {
//    StrategyInitializer.startUp();
//    Classifier.getStrategy().initialize(ClassifierIsARelationship.CLASS);
  }

  @Override
  public void shutdown()
  {
//    StrategyInitializer.shutDown();
//    Classifier.getStrategy().shutdown();
  }
}
