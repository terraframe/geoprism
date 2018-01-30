/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Runway SDK(tm). If not, see
 * <http://www.gnu.org/licenses/>.
 */
package net.geoprism;

import java.io.File;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.business.ontology.CompositeStrategy;
import com.runwaysdk.business.ontology.OntologyStrategyBuilderIF;
import com.runwaysdk.business.ontology.OntologyStrategyFactory;
import com.runwaysdk.business.ontology.OntologyStrategyIF;
import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.io.Versioning;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXSourceParser;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.generated.system.gis.geo.UniversalAllPathsTableQuery;
import com.runwaysdk.patcher.RunwayPatcher;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategy;
import com.runwaysdk.system.metadata.ontology.GeoEntitySolrOntologyStrategy;

import net.geoprism.configuration.GeoprismConfigurationResolver;
import net.geoprism.context.PatchingContextListener;
import net.geoprism.context.ProjectDataConfiguration;
import net.geoprism.data.CachedEndpoint;
import net.geoprism.data.LocationImporter;
import net.geoprism.data.XMLEndpoint;
import net.geoprism.data.XMLLocationImporter;
import net.geoprism.data.aws.AmazonEndpoint;
import net.geoprism.data.importer.GeoprismImportPlugin;
import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierAllPathsTableQuery;
import net.geoprism.ontology.ClassifierIsARelationship;

public class GeoprismPatcher
{
  private static Logger logger = LoggerFactory.getLogger(PatchingContextListener.class);

  private File          metadataDir;

  public GeoprismPatcher(File metadataDir)
  {
    this.metadataDir = metadataDir;
  }

  public static void main(String[] args)
  {
    String metadataPath = null;

    if (args.length > 0)
    {
      metadataPath = args[0];
    }
    if (args.length > 1 && args[1] != "null")
    {
      String externalConfigDir = args[1];

      GeoprismConfigurationResolver resolver = (GeoprismConfigurationResolver) ConfigurationManager.Singleton.INSTANCE.getConfigResolver();
      resolver.setExternalConfigDir(new File(externalConfigDir));
    }
    
    File fMetadataPath = null;
    if (metadataPath == null)
    {
      metadataPath = DeployProperties.getDeployBin();
      fMetadataPath = new File(metadataPath, "metadata");
    }
    else
    {
      fMetadataPath = new File(metadataPath);
    }
    
    
    // The Runway bootstrapping cannot be done within a request
    GeoprismPatcher patcher = new GeoprismPatcher(fMetadataPath);
//    patcher.initialize();
    
    if (args.length > 2)
    {
      String[] runwayArgs = Arrays.copyOfRange(args, 2, args.length);
      
      RunwayPatcher.main(runwayArgs);
    }
    else
    {
      RunwayPatcher.main(new String[]{});
    }

    
    executeWithRequest(fMetadataPath, patcher);
  }

  @Request
  private static void executeWithRequest(File fMetadataPath, GeoprismPatcher patcher)
  {
    execute(fMetadataPath, patcher);
  }

  @Transaction
  public static void execute(File metadataDir, GeoprismPatcher patcher)
  {
    patcher.startup();
    patcher.shutdown();
  }

  public void initialize()
  {
    // Heads up : This method is not run if the patcher is run from a main method.
    
    LocalProperties.setSkipCodeGenAndCompile(true);

    RunwayPatcher.main(new String[]{});
  }

  public void startup()
  {
    SAXSourceParser.registerPlugin(new GeoprismImportPlugin());

    if (GeoprismProperties.getSolrLookup())
    {
      OntologyStrategyFactory.set(GeoEntity.CLASS, new OntologyStrategyBuilderIF()
      {
        @Override
        public OntologyStrategyIF build()
        {
          return new CompositeStrategy(DatabaseAllPathsStrategy.factory(GeoEntity.CLASS), new GeoEntitySolrOntologyStrategy());
        }
      });
    }

    this.patchMetadata();
  }

  protected String[] getModules()
  {
    return new String[] { "geoprism" };
  }

  @Transaction
  protected boolean patchMetadata()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);

    String[] modules = this.getModules();

    for (String module : modules)
    {
      File metadata = new File(metadataDir, module);

      if (metadata.exists() && metadata.isDirectory())
      {
        logger.info("Importing metadata schema files from [" + metadata.getAbsolutePath() + "].");
        Versioning.main(new String[] { metadata.getAbsolutePath() });
      }
      else
      {
        logger.error("Metadata schema files were not found at [" + metadata.getAbsolutePath() + "]! Unable to import schemas.");
      }
    }

    /*
     * Rebuild the all path tables if required
     */
    boolean initialized = Classifier.getStrategy().isInitialized();

    Classifier.getStrategy().initialize(ClassifierIsARelationship.CLASS);
    Universal.getStrategy().initialize(AllowedIn.CLASS);
    GeoEntity.getStrategy().initialize(LocatedIn.CLASS);

    if (new UniversalAllPathsTableQuery(new QueryFactory()).getCount() == 0)
    {
      Universal.getStrategy().reinitialize(AllowedIn.CLASS);
    }

    if (new GeoEntityAllPathsTableQuery(new QueryFactory()).getCount() == 0)
    {
      GeoEntity.getStrategy().reinitialize(LocatedIn.CLASS);
    }

    if (new ClassifierAllPathsTableQuery(new QueryFactory()).getCount() == 0)
    {
      Classifier.getStrategy().reinitialize(ClassifierIsARelationship.CLASS);
    }

    /*
     * Load location data
     */
    ProjectDataConfiguration configuration = new ProjectDataConfiguration();

    XMLEndpoint endpoint = this.getEndpoint();

    LocationImporter importer = new XMLLocationImporter(endpoint);
    importer.loadProjectData(configuration);

    return initialized;
  }

  public void shutdown()
  {

  }

  private XMLEndpoint getEndpoint()
  {
    String cacheDirectory = System.getProperty("endpoint.cache");

    if (cacheDirectory != null)
    {
      return new CachedEndpoint(new AmazonEndpoint(), new File(cacheDirectory));
    }

    return new AmazonEndpoint();
    // return new LocalEndpoint(new File("/home/terraframe/Documents/geoprism/DSEDP/cache"));
  }
}
