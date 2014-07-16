package com.runwaysdk.geodashboard;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.ontology.Classifier;
import com.runwaysdk.geodashboard.ontology.ClassifierIsARelationship;
import com.runwaysdk.gis.StrategyInitializer;

public class StrategyContextListener implements ServerContextListener, Reloadable
{
  @Override
  public void startup()
  {
    StrategyInitializer.startUp();
    Classifier.getStrategy().initialize(ClassifierIsARelationship.CLASS);
  }

  @Override
  public void shutdown()
  {
    StrategyInitializer.shutDown();
    Classifier.getStrategy().shutdown();
  }
}
