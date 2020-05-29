/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.build;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.build.DatabaseBootstrapper;
import com.runwaysdk.build.DatabaseBuilder;
import com.runwaysdk.business.ontology.CompositeStrategy;
import com.runwaysdk.business.ontology.OntologyStrategyBuilderIF;
import com.runwaysdk.business.ontology.OntologyStrategyFactory;
import com.runwaysdk.business.ontology.OntologyStrategyIF;
import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.dataDefinition.GISImportPlugin;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXSourceParser;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generated.system.gis.geo.AllowedInAllPathsTableQuery;
import com.runwaysdk.generated.system.gis.geo.LocatedInAllPathsTableQuery;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.resource.ClasspathResource;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategy;
import com.runwaysdk.system.metadata.ontology.GeoEntitySolrOntologyStrategy;

import net.geoprism.GeoprismProperties;
import net.geoprism.PluginUtil;
import net.geoprism.context.PatchingContextListener;
import net.geoprism.context.ProjectDataConfiguration;
import net.geoprism.data.CachedEndpoint;
import net.geoprism.data.LocationImporter;
import net.geoprism.data.XMLEndpoint;
import net.geoprism.data.XMLLocationImporter;
import net.geoprism.data.aws.AmazonEndpoint;
import net.geoprism.data.importer.GeoprismImportPlugin;
import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierIsARelationship;
import net.geoprism.ontology.ClassifierIsARelationshipAllPathsTableQuery;

public class GeoprismDatabaseBuilder implements GeoprismDatabaseBuilderIF
{
  private static Logger logger = LoggerFactory.getLogger(PatchingContextListener.class);

  private File          metadataDir;
  
  private String[]      runwayArgs;
  
  static
  {
    try
    {
      checkDuplicateClasspathResources();
    }
    catch (Throwable t)
    {
      t.printStackTrace();
      throw t;
    }
  }

  public GeoprismDatabaseBuilder()
  {
    
  }
  
  public void initialize(File metadataDir)
  {
    this.metadataDir = metadataDir;
    this.setRunwayArgs(new String[]{});
  }
  
  public void initialize(String[] cliArgs)
  {
    this.processCLIArgs(cliArgs);
  }

  public static void main(String[] args)
  {
    ServerProperties.setAllowModificationOfMdAttribute(true);

    GeoprismDatabaseBuilderIF builder = PluginUtil.getDatabaseBuilder();
    builder.initialize(args);
    builder.run();
  }
  
  protected void processCLIArgs(String[] args)
  {
    CommandLineParser parser = new DefaultParser();
    
    Options options = DatabaseBuilder.buildCliOptions();
    
    options.addOption(Option.builder("metadataDir").hasArg().argName("metadataDir").longOpt("metadataDir").desc("The path to the location of metadata schema files. Optional").optionalArg(true).build());
    
    try
    {
      CommandLine line = parser.parse( options, args );
      
      String metadataDir = line.getOptionValue("metadataDir");
      
      if (metadataDir == null)
      {
        metadataDir = DeployProperties.getDeployBin();
        this.metadataDir = new File(metadataDir, "metadata");
      }
      else
      {
        this.metadataDir = new File(metadataDir);
      }
      
      this.setRunwayArgs(args);
    }
    catch (ParseException e)
    {
      throw new RuntimeException(e);
    }
  }
  
//  private String[] getRunwayBootstrapArgs(Boolean isBootstrap)
//  {
//    ArrayList<String> clean = new ArrayList<String>();
//    
//    for (String rwArg : this.runwayArgs)
//    {
//      if (!rwArg.contains("mode") && (!rwArg.contains("clean") || isBootstrap))
//      {
//        clean.add(rwArg);
//      }
//    }
//    
//    if (isBootstrap)
//    {
//      clean.add("--mode=" + RunwayPatcher.MODE_BOOTSTRAP);
//    }
//    else
//    {
//      clean.add("--mode=" + RunwayPatcher.MODE_STANDARD);
//    }
//    
//    return clean.toArray(new String[clean.size()]);
//  }
  
  public void setRunwayArgs(String[] rwArgs)
  {
    ArrayList<String> clean = new ArrayList<String>();
    
    for (String rwArg : rwArgs)
    {
      if (!(rwArg.contains("metadataDir") || rwArg.contains("externalConfigDir") || rwArg.contains("modules")))
      {
        clean.add(rwArg);
      }
    }
    
    if (!clean.contains("plugins"))
    {
      clean.add("--plugins=" + GISImportPlugin.class.getName() + "," + GeoprismImportPlugin.class.getName());
    }
    
    if (!clean.contains("rootUser") && !clean.contains("rootPass") && !clean.contains("patch"))
    {
      clean.add("--patch");
    }
      
    
    this.runwayArgs = clean.toArray(new String[clean.size()]);
  }

  @Request
  protected void runWithRequest()
  {
    runWithTransaction();
  }

  @Transaction
  public void runWithTransaction()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);
    
    this.configureStrategies();

    /*
     * Rebuild the all path tables if required
     */
    Classifier.getStrategy().initialize(ClassifierIsARelationship.CLASS);
    Universal.getStrategy().initialize(AllowedIn.CLASS);
    GeoEntity.getStrategy().initialize(LocatedIn.CLASS);

    if (new AllowedInAllPathsTableQuery(new QueryFactory()).getCount() == 0)
    {
      Universal.getStrategy().reinitialize(AllowedIn.CLASS);
    }

    if (new LocatedInAllPathsTableQuery(new QueryFactory()).getCount() == 0)
    {
      GeoEntity.getStrategy().reinitialize(LocatedIn.CLASS);
    }

    if (new ClassifierIsARelationshipAllPathsTableQuery(new QueryFactory()).getCount() == 0)
    {
      Classifier.getStrategy().reinitialize(ClassifierIsARelationship.CLASS);
    }
    
    importLocationData();
  }

  protected void configureStrategies()
  {
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
  }
  
  public void validate()
  {
    // TODO : Check their postgres version and make sure its 9.5 +
  }

  public void run()
  {
    validate();
    
    DatabaseBuilder.main(this.runwayArgs);
    
    runWithRequest();
  }
  
  protected void importLocationData()
  {
    ProjectDataConfiguration configuration = new ProjectDataConfiguration();

    XMLEndpoint endpoint = this.getEndpoint();

    LocationImporter importer = new XMLLocationImporter(endpoint);
    importer.loadProjectData(configuration);
  }

  private XMLEndpoint getEndpoint()
  {
    String cacheDirectory = System.getProperty("endpoint.cache");

    if (cacheDirectory != null)
    {
      return new CachedEndpoint(new AmazonEndpoint(), new File(cacheDirectory));
    }

    return new AmazonEndpoint();
    
 //   return new LocalEndpoint(new File("/Users/nathan/git/geoprism-registry/georegistry-server/cache/deployable_countries"));
  }
  
  /**
   * Duplicate resources on the classpath may cause issues. This method checks the runwaysdk directory because conflicts there are most common.
   */
  public static void checkDuplicateClasspathResources()
  {
    Set<ClasspathResource> existingResources = new HashSet<ClasspathResource>();
    
    List<ClasspathResource> resources = ClasspathResource.getResourcesInPackage("runwaysdk");
    for (ClasspathResource resource : resources)
    {
      ClasspathResource existingRes = null;
      
      for (ClasspathResource existingResource : existingResources)
      {
        if (existingResource.getAbsolutePath().equals(resource.getAbsolutePath()))
        {
          existingRes = existingResource;
          break;
        }
      }
      
      if (existingRes != null)
      {
        System.out.println("WARNING : resource path [" + resource.getAbsolutePath() + "] is overloaded.  [" + resource.getURL() + "] conflicts with existing resource [" + existingRes.getURL() + "].");
      }
      
      existingResources.add(resource);
    }
  }
}
