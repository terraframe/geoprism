package com.runwaysdk.patcher.domain;


import org.apache.log4j.Logger;

import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategy;
import com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategyQuery;

import net.geoprism.ontology.Classifier;

public class GeoprismOntologyStrategyDataPatcher
{
  private static Logger logger = Logger.getLogger(GeoprismOntologyStrategyDataPatcher.class);
  
  public static void main(String[] args)
  {
//    updateUnsetStrategy(GeoEntity.CLASS);
//    updateUnsetStrategy(Universal.CLASS);
    updateUnsetStrategy(Classifier.CLASS);
  }
  
  private static void updateUnsetStrategy(String class1)
  {
    DatabaseAllPathsStrategy strategy = getUnsetStrategy();
    
    if (strategy != null)
    {
      strategy.lock();
      strategy.setTermClass(class1);
      strategy.apply();
    }
  }

  private static DatabaseAllPathsStrategy getUnsetStrategy()
  {
    DatabaseAllPathsStrategyQuery query = new DatabaseAllPathsStrategyQuery(new QueryFactory());
    query.WHERE(query.getTermClass().EQ((String) null));

    OIterator<? extends DatabaseAllPathsStrategy> it = query.getIterator();
 
    try
    {
      if (it.hasNext())
      {
        return it.next();
      }

//      throw new ProgrammingErrorException("Unable to find an unset database ontology strategy");
      logger.info("Unable to find unset database ontology strategy");
      return null;
    }
    finally
    {
      it.close();
    }
  }
}
