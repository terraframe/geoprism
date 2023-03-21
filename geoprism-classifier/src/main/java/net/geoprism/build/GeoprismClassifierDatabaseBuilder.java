package net.geoprism.build;

import com.runwaysdk.query.QueryFactory;

import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierIsARelationship;
import net.geoprism.ontology.ClassifierIsARelationshipAllPathsTableQuery;

public class GeoprismClassifierDatabaseBuilder extends GeoprismDatabaseBuilder
{
  @Override
  public void configureStrategies()
  {
    super.configureStrategies();
    
    Classifier.getStrategy().initialize(ClassifierIsARelationship.CLASS);

    if (new ClassifierIsARelationshipAllPathsTableQuery(new QueryFactory()).getCount() == 0)
    {
      Classifier.getStrategy().reinitialize(ClassifierIsARelationship.CLASS);
    }
  }
}
