package com.runwaysdk.geodashboard;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverInitializer;
import com.runwaysdk.geodashboard.ontology.Classifier;
import com.runwaysdk.geodashboard.ontology.ClassifierIsARelationship;
import com.runwaysdk.gis.StrategyInitializer;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.scheduler.SchedulerManager;

public class ServerInitializer implements Reloadable
{

  private static final Log log = LogFactory.getLog(ServerInitializer.class);

  @Request
  public static void initialize()
  {
    doInitialize();
  }
  @Transaction
  public static void doInitialize() {
    StrategyInitializer.startUp();
    Classifier.getStrategy().initialize(ClassifierIsARelationship.CLASS);
    log.debug("COMLPETE: StrategyInitializer.startUp()");
    
    SchedulerManager.start();
    log.debug("COMLPETE: SchedulerManager.start();");

    SessionEntry.deleteAll();
    log.debug("COMLPETE: SessionEntry.deleteAll();");

    GeoserverInitializer.setup();
    log.debug("COMLPETE: GeoserverInitializer.setup();");
  }

  @Request
  public static void destroy()
  {
    doDestroy();
  }
  @Transaction
  public static void doDestroy() {
    StrategyInitializer.shutDown();
    Classifier.getStrategy().shutdown();
    
    SchedulerManager.shutdown();
    
    SessionEntry.deleteAll();
  }
}
